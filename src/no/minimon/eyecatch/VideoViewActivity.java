package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoViewActivity extends FragmentActivity {

	public static final String FILENAME = "filename";
	private static final String LOG_VV = "VIDEOVIEW";
	private VideoView videoView;
	private Random random;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_videoview);

		random = new Random();
		videoView = (VideoView) findViewById(R.id.videoView);

		String uri = SharedPreferencesUtil.getCurrentVideoUri(this);
		int duration_of_video = SharedPreferencesUtil
				.getDurationOnCurrentVideo(this);
		int threshold = SharedPreferencesUtil.getAllowedVideoDuration(this);
		int lastSeek = SharedPreferencesUtil.getLastSeekOnCurrentVideo(this);
		Log.d(LOG_VV, "Duration: " + duration_of_video + "\nThreshold: "
				+ threshold + "\nLast seek: " + lastSeek);
		
		threshold = lastSeek + threshold;
		if (threshold > duration_of_video) {
			threshold = duration_of_video;
			threshold -= 500; // to avoid bad timing
		}

		videoView.setVideoURI(Uri.parse(uri));
		videoView.setZOrderOnTop(true);

		new ASync().execute(duration_of_video, lastSeek, threshold);

		videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}
	}

	private class ASync extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int duration = params[0];
			int lastSeek = params[1];
			int threshold = params[2];

			Log.d(LOG_VV, "Threshold: " + threshold + "\nLast seek: "
					+ lastSeek);

			videoView.start();
			videoView.seekTo(lastSeek);

			while (videoView.getCurrentPosition() < threshold) {

			}

			lastSeek = videoView.getCurrentPosition();
			if (lastSeek < 0 || lastSeek > duration - 1000) {
				lastSeek = 0;
			}
			videoView.stopPlayback();
			SharedPreferencesUtil.setLastSeekOnCurrentVideo(getApplication(),
					lastSeek);
			Log.d(LOG_VV, "Video stopped at " + lastSeek);

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
