package com.wy.recorder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class AboutSetFragment extends Fragment{
	
	private RadioGroup group;
	public static int radioFormatId;
	private SharedPreferences sp;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View layout = inflater.inflate(R.layout.about_set, container, false);
		sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		/*RadioButton btn = (RadioButton) layout.findViewById(R.id.set_radioArm);
		int id = btn.getId();*/
		
		radioFormatId = sp.getInt("radioPathId", 2131099718);
		group = (RadioGroup)layout.findViewById(R.id.set_radioSavedFormat);
		group.check(radioFormatId);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				radioFormatId = arg0.getCheckedRadioButtonId();
				//根据ID获取RadioButton的实例                 
				SharedPreferences.Editor editor = sp.edit();
				editor.putInt("radioPathId", radioFormatId);
				editor.commit();
			}
			
		});
		
		return layout;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
