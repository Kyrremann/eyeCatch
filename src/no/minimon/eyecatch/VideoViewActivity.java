package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.game.EyeCatchGameActivity;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoViewActivity extends FragmentActivity {

	public static final String FILENAME = "filename";
	private static final String LOG_VV = "VIDEOVIEW";
	private VideoView videoView;
	private Random random;
	private boolean endGame, video_shorter_then_threshold;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_videoview);

		random = new Random();
		endGame = getIntent().getBooleanExtra(EyeCatchGameActivity.ENDGAME,
				false);
		videoView = (VideoView) findViewById(R.id.videoView);

		String uri = SharedPreferencesUtil.getCurrentVideoUri(this);
		int duration_of_video = SharedPreferencesUtil
				.getDurationOnCurrentVideo(this);
		int threshold = SharedPreferencesUtil.getAllowedVideoDuration(this);
		int lastSeek = SharedPreferencesUtil.getLastSeekOnCurrentVideo(this);
		Log.d(LOG_VV, "Lastseek: " + lastSeek);
		Log.d(LOG_VV, "Threshold: " + threshold);
		Log.d(LOG_VV, "Duration of video: " + duration_of_video);

		threshold = lastSeek + threshold;
		if (threshold > duration_of_video) {
			if (endGame) { // you have already seen the whole video
				setResult(EyeCatchGameActivity.RESULT_ENDGAME);
				finish();
			} else {
				video_shorter_then_threshold = true;
			}
			threshold = duration_of_video;
			Log.d(LOG_VV, "Updated threshold to " + threshold);
		}

		videoView.setVideoURI(Uri.parse(uri));
		videoView.setZOrderOnTop(true);

		if (video_shorter_then_threshold) {
			videoView.start();
			videoView.seekTo(lastSeek);
			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					SharedPreferencesUtil.setLastSeekOnCurrentVideo(getApplication(),
							0);
					Log.d(LOG_VV, "Seek saved at " + 0);
					setResult(EyeCatchGameActivity.RESULT_VIDEOVIEW);
					finish();
				}
			});
		} else if (!endGame) {
			new ASync().execute(duration_of_video, lastSeek, threshold);
		} else {
			videoView.start();
			videoView.seekTo(lastSeek);
			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					setResult(EyeCatchGameActivity.RESULT_ENDGAME);
					finish();
				}
			});
		}

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

			videoView.start();
			videoView.seekTo(lastSeek);

			while (videoView.getCurrentPosition() < threshold) {

			}

			lastSeek = videoView.getCurrentPosition();
			Log.d(LOG_VV, "Video stopped at " + lastSeek);
			if (lastSeek < 0 || lastSeek > duration - 1000) {
				lastSeek = 0;
			}
			videoView.stopPlayback();
			SharedPreferencesUtil.setLastSeekOnCurrentVideo(getApplication(),
					lastSeek);
			Log.d(LOG_VV, "Seek saved at " + lastSeek);
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
