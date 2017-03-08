package com.sofienmediaselector;

import java.util.ArrayList;

import com.sofienmediaselector.loadPictureTask;
import com.sofienmediaselector.loadVideoTask;
import com.sofienmediaselector.DeviceImageVideoGridviewAdapter;
import com.sofienmediaselector.SelectedElementGridview_Adapter;
import com.sofienmediaselector.ImageOrVideoItem;
import com.sofienmediaselector.storedMedias;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Intent;

public class VideoPicturePickerActivity extends Activity 
{
	private GridView DeviceImageVideoGridview, SelectedElementGridview;
	private RelativeLayout SelectedElemenetContainer;
	private static DeviceImageVideoGridviewAdapter DeviceImageVideoGridview_Adapter;
	private static SelectedElementGridview_Adapter SelectedElementGridview_Adapter;
	private TextView doneText;
	private FrameLayout done_button,cancel_button,add_button;
	private ImageView imagePreview;
	private VideoView videoPreview;
	private int selectedElement = -1, limit_Select = 1;
	private ArrayList<ImageOrVideoItem> SelectedElement_List = new ArrayList<ImageOrVideoItem>();
	private boolean Is_multiSelect;
	private FakeR fakeR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				fakeR = new FakeR(this);

		setContentView(fakeR.getId("layout", "video_picture_picker_activity"));
		
		// hide default headerBar
		getActionBar().hide();
		
		//Is_multiSelect = true;
		//limit_Select = 5;
		limit_Select = getIntent().getIntExtra("limit_Select",5);
		Is_multiSelect = getIntent().getBooleanExtra("Is_multiSelect",false);
		//If pictures and videos are already fetched , no need to reload it (it takes a long time)
		if (storedMedias.imageItems.isEmpty())
			getData();
		
		//linking View to Controller
		DeviceImageVideoGridview = (GridView) findViewById(fakeR.getId("id", "gridView"));
		SelectedElemenetContainer = (RelativeLayout) findViewById(fakeR.getId("id", "RelativeLayout1"));
		doneText = ((TextView) findViewById(fakeR.getId("id", "actionbar_done_textview")));
		cancel_button = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_discard"));
		done_button = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_done"));
		add_button = (FrameLayout) findViewById(fakeR.getId("id", "actionbar_add"));
		SelectedElementGridview = (GridView) findViewById(fakeR.getId("id", "gridView2"));
		imagePreview = (ImageView) findViewById(fakeR.getId("id", "imageView1"));
		videoPreview = (VideoView) findViewById(fakeR.getId("id", "videoView1"));
		
		//display or hide in order of Plugin's options  
		doneText.setEnabled(false);
		SelectedElemenetContainer.setVisibility(View.GONE);

	
		if (!Is_multiSelect) 
		{
			add_button.setVisibility(View.GONE);		
			
		}
		
		done_button.setEnabled(false);
		done_button.setAlpha(0.3f);
		
		// define DeviceImageVideoGridview_Adapter from view  and link it with his related girdview 
		DeviceImageVideoGridview_Adapter = new DeviceImageVideoGridviewAdapter(this,fakeR.getId("layout", "simple_list_item_1"), storedMedias.imageItems);
		DeviceImageVideoGridview.setAdapter(DeviceImageVideoGridview_Adapter);
		
		// define SelectedElementGridview_Adapter from view  and link it with his related girdview 
		SelectedElementGridview_Adapter = new SelectedElementGridview_Adapter(this,fakeR.getId("layout", "simple_list_item_2"), SelectedElement_List);
		SelectedElementGridview.setAdapter(SelectedElementGridview_Adapter);
		
		
		// cancel_button's clickEvent
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// done_button's clickEvent
		done_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if (selectedElement > -1 && !Is_multiSelect) 
				{
							SelectedElement_List.add(storedMedias.imageItems.get(selectedElement));
				}

				 Intent data = new Intent();
  				 Bundle res = new Bundle();
				 ArrayList<String> resultList = new ArrayList<String>();
                res.putStringArrayList("type", convertListOfObjectToListOfStrings(SelectedElement_List).get(0));
                res.putStringArrayList("path", convertListOfObjectToListOfStrings(SelectedElement_List).get(1));
                data.putExtras(res);
                setResult(RESULT_OK, data);
            finish();
			}
		});
		
		// add_button's clickEvent
		add_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedElement > -1) {
					if (SelectedElement_List.contains(storedMedias.imageItems.get(selectedElement)))
					
						Toast.makeText(getBaseContext(), getResources().getString(fakeR.getId("string", "AlreadyAdded")),
								Toast.LENGTH_SHORT).show();
					else {
						if (SelectedElement_List.size() == limit_Select) {
							Toast.makeText(getBaseContext(), getResources().getString(fakeR.getId("string", "limitMultiselect")),
									Toast.LENGTH_SHORT).show();

						} else {
							SelectedElement_List.add(storedMedias.imageItems.get(selectedElement));
							SelectedElementGridview.setNumColumns(SelectedElement_List.size());
							setDynamicWidth(SelectedElementGridview);

							SelectedElementGridview.setAdapter(SelectedElementGridview_Adapter);
							SelectedElemenetContainer.setVisibility(View.VISIBLE);
						}

					}

				} else 
					Toast.makeText(getBaseContext(), getResources().getString(fakeR.getId("string", "noSelection")), Toast.LENGTH_SHORT).show();
				if (!SelectedElement_List.isEmpty()) {
					doneText.setEnabled(true);
					done_button.setEnabled(true);
					done_button.setAlpha(1f);

				}
			}
		});

		
		// DeviceImageVideoGridview element's clickEvent
		DeviceImageVideoGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (selectedElement > -1)
					storedMedias.imageItems.get(selectedElement).setSelected(false);
				if (!Is_multiSelect) 
					{
						doneText.setEnabled(true);
						done_button.setEnabled(true);
						done_button.setAlpha(1f);						
					}
				ImageOrVideoItem item = storedMedias.imageItems.get(position);
				storedMedias.imageItems.get(position).setSelected(true);
				DeviceImageVideoGridview_Adapter.notifyDataSetChanged();
				

				if (item.isVideo()) 
				{
					imagePreview.setVisibility(View.GONE);
					videoPreview.setVisibility(View.VISIBLE);
					videoPreview.setVideoPath(item.getUrl());
					videoPreview.start();
				} else 
				{
					imagePreview.setVisibility(View.VISIBLE);
					videoPreview.setVisibility(View.GONE);
					imagePreview.setImageBitmap(decodeSampledBitmapFromResource(item.getUrl(), 500, 500));

				}
				selectedElement = position;


			}
		});
		
		// SelectedElementGridview element's clickEvent
		SelectedElementGridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				SelectedElement_List.remove(position);
				SelectedElementGridview_Adapter.notifyDataSetChanged();
				if (SelectedElement_List.isEmpty()) 
				{
					SelectedElemenetContainer.setVisibility(View.GONE);
					doneText.setEnabled(false);
					done_button.setEnabled(false);
					done_button.setAlpha(0.3f);
				}

			}
		});

	}

	//fetching pictures and videos from device
	private void getData() 
	{
		storedMedias.imageItems = new ArrayList<ImageOrVideoItem>();
		Uri uri;
		Cursor cursor;
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

		int column_index_data, column_index_folder_name;
		String absolutePathOfImage = null;
		uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Uri uri2 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

		String[] projection = { MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.DATE_ADDED };
		String[] projection2 = { MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
				MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.VideoColumns.DATE_ADDED };

		cursor = this.getContentResolver().query(uri, projection, null, null,
				MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

		Cursor cursor2 = this.getContentResolver().query(uri2, projection2, null, null, null);

		column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		int column_index_data2 = cursor2.getColumnIndexOrThrow(MediaColumns.DATA);
		column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

		int date_insert = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

		int date_insert2 = cursor2.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED);
		int column_index_folder_name2 = cursor2
				.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
		int column_index_folder_name3 = cursor2.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION);
		
		while (cursor2.moveToNext()) {
			String absolutePathOfImage2 = cursor2.getString(column_index_data2);
			new loadVideoTask(absolutePathOfImage2, cursor2.getString(column_index_folder_name2),
					cursor2.getString(column_index_folder_name3), Long.parseLong(cursor2.getString(date_insert2)),false)
							.execute();
		}
		cursor2.close();

		while (cursor.moveToNext()) {
			long time = Long.parseLong(cursor.getString(date_insert));

			long now = System.currentTimeMillis();
			CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time * 1000, now,
					DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);

			absolutePathOfImage = cursor.getString(column_index_data);
			storedMedias.imageItems.add(new ImageOrVideoItem(null, cursor.getString(column_index_folder_name),
					(String) relativeTimeStr, absolutePathOfImage, false, time));
			new loadPictureTask(storedMedias.imageItems.get(storedMedias.imageItems.size() - 1), absolutePathOfImage,false)
					.execute();

			
		}

		cursor.close();

	}

	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

		
	public  ArrayList<ArrayList<String>> convertListOfObjectToListOfStrings(ArrayList<ImageOrVideoItem> ImageOrVideoItemList) {

		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
					ArrayList<String> type=new ArrayList<String>();
					ArrayList<String> path=new ArrayList<String>();
					

        for (int i = 0; i < ImageOrVideoItemList.size(); i++)
		{
			if(ImageOrVideoItemList.get(i).isVideo())
				type.add("video");
			else
				type.add("picture");
			
			path.add(ImageOrVideoItemList.get(i).getUrl());
			result.add(type);
			result.add(path);


		}
		return result;
	}
	
	//Function to notify the adaptor of new changes in data to show
	public static void notifyDataSetChanged() 
	{
		DeviceImageVideoGridview_Adapter.notifyDataSetChanged();
	}

	public static Bitmap decodeSampledBitmapFromResource(String absolutePath, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(absolutePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(absolutePath, options);

	}

	private void setDynamicWidth(GridView gridView) 
	{
		ListAdapter gridViewAdapter = gridView.getAdapter();
		if (gridViewAdapter == null) 
		{
			return;
		}
		int totalWidth;
		int items = gridViewAdapter.getCount();
		View listItem = gridViewAdapter.getView(0, null, gridView);
		listItem.measure(0, 0);
		totalWidth = 200;
		totalWidth = totalWidth * items;
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.width = totalWidth;
		params.height = 200;
		gridView.setLayoutParams(params);
	}

}
