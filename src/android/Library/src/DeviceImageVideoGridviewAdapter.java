package com.sofienmediaselector;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceImageVideoGridviewAdapter extends ArrayAdapter<ImageOrVideoItem> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<ImageOrVideoItem> data = new ArrayList<ImageOrVideoItem>();

	public DeviceImageVideoGridviewAdapter(Context context, int layoutResourceId, ArrayList<ImageOrVideoItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;

		if (row == null)
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(FakeR.getId(context, "id", "Folder"));
			holder.image = (ImageView) row.findViewById(FakeR.getId(context,"id", "imageS"));
			holder.logo = (ImageView) row.findViewById(FakeR.getId(context,"id", "imageView1"));
			holder.datails = (TextView) row.findViewById(FakeR.getId(context,"id", "datails"));	
			
			row.setTag(holder);
		} 
		else 
			{
				holder = (ViewHolder) row.getTag();
			}
		//get pictureOrVideo element that is ready to be rendered 
		ImageOrVideoItem item = data.get(position);
		
		holder.imageTitle.setText(item.getTitle());
		holder.image.setImageBitmap(item.getImage());
		
		if (item.isVideo())
			{
				holder.logo.setImageResource(FakeR.getId(context, "drawable","video"));
			}
		else 
			{
				holder.logo.setImageResource(FakeR.getId(context, "drawable","imager"));
	
			}
		holder.datails.setText(item.getDuration());
		holder.imageTitle.setText(context.getResources().getString(FakeR.getId(context, "string","from")) + " " + item.getTitle());
		if (item.isSelected())
			{
				holder.image.setImageAlpha(125);
	
			}
		else 
			{
				holder.image.setImageAlpha(255);
	
			}
		

		return row;
	}

	
//******** Define ViewHolder Class *************
static class ViewHolder 
	{
		TextView imageTitle;
		TextView datails;
		ImageView image;
		ImageView logo;
	}

}
