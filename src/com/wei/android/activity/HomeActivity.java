package com.wei.android.activity;


import com.wei.android.utils.GridAdapater;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class HomeActivity extends Activity {
	
	private GridView gridView;
	private String[] mItems = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };

	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView=(GridView) findViewById(R.id.gn_griview);
        
        GridAdapater gridAdapater=new GridAdapater(mItems,mPics,
        		HomeActivity.this,R.layout.grid_itemlist);//�����Զ���������
        gridView.setAdapter(gridAdapater);
        
    }

}
