package com.sofienmediaselector;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class SelectedElementGridview_Adapter extends ArrayAdapter<ImageOrVideoItem>
{

	private Context context;
	private int layoutResourceId;
	private ArrayList<ImageOrVideoItem> data = new ArrayList<ImageOrVideoItem>();

	public SelectedElementGridview_Adapter(Context context, int layoutResourceId, ArrayList<ImageOrVideoItem> data) {
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
				holder.image = (ImageView) row.findViewById(FakeR.getId(context,"id", "imageS"));
				row.setTag(holder);
			}
		else 
			{
				holder = (ViewHolder) row.getTag();
			}

		ImageOrVideoItem item = data.get(position);
		holder.image.setImageBitmap(item.getImage());
		
		return row;
	}

	
//******** Define ViewHolder Class *************	
static class ViewHolder
	{
		ImageView image;
	
	}

}
