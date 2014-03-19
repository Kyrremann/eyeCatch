package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.game.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.WEST;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.CORRECT;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.FAIL;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_DATE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_FACE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_TYPE;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("InlinedApi")
public class GeneralizationTestingActivity extends AbstractGameActivity {

	private String name, FACE_TYPE;
	private JSONObject statistic;
	private CountDownTimer startGameCountDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		name = SharedPreferencesUtil.getCurrentUsersName(this);
		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);
		FACE_TYPE = "A";

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
		imageFace.setVisibility(VISIBLE);
		setBoxesVisibility(VISIBLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
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
			System.out.println(CURRENT_FACE_DIRECTION);
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
				FACE_TYPE = "B";
			} else if (ROUND_COUNTER == 2) {
				FACE_TYPE = "C";
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
		if (FACE_TYPE.equals("A")) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.mariama_center));
		} else if (FACE_TYPE.equals("B")) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.aurelien_center));
		} else if (FACE_TYPE.equals("C")) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.joelle_center));
		}
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

	private void initBoxes() {
		imageWest = (ImageView) findViewById(R.id.image_west);
		imageEast = (ImageView) findViewById(R.id.image_east);
		imageNorth = (ImageView) findViewById(R.id.image_north);
		imageSouth = (ImageView) findViewById(R.id.image_south);
		imageNorthWest = (ImageView) findViewById(R.id.image_north_west);
		imageNorthEast = (ImageView) findViewById(R.id.image_north_east);
		imageSouthWest = (ImageView) findViewById(R.id.image_south_west);
		imageSouthEast = (ImageView) findViewById(R.id.image_south_east);
	}

	private void initFace() {
		imageFace = (ImageView) findViewById(R.id.image_face);
	}

	private void loadImagesIntoFaces() {
		if (FACE_TYPE.equals("A")) {
			loadFacesOfMariama();
		} else if (FACE_TYPE.equals("B")) {
			loadFacesOfAurelien();
		} else if (FACE_TYPE.equals("C")) {
			loadFacesOfJoelle();
		}
	}

	private void loadFacesOfMike() {
		faces.put(WEST, getResources().getDrawable(R.drawable.mike_w));
		faces.put(EAST, getResources().getDrawable(R.drawable.mike_e));
		faces.put(NORTH, getResources().getDrawable(R.drawable.mike_n));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.mike_s));
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.mike_nw));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.mike_ne));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.mike_sw));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.mike_se));
	}

	private void loadFacesOfJoelle() {
		faces.put(WEST, getResources().getDrawable(R.drawable.joelle_w));
		faces.put(EAST, getResources().getDrawable(R.drawable.joelle_e));
		faces.put(NORTH, getResources().getDrawable(R.drawable.joelle_n));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.joelle_s));
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.joelle_nw));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.joelle_ne));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.joelle_sw));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.joelle_se));
	}

	private void loadFacesOfAurelien() {
		faces.put(WEST, getResources().getDrawable(R.drawable.aurelien_w));
		faces.put(EAST, getResources().getDrawable(R.drawable.aurelien_e));
		faces.put(NORTH, getResources().getDrawable(R.drawable.aurelien_n));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.aurelien_s));
		faces.put(NORTH_WEST, getResources()
				.getDrawable(R.drawable.aurelien_nw));
		faces.put(NORTH_EAST, getResources()
				.getDrawable(R.drawable.aurelien_ne));
		faces.put(SOUTH_WEST, getResources()
				.getDrawable(R.drawable.aurelien_sw));
		faces.put(SOUTH_EAST, getResources()
				.getDrawable(R.drawable.aurelien_se));
	}

	private void loadFacesOfMariama() {
		faces.put(WEST, getResources().getDrawable(R.drawable.mariama_w));
		faces.put(EAST, getResources().getDrawable(R.drawable.mariama_e));
		faces.put(NORTH, getResources().getDrawable(R.drawable.mariama_n));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.mariama_s));
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.mariama_nw));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.mariama_ne));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.mariama_sw));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.mariama_se));
	}

	private void setBoxesVisibility(int visibility) {
		imageWest.setVisibility(visibility);
		imageEast.setVisibility(visibility);
		imageNorth.setVisibility(visibility);
		imageSouth.setVisibility(visibility);
		imageNorthWest.setVisibility(visibility);
		imageNorthEast.setVisibility(visibility);
		imageSouthWest.setVisibility(visibility);
		imageSouthEast.setVisibility(visibility);
	}

	private int getRandomBox() {
		return random.nextInt(8);
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
