package com.project.ocrreader;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;

import android.net.*;
import java.io.*;
import android.os.Environment;
import android.provider.MediaStore;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;


public class  MainActivity extends Activity {

	private static final String TAG =MainActivity.class.getSimpleName() ;
	private final int TAKE_PICTURE = 0;
	private final int SELECT_FILE = 1;

	
	private String resultUrl = "result.txt";
	private Image imageResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	public void captureImageFromSdCard( View view ) {
    showPicker(101);
    }
	
	public static final int MEDIA_TYPE_IMAGE = 1;

	/*@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;

		String imageFilePath = null;

		switch (requestCode) {
			case TAKE_PICTURE:
				imageFilePath = getOutputMediaFileUri().getPath();
				Log.i("Image path","path="+imageFilePath);
				break;
			case SELECT_FILE: {
				Uri imageUri = data.getData();

				String wholeID = DocumentsContract.getDocumentId(imageUri);

				// Split at colon, use second item in the array
				String id = wholeID.split(":")[1];

				String[] column = { MediaStore.Images.Media.DATA };

				// where id is equal to
				String sel = MediaStore.Images.Media._ID + "=?";

				Cursor cursor = getBaseContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						column, sel, new String[]{ id }, null);

				assert cursor != null;
				int columnIndex = cursor.getColumnIndex(column[0]);

				if (cursor.moveToFirst()) {
					imageFilePath = cursor.getString(columnIndex);
				}
				cursor.close();
			}
			break;
		}

		//Remove output file
		deleteFile(resultUrl);

		Intent results = new Intent( this, ResultsActivity.class);
		results.putExtra("IMAGE_PATH", imageFilePath);
		results.putExtra("RESULT_PATH", resultUrl);
		startActivity(results);
	}*/

	/*private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	*//** Create a File for saving an image or video *//*
	private static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "ABBYY Cloud OCR SDK Demo App");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create a media file name
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

	    return mediaFile;
	}*/

  /*  public void captureImageFromCamera( View view) {
    	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, TAKE_PICTURE);
    } */

	private void showPicker(int code) {
		ImagePicker.create(this)
				.returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
				.folderMode(true) // folder mode (false by default)
				.toolbarFolderTitle("Select ") // folder selection title
				.toolbarImageTitle("select") // image selection title
				.toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
				.includeVideo(false)// Show video on image picker
				.single()
				.showCamera(true)
				.start(IpCons.RC_IMAGE_PICKER);
	}
	@Override
	protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
		Log.v(TAG, "rcode" + requestCode);
		if (requestCode == IpCons.RC_IMAGE_PICKER) {
			if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
				try {
					if (requestCode == IpCons.RC_IMAGE_PICKER) {
						try {
							imageResult = ImagePicker.getFirstImageOrNull(data);
							System.out.println(imageResult.getPath());
							Intent results = new Intent( this, ResultsActivity.class);
							results.putExtra("IMAGE_PATH", imageResult.getPath());
							results.putExtra("RESULT_PATH", resultUrl);
							startActivity(results);
						/*	UCrop.Options options = new UCrop.Options();
							options.setCompressionQuality(50);
							UCrop.of(Uri.fromFile(new File(imageResult.getPath())), Uri.fromFile(new File(imageResult.getPath())))
									.withAspectRatio(5, 5)
									.withMaxResultSize(Utils.WIDTH, Utils.HEIGHT).withOptions(options)
									.start(EditContact.this, UCrop.REQUEST_CROP);
*/
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Log.v(TAG, "not cropped");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}


		}
		super.onActivityResult(requestCode, resultCode, data);
	}


}
