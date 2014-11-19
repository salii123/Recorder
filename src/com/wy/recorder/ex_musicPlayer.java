package com.wy.recorder;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.wy.bean.Record;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;

 @SuppressLint({ "NewApi", "ShowToast" })
 public class ex_musicPlayer extends ListFragment implements  OnCreateContextMenuListener {
	 TextView textView;
	// 播放对象
	private MediaPlayer m_musicplayer;
	// 播放列表
	private List<String> m_playlist = new ArrayList<String>();
	// 当前播放索引位置
	private int m_list_item = 0;
	// 音乐路径
	
	private static String m_musicpath = new String(getRecordStorageDir());
    @SuppressLint("NewApi") 
    private static String getRecordStorageDir() {
			String path = Environment.getExternalStorageDirectory().toString() +"/recorder/";
			Log.i("RECORD_DIR", path);
			return path;
		}
    
	public Button stop;
	public Button start;
	public Button pause;
	public SeekBar seekbar;
	public TextView currentTime;
	public Button btnPlay;
	public Button btnRecord;
	Activity activity;
	
    private MyListAdapter adapter;
    
    private RelativeLayout layout2;
    private Button cancle,delete;
	static int itemId;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		//setContentView(R.layout.fragment_play);
		View layout = inflater.inflate(R.layout.fragment_play, container, false);
		m_musicplayer = new MediaPlayer();

		textView = (TextView) layout.findViewById(R.id.playPath);
		textView.setText(getRecordStorageDir());
		
		
		start = (Button) layout.findViewById(R.id.start);
		stop = (Button) layout.findViewById(R.id.stop);
		pause = (Button) layout.findViewById(R.id.pause);
		seekbar=(SeekBar)layout.findViewById(R.id.seekbar);
		currentTime = (TextView) layout.findViewById(R.id.currtime);
		currentTime.setText("0:00");
		btnPlay = (Button)layout.findViewById(R.id.btnPlay);
		btnRecord = (Button)layout.findViewById(R.id.btnRecord);
	
		activity = getActivity();
		
        layout2 = (RelativeLayout)layout.findViewById(R.id.relative);
        cancle   = (Button)layout.findViewById(R.id.cancle);
        delete   = (Button)layout.findViewById(R.id.delete);
       
//		musicList();// 获取播放列表
		listener();// 监听
		return layout;
		
	}

	
	Handler handler = new Handler();  
    Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
           try {
			seekbar.setProgress(m_musicplayer.getCurrentPosition());  
			   updateTextView();
			    //每次延迟100毫秒再启动线程  
			   handler.postDelayed(updateThread, 200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
        }  
    };  
  
	private void listener() {
		// TODO Auto-generated method stub
		// 停止
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_musicplayer!=null) {
					m_musicplayer.stop();
					handler.removeCallbacks(updateThread);  
				}
			}
		});

		// 开始
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//  m_musicplayer.start();
				playMusic(m_playlist.get(m_list_item));
				handler.post(updateThread);  
			}
		});

		// 暂停
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (m_musicplayer.isPlaying()) {
					m_musicplayer.pause();
				} else {
					m_musicplayer.start();
				}
			}
		});
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
            @Override  
            public void onProgressChanged(SeekBar seekBar, int progress,  
                    boolean fromUser) {  
            	String test = "" + progress;
            	Log.d("Progress", test );
            	// fromUser判断是用户改变的滑块的值  
                if(fromUser==true){  
                	m_musicplayer.start();
                	m_musicplayer.seekTo(progress);  
                }  
            }  
            @Override  
            public void onStartTrackingTouch(SeekBar seekBar) {  
                // TODO Auto-generated method stub  
            }  
            @Override  
            public void onStopTrackingTouch(SeekBar seekBar) {  
                // TODO Auto-generated method stub        
            }  
        });  
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getFragmentManager().popBackStack();
			}
		});
		
		 /*cancle.setOnClickListener(listener());
        delete.setOnClickListener(listener());*/
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	            layout2.setVisibility(View.INVISIBLE);
			}
		});
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				String str = "";
				builder.setMessage(str);
				builder
				.setTitle("删除")
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								if(adapter.mChecked.size() <= 0){ 
									Toast.makeText(getActivity(), "请选择要操作额选项。", Toast.LENGTH_SHORT);
									return;
								}
								for (int i = 0; i < adapter.mChecked.size(); i++) {
									if (adapter.mChecked.get(i)) {
										adapter.mChecked.remove(i);
										File file = new File(m_playlist.get(i));
										file.delete();
									}
								}
								musicList();
								//toast.setGravity(Gravity.CENTER, 0, 0); 

								//Toast.makeText(getActivity(), "已删除选定文件。", Toast.LENGTH_SHORT).setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int whichButton) {
								for (int i = 0; i < adapter.mChecked.size(); i++) {
									if (adapter.mChecked.get(i)) {
										adapter.mChecked.remove(i);
									}
								}
							}
						}).show();
	            layout2.setVisibility(View.INVISIBLE);
			}
		});
	}
//	private String toTime(int time){  
//        int minute = time / 1000 / 60;  
//        int s = time / 1000 % 60;  
//        String mm = null;  
//        String ss = null;  
//        if(minute<10)mm = "0" + minute;  
//        else mm = minute + "";     
//        if(s <10)ss = "0" + s;  
//        else ss = "" + s;     
//        return mm + ":" + ss;  
//    } 
	
	 private void updateTextView()
	   	{
	   		if (m_musicplayer != null&&m_musicplayer.isPlaying())
	   		{
	   			int m = m_musicplayer.getCurrentPosition() / 1000;
	   			int s = m / 60;
	   			int add = m % 60;
	   			if (add < 10)
	   				currentTime.setText(s + ":0" + add);
	   			else
	   				currentTime.setText(s + ":" + add);
	   			    seekbar.setProgress(m_musicplayer.getCurrentPosition());
	   		}
	   	}
	 
	// 获取SD音乐
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		musicList();
	}

	private void musicList() {
		// TODO Auto-generated method stub
		ListView lv;
		lv = (ListView)this.getView().findViewById(android.R.id.list);
		registerForContextMenu(lv);
		m_playlist.clear();
		search(m_musicpath,"amr", m_playlist);
		adapter = new MyListAdapter(this.getData());
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				itemId = (int)arg3;
				return false;
			}});   

		lv.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		
		menu.setHeaderTitle("文件修改");
		menu.add(0, 0, 0, "重命名");
		menu.add(0, 1, 0, "刪除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 0:
			final EditText editTxt = new EditText(getActivity());
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("重命名")
			.setView(editTxt)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					if(!editTxt.getText().toString().equals("")){
						File file = new File(m_playlist.get(itemId));
						File f = new File(Environment.getExternalStorageDirectory().toString() +"/recorder/" + editTxt.getText().toString() + ".amr");
						file.renameTo(f);
						musicList();
						Toast.makeText(getActivity(), "文件重命名成功", Toast.LENGTH_SHORT)
						.show();
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
						editTxt.setFocusable(true);
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
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					return;
				}
			}).show();
			break;
		case 1:
			File file = new File(m_playlist.get(itemId));
			file.delete();
			musicList();
			Toast.makeText(getActivity(), "文件已删除", Toast.LENGTH_SHORT)
			.show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	// 遍历路径下指定后缀名
	private void search(String dir, final String suffix, List<String> list) {
		File file = new File(dir);
		// 遍历该目录中所有文件
		File[] files = file.listFiles();
		if ((files != null) && (files.length > 0)) {
			for (File tmpfile : files) {
				// 如果是文件夹，继续遍历该目录
				if (tmpfile.isDirectory()) {
					search(tmpfile.getPath(), suffix, list);
				} else {
					//判断文件后缀名
					if (tmpfile.getPath().endsWith(suffix)) {
						list.add(tmpfile.getPath());
					}
				}
			}
		}
	}

	private List<Record> getData() {
		List<Record> m_list = new ArrayList<Record>();
		Record record;
		for (int i = 0; i < m_playlist.size(); i++) {
			record = new Record();
			//获取录音信息
			File file = new File(m_playlist.get(i)); 
			String fileModifiedDate = "最后修改时间:" + FormatFileModifiedData(file.lastModified());
			String fileSize = "大小:" + FormetFileSize(file.length());
			String info = fileSize + " " + fileModifiedDate;
			record.setRname(file.getName());
			record.setRinfo(info);
			m_list.add(record);
		}
		return m_list;
	}
//	private List<Map<String, Object>> getData() {
//		List<Map<String, Object>> m_list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map;	
//		for (int i = 0; i < m_playlist.size(); i++) {
//			map = new HashMap<String, Object>();
//			//获取录音信息
//			File file = new File(m_playlist.get(i)); 
//			map.put("seconder_name", file.getName());
//			m_list.add(map);
//		}
//		return m_list;
//	}
	
	//将文件修改时间改为日期格式
	private String FormatFileModifiedData(long fileModifiedData) {
		Calendar   cal=Calendar.getInstance();  
		cal.setTimeInMillis(fileModifiedData);
		String time = cal.getTime().toLocaleString();
		return time;
	}
	
	//将文件大小改为K、M格式
	private static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}
	 
	// 播放音乐
	void playMusic(String path) {
		try {
			m_musicplayer.reset();
			m_musicplayer.setDataSource(path);
			m_musicplayer.prepare();// 准备同步
			//m_musicplayer.prepareAsync();
			m_musicplayer.setOnPreparedListener(new PrepareListener());
			m_musicplayer.start();
			seekbar.setMax(m_musicplayer.getDuration()); //(不要放错位置！！！)获得歌曲的长度并设置成播放进度条的最大值
			if (m_playlist.isEmpty() == true) {
				Toast.makeText(activity, "播放列表为空", 1000).show();
				return;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 private final class PrepareListener implements OnPreparedListener {
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				m_musicplayer.start();
			}    	
	    }
	 
	// 选择列表项时，播放音乐
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		m_list_item = position;
		String path = m_playlist.get(m_list_item);
		playMusic(path);
		handler.post(updateThread);  
	}
	
	 
	@Override
	public void onDestroyView() {
		m_musicplayer.stop();
		m_musicplayer.release();
		
		super.onDestroyView();
	}
	

	class MyListAdapter extends BaseAdapter{
        List<Boolean> mChecked;
        List<Record> listRecord;
        HashMap<Integer,View> map = new HashMap<Integer,View>(); 
         
        public MyListAdapter(List<Record> list){
            listRecord = list;
            mChecked = new ArrayList<Boolean>();
            for(int i=0;i<list.size();i++){
                mChecked.add(false);
            }
        }
 
        @Override
        public int getCount() {
            return m_playlist.size();
        }
 
        @Override
        public Object getItem(int position) {
            return m_playlist.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;
             
            if (map.get(position) == null) {
                Log.e("MainActivity","position1 = "+position);
               
                LayoutInflater mInflater = (LayoutInflater) getActivity().getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.m_musicitem, null);
                holder = new ViewHolder();
                holder.selected = (CheckBox)view.findViewById(R.id.list_checkBox);
                holder.seconder_name = (TextView) view.findViewById(R.id.list_reconder_name);
                holder.info = (TextView) view.findViewById(R.id.list_info);
                final int p = position;
                map.put(position, view);
                holder.selected.setOnClickListener(new View.OnClickListener() {
                     
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        layout2.setVisibility(View.VISIBLE);
                        mChecked.set(p, cb.isChecked());
                    }
                });
                
                view.setTag(holder);
            }else{
                Log.e("MainActivity","position2 = "+position);
                view = map.get(position);
                holder = (ViewHolder)view.getTag();
            }
             
            holder.selected.setChecked(mChecked.get(position));
//            holder.selected.
            holder.seconder_name.setText(listRecord.get(position).getRname()+"  ");
            holder.info.setText(listRecord.get(position).getRinfo());
            return view;
        }
    }
	static class ViewHolder{
        CheckBox selected;
        TextView seconder_name;
        TextView info;
        
    }

}