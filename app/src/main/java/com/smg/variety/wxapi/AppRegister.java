package com.smg.variety.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class AppRegister extends BroadcastReceiver {
	public static final String APP_ID = "wxa560863512d48f9c";//微信支付appidAppID：wxd96b8dd3e5733967
	@Override
	public void onReceive(Context context, Intent intent) {
		 IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(APP_ID);
	}
}
