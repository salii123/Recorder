package com.wy.recorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ToRecordFragment extends Fragment {

//	OnJumpListener jumpListener;
	
//	public interface OnJumpListener {  
//		   public void onJumpPlay(int position);  
//    } 
	
	
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		 try {  
//			 jumpListener = (OnJumpListener) activity;  
//		      } catch (ClassCastException e) {  
//		        throw new ClassCastException(activity.toString()  
//		             + " must implement OnJumpListener");  
//		 } 
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if( container!=null){  
		    ViewGroup parent=(ViewGroup)container.getParent();  
	        if( parent!=null)  
	            parent.removeView(container);      
	    }
		
		View layout = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		return layout;
	}
	

}
