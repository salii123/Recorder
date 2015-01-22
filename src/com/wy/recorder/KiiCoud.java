package com.wy.recorder;

import java.io.File;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.kii.cloud.storage.resumabletransfer.AlreadyStartedException;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferProgressCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;
import com.kii.cloud.storage.resumabletransfer.StateStoreAccessException;
import com.kii.cloud.storage.resumabletransfer.SuspendedException;
import com.kii.cloud.storage.resumabletransfer.TerminatedException;

public class KiiCoud {
	
	public static List<FileDetail> getOnlineFileList(){
		return null;
	}
	public static boolean uploadFile(Context activity, String fileName){
		FileDetail file = new FileDetail(fileName);
		final String BUCKET_NAME = "book";
		// TODO Auto-generated method stub
		KiiBucket bucket = Kii
				.bucket(BUCKET_NAME);
		KiiObject object = bucket.object();
		object.set("name", file.getFileName());
		object.set("size", file.getSize());
		File upFile = new File(Environment.getExternalStorageDirectory().toString() +"/recorder/" + fileName);
		KiiUploader uploader = object.uploader(activity,upFile);
		try {
			uploader.transfer(new KiiRTransferProgressCallback() {

				@Override
				public void onProgress(KiiRTransfer arg0,
						long arg1, long arg2) {
					// TODO Auto-generated method stub

				}
			});
		} catch (AlreadyStartedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SuspendedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (TerminatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (StateStoreAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	
		return true;
	}
	
	/*public static FileDetail findOnlineFileByName(){
		return null;
	}*/
	
	public static class FileDetail {
        private String fileName;
        private long size;
        
        public FileDetail(String fileName)  {
    		File file = new File(Environment.getExternalStorageDirectory().toString() + fileName);
    		this.fileName = fileName;
    		this.size = file.length();	
    	}
        public String getFileName() {
    		return fileName;
    	}

    	public void setFileName(String fileName) {
    		this.fileName = fileName;
    	}
    	public void setSize(long size) {
    		this.size = size;
    	}
    	public String getSize() {
    		this.size = size;
			return fileName;
	    }
	}

}
