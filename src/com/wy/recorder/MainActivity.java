package com.wy.recorder;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.kubility.demo.MP3Recorder;

import ThreadCase.MergeRecords;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private static Button bPlay;
	private static Button btnRun;
	private static Button btnSave;
	private static Button btnDelete;
	private static ArrayList<String> list;
	private static ImageView imgRecord;
	private Fragment fgAbtMore;
	private Fragment fgAbtSet;
	FragmentTransaction leftDrawer;
	Fragment container;
	static Button recorder;
	static Button button_stop;
	static Chronometer chronometer;
	public static CallRecorder  callRecorder ;
	static String PREFIX = "CR-";
	//private static File file;
	static int click = 0;
	static ex_musicPlayer musicPlayer = new ex_musicPlayer();
	private static MP3Recorder mp3Recorder;
	static Activity activity;
//	static ToPlayFragment fgPlay;
	static ex_musicPlayer fgPlay;
	
	//static ToRecordFragment fgRecord;
	static File fileMerged;
	private static int id;
	static String mp3Path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		
		container = getSupportFragmentManager().findFragmentById(R.id.container);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		activity = this;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(activity); 
		id = sp.getInt("radioFormatId", 2131099718);
		
		getOverflowMenu();
		
		leftDrawer = getSupportFragmentManager().beginTransaction();
	}
	
	
	
	//???????б??????
	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null){
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*//?滻linearlayout
	public void replaceLinearView(View v) {
		int f = LinearLayout.LayoutParams.MATCH_PARENT;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(f, f);
		LinearLayout fragment_place = (LinearLayout)findViewById(R.id.mLinerL);
		fragment_place.removeAllViews();
		fragment_place.addView(v, params);
		
	}*/
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
	}
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		//FragmentTransaction fragmentTransaction = container.getChildFragmentManager().beginTransaction();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		
		switch (position + 1) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				fgAbtSet = new AboutSetFragment();
				fragmentTransaction.replace(R.id.container, fgAbtSet);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				break;
			case 4:
				fgAbtMore = new AboutMoreFragment();
				fragmentTransaction.replace(R.id.container, fgAbtMore);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				break;
		}
	}

	//改变ActionBar的title内容
	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.unlogin);
				break;
			case 2:
				mTitle = getString(R.string.favorite);
				break;
			case 3:
				mTitle = getString(R.string.set);
				break;
			case 4:
				mTitle = getString(R.string.more);
				break;
		}
		
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
			/*SearchManager searchManager =
		           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		    SearchView searchView =
		            (SearchView) menu.findItem(R.id.menu_search).getActionView();
		    searchView.setSearchableInfo(
		            searchManager.getSearchableInfo(getComponentName()));*/
		/*SearchView view = (SearchView) searchItem.getActionView();
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});*/
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

//		OnJumpListener jumpListener;
		
		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			
			//fgPlay = new ToPlayFragment();
			bPlay = (Button) rootView.findViewById(R.id.btnPlay);
			bPlay.setOnClickListener(listener);
			
			//fgRecord = new ToRecordFragment();
			btnRun = (Button) rootView.findViewById(R.id.btnRun);
			btnRun.setOnClickListener(listener);
			btnSave = (Button) rootView.findViewById(R.id.btnSave);
			btnSave.setOnClickListener(listener);
			btnDelete = (Button) rootView.findViewById(R.id.btnDelete);
			btnDelete.setOnClickListener(listener);
			imgRecord = (ImageView) rootView.findViewById(R.id.imgRecord);
			
			//recorder = (Button) rootView.findViewById(R.id.btnRun);
			chronometer = (Chronometer) rootView.findViewById(R.id.chronometer1);
			
			return rootView;
		}
		
		 //button相应事件
		protected  View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final FragmentTransaction ft = getFragmentManager()
						.beginTransaction();

				Button btn = (Button) v;
				switch (btn.getId()) {
					case R.id.btnPlay:
						// jumpListener.onJumpPlay(1);
						if(list == null){
							fgPlay = new ex_musicPlayer();
							ft.replace(R.id.container, fgPlay);
							ft.addToBackStack(null);
							ft.commit();
						}else {
							AlertDialog.Builder builderDelete = new AlertDialog.Builder(
									getActivity());
							builderDelete
									.setTitle("录音文件未保存，确认离开吗？")
									.setPositiveButton("确认",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0, int arg1) {
													// TODO Auto-generated method stub
													if (list != null) {
														for (int i = 0; i < list.size(); i++) {
															File fileOne = new File(
																	(String) list
																			.get(i));
															fileOne.delete();
														}
													}
													list.clear();
													list = null;
													chronometer.stop();
													chronometer.setBase(SystemClock
															.elapsedRealtime());
													fgPlay = new ex_musicPlayer();
													ft.replace(R.id.container, fgPlay);
													ft.addToBackStack(null);
													ft.commit();
												}
											})
									.setNegativeButton("取消",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													return;
												}
											}).show();
						}
						
//						Intent intentRecorder = new Intent(activity, ex_musicPlayer.class);
//						startActivity(intentRecorder);
						break;
					case R.id.btnRun:
						//Timer timer = new Timer();
						//if(AboutSetFragment.radioFormatId == ){}
						if (click == 0) {
							
							chronometer.setBase(SystemClock.elapsedRealtime());
							chronometer.start();
							if(id != 2131099718){
								mp3Path = Environment.getExternalStorageDirectory()
							            + "/recorder/" + CallRecorder.getTime() +".mp3";
								mp3Recorder  = new MP3Recorder(mp3Path, 8000);
								mp3Recorder.start();
							}else{
								callRecorder = new CallRecorder();
								try {
									callRecorder.startRecording();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.recording));
							//setAlphaAnimation(imgRecord);
							click += 1;
						} else if (click%2 == 1) {   
							imgRecord.setImageDrawable(getResources().getDrawable(
									R.drawable.record));
							btnRun.setBackground(getResources().getDrawable(R.drawable.stop));//暂停
							if(id != 2131099718){
								if (mp3Recorder.isRecording()) {
				                    if (mp3Recorder.isPaus()) {
				                    	mp3Recorder.restore();
				                    } else {
				                    	mp3Recorder.pause();
				                    }
				                }
							}else{
								try {
									callRecorder.stopRecording();
									
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								String filePath = callRecorder.getFile().getPath();
								list = new ArrayList<String>();
								list.add(filePath);
							}
							chronometer.stop();
							click++;
							
						}else if(0 == click%2) {                   //继续
							if(id != 2131099718){
								if (mp3Recorder.isRecording()) {
				                    if (mp3Recorder.isPaus()) {
				                    	mp3Recorder.restore();
				                    } else {
				                    	mp3Recorder.pause();
				                    }
				                }
							}else {
								try {
									callRecorder.startRecording();
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.recording));
							btnRun.setBackground(getResources().getDrawable(R.drawable.run));
							//chronometer.setBase(SystemClock.elapsedRealtime() - 10*1000);
							chronometer.start();
							click++;
						}
						break;
					case R.id.btnSave:
						imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.record));
						btnRun.setBackground(getResources().getDrawable(R.drawable.run));
						if(id != 2131099718){
							if(mp3Path != null){
								mp3Recorder.stop();
							}
							else{
								Toast.makeText(getActivity(), "没有录音文件需要保存。", Toast.LENGTH_SHORT)
								.show();
								break;
							}
						}else{
							if(callRecorder == null){ 
								Toast.makeText(getActivity(), "没有录音文件需要保存。", Toast.LENGTH_SHORT)
								.show();
								break;
							}
							String fileEndPath = callRecorder.getFile().getPath();
							
							//在此之前未点击暂停或list未装载任何东西
							if((fileEndPath != null && list == null)){
								list = new ArrayList<String>();
								list.add(fileEndPath);
							}else if(fileEndPath != list.get(list.size()-1)){
								list.add(fileEndPath);
							}
							
							MergeRecords mergeRecord = new MergeRecords();
							mergeRecord.setList(list);
							fileMerged = mergeRecord.save();
						}
						showDialog();
						click = 0;
						chronometer.stop();
//						callRecorder = null;不能置空否则无法改名
						//chronometer.setBase(SystemClock.elapsedRealtime());
						break;
					case R.id.btnDelete:
						if(id != 2131099718){
							File f = new File(mp3Path);
							if(f == null){
								Toast.makeText(getActivity(), "没有录音文件需要删除。", Toast.LENGTH_SHORT)
								.show();
								break;
							}
						}else{
							if(callRecorder == null){
								Toast.makeText(getActivity(), "没有录音文件需要删除。", Toast.LENGTH_SHORT)
								.show();
								break;
							}
						}
					//String filePath = callRecorder.getFile().getPath();
					AlertDialog.Builder builderDelete = new AlertDialog.Builder(
							getActivity());
					builderDelete
							.setTitle("删除")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											
											imgRecord
													.setImageDrawable(getResources()
															.getDrawable(
																	R.drawable.record));
											btnRun.setBackground(getResources()
													.getDrawable(R.drawable.run));
											
											if(id != 2131099718){
												mp3Path = null;
											}else{
												if (list != null) {
													for (int i = 0; i < list.size(); i++) {
														File fileOne = new File(
																(String) list
																		.get(i));
														fileOne.delete();
													}
												}
												list.clear();
												list = null;
											}
											chronometer.stop();
											chronometer.setBase(SystemClock
													.elapsedRealtime());
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											return;
										}
									}).show();
					callRecorder = null;
					click = 0;
					break;
					default:
						break;
					}
			}
		};
		
		public void showDialog() {
			final EditText editText = new EditText(getActivity());
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("重命名")
			.setView(editText)
			.setPositiveButton("确认", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					imgRecord.setImageDrawable(getResources().getDrawable(R.drawable.record));
					btnRun.setBackground(getResources().getDrawable(R.drawable.run));
					if(id != 2131099718){
						File f = new File(Environment.getExternalStorageDirectory().toString() +"/recorder/" + editText.getText().toString() + ".mp3");
					    File file = new File(mp3Path);
						file.renameTo(f);
					}else {
						if(callRecorder != null){
							if(!editText.getText().toString().equals("")) { 
								File f = new File(Environment.getExternalStorageDirectory().toString() +"/recorder/" + editText.getText().toString() + ".amr");
								fileMerged.renameTo(f);
								Field field;
								try {
									field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true); 
									field.set(dialog, true);
								} catch (Exception  e) {
									e.printStackTrace();
								} 
							}
							else{
								Toast.makeText(getActivity(), "文件名不能为空", Toast.LENGTH_SHORT)
								.show();
								editText.setFocusable(true);
								Field field;
								try {
									field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true); 
									field.set(dialog, false);
								} catch (Exception  e) {
									e.printStackTrace();
								} 
							}
						}
						if(list != null) {
							for(int i=0;i<list.size();i++){
								File fileOne=new File((String) list.get(i));
								fileOne.delete();
							}
						}
						list.clear();
						list = null;
						callRecorder = null;
						click = 0;
					}
					chronometer.stop();
					chronometer.setBase(SystemClock.elapsedRealtime());
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
//					callRecorder = null;
					Field field;
					try {
						field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true); 
						field.set(dialog, true);
					} catch (Exception  e) {
						e.printStackTrace();
					}
					if(list != null) {
						for(int i=0;i<list.size();i++){
							File fileOne=new File((String) list.get(i));
							fileOne.delete();
						}
					}
					return;
				}
			}).show();
		}
		@Override
		public void onAttach(Activity activity) {
			
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
}	 