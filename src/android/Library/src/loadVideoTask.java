package com.sofienmediaselector;

import com.sofienmediaselector.VideoPicturePickerActivity;
import com.sofienmediaselector.ImageOrVideoItem;
import com.sofienmediaselector.storedMedias;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;

//to avoid blocking the activity until loading every video Thumbnail ,I used a thread to work in background instead
public class loadVideoTask extends AsyncTask<Void, Integer, Bitmap> 
{
	private String path,S1,S2;
	private Long date_insert;
	private boolean forInit;

	public loadVideoTask(String path,String S1,String S2,Long date_insert,boolean forInit)
		{
			this.path=path;
			this.S1=S1;
			this.S2=S2;
			this.date_insert=date_insert;
			this.forInit=forInit;

		}
	
    protected Bitmap doInBackground(Void... params)
	    {
	    	return ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.MINI_KIND);
	    }

    protected void onProgressUpdate(Integer... progress)
	    {
	    }

    protected void onPostExecute(Bitmap result)
	    {
    		//creation of new ImageOrVideoItem and store it 
	    	storedMedias.imageItems.add(new ImageOrVideoItem(result, S1, S2,path, true,date_insert));
	    	
	    	//lets sort the stored imagesAndVideo basing on date of insertion
	    	storedMedias.sortAll();
	    	
	    	//notify the adaptor for our new changes
			if(!forInit)
	    	VideoPicturePickerActivity.notifyDataSetChanged();
	    }

    
}
