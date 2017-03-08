/**
 * An Image Picker Plugin for Cordova/PhoneGap.
 */
package com.sofienmediaselector;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.os.Build;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;


import java.util.ArrayList;

import com.sofienmediaselector.loadPictureTask;
import com.sofienmediaselector.loadVideoTask;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;



public class VideoPicturePreviewPicker extends CordovaPlugin {

	private CallbackContext callbackContext;
	private JSONObject params;

	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
		 this.callbackContext = callbackContext;
		 this.params = args.getJSONObject(0);

		 Context context = this.cordova.getActivity().getApplicationContext();

		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int read = context
				.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = context
				.checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (read != PackageManager.PERMISSION_GRANTED && write != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(cordova.getActivity(), GettingPermissionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
				if (action.equals("openPicker")) {
					Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);
					boolean Is_multiSelect = false;
					int limit_Select = 5;
					if (this.params.has("limit_Select")) {
						limit_Select = this.params.getInt("limit_Select");
					}
					if (this.params.has("Is_multiSelect")) {
						Is_multiSelect= this.params.getBoolean("Is_multiSelect");
						//Is_multiSelect= Boolean.parseBoolean(this.params.getInt("Is_multiSelect")); 

					}
					
					intent.putExtra("Is_multiSelect", Is_multiSelect);
					intent.putExtra("limit_Select", limit_Select);

					if (this.cordova != null) {
						this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
					}
				}
			}
        } else {
			if (action.equals("openPicker")) {
				Intent intent = new Intent(cordova.getActivity(), VideoPicturePickerActivity.class);
				boolean Is_multiSelect = false;
				int limit_Select = 5;
					if (this.params.has("limit_Select")) {
						limit_Select = this.params.getInt("limit_Select");
					}
					if (this.params.has("Is_multiSelect")) 
						Is_multiSelect= this.params.getBoolean("Is_multiSelect");
						//Is_multiSelect= Boolean.parseBoolean(this.params.getInt("Is_multiSelect")); 
				intent.putExtra("Is_multiSelect", Is_multiSelect);
				intent.putExtra("limit_Select", limit_Select);

				if (this.cordova != null) {
					this.cordova.startActivityForResult((CordovaPlugin) this, intent, 0);
				}
			}

			
		}
		if (action.equals("init")) {
				
												Uri uri;
												Cursor cursor;
												BitmapFactory.Options bmOptions = new BitmapFactory.Options();
												bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

												int column_index_data, column_index_folder_name;
												String absolutePathOfImage = null;
												uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
												Uri uri2 = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

												String[] projection = { MediaColumns.DATA,
														MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
														MediaStore.Images.Media.DATE_ADDED };
												String[] projection2 = { MediaStore.Video.VideoColumns.DATA,
														MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
														MediaStore.Video.VideoColumns.DURATION,
														MediaStore.Video.VideoColumns.DATE_ADDED };

												cursor = context.getContentResolver().query(uri, projection, null, null,
														MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

												Cursor cursor2 = context.getContentResolver().query(uri2, projection2,
														null, null, null);

												column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
												int column_index_data2 = cursor2
														.getColumnIndexOrThrow(MediaColumns.DATA);
											
												column_index_folder_name = cursor
														.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

												int date_insert = cursor
														.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
												
												int date_insert2 = cursor2
														.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED);
												int column_index_folder_name2 = cursor2
														.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
												int column_index_folder_name3 = cursor2
														.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION);
											
												
												while (cursor2.moveToNext()) {
													String absolutePathOfImage2 = cursor2.getString(column_index_data2);
													

													
													
													new loadVideoTask(absolutePathOfImage2,
															cursor2.getString(column_index_folder_name2),cursor2
															.getString(column_index_folder_name3),Long.parseLong(cursor2.getString(date_insert2)),true).execute();
												
												}
												cursor2.close();

												while (cursor.moveToNext()) {
													long time = Long.parseLong(cursor.getString(date_insert));

													long now = System.currentTimeMillis();
													CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(
															time * 1000, now, DateUtils.SECOND_IN_MILLIS,
															DateUtils.FORMAT_ABBREV_RELATIVE);

													absolutePathOfImage = cursor.getString(column_index_data);
													storedMedias.imageItems.add(new ImageOrVideoItem(null, cursor
															.getString(column_index_folder_name),
															(String) relativeTimeStr, absolutePathOfImage, false,time));
													new loadPictureTask(storedMedias.imageItems.get(storedMedias.imageItems.size() - 1),
															absolutePathOfImage,true).execute();

												
												}

												cursor.close();


	
			}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (resultCode == Activity.RESULT_OK && data != null) {
			ArrayList<String> type = data.getStringArrayListExtra("type");
			ArrayList<String> path = data.getStringArrayListExtra("path");
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        JSONObject jResult = new JSONObject();// main object

        for (int i = 0; i < type.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object

           
            try {
                jGroup.put("type", type.get(i));
                jGroup.put("path", path.get(i));
               

                jArray.put(jGroup);

                // /itemDetail Name is JsonArray Name
                jResult.put("selectedMedia", jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
			this.callbackContext.success(jResult);
		} else {
			this.callbackContext.error("No images selected");
		}
	}
}
