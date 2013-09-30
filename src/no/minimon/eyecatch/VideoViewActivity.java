package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
		
		int lastSeek = SharedPreferencesUtil.getLastSeekOnCurrentVideo(this);
		Log.d("SEEK", "" + lastSeek);
		
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
			int duration = params[0];
			int lastSeek = params[1];
			// if (duration != -1) {
			// seek = random.nextInt(duration - (5000 + lastSeek));
			// }
			seek += lastSeek;

			videoView.start();
			videoView.seekTo(seek);
			while (videoView.getCurrentPosition() < seek + 5000) {
				if (!videoView.isPlaying()) {
					seek = 0;
					videoView.seekTo(seek);
					videoView.start();
				}
			}
			seek = videoView.getCurrentPosition();
			SharedPreferencesUtil.setLastSeekOnCurrentVideo(
					getApplicationContext(), seek);
			videoView.stopPlayback();
			setResult(EyeCatchGameActivity.RESULT_VIDEOVIEW);
			finish();
			return null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO: Start of the code to listen to child-lock
		// System.out.println(event.getPointerCount());
		// PointerCoords pointerCoords = new PointerCoords();
		// event.getPointerCoords(0, pointerCoords);
		// System.out.printf("X: %f - Y: %f", pointerCoords.x, pointerCoords.y);
		// pointerCoords = new PointerCoords();
		// if (event.getPointerCount() > 1) {
		// event.getPointerCoords(1, pointerCoords);
		// System.out
		// .printf("X: %f - Y: %f", pointerCoords.x, pointerCoords.y);
		// }
		/*if (event.getPointerCount() == 3) {
			videoView.stopPlayback();
			super.onBackPressed();
		}*/
		return super.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		// Overriding to force child-safe
		// super.onBackPressed();
	}
}
