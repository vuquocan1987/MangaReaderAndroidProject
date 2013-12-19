package anvu.bk.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageLoader {
	public static Bitmap getBitmapForLink(String path){
		Bitmap bm = null;
		try {
		File file = new File(path);
		FileInputStream fis;
		
			fis = new FileInputStream(file);
			bm = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bm;
	}
}
