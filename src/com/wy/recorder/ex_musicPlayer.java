package com.wy.recorder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

 @SuppressLint("NewApi")
 public class ex_musicPlayer extends ListActivity {
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_play);

		m_musicplayer = new MediaPlayer();

		textView = (TextView) findViewById(R.id.playPath);
		textView.setText(getRecordStorageDir());
		
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		pause = (Button) findViewById(R.id.pause);
		seekbar=(SeekBar)findViewById(R.id.seekbar);
		currentTime = (TextView) findViewById(R.id.currtime);
		currentTime.setText("0:00");
		btnPlay = (Button)findViewById(R.id.btnPlay);
		btnRecord = (Button)findViewById(R.id.btnRecord);
		activity = this;
		
		musicList();// 获取播放列表
		listener();// 监听
		
	}

	
	Handler handler = new Handler();  
    Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
           seekbar.setProgress(m_musicplayer.getCurrentPosition());  
           updateTextView();
            //每次延迟100毫秒再启动线程  
           handler.postDelayed(updateThread, 200);    
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
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
	}
	private String toTime(int time){  
        int minute = time / 1000 / 60;  
        int s = time / 1000 % 60;  
        String mm = null;  
        String ss = null;  
        if(minute<10)mm = "0" + minute;  
        else mm = minute + "";     
        if(s <10)ss = "0" + s;  
        else ss = "" + s;     
        return mm + ":" + ss;  
    } 
	
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
	private void musicList() {
		// TODO Auto-generated method stub
		m_playlist.clear();
		search(m_musicpath,"amr", m_playlist);

		SimpleAdapter adapter = new SimpleAdapter(this, this.getData(),
				R.layout.m_musicitem,
				new String[] { "seconder_name" }, new int[] {
						R.id.reconder_name});
		setListAdapter(adapter);
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

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> m_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;	
		for (int i = 0; i < m_playlist.size(); i++) {
			map = new HashMap<String, Object>();
			//获取录音信息
			File file = new File(m_playlist.get(i)); 
			map.put("seconder_name", file.getName());
			m_list.add(map);
		}
		return m_list;
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
				Toast.makeText(this, "播放列表为空", 1000).show();
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		m_list_item = position;
		String path = m_playlist.get(m_list_item);
		playMusic(path);
		handler.post(updateThread);  
	}

	// 当用户返回时结束音乐并释放音乐对象
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			m_musicplayer.stop();
			m_musicplayer.release();
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}