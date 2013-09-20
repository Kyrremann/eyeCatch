package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;
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
	private Random random;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_videoview);
		
		random = new Random();
		videoView = (VideoView) findViewById(R.id.videoView);
		String uri = SharedPreferencesUtil.getCurrentVideoUri(this);
		int duration = SharedPreferencesUtil.getDurationOnCurrentVideo(this);
		videoView.setVideoURI(Uri.parse(uri));
		videoView.setZOrderOnTop(true);
		// videoView.start();

		new ASync().execute(duration);
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
	}

	private class ASync extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			if (params.length == 1 && params[0] != -1) {
				int seek = random.nextInt(params[0] - 5000);
				videoView.seekTo(seek);
			}
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
