package com.diyal.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diyal.app.GasStationApplication;

@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment {
	protected GasStationApplication application;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		application = (GasStationApplication) getActivity().getApplication();
		View view = inflater.inflate(initLayout(), container, false);
		initView(view);
		initData(savedInstanceState);
		return view;
	}

	// 初始化layout
	public abstract int initLayout();

	// 初始化控件
	public abstract void initView(View view);

	// 初始化数据
	public abstract void initData(Bundle savedInstanceState);

}
