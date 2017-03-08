package com.sofienmediaselector;

import java.util.ArrayList;
import java.util.Collections;

//lets store imagesAndVideos during the app (no need to refill it every time the related activity fill it and loose time)  
public class storedMedias 
{
	public static ArrayList<ImageOrVideoItem> imageItems = new ArrayList<ImageOrVideoItem>();
	public static void sortAll()
		{
			Collections.sort(imageItems, ImageOrVideoItem.getCompByName());
		}

}
