package com.wy.recorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ToPlayFragment extends Fragment{
	
	//int mCurrentPosition = -1;
	private  Button bRecord;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (savedInstanceState != null) {
//            mCurrentPosition = savedInstanceState.getInt("position");
//        }
		View layout = inflater.inflate(R.layout.fragment_play, container, false);
		System.out.println("ppp");
		
		bRecord = (Button)layout.findViewById(R.id.btnRecord);
		bRecord.setOnClickListener(new OnClickListener() {
			//FragmentTransaction ft = getFragmentManager().beginTransaction();
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getFragmentManager().popBackStack();
			}
		});
		
		
		return layout;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
