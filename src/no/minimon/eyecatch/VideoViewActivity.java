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
		int lastSeek = SharedPreferencesUtil.getLastSeekOnCurrentVideo(this);

		new ASync().execute(duration, lastSeek);
		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
	}

	private class ASync extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int seek = 0;
			videoView.start();
			if (params.length >= 1 && params[0] != -1) {
				seek = random.nextInt(params[0] - 5000);
			}
			if (params.length == 2) {
				seek += params[1];
			}
			videoView.seekTo(seek);
			while (videoView.getCurrentPosition() < seek + 5000) {
				// What to do when video is done/loops
			}
			SharedPreferencesUtil.setLastSeekOnCurrentVideo(
					getApplicationContext(), videoView.getCurrentPosition());
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
