package com.lin.yuantiku.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lin.yuantiku.R;
import com.lin.yuantiku.entity.ChooseItem;
import com.lin.yuantiku.ui.customview.MTextView;
import com.lin.yuantiku.utils.RxBus;
import com.lin.yuantiku.utils.event.EventUpdateAnswer;
import com.lin.yuantiku.utils.event.EventUpdateResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Colin.Zhang on 2017/3/29.
 */

public class ChildFragment extends BaseFragment {

    private List<EditText> editTexts = new ArrayList<>();
    private ChooseItem chooseItem;

    @BindView(R.id.mtextview)
    MTextView mTextView;
    @BindView(R.id.rlParent)
    RelativeLayout relativeLayout;
    @BindView(R.id.tvInd)
    TextView tvInd;
    @BindView(R.id.loadingView)
    View loadingView;

    private Handler mHandler = new Handler();
    private Runnable mTextViewRunnable = new Runnable() {
        @Override
        public void run() {
            addTextView();
            hideLoading();
        }
    };

    public static ChildFragment getInstance(ChooseItem chooseItem) {
        ChildFragment cf = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", chooseItem);
        cf.setArguments(bundle);
        return cf;
    }

    @Override
    public void initUiAndData() {
        chooseItem = getArguments().getParcelable("item");
        tvInd.setText((chooseItem.index + 1) + "/" + chooseItem.total);
    }

    @Override
    public int layoutId() {
        return R.layout.frag_child;
    }


    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initTextContent();
            }
        }, 50);
    }

    private void initTextContent() {
        String source = chooseItem.body;
        SpannableString ss = new SpannableString(source);
        for (int i = 0; i < source.length() - 1; i++) {
            if (ss.charAt(i) == '*') {
                ImageSpan is = new ImageSpan(getActivity(), R.drawable.testbg_null);
                ss.setSpan(is, i, i + 6, 0);
            }
        }
        mTextView.setMText(ss);
        mTextView.setTextSize(15);
        mTextView.setTextColor(Color.BLACK);
        mTextView.invalidate();
        mHandler.postDelayed(mTextViewRunnable, 200);
    }

    private void addTextView() {
        Map<Integer, List<Integer>> map = mTextView.getListMap();
        for (int i = 0; i < map.size(); i++) {
            List<Integer> list = map.get(i);
            final EditText editText = new EditText(getActivity());
            editText.setTextColor(getResources().getColor(R.color.flag_color));
            if (chooseItem.answer != null && chooseItem.answer.size() > 0
                    && !TextUtils.isEmpty(chooseItem.answer.get(i))
                    && !TextUtils.isEmpty(chooseItem.answer.get(i).trim())) {
                editText.setText(chooseItem.answer.get(i));
            }
            editText.setSingleLine();
            editText.setGravity(Gravity.CENTER);
            editText.setTag(i);
            editText.setTextSize(15);
            setTextChangeListener(editText, i);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.box_width), getResources()
                    .getDimensionPixelSize(R.dimen.box_height));
            rl.leftMargin = list.get(0);
            rl.topMargin = list.get(1);
            editTexts.add(editText);
            relativeLayout.addView(editText, rl);
        }
    }

    private void setTextChangeListener(final EditText editText, final int i) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chooseItem.answer == null) {
                    chooseItem.answer = new HashMap<>();
                }
                chooseItem.answer.put(i, s.toString());
                RxBus.getDefault().post(new EventUpdateAnswer(chooseItem));
                chooseItem.isAnswer = s.length() > 0;
                RxBus.getDefault().post(new EventUpdateResult(chooseItem));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mTextViewRunnable);
    }
}
