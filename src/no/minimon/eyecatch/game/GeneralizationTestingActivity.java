package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.util.AndroidVersionUtil.isRunningHoneyComb;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.CORRECT;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.FAIL;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_DATE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_FACE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_TYPE;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.AndroidVersionUtil;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("InlinedApi")
public class GeneralizationTestingActivity extends AbstractGameActivity {

	private String name;
	private JSONObject statistic;
	private CountDownTimer startGameCountDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		name = SharedPreferencesUtil.getCurrentUsersName(this);
		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);
		FACE_TYPE = getString(R.string.face_mariama);
		BOX_TYPE = SharedPreferencesUtil.getBox(this);

		random = new Random();
		faces = new SparseArray<Drawable>(8);
		statistic = new JSONObject();
		try {
			statistic.put(STATISTIC_DATE, System.currentTimeMillis());
			statistic.put(STATISTIC_TYPE,
					SharedPreferencesUtil.STATISTIC_GENERALIZATION);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		initBoxes();
		initFace();
		loadImagesIntoFaces();
		loadBoxImages();
		imageFace.setVisibility(VISIBLE);
		setBoxesVisibility(VISIBLE);

		if (isRunningHoneyComb()) {
			getActionBar().hide();
		}

		startGameCountDown = new CountDownTimer(2000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				newGameRound();
			}
		};
		startGameCountDown.start();
	}

	private void newGameRound() {
		GAME_MODE = PAUSE;
		if (CURRENT_ITERATION >= NUMBER_OF_TRIALS) {
			newRoundOrEndOfTesting();
		} else {
			CURRENT_ITERATION++;

			while (CURRENT_FACE_DIRECTION == LAST_FACE_DIRECTION) {
				CURRENT_FACE_DIRECTION = getRandomBox();
			}
			LAST_FACE_DIRECTION = CURRENT_FACE_DIRECTION;
			imageFace.setImageDrawable(faces.get(CURRENT_FACE_DIRECTION));
			GAME_MODE = GAME_ON;
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void newRoundOrEndOfTesting() {
		GAME_MODE = PAUSE;
		if (ROUND_COUNTER >= 2) {
			finishTesting();
		} else {
			saveRoundStatistic();
			ROUND_COUNTER++;
			if (ROUND_COUNTER == 1) {
				FACE_TYPE = getString(R.string.face_aurelien);
			} else if (ROUND_COUNTER == 2) {
				FACE_TYPE = getString(R.string.face_joelle);
			}
			CLICKS_CORRECT = 0;
			CLICKS_FAIL = 0;
			CURRENT_ITERATION = 0;
			CURRENT_FACE_DIRECTION = -1;
			LAST_FACE_DIRECTION = -1;
			loadImagesIntoFaces();
			centerFace();
			startGameCountDown.start();
		}
	}

	private void centerFace() {
		imageFace.setImageDrawable(getCorrectCenterImage());
	}

	private void finishTesting() {
		saveRoundStatistic();
		saveStatistic();
		finish();
	}

	private void saveStatistic() {
		SharedPreferencesUtil.addOrUpdateStatisticOnUser(
				getApplicationContext(), name, statistic);
	}

	public void onImageClicked(View view) {
		if (GAME_MODE == GAME_ON) {
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
	}

	private void checkForCorrectDirection(int direction) {
		if (CURRENT_FACE_DIRECTION == direction) {
			CLICKS_CORRECT++;
		} else {
			CLICKS_FAIL++;
		}
		newGameRound();
	}

	private void saveRoundStatistic() {
		JSONObject roundStats = new JSONObject();
		try {
			roundStats.put(CORRECT, CLICKS_CORRECT);
			roundStats.put(FAIL, CLICKS_FAIL);
			roundStats.put(STATISTIC_FACE, FACE_TYPE);
			statistic.put(FACE_TYPE, roundStats);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& GAME_MODE == GAME_ON) {
			GAME_MODE = PAUSE;
			CLICKS_FAIL++;
			newGameRound();
		}
		return super.onTouchEvent(event);
	}

}
