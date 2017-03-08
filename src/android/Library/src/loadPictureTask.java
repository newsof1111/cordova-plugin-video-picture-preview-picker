package com.sofienmediaselector;

import com.sofienmediaselector.ImageOrVideoItem;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.sofienmediaselector.VideoPicturePickerActivity;

//to avoid blocking the activity until loading every picture ,I used a thread to work in background instead
public class loadPictureTask extends AsyncTask<Void, Integer, Bitmap> 
{
	private ImageOrVideoItem ImageItem;
	private String URL;
	private boolean forInit;

	public loadPictureTask(ImageOrVideoItem ImageItem,String absolutePathOfImage,boolean forInit)
		{
			this.ImageItem=ImageItem;
			this.URL=absolutePathOfImage;
			this.forInit=forInit;

		}
	
    protected Bitmap doInBackground(Void... params) 
	    {
	    	return VideoPicturePickerActivity.decodeSampledBitmapFromResource(URL,100,100);
	    }

    protected void onProgressUpdate(Integer... progress) 
	    {
	    }

    protected void onPostExecute(Bitmap result)
	    {
	    	ImageItem.setImage(result);
	    	
	    	//notify the adaptor for our new changes
			if(!forInit)
	    	VideoPicturePickerActivity.notifyDataSetChanged();
	    }

	
    
}
