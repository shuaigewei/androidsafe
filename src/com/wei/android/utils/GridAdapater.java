package com.wei.android.utils;

import java.util.zip.Inflater;

import com.wei.android.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义适配器
 * @author Corsair
 *
 */
public class GridAdapater extends BaseAdapter {

	private String[] mItems;
	private int[] mPics;
	private int griditem;
	private LayoutInflater inflater;
	private ImageView imageView;//条目布局中的图
	private TextView textView;//条目布局中的文字
	public GridAdapater(String[] mItems, int[] mPics,Context context,int griditem) {
		this.mItems=mItems;
		this.mPics=mPics;
		this.griditem=griditem;
		 inflater=(LayoutInflater)context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);//获取系统服务生成布局管理器
	}

	public int getCount() {
		return mItems.length;
	}

	@Override
	public Object getItem(int position) {
		return mItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		{
			convertView= inflater.inflate(griditem, null);//把格子布局xml编程布局文件
			imageView=(ImageView) convertView.findViewById(R.id.gv_imageview);
			textView=(TextView) convertView.findViewById(R.id.gv_textview);
			convertView.setTag(new StoryView(imageView,textView));//把存这图像view和文字view的story对象存进
		}
		else
		{
			StoryView storyView=(StoryView) convertView.getTag();//取出存在View的StoryView
			imageView=storyView.imageView;//拿出存储的imageView
			textView=storyView.textView;
			
		}
		imageView.setImageResource(mPics[position]);
		textView.setText(mItems[position]);
		return convertView;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 存储找到的imageView和textView，不用每次都找id节省资源
	 * @author Corsair
	 *
	 */
	private final class StoryView{
		public ImageView imageView;
		public TextView textView;
		public StoryView(ImageView imageView, TextView textView) {
			this.imageView = imageView;
			this.textView = textView;
		}
	}

}
