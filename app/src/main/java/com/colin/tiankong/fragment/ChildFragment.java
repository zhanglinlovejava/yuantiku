package com.colin.tiankong.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colin.tiankong.R;
import com.colin.tiankong.customview.MTextView;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.utils.event.EventUpdateAnswer;
import com.colin.tiankong.utils.event.EventUpdateResult;
import com.colin.tiankong.utils.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin.Zhang on 2017/3/29.
 */

public class ChildFragment extends BaseFragment {
    public static ChildFragment getInstance(ChooseItem chooseItem) {
        ChildFragment cf = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", chooseItem);
        cf.setArguments(bundle);
        return cf;
    }

    private List<EditText> editTexts = new ArrayList<>();
    private ChooseItem chooseItem;
    private MTextView mTextView;
    private RelativeLayout relativeLayout;
    private View loadingView;
    private TextView tvInd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (MTextView) view.findViewById(R.id.mtextview);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlParent);
        chooseItem = getArguments().getParcelable("item");
        tvInd = (TextView) view.findViewById(R.id.tvInd);
        loadingView = view.findViewById(R.id.loadingView);
        tvInd.setText((chooseItem.index + 1) + "/" + chooseItem.total);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addTextView();
                hideLoading();
            }
        }, 200);
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
            final int finalI = i;
            setTextChangeListener(editText, finalI);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.box_width), getResources()
                    .getDimensionPixelSize(R.dimen.box_height));
            rl.leftMargin = list.get(0);
            rl.topMargin = list.get(1);
            editTexts.add(editText);
            relativeLayout.addView(editText, rl);
        }
    }

    private void setTextChangeListener(final EditText editText, final int finalI) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chooseItem.answer == null) {
                    chooseItem.answer = new HashMap<>();
                }
                chooseItem.answer.put(finalI, s.toString());
                RxBus.getDefault().post(new EventUpdateAnswer(chooseItem));
                ResultEntity resultEntity = new ResultEntity();
                resultEntity.index = chooseItem.index;
                resultEntity.isAnswer = s.length() > 0;
                RxBus.getDefault().post(new EventUpdateResult(resultEntity));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
