package com.gotit.quyle.tqn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gotit.quyle.tqn.R;

/**
 * Created by QUYLE on 1/9/18.
 */

public class NotificationFragment extends Fragment {
    TextView mTitleTextView;


    public NotificationFragment() {

    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mTitleTextView.setText(getString(R.string.title_setting));
        return view;
    }

}
