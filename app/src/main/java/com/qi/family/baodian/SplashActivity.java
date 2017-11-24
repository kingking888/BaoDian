package com.qi.family.baodian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qi.family.baodian.utils.ActivityUtil;

public class SplashActivity extends Activity {

    private Handler handler;
    private SharedPreferences sp;
    private static final int DUR_DELAY=2500;
    private TextView v;
    private String ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_splash);

        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 管理activity
        ActivityUtil.getInstance().addActivity(this);
        try {
            ver=getVersionName();
            Log.i("版本号", ver);
            while (ver == null) {
                ver=getVersionName();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        v=(TextView) findViewById(R.id.id_v);
        v.setText("v" + ver);
        String USER_INFO_SP="";
        try {
            USER_INFO_SP=getPackageManager().getPackageInfo(getPackageName(), 0).packageName + "LOGIN_STATE_INFO";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 判断首次登陆
        sp=getSharedPreferences(USER_INFO_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if (sp.getBoolean("isFirst", true)) {
            editor.putBoolean("isFirstRun", false);
            editor.commit(); // 保存数据
            // 检测跳转
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this, GuideViewActivity.class);
                    startActivity(intent);

                }
            }, DUR_DELAY);
        } else {
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this, ZYiMainActivity.class);
                    startActivity(intent);
                }
            }, DUR_DELAY);
        }
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager=getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo=packageManager.getPackageInfo(getPackageName(), 0);
        // int versionCode = packInfo.versionCode; //版本号
        String version=packInfo.versionName; // 版本名字
        return version;
    }
}