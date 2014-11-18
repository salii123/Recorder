package ThreadCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;


public class MergeRecords  {

	ArrayList list;
	
	public void setList(ArrayList list) {
		this.list = list;
	}
	//
	private String getTime(){  
	    SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH：mm：ss");        
	    Date  curDate=new  Date(System.currentTimeMillis());//获取当前时间        
	    String   time   =   formatter.format(curDate);    
	    System.out.println("当前时间");  
	    return time;  
	}
	public File save() {
		FileOutputStream fileOutputStream = null;
		File fileEnd = new File(Environment.getExternalStorageDirectory().toString() +"/recorder/"+ getTime()+ ".amr");
		if(!fileEnd.exists()){
			try {
				fileEnd.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(fileEnd);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<list.size();i++){
			File fileOne=new File((String) list.get(i));
			try {
				FileInputStream fileInputStream=new FileInputStream(fileOne);
				byte  []myByte=new byte[fileInputStream.available()];
				//文件长度
				int length = myByte.length;
		
				//头文件
				if(i==0){
						while(fileInputStream.read(myByte)!=-1){
								fileOutputStream.write(myByte, 0,length);
							}
						}
					
				//之后的文件，去掉头文件就可以了
				else{
					while(fileInputStream.read(myByte)!=-1){
						
						fileOutputStream.write(myByte, 6, length-6);
					}
				}
				
				
				fileOutputStream.flush();
				fileInputStream.close();
				System.out.println("合成文件长度："+fileEnd.length());
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileEnd;
	}
	
	public int deleteOlder() {
		
		return 0;
	}
	
//	void Run() {
//		
//		// get together saving
//		
//		// creating new file
//		save();
//		// delete other files
//		deleteOlder();
//	}
}
