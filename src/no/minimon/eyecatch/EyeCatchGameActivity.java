package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EyeCatchGameActivity extends FragmentActivity {

	private static final int VISIBLE = View.VISIBLE;
	private static final int INVISIBLE = View.INVISIBLE;
	private static final int GAME_PAUSE = 2;
	private static final int GAME_ON = 1;
	private static final int BEFORE_GAME = 0;

	public static final int RESULT_VIDEOVIEW = 256;
	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	public static final int NORTH_WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int SOUTH_WEST = 6;
	public static final int SOUTH_EAST = 7;

	private int CURRENT_FACE = -1;
	private int GAME_MODE;
	private int NUMBER_OF_TRIALS;
	private int CURRENT_ITERATION = 0;
	private int CURRENT_ITERATION_CORRECT = 0;
	private int CURRENT_ITERATION_FAIL = 0;
	private int MASTERY_CRITERIA;
	private long LEVEL_DURATION;
	private int TESTING_LEVEL = 0;
	private int TRAINING_LEVEL = 0;
	private int FACE_RANGE = 0;

	private View contentView;
	private ImageView imageFace, imageNorth, imageNorthEast, imageEast,
			imageSouthEast, imageSouth, imageSouthWest, imageWest,
			imageNorthWest;
	private CountDownTimer countDownLevelDuration, countDownTestingBegin;
	private TextView watermark;

	private SparseArray<Drawable> faces;
	private Random random;
	private boolean testingLevel = true;
	private JSONObject statistic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		// TRAINING_LEVEL = 7; TESTING_LEVEL = 7;

		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);
		LEVEL_DURATION = SharedPreferencesUtil.getDurationPerTrial(this);
		MASTERY_CRITERIA = SharedPreferencesUtil.getMasteryCriteria(this);

		SharedPreferencesUtil.setLastSeekOnCurrentVideo(this, 0);
		statistic = SharedPreferencesUtil.createNewStatisticOnCurrentUser(this);

		faces = new SparseArray<Drawable>(8);
		loadImagesIntoFaces();
		random = new Random();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		imageFace = (ImageView) findViewById(R.id.image_face);
		watermark = (TextView) findViewById(R.id.watermark);

		initDurations();
		initBoxes();
		setBoxesVisibility(INVISIBLE);
		GAME_MODE = GAME_PAUSE;
		changeFaceToStar();
		setWatermark();
	}

	private void setWatermark() {
		String text = "";
		if (!testingLevel) {
			text += getString(R.string.training);
			text += " " + (char) (65 + TRAINING_LEVEL);
		} else {
			text += getString(R.string.testing);
			if (TESTING_LEVEL < 8) {
				text += " pre-" + (char) (65 + TESTING_LEVEL);
			} else {
				text += " post-H";
			}

		}
		watermark.setText(text);
	}

	private void initDurations() {
		countDownTestingBegin = new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if (testingLevel) {
					loadTrainingOrTesting();
					GAME_MODE = GAME_ON;
				}
			}
		};

		countDownLevelDuration = new CountDownTimer(LEVEL_DURATION, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				wrongAction();
			}
		};
	}

	public void onImageClicked(View view) {
		int id = view.getId();
		switch (GAME_MODE) {
		case BEFORE_GAME:
			if (view.getId() == R.id.image_face) {
				// Not really needed
				initGame();
			}
		case GAME_PAUSE:
			if (id == R.id.image_face) {
				if (testingLevel) {
					setBoxesVisibility(VISIBLE);
					changeFaceToCenter();
					countDownTestingBegin.start();
				} else {
					countDownTestingBegin.cancel();
					countDownLevelDuration.cancel();
					loadTrainingOrTesting();
					GAME_MODE = GAME_ON;
				}
			}
			break;
		case GAME_ON:
			switch (id) {
			case R.id.image_face:
				if (testingLevel) {
					// wrongAction(); // Not anymore
				} else if (TRAINING_LEVEL == 0 || TRAINING_LEVEL == 1) {
					correctActionVideoOrNext();
				}
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
				updateFaceWithNewImage();
				break;
			}
		default:
			break;
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void wrongAction() {
		CURRENT_ITERATION++;
		CURRENT_ITERATION_FAIL++;
		Log.d("WRONG", "CI: " + CURRENT_ITERATION + " - CIC: " + CURRENT_ITERATION_CORRECT + " - CIF: " + CURRENT_ITERATION_FAIL);
		if (!testingLevel) {
			CURRENT_ITERATION_CORRECT = 0;
			continueOrNext();
		} else {
			continueOrNext();
		}
	}

	private void checkForCorrectDirection(int direction) {
		countDownLevelDuration.cancel();
		if (CURRENT_FACE == direction) {
			correctActionVideoOrNext();
		} else {
			wrongAction();
		}
	}

	private void correctActionVideoOrNext() {
		countDownLevelDuration.cancel();
		CURRENT_ITERATION++;
		CURRENT_ITERATION_CORRECT++;
		Log.d("RIGHT", "CI: " + CURRENT_ITERATION + " - CIC: " + CURRENT_ITERATION_CORRECT + " - CIF: " + CURRENT_ITERATION_FAIL);
		if (!testingLevel) {
			startVideoViewActivity();
		} else {
			continueOrNext();
		}
	}

	public void startVideoViewActivity() {
		startActivityForResult(new Intent(this, VideoViewActivity.class),
				RESULT_VIDEOVIEW);
		// Toast.makeText(this, "Showing video", Toast.LENGTH_SHORT).show();
		// onActivityResult(0, RESULT_VIDEOVIEW, new Intent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_VIDEOVIEW) {
			continueOrNext();
		} else {
			super.onActivityResult(requestCode, requestCode, intent);
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void continueOrNext() {
		if (testingLevel && CURRENT_ITERATION >= NUMBER_OF_TRIALS) {
			doneWithLastRound();
		} else if (!testingLevel && CURRENT_ITERATION_CORRECT >= MASTERY_CRITERIA) {
			doneWithLastRound();
		} else {
			continueWithCurrent();
		}
	}

	private void continueWithCurrent() {
		GAME_MODE = GAME_PAUSE;

		if (!testingLevel) {
			setBoxesVisibility(INVISIBLE);
			loadTrainingOrTesting();
			GAME_MODE = GAME_ON;
		} else {
			changeFaceToCenter();
			countDownTestingBegin.start();
		}

		setWatermark();
	}

	private void doneWithLastRound() {
		addStatesticToJson();
		if (!testingLevel) {
			TRAINING_LEVEL++;
		} else {
			TESTING_LEVEL++;
		}

		CURRENT_ITERATION = 0;
		CURRENT_ITERATION_CORRECT = 0;
		CURRENT_ITERATION_FAIL = 0;
		GAME_MODE = GAME_PAUSE;
		testingLevel = !testingLevel;

		setBoxesVisibility(INVISIBLE);
		// changeFaceToCenter();
		changeFaceToStar();

		setWatermark();
	}

	private void addStatesticToJson() {
		try {
			if (testingLevel) {
				statistic.accumulate(SharedPreferencesUtil.STATISTIC_TESTING,
						CURRENT_ITERATION_CORRECT);
			} else {
				statistic.accumulate(SharedPreferencesUtil.STATISTIC_TRAINING,
						CURRENT_ITERATION + CURRENT_ITERATION_CORRECT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void changeFaceToCenter() {
		imageFace.setImageDrawable(getResources().getDrawable(
				R.drawable.mariama_center));
	}

	public void changeFaceToStar() {
		imageFace.setImageDrawable(getResources().getDrawable(R.drawable.star));
	}

	private void initGame() {
		loadTrainingOrTesting();
		GAME_MODE = GAME_ON;
	}

	private void loadTrainingOrTesting() {
		if (!testingLevel) {

			Animation animation = new AlphaAnimation(0f, 1f);
			animation.setDuration(1200);

			switch (TRAINING_LEVEL) {
			case 0: // Training level A
				FACE_RANGE = 0;
				imageFace.setImageDrawable(getResources().getDrawable(
						R.drawable.mariama_center_brighted));
				break;
			case 1: // Training level B
				FACE_RANGE = 0;
				changeFaceToCenter();
				break;
			case 2: // Training level C
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				if (CURRENT_FACE == WEST) {
					imageWest.setVisibility(VISIBLE);
				} else if (CURRENT_FACE == EAST) {
					imageEast.setVisibility(VISIBLE);
				}
				break;
			case 3: // Training level D
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				break;
			case 4: // Training level E
				FACE_RANGE = 3;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				break;
			case 5: // Training level F
				FACE_RANGE = 4;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				imageSouth.setVisibility(VISIBLE);
				break;
			case 6: // Training level G
				FACE_RANGE = 6;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				imageSouth.setVisibility(VISIBLE);
				imageNorthEast.setVisibility(VISIBLE);
				imageNorthWest.setVisibility(VISIBLE);
				break;
			case 7: // Training level H
				FACE_RANGE = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
				break;
			case 8:
				// endGameActivity();
				testingLevel = true;
				loadTrainingOrTesting();
				break;
			default:
				break;
			}
			imageFace.startAnimation(animation);
		} else {
			if (TESTING_LEVEL < 9) {
				FACE_RANGE = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
			} else if (TESTING_LEVEL == 9) {
				endGameActivity();
			}
		}

		countDownLevelDuration.start();
	}

	public void endGameActivity() {
		countDownLevelDuration.cancel();
		countDownTestingBegin.cancel();
		finish();
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

	private void updateFaceWithNewImage() {
		imageFace.setImageDrawable(getRandomFace());
	}

	private Drawable getRandomFace() {
		CURRENT_FACE = random.nextInt(FACE_RANGE);
		return faces.get(CURRENT_FACE);
	}

	private void loadImagesIntoFaces() {
		faces.put(WEST, getResources().getDrawable(R.drawable.mariama_w));
		faces.put(EAST, getResources().getDrawable(R.drawable.mariama_e));
		faces.put(NORTH, getResources().getDrawable(R.drawable.mariama_n));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.mariama_s));
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.mariama_nw));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.mariama_ne));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.mariama_sw));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.mariama_se));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		if (GAME_MODE == GAME_ON && event.getAction() == MotionEvent.ACTION_DOWN) {
			GAME_MODE = GAME_PAUSE;
			wrongAction();
		}
		return super.onTouchEvent(event);
	}
	

	@Override
	protected void onDestroy() {
		Log.d("JSON",
				"Saving statistics: "
						+ SharedPreferencesUtil
								.addOrUpdateStatisticOnUser(this,
										SharedPreferencesUtil
												.getCurrentUsersName(this),
										statistic));
		countDownLevelDuration.cancel();
		countDownTestingBegin.cancel();
		super.onDestroy();
	}
}
