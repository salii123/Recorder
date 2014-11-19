package com.wy.recorder;

import android.R;
import android.os.Bundle;
import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CallRecorder extends Activity {

	private  MediaRecorder mediaRecorder;
	private File file = null;
	static String PREFIX = "CR-"; 
	static final String EXTENSION = ".amr";
	public static boolean recorderOn = false;

	
	private String mDisplayName;
	private String mDisplayNumber;

	Context mContext;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_item);
		
		//chronometer=(Chronometer)findViewById(R.id.chronometer1);
		/*button_stop=(Button)this.findViewById(R.id.button2);
		button_recorder.setOnClickListener(new AudioListerner());*/
	}
	public CallRecorder(){}
	

	
	public CallRecorder(Context context, String displayName,
			String displayNumber) {
		mContext = context;
		mDisplayName = displayName;
		mDisplayNumber = displayNumber;

		if (null != mDisplayName && !mDisplayName.trim().equals("")) {
			PREFIX = PREFIX + mDisplayName + "-";
		} else {
			PREFIX = PREFIX + mDisplayNumber + "-";
		}

	}

	// end
	

	public void startRecording() throws Exception {
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		String dir = Environment.getExternalStorageDirectory().toString() +"/recorder/";
		System.out.println(dir);
	    file = new File(dir);
		if(!file.exists()){
			file.mkdir();
		}
		file = new File(dir + getTime()+ ".amr");
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaRecorder.setOutputFile(file.toString());
		mediaRecorder.prepare();
		mediaRecorder.start();
	}

	private String getTime(){  
	    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH：mm：ss");        
	    Date  curDate=new  Date(System.currentTimeMillis());//获取当前时间        
	    String   time   =   formatter.format(curDate);    
	    System.out.println("当前时间");  
	    return time;  
	} 

	
	public void stopRecording() {
		if(mediaRecorder == null){
			return;
		}
		mediaRecorder.stop();
		mediaRecorder.release();
		
		mediaRecorder = null;
		PREFIX = "CR-";
	}
	public File getFile() {
		return file;
	}

	
	/*public void saveToDB() {
		ContentValues values = new ContentValues(3);
		long current = System.currentTimeMillis();
		long modDate = file.lastModified();
		Date date = new Date(current);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String title = formatter.format(date);
		values.put(MediaStore.Audio.Media.IS_MUSIC, "0");
		values.put(MediaStore.Audio.Media.TITLE, title);
		values.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.DATE_MODIFIED, (int) (modDate / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "CallRecord");
		values.put(MediaStore.Audio.Media.ALBUM, "CallRecorder");
		ContentResolver contentResolver = mContext.getContentResolver();
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);
		mContext.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}*/

	
}
