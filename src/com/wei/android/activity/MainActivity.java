package com.wei.android.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wei.android.utils.StreamUtils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
   //标志
	private static final int NO_CONNECT=1;
	private static final int NO_RESOVER=2;
	private static final int UPDATA=3;
	private static final int ENTEHOME=4;//进入主页的标志位
	
	Message message=new Message();//信息
	HttpURLConnection connection;//网络连接
	   
   private PackageInfo packageInfo;
   private TextView text_version;
   private TextView progress_text;
   private String jsonnews;//网络获取的json
   
   Handler handler=new Handler()
   {
	public void handleMessage(Message msg) {
		
		super.handleMessage(msg);
		switch (msg.what) {
		case NO_CONNECT://连接不上网络
			Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
			enterHome();
			break;
		case NO_RESOVER://解析Json文件失败
			Toast.makeText(getApplicationContext(), "数据解析失败", Toast.LENGTH_LONG).show();
			enterHome();
			break;
		case UPDATA://选择是否进行更新
			choiceupdata();
			break;
		case ENTEHOME://没有新版本，直接进入主页
			enterHome();
			break;

		default:
			break;
		}
	}
 

	   
   };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_version=(TextView) findViewById(R.id.text_version);
        progress_text=(TextView) findViewById(R.id.progress_text);
        text_version.setText("版本号："+getversionname());//闪屏显示当前的版本号
        checkversion();//查看时候有新版本
        
    }

    /**
     * 弹出对话框
     */
    		
	private void choiceupdata() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更新提示");
		builder.setMessage("是否要更新");
		builder.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "按了确定", Toast.LENGTH_LONG).show();
				dowmloadfile();//下载文件
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "按了取消", Toast.LENGTH_LONG).show();
				enterHome();
			}
		});
	       builder.setOnCancelListener(new OnCancelListener() {//更新框弹出，按返回进入Home主页
	   		public void onCancel(DialogInterface dialog) {
	   			enterHome();
	   		}
	   	});
       builder.show();

	}

	
	/**
	 * 使用xutils开始下载文件
	 */
	protected void dowmloadfile() {
		if((Environment.getExternalStorageState()).equals(Environment.MEDIA_MOUNTED))
		{
			progress_text.setVisibility(View.VISIBLE);
			String downloadpath="http://192.168.1.104:8080/androidsafe.apk";
			final String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(downloadpath, target, new RequestCallBack<File>(){

				public void onFailure(HttpException exception, String string) {
					System.out.println("下载失败");
				}

				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println(current+"/"+total);
					progress_text.setText("下载进度"+(current/total)*100+"%");
				}

				public void onStart() {
					super.onStart();
				}

				public void onSuccess(ResponseInfo<File> info) {
					System.out.println("下载成功："+target);
					Intent intent2=new Intent();
					intent2.setAction("android.intent.action.VIEW");
					intent2.addCategory("android.intent.category.DEFAULT");
					intent2.setDataAndType(Uri.fromFile(new File(target)),
							"application/vnd.android.package-archive");
					//startActivity(intent2);//这个意图启动没有返回的方法，会导致取消安装就卡死在闪屏页
					startActivityForResult(intent2, 0);
				}
				
			});
		}
		
	}
	
	/**
	 * 取消安装回调该方法
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		enterHome();
	}

	/**
	 * 进入主页
	 */
	protected void enterHome() {
		Intent intent=new Intent(this, HomeActivity.class);//新建意图进入主页
		startActivity(intent);
		finish();
	}

	/**
	 * 连接获取数据，并且检查是否要更新
	 */
    private void checkversion() {
    	final long starttime=System.currentTimeMillis();
    	new Thread(){
			public void run() {
				try {
					
					String path="http://192.168.1.104:8080/jsonnews.json";
					URL url=new URL(path);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(5000);
					connection.setConnectTimeout(5000);
					connection.connect();
					if(connection.getResponseCode()==200)
					{
						InputStream inputStream = connection.getInputStream();
						jsonnews=StreamUtils.getstream(inputStream);
						//System.out.println("网络返回:" + jsonnews);
						JSONObject jsonObject=new JSONObject(jsonnews);//把Json分段
						int code=jsonObject.getInt("versioncode");
						if(code>(packageInfo.versionCode))
						{
							message.what=UPDATA;//更新
						}
						else
						{
							message.what=ENTEHOME;//直接进入主页，没有更新
						}
					}
				} 
				   catch (IOException e) {
					//创建不了链接
					   message.what=NO_CONNECT;
					   e.printStackTrace();
				} catch (JSONException e) {
					//解析不正确
					message.what=NO_RESOVER;
					e.printStackTrace();
				}finally
				{
					long endtime=System.currentTimeMillis();
					long time=endtime-starttime;//一共过了多长时间
					if(time<2000)
					{
						try {
							Thread.sleep(2000-time);//强制休眠到2s
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					connection.disconnect();//取消链接,不用一直请求服务器
					handler.sendMessage(message);//handler返回更新主线UI
				}
			}
    		
    	}.start();
    	
	}

    /**
     * 通过包管理器去的包的属性
     * @return
     */

	private String getversionname() {
		PackageManager manager=getPackageManager();//取得包管理器
		try {
			packageInfo = manager.getPackageInfo(getPackageName(), 0);//获取包信息
			String name=packageInfo.versionName;
			return name;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return null;
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
