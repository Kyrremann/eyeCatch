package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.game.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.RESULT_VIDEOVIEW;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.WEST;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.VideoViewActivity;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

@SuppressLint("InlinedApi")
public class TouchTrainingActivity extends AbstractGameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);
		
		FACE_TYPE = SharedPreferencesUtil.getFace(this);

		random = new Random();
		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		initFace();
		imageFace.setImageDrawable(getCorrectCenterImage());
		
		initBoxes();
		setBoxesVisibility(INVISIBLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				startGame();
			}
		}.start();
	}

	private void startGame() {
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		setBoxesVisibility(INVISIBLE);
		while (CURRENT_FACE_DIRECTION == LAST_FACE_DIRECTION) {
			CURRENT_FACE_DIRECTION = getRandomBox();
		}
		LAST_FACE_DIRECTION = CURRENT_FACE_DIRECTION;
		switch (CURRENT_FACE_DIRECTION) {
		case WEST:
			imageWest.setVisibility(VISIBLE);
			break;
		case EAST:
			imageEast.setVisibility(VISIBLE);
			break;
		case NORTH:
			imageNorth.setVisibility(VISIBLE);
			break;
		case SOUTH:
			imageSouth.setVisibility(VISIBLE);
			break;
		case NORTH_WEST:
			imageNorthWest.setVisibility(VISIBLE);
			break;
		case NORTH_EAST:
			imageNorthEast.setVisibility(VISIBLE);
			break;
		case SOUTH_WEST:
			imageSouthWest.setVisibility(VISIBLE);
			break;
		case SOUTH_EAST:
			imageSouthEast.setVisibility(VISIBLE);
			break;
		default:
			break;
		}
	}

	public void onImageClicked(View view) {
		switch (view.getId()) {
		case R.id.image_face:
			break;
		default: // This is a fix, as you can only click on elements that are
					// visible
			showVideo();
			break;
		}
	}

	private void showVideo() {
		Intent intent = new Intent(this, VideoViewActivity.class);
		startActivityForResult(intent, RESULT_VIDEOVIEW);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_VIDEOVIEW) {
			startGame();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
