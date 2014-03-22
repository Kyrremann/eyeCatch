package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.game.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.WEST;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("InlinedApi")
public class ExtraTestingActivity extends AbstractGameActivity {

	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		name = SharedPreferencesUtil.getCurrentUsersName(this);
		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);
		FACE_TYPE = SharedPreferencesUtil.getFace(this);

		random = new Random();
		faces = new SparseArray<Drawable>(8);

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		initBoxes();
		initFace();
		loadImagesIntoFaces();
		setBoxesVisibility(VISIBLE);
		imageFace.setImageDrawable(getCorrectCenterImage());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				newGameRound();
			}
		}.start();
	}

	private void newGameRound() {
		if (CURRENT_ITERATION >= NUMBER_OF_TRIALS) {
			imageFace.setImageDrawable(getCorrectCenterImage());
			saveStatistic();
			finish();
			return;
		} else {
			CURRENT_ITERATION++;
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		while (CURRENT_FACE_DIRECTION == LAST_FACE_DIRECTION) {
			CURRENT_FACE_DIRECTION = getRandomBox();
		}
		LAST_FACE_DIRECTION = CURRENT_FACE_DIRECTION;
		imageFace.setImageDrawable(faces.get(CURRENT_FACE_DIRECTION));
		imageFace.setVisibility(VISIBLE);
		setBoxesVisibility(VISIBLE);
	}

	public void onImageClicked(View view) {
		switch (view.getId()) {
		case R.id.image_face:
			break;
		case R.id.image_west:
			checkForCorrectDirection(WEST);
			break;
		case R.id.image_east:
			checkForCorrectDirection(EAST);
			break;
		case R.id.image_north:
			checkForCorrectDirection(NORTH);
			break;
		case R.id.image_south:
			checkForCorrectDirection(SOUTH);
			break;
		case R.id.image_north_west:
			checkForCorrectDirection(NORTH_WEST);
			break;
		case R.id.image_north_east:
			checkForCorrectDirection(NORTH_EAST);
			break;
		case R.id.image_south_west:
			checkForCorrectDirection(SOUTH_WEST);
			break;
		case R.id.image_south_east:
			checkForCorrectDirection(SOUTH_EAST);
			break;
		default:
			break;
		}
	}

	private void checkForCorrectDirection(int direction) {
		if (CURRENT_FACE_DIRECTION == direction) {
			CLICKS_CORRECT++;
		} else {
			CLICKS_FAIL++;
		}
		newGameRound();
	}

	private void saveStatistic() {
		SharedPreferencesUtil.createAndAddStatisticForExtraTesting(this, name,
				CLICKS_CORRECT, CLICKS_FAIL);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			CLICKS_FAIL++;
			newGameRound();
		}
		return super.onTouchEvent(event);
	}

}
