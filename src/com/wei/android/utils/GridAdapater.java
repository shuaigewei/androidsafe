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
 * �Զ���������
 * @author Corsair
 *
 */
public class GridAdapater extends BaseAdapter {

	private String[] mItems;
	private int[] mPics;
	private int griditem;
	private LayoutInflater inflater;
	private ImageView imageView;//��Ŀ�����е�ͼ
	private TextView textView;//��Ŀ�����е�����
	public GridAdapater(String[] mItems, int[] mPics,Context context,int griditem) {
		this.mItems=mItems;
		this.mPics=mPics;
		this.griditem=griditem;
		 inflater=(LayoutInflater)context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);//��ȡϵͳ�������ɲ��ֹ�����
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
			convertView= inflater.inflate(griditem, null);//�Ѹ��Ӳ���xml��̲����ļ�
			imageView=(ImageView) convertView.findViewById(R.id.gv_imageview);
			textView=(TextView) convertView.findViewById(R.id.gv_textview);
			convertView.setTag(new StoryView(imageView,textView));//�Ѵ���ͼ��view������view��story������
		}
		else
		{
			StoryView storyView=(StoryView) convertView.getTag();//ȡ������View��StoryView
			imageView=storyView.imageView;//�ó��洢��imageView
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
	 * �洢�ҵ���imageView��textView������ÿ�ζ���id��ʡ��Դ
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
