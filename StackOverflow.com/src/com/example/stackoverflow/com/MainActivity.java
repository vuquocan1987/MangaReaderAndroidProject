/*
 * @author Maver1ck
 * */
package com.example.stackoverflow.com;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stackoverflow.com.ImageDownloader.ImageLoaderListener;

public class MainActivity extends Activity implements View.OnClickListener {

	private Button download, downloadBG, save,load;
	private ImageView img;
	private ProgressBar pb;
	private EditText etUrl;
	private TextView percent;
	private ImageDownloader mDownloader;
	private static Bitmap bmp;
	private FileOutputStream fos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();

	}

	/*--- initialize layout compinents ---*/
	private void initViews() {

		download = (Button) findViewById(R.id.btnDownload);
		downloadBG = (Button) findViewById(R.id.btnDownloadBackground);
		save = (Button) findViewById(R.id.btnSave);
		load = (Button) findViewById (R.id.btnLoad);
		/*--- we are using 'this' because our class implements the OnClickListener ---*/
		download.setOnClickListener(this);
		downloadBG.setOnClickListener(this);
		save.setOnClickListener(this);
		load.setOnClickListener(this);
		save.setEnabled(false);
		img = (ImageView) findViewById(R.id.image);
		img.setScaleType(ScaleType.CENTER_CROP);
		pb = (ProgressBar) findViewById(R.id.pbDownload);
		pb.setVisibility(View.INVISIBLE);
		etUrl = (EditText) findViewById(R.id.etUrl);
		percent = (TextView) findViewById(R.id.tvPercent);
		percent.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onClick(View v) {
		/*--- determine which button was clicked ---*/
		switch (v.getId()) {
		case R.id.btnLoad:
			FileInputStream in;
			Toast.makeText(this, "error reading :/", Toast.LENGTH_SHORT).show();
			
			try
			{
			 in = openFileInput("testfile.png");
			 bmp = BitmapFactory.decodeStream(in);
			 img.setImageBitmap(bmp);
			}
			catch (FileNotFoundException e)
			{
				Toast.makeText(this, "error reading :/", 5000).show();
			}
			break;
		case R.id.btnDownload:

			/*--- we use trim() to remove whitespaces which could be entered ---*/
			if (etUrl.getText().toString().trim().length() > 0) {
				bmp = ImageDownloader.getBitmapFromURL(etUrl
						.getText().toString().trim());
				img.setImageBitmap(bmp);
				save.setEnabled(true);
			}
			
			

			break;

		case R.id.btnDownloadBackground:

			/*--- check whether there is some Text entered ---*/
			Toast.makeText(this, "error reading :/", Toast.LENGTH_SHORT).show();
			if (etUrl.getText().toString().trim().length() > 0) {
				/*--- instantiate our downloader passing it required components ---*/
				mDownloader = new ImageDownloader(etUrl.getText().toString()
						.trim(), pb, save, img, percent, MainActivity.this, bmp, new ImageLoaderListener() {
					@Override
					public void onImageDownloaded(Bitmap bmp) {
						MainActivity.bmp = bmp;
		     /*--- here we assign the value of bmp field in our Loader class 
		               * to the bmp field of the current class ---*/	
					}
					});
				/*--- we need to call execute() since nothing will happen otherwise ---*/
				mDownloader.execute();
			
			}

			break;

		case R.id.btnSave:
			/*--- we do not need to pass our bitmap to this method 
			 * since it's our class field and already initialized at this point*/
			if(bmp==null){
				
				
			}
			//saveImageToSD();
			saveImageToInternal();
			break;
		}	

	}
	private void saveImageToInternal(){
		String filename = "testfile.png";
		//ByteArrayOutputStream IS FileOutputStream which the content is wrote to an byte array
		ByteArrayOutputStream outputStream= 	new ByteArrayOutputStream() ;
		bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		File file = new File (this.getFilesDir(),filename);
		try {
			file.createNewFile();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try {
			fos.write(outputStream.toByteArray());
			fos.close();
			Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private void saveImageToSD() {

		/*--- this method will save your downloaded image to SD card ---*/
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		/*--- you can select your preferred CompressFormat and quality. 
		 * I'm going to use JPEG and 100% quality ---*/
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		/*--- create a new file on SD card ---*/
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "myDownloadedImage.jpg");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*--- create a new FileOutputStream and write bytes to file ---*/
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fos.write(bytes.toByteArray());
			fos.close();
			Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
