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
   //��־
	private static final int NO_CONNECT=1;
	private static final int NO_RESOVER=2;
	private static final int UPDATA=3;
	private static final int ENTEHOME=4;//������ҳ�ı�־λ
	
	Message message=new Message();//��Ϣ
	HttpURLConnection connection;//��������
	   
   private PackageInfo packageInfo;
   private TextView text_version;
   private TextView progress_text;
   private String jsonnews;//�����ȡ��json
   
   Handler handler=new Handler()
   {
	public void handleMessage(Message msg) {
		
		super.handleMessage(msg);
		switch (msg.what) {
		case NO_CONNECT://���Ӳ�������
			Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_LONG).show();
			enterHome();
			break;
		case NO_RESOVER://����Json�ļ�ʧ��
			Toast.makeText(getApplicationContext(), "���ݽ���ʧ��", Toast.LENGTH_LONG).show();
			enterHome();
			break;
		case UPDATA://ѡ���Ƿ���и���
			choiceupdata();
			break;
		case ENTEHOME://û���°汾��ֱ�ӽ�����ҳ
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
        text_version.setText("�汾�ţ�"+getversionname());//������ʾ��ǰ�İ汾��
        checkversion();//�鿴ʱ�����°汾
        
    }

    /**
     * �����Ի���
     */
    		
	private void choiceupdata() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("������ʾ");
		builder.setMessage("�Ƿ�Ҫ����");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "����ȷ��", Toast.LENGTH_LONG).show();
				dowmloadfile();//�����ļ�
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "����ȡ��", Toast.LENGTH_LONG).show();
				enterHome();
			}
		});
	       builder.setOnCancelListener(new OnCancelListener() {//���¿򵯳��������ؽ���Home��ҳ
	   		public void onCancel(DialogInterface dialog) {
	   			enterHome();
	   		}
	   	});
       builder.show();

	}

	
	/**
	 * ʹ��xutils��ʼ�����ļ�
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
					System.out.println("����ʧ��");
				}

				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println(current+"/"+total);
					progress_text.setText("���ؽ���"+(current/total)*100+"%");
				}

				public void onStart() {
					super.onStart();
				}

				public void onSuccess(ResponseInfo<File> info) {
					System.out.println("���سɹ���"+target);
					Intent intent2=new Intent();
					intent2.setAction("android.intent.action.VIEW");
					intent2.addCategory("android.intent.category.DEFAULT");
					intent2.setDataAndType(Uri.fromFile(new File(target)),
							"application/vnd.android.package-archive");
					//startActivity(intent2);//�����ͼ����û�з��صķ������ᵼ��ȡ����װ�Ϳ���������ҳ
					startActivityForResult(intent2, 0);
				}
				
			});
		}
		
	}
	
	/**
	 * ȡ����װ�ص��÷���
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		enterHome();
	}

	/**
	 * ������ҳ
	 */
	protected void enterHome() {
		Intent intent=new Intent(this, HomeActivity.class);//�½���ͼ������ҳ
		startActivity(intent);
		finish();
	}

	/**
	 * ���ӻ�ȡ���ݣ����Ҽ���Ƿ�Ҫ����
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
						//System.out.println("���緵��:" + jsonnews);
						JSONObject jsonObject=new JSONObject(jsonnews);//��Json�ֶ�
						int code=jsonObject.getInt("versioncode");
						if(code>(packageInfo.versionCode))
						{
							message.what=UPDATA;//����
						}
						else
						{
							message.what=ENTEHOME;//ֱ�ӽ�����ҳ��û�и���
						}
					}
				} 
				   catch (IOException e) {
					//������������
					   message.what=NO_CONNECT;
					   e.printStackTrace();
				} catch (JSONException e) {
					//��������ȷ
					message.what=NO_RESOVER;
					e.printStackTrace();
				}finally
				{
					long endtime=System.currentTimeMillis();
					long time=endtime-starttime;//һ�����˶೤ʱ��
					if(time<2000)
					{
						try {
							Thread.sleep(2000-time);//ǿ�����ߵ�2s
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					connection.disconnect();//ȡ������,����һֱ���������
					handler.sendMessage(message);//handler���ظ�������UI
				}
			}
    		
    	}.start();
    	
	}

    /**
     * ͨ����������ȥ�İ�������
     * @return
     */

	private String getversionname() {
		PackageManager manager=getPackageManager();//ȡ�ð�������
		try {
			packageInfo = manager.getPackageInfo(getPackageName(), 0);//��ȡ����Ϣ
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
