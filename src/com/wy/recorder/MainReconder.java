package com.wy.recorder;

import android.app.Application;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.Kii.Site;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class MainReconder extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Kii.initialize("75c1d412", "65996d76456eb44d5159c6e63165b3a3", Site.US);
		KiiUser.logIn(new KiiUserCallBack() {
			@Override
			public void onLoginCompleted(int token, KiiUser user,
					Exception exception) {
				// TODO Auto-generated method stub
				super.onLoginCompleted(token, user, exception);
				System.out.println("login");
			}
		}, "tan", "tanli");
	}
}
