package com.example.mangadownloader.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.htmlcleaner.XPatherException;

import Model.Chapter;
import Model.HtmlHelperPageGetter;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DownloadChapterService extends IntentService {
	public static String URL = "URL";
	public static String MANGA_NAME = "mangaName";
	public static String PAGE_NUMB = "pageNumb";
	public DownloadChapterService(Chapter chapter){
		
		super("DownloadChapter");
		android.os.Debug.waitForDebugger();
		System.out.println("onconstructor");
		Log.d("md", "onconstructor");
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		android.os.Debug.waitForDebugger();
		System.out.println("oncreate");
		Log.d("md", "oncreate");
	}
	public DownloadChapterService() {
		
		super("DownloadChapter");
		
		android.os.Debug.waitForDebugger();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("md", "onHandleIntent");
		System.out.println("onHandleIntent");
		android.os.Debug.waitForDebugger();
		String urlPath = intent.getStringExtra(URL);
		String fileName = intent.getStringExtra(MANGA_NAME);
		int pageNum = intent.getIntExtra(PAGE_NUMB, 0);
		HtmlHelperPageGetter getter;
		try {
			getter = new HtmlHelperPageGetter(new URL(urlPath));
			List<String> bitmapPageLists = getter.getFileList();
			String currentFileName;
			for (int i = 0;i<bitmapPageLists.size();i++) {
				currentFileName = fileName+i;
				File file = new File(Environment.getExternalStorageDirectory(),fileName);
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				getBitmapFromURL(bitmapPageLists.get(i)).compress(Bitmap.CompressFormat.PNG, 100, bytes);
				fos.write(bytes.toByteArray());
				fos.close();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static Bitmap getBitmapFromURL (String link){
		try{
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e){
			e.printStackTrace();
			Log.d("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}
}
