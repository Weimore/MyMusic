package com.example.mymusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.listener.ActivityListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wn123 on 2017/3/26.
 */

public abstract class BaseListFragment<T> extends Fragment {
    private View view;
    private int mLayoutId;
    private int mItemLayoutId;
    protected TextView mTitleText;
    protected List<T> mLists = null;
    private TextView titleText;
    private Button backButton;
    private ListView mListView;
    protected SharedPreferences mPref;
    protected SharedPreferences.Editor editor;
    MainActivity activity;
    private String mText;
    private ActivityListener mListener;

    public BaseListFragment(int layoutId, int itemLayoutId, List<T> lists, String text) {
        mLayoutId = layoutId;
        mItemLayoutId = itemLayoutId;
        mLists = lists;
        mText = text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(mLayoutId, container, false);
        activity = (MainActivity) getActivity();
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);

        mListView = (ListView) view.findViewById(R.id.list_view);

        mPref = getContext().getSharedPreferences("DATA", MODE_PRIVATE);
        editor = mPref.edit();

        setMyAdapter(activity, mLists, mItemLayoutId, mListView);
        return view;
    }

    public abstract void setMyAdapter(Activity activity, List<T> lists, int itemLayoutId, ListView listView);

    public abstract String setChangeList(SharedPreferences.Editor editor);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleText.setText(mText);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nowList=setChangeList(editor);
                activity.playSong(position,nowList);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();  //按上面的返回键返回之前的碎片界面
            }
        });
    }

}
