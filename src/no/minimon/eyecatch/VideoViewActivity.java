package no.minimon.eyecatch;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.VideoView;

public class VideoViewActivity extends FragmentActivity {

	public static final String FILENAME = "filename";
	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_videoview);
		videoView = (VideoView) findViewById(R.id.videoView);
		SharedPreferences preferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);
		String uri = preferences.getString(EyeCatchActivity.CURRENT_VIDEO, "");
		// DURATION
		videoView.setVideoURI(Uri.parse(uri));
		videoView.setZOrderOnTop(true);
		// videoView.start();

		new ASync().execute();
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
	}

	private class ASync extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			// Random random = new Random();
			// int seek = random.nextInt(params[0] - 5000);
			// videoView.seekTo(seek);
			videoView.start();
			while (videoView.getCurrentPosition() < 5000) {
			}
			videoView.stopPlayback();
			setResult(EyeCatchGameActivity.RESULT_VIDEOVIEW);
			finish();
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		// Overriding to force child-safe
		// super.onBackPressed();
	}
}
