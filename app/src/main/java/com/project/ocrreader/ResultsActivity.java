package com.project.ocrreader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;


public class ResultsActivity extends Activity implements TextToSpeech.OnInitListener {
	StringBuffer contents;
	String outputPath;
	TextView tv;
	TextToSpeech t1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//MaryLink.load(this);
		tv = new TextView(this);
		setContentView(tv);
		t1=new TextToSpeech(this,this);
		
		String imageUrl = "unknown";
		
		Bundle extras = getIntent().getExtras();
		if( extras != null) {
			imageUrl = extras.getString("IMAGE_PATH" );
			outputPath = extras.getString( "RESULT_PATH" );
		}
		
		// Starting recognition process
		new AsyncProcessTask(this).execute(imageUrl, outputPath);
	}
	@Override
	public void onPause() {
		if(t1 !=null){
			t1.stop();
			t1.shutdown();
		}
		//MaryLink.getInstance().stopTTS();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//if(contents.toString()!=null){
		//MaryLink.getInstance().startTTS(contents.toString());
			//}
	}

	public void updateResults(Boolean success) {
		if (!success)
			return;
		try { contents = new StringBuffer();

			FileInputStream fis = openFileInput(outputPath);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}
			Log.i("Text","cx"+contents.toString());
			displayMessage(contents.toString());
			//MaryLink.getInstance().startTTS(contents.toString());
		} catch (Exception e) {
			displayMessage("Error: " + e.getMessage());
		}

	}

	
	public void displayMessage( String text )
	{
		tv.post( new MessagePoster( text ) );


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_results, menu);
		return true;
	}

	@SuppressLint("NewApi")
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = t1.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				if(contents!=null){
				t1.speak(contents.toString(), TextToSpeech.QUEUE_FLUSH, null);}
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	class MessagePoster implements Runnable {
		public MessagePoster( String message )
		{
			_message = message;
		}

		public void run() {
			tv.append( _message + "\n" );
			setContentView( tv );

		}

		private final String _message;
	}
}
