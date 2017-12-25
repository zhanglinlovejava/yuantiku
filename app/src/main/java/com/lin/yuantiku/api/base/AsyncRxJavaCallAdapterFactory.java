/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lin.yuantiku.api.base;

import com.google.gson.reflect.TypeToken;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * 1. Reverses the changes of https://github.com/square/retrofit/pull/1117
 * <p>
 * I find it pointless to wrap a synchronous call in an Observable. That could be easily achieved by
 * Single.fromCallable().
 * <p>
 * 2. Adds Completable support, which didn't exist when async calling was removed
 * <p>
 * 3. Adds the possibility to set an error handler
 */
public final class AsyncRxJavaCallAdapterFactory extends CallAdapter.Factory {
    public static final Type TYPE_VOID = new TypeToken<Void>() {
    }.getType();
    private final ErrorHandler mErrorHandler;

    private AsyncRxJavaCallAdapterFactory(ErrorHandler errorHandler) {
        mErrorHandler = errorHandler;
    }

    public static AsyncRxJavaCallAdapterFactory create(ErrorHandler errorHandler) {
        return new AsyncRxJavaCallAdapterFactory(errorHandler);
    }

    public static AsyncRxJavaCallAdapterFactory create() {
        return new AsyncRxJavaCallAdapterFactory(new ErrorHandler() {
            @Override
            public ThrowableResult handleError(String errorMsg) {
                return new ThrowableResult(errorMsg);
            }

        });
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        boolean isSingle = rawType == Single.class;
        boolean isCompletable = rawType == Completable.class;
        boolean isObservable = rawType == Observable.class;
        if (!isObservable && !isSingle && !isCompletable) {
            return null;
        }
        if (isCompletable) {
            CallAdapter<Observable<?>> callAdapter =
                    new SimpleCallAdapter(TYPE_VOID, mErrorHandler);
            return makeCompletable(callAdapter);
        }
        checkParameterizedType(returnType);
        if (isSingle) {
            CallAdapter<Observable<?>> callAdapter = newCallAdapter(returnType, mErrorHandler);
            return makeSingle(callAdapter);
        }
        return newCallAdapter(returnType, mErrorHandler);
    }

    private static CallAdapter<Completable> makeCompletable(
            final CallAdapter<Observable<?>> callAdapter) {
        return new CallAdapter<Completable>() {
            @Override
            public Type responseType() {
                return callAdapter.responseType();
            }

            @Override
            public <R> Completable adapt(Call<R> call) {
                Observable<?> observable = callAdapter.adapt(call);
                return observable.toCompletable();
            }
        };
    }

    private static void checkParameterizedType(Type returnType) {
        Class<?> rawType = getRawType(returnType);
        String name = rawType.getSimpleName();
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    name + " return type must be parameterized as " + name + "<Foo> or " +
                            name + "<? extends Foo>");
        }
    }

    private CallAdapter<Observable<?>> newCallAdapter(Type returnType, ErrorHandler errorHandler) {
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            checkParameterizedType(observableType);
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResponseCallAdapter(responseType);
        }
        if (rawObservableType == Result.class) {
            checkParameterizedType(observableType);
            Type responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new ResultCallAdapter(responseType);
        }
        return new SimpleCallAdapter(observableType, errorHandler);
    }

    private static CallAdapter<Single<?>> makeSingle(final CallAdapter<Observable<?>> callAdapter) {
        return new CallAdapter<Single<?>>() {
            @Override
            public Type responseType() {
                return callAdapter.responseType();
            }

            @Override
            public <R> Single<?> adapt(Call<R> call) {
                Observable<?> observable = callAdapter.adapt(call);
                return observable.toSingle();
            }
        };
    }

    /**
     * @author Matthias Schmitt
     */
    public interface ErrorHandler {
        ThrowableResult handleError(String errorMsg);
    }

    static final class CallOnSubscribe<T> implements Observable.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        private CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override
        public void call(final Subscriber<? super Response<T>> subscriber) {
            final Call<T> call = originalCall.clone();
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    call.cancel();
                }
            }));
            if (subscriber.isUnsubscribed()) {
                return;
            }
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {

                    if (!subscriber.isUnsubscribed() && t != null) {
                        if (t.getMessage() != null && t.getMessage().contains("JsonSyntaxException")) {
                            subscriber.onError(new ThrowableResult("服务器返回数据异常，请稍候重试").setErrorCode(-1));
                        } else {
                            subscriber.onError(new ThrowableResult("网络错误").setErrorCode(-1));
                        }
                    }
                }
            });
        }
    }

    static final class ResponseCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;

        ResponseCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Response<R>> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call));
        }
    }

    private static final class SimpleCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;
        private final ErrorHandler mErrorHandler;

        SimpleCallAdapter(Type responseType, ErrorHandler errorHandler) {
            this.responseType = responseType;
            mErrorHandler = errorHandler;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<R> adapt(Call<R> call) {
            return Observable.create(new CallOnSubscribe<>(call))
                    .flatMap(new Func1<Response<R>, Observable<R>>() {
                        @Override
                        public Observable<R> call(Response<R> response) {
                            try {
                                if (response.isSuccessful()) {
                                    if (response.body() instanceof com.lin.yuantiku.entity.base.Result) {
                                        com.lin.yuantiku.entity.base.Result result = (com.lin.yuantiku.entity.base.Result) response.body();
                                        if (result != null) {
                                            if (0 == result.error_code) {
                                                return Observable.just(response.body());
                                            } else if (result.error_code == 10101) {
//                                                RxBus.getDefault().post(new EventTokenExpires(result.error_code, result.error_msg));
                                                return handleError(result);
                                            } else {
                                                return handleError(result);
                                            }
                                        } else {
                                            return Observable.error(mErrorHandler.handleError("网络错误").setErrorCode(-1));
                                        }
                                    } else {
                                        return Observable.just(response.body());
                                    }
                                } else {
                                    if (response.code() >= 400) {
//                                        RxBus.getDefault().post(new EventTokenExpires(response.code(), response.raw().toString()));
                                        return Observable.error(mErrorHandler.handleError("服务器繁忙").setErrorCode(response.code()));
                                    }
                                    return Observable.error(mErrorHandler.handleError("网络错误").setErrorCode(-1));
                                }
                            } catch (Exception e) {
//                                String errorMsg = response.raw() + "\\br" + getStackTraceString(e.getCause());
//                                RxBus.getDefault().post(new EventTokenExpires(response.code(), errorMsg));
                                return Observable.error(mErrorHandler.handleError("网络错误").setErrorCode(-1));
                            }
                        }

                        public String getStackTraceString(Throwable ex) {
                            StringBuilder sb = new StringBuilder();
                            Writer writer = new StringWriter();
                            PrintWriter printWriter = new PrintWriter(writer);
                            ex.printStackTrace(printWriter);
                            Throwable cause = ex.getCause();
                            while (cause != null) {
                                cause.printStackTrace(printWriter);
                                cause = cause.getCause();
                            }
                            printWriter.close();
                            String result = writer.toString();
                            return sb.append(result).toString();
                        }

                        private Observable<R> handleError(com.lin.yuantiku.entity.base.Result result) {
                            String errorMsg;
                            if (result.error_code == 10101) {
                                result.error_msg = "用户认证失败，请重新登录";
                            }
                            if (result.error_msg == null) {
                                errorMsg = "网络错误";
                            } else {
                                errorMsg = result.error_msg;
                            }
                            return Observable.error(mErrorHandler.handleError(errorMsg).setErrorCode(result.error_code));
                        }
                    });

        }
    }


    private static final class ResultCallAdapter implements CallAdapter<Observable<?>> {
        private final Type responseType;

        ResultCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public <R> Observable<Result<R>> adapt(Call<R> call) {
            Observable<Result<R>> observable = Observable.create(new CallOnSubscribe<>(call)) //
                    .map(new Func1<Response<R>, Result<R>>() {
                        @Override
                        public Result<R> call(Response<R> response) {
                            return Result.response(response);
                        }
                    }).onErrorReturn(new Func1<Throwable, Result<R>>() {
                        @Override
                        public Result<R> call(Throwable throwable) {
                            return Result.error(throwable);
                        }
                    });
            return observable;
        }
    }
}
