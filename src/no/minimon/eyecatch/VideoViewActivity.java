package no.minimon.eyecatch;

import android.annotation.SuppressLint;
import no.minimon.eyecatch.game.EyeCatchGameActivity;
import no.minimon.eyecatch.util.AndroidVersionUtil;
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

import static no.minimon.eyecatch.util.AndroidVersionUtil.isRunningHoneyComb;
import static no.minimon.eyecatch.util.AndroidVersionUtil.isRunningJellyBean;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoViewActivity extends FragmentActivity {

	public static final String FILENAME = "filename";
	private static final String LOG_VV = "VIDEOVIEW";
	private VideoView videoView;
	private boolean endGame, video_shorter_then_threshold;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_videoview);

		endGame = getIntent().getBooleanExtra(EyeCatchGameActivity.ENDGAME,
				false);
		videoView = (VideoView) findViewById(R.id.videoView);

		String uri = SharedPreferencesUtil.getCurrentVideoUri(this);
		int duration_of_video = SharedPreferencesUtil.getDurationOnCurrentVideo(this);
		int threshold = SharedPreferencesUtil.getAllowedVideoDuration(this);
		int lastSeek = SharedPreferencesUtil.getLastSeekOnCurrentVideo(this);
		Log.d(LOG_VV, "Lastseek: " + lastSeek);
		Log.d(LOG_VV, "Threshold: " + threshold);
		Log.d(LOG_VV, "Duration of video: " + duration_of_video);

		threshold = lastSeek + threshold;
		Log.d(LOG_VV, "Difference: " + threshold);
		if (threshold > duration_of_video) {
			if (endGame) { // you have already seen the whole video
				Log.d(LOG_VV, "You have already seen the whole movie");
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
			Log.d(LOG_VV, "Video is shorter then threshold");
			videoView.start();
			videoView.seekTo(lastSeek);
			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					SharedPreferencesUtil.setLastSeekOnCurrentVideo(getApplication(),
							0);
					Log.d(LOG_VV, "Seek reset to " + 0);
					setResult(EyeCatchGameActivity.RESULT_VIDEOVIEW);
					finish();
				}
			});
		} else if (!endGame) {
			Log.d(LOG_VV, "This is not the end game, aka last video");
			new ASync().execute(duration_of_video, lastSeek, threshold);
		} else {
			Log.d(LOG_VV, "This is the las video, aka the end game");
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
		if (isRunningHoneyComb()) {
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
			Log.d(LOG_VV, "Video seeking to " + lastSeek);

			while (videoView.getCurrentPosition() < threshold) {}

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

	@SuppressLint("InlinedApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isRunningJellyBean()) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
}
