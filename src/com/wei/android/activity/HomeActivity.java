package com.wei.android.activity;


import com.wei.android.utils.GridAdapater;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class HomeActivity extends Activity {
	
	private GridView gridView;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

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
        		HomeActivity.this,R.layout.grid_itemlist);//创建自定义适配器
        gridView.setAdapter(gridAdapater);
        
    }

}
