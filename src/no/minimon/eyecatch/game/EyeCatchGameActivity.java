package no.minimon.eyecatch.game;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.VideoViewActivity;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EyeCatchGameActivity extends AbstractGameActivity implements
		AnimationListener {

	private static final int GAME_PAUSE = 2;
	private static final int BEFORE_GAME = 0;

	private int GAME_MODE;
	private int CURRENT_ITERATION = 0;
	private int CURRENT_ITERATION_CORRECT = 0;
	private int CURRENT_ITERATION_FAIL = 0;
	private int MASTERY_CRITERIA;
	private long LEVEL_DURATION;
	private long ERROR_DURATION;
	private int TESTING_LEVEL = 0;
	private int TRAINING_LEVEL = 0;
	private int FACE_RANGE = 0;

	private ImageView imageWestCircle, imageEastCircle;
	private CountDownTimer countDownLevelDuration, countDownBeginGameLevel,
			countDownBlankScreen;
	private TextView watermark, watermarkData;

	private boolean testingLevel = true;
	private boolean keepFace;
	private boolean deleteContinueWhenDone;
	private JSONObject statistic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		GAME_MODE = GAME_PAUSE;

		if (getIntent().getBooleanExtra(SharedPreferencesUtil.CONTINUE, false)) {
			getContinueInformation();
			deleteContinueWhenDone = true;
		} else {
			statistic = SharedPreferencesUtil.createNewStatistic(this);
		}

		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);
		LEVEL_DURATION = SharedPreferencesUtil.getDurationPerTrial(this);
		MASTERY_CRITERIA = SharedPreferencesUtil.getMasteryCriteria(this);
		ERROR_DURATION = SharedPreferencesUtil.getErrorDuration(this);
		FACE_TYPE = SharedPreferencesUtil.getFace(this);
		BOX_TYPE = SharedPreferencesUtil.getBox(this);
		SharedPreferencesUtil.setLastSeekOnCurrentVideo(this, 0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		faces = new SparseArray<Drawable>(8);
		random = new Random();

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		imageFace = (ImageView) findViewById(R.id.image_face);
		watermark = (TextView) findViewById(R.id.watermark);
		watermarkData = (TextView) findViewById(R.id.watermarkData);

		initCircles();
		initDurations();
		initBoxes();
		loadImagesIntoFaces();
		loadBoxImages();
		setBoxesVisibility(INVISIBLE);
		setCirclesVisbility(INVISIBLE);
		changeFaceToStar();
		setWatermark();
	}

	private void removeContinueInformation() {
		SharedPreferencesUtil.removeContinueJson(this);
	}

	private void getContinueInformation() {
		JSONObject continueInformation = SharedPreferencesUtil
				.getContinueJson(this);
		try {
			long date = continueInformation
					.getLong(SharedPreferencesUtil.CONTINUE_DATE);
			TESTING_LEVEL = continueInformation
					.getInt(SharedPreferencesUtil.CONTINUE_TESTING);
			TRAINING_LEVEL = continueInformation
					.getInt(SharedPreferencesUtil.CONTINUE_TRAINING);
			testingLevel = continueInformation
					.getBoolean(SharedPreferencesUtil.CONTINUE_TESTING_OR_TRAINING);
			SharedPreferencesUtil.setCurrentUser(this,
					continueInformation.getString(SharedPreferencesUtil.NAME));
			statistic = SharedPreferencesUtil.getStatesticFromCurrentUser(this,
					date);

			if (statistic != null) {
				if (testingLevel) {
					JSONArray array = statistic
							.getJSONArray(SharedPreferencesUtil.STATISTIC_TESTING);
					int length = array.length();
					if (length > 0) {
						String testing = array.getString(length - 1);
						JSONArray shorterArray = new JSONArray();
						for (int i = 0; i < length - 1; i++) {
							shorterArray.put(array.get(i));
						}
						statistic.put(SharedPreferencesUtil.STATISTIC_TESTING,
								shorterArray);
						CURRENT_ITERATION_CORRECT = Integer.valueOf(testing);
					}
				} else {
					JSONArray array = statistic
							.getJSONArray(SharedPreferencesUtil.STATISTIC_TRAINING);
					int length = array.length();
					if (length > 0) {
						String training = array.getString(length - 1);
						JSONArray shorterArray = new JSONArray();
						for (int i = 0; i < length - 1; i++) {
							shorterArray.put(array.get(i));
						}
						statistic.put(SharedPreferencesUtil.STATISTIC_TRAINING,
								shorterArray);
						String[] split = training.split("/");
						CURRENT_ITERATION_FAIL = Integer.valueOf(split[0]);
						CURRENT_ITERATION_CORRECT = Integer.valueOf(split[1]);
						CURRENT_ITERATION = 0;
					}
				}
			} else {
				statistic = SharedPreferencesUtil.createNewStatistic(this);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setWatermark() {
		String text = "";
		if (isTraining()) {
			if (TRAINING_LEVEL >= 8) {
				text = "Done";
			} else {
				text += getString(R.string.training);
				text += " " + (char) (65 + TRAINING_LEVEL);
			}
		} else {
			text += getString(R.string.testing);
			if (TESTING_LEVEL < 8) {
				text += " pre-" + (char) (65 + TESTING_LEVEL);
			} else {
				text += " post-H";
			}

		}
		watermark.setText(text);
		if (isTraining()) {
			watermarkData.setText("MC: " + CURRENT_ITERATION + " C: "
					+ CURRENT_ITERATION_CORRECT + " F: "
					+ CURRENT_ITERATION_FAIL);
		} else {
			watermarkData.setText("T: " + CURRENT_ITERATION + " C: "
					+ (CURRENT_ITERATION - CURRENT_ITERATION_FAIL) + " F: "
					+ CURRENT_ITERATION_FAIL);

		}
	}

	private void initDurations() {
		countDownBeginGameLevel = new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				loadTrainingOrTesting();
				GAME_MODE = GAME_ON;
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
		countDownBlankScreen = new CountDownTimer(ERROR_DURATION, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				showFace();
				continueOrNext();
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
					countDownBeginGameLevel.start();
				} else {
					countDownBeginGameLevel.cancel();
					countDownLevelDuration.cancel();
					loadTrainingOrTesting();
					GAME_MODE = GAME_ON;
				}
			}
			break;
		case GAME_ON:
			switch (id) {
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
				updateFaceWithNewImage();
				break;
			}
		default:
			break;
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void wrongAction() {
		if (isTraining()) {
			keepFace = true;
		}

		CURRENT_ITERATION++;
		CURRENT_ITERATION_FAIL++;
		logIteration("WRONG");
		if (isTraining()) {
			CURRENT_ITERATION = 0;
			hideFace();
			setBoxesVisibility(INVISIBLE);
			if (TRAINING_LEVEL == 0
					|| TRAINING_LEVEL == 1) {
				setCirclesVisbility(INVISIBLE);
			}
			countDownBlankScreen.start();
		} else {
			continueOrNext();
		}
	}

	private void checkForCorrectDirection(int direction) {
		countDownLevelDuration.cancel();
		if (CURRENT_FACE_DIRECTION == direction) {
			correctActionVideoOrNext();
		} else {
			wrongAction();
		}
	}

	private void correctActionVideoOrNext() {
		countDownLevelDuration.cancel();
		CURRENT_ITERATION++;
		CURRENT_ITERATION_CORRECT++;
		logIteration("RIGHT");
		if (isTraining()) {
			startVideoViewActivity();
		} else {
			continueOrNext();
		}
	}

	private void logIteration(String tag) {
		Log.d(tag, "CI: " + CURRENT_ITERATION + " - CIC: "
				+ CURRENT_ITERATION_CORRECT + " - CIF: "
				+ CURRENT_ITERATION_FAIL);
	}

	public void startVideoViewActivity() {
		startActivityForResult(new Intent(this, VideoViewActivity.class),
				RESULT_VIDEOVIEW);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_VIDEOVIEW) {
			continueOrNext();
		} else if (resultCode == RESULT_ENDGAME) {
			finish();
		} else {
			super.onActivityResult(requestCode, requestCode, intent);
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void continueOrNext() {
		if (testingLevel && CURRENT_ITERATION >= NUMBER_OF_TRIALS) {
			doneWithLastRound();
		} else if (isTraining() && CURRENT_ITERATION >= MASTERY_CRITERIA) {
			doneWithLastRound();
		} else {
			continueWithCurrent();
		}
	}

	private void continueWithCurrent() {
		GAME_MODE = GAME_PAUSE;

		if (isTraining()) {
			if (TRAINING_LEVEL == 0
					|| TRAINING_LEVEL == 1) {
				setCirclesVisbility(INVISIBLE);
			}
			setBoxesVisibility(INVISIBLE);
			loadTrainingOrTesting();
			GAME_MODE = GAME_ON;
		} else {
			changeFaceToCenter();
			countDownBeginGameLevel.start();
		}

		setWatermark();
	}

	private void doneWithLastRound() {
		addStatesticToJson();

		if (isTraining()) {
			if (TRAINING_LEVEL == 0
					|| TRAINING_LEVEL == 1) {
				setCirclesVisbility(INVISIBLE);
			}
			TRAINING_LEVEL++;
		} else {
			TESTING_LEVEL++;
		}

		CURRENT_ITERATION = 0;
		CURRENT_ITERATION_CORRECT = 0;
		CURRENT_ITERATION_FAIL = 0;
		GAME_MODE = GAME_PAUSE;
		testingLevel = isTraining();
		keepFace = false;

		setBoxesVisibility(INVISIBLE);
		changeFaceToStar();

		setWatermark();

		if (TRAINING_LEVEL == 9 || TESTING_LEVEL == 9) {
			createAndShowEndGameDialog();
		}
	}

	private void addStatesticToJson() {
		try {
			if (testingLevel) {
				statistic.accumulate(SharedPreferencesUtil.STATISTIC_TESTING,
						CURRENT_ITERATION_CORRECT);
			} else {
				statistic.accumulate(SharedPreferencesUtil.STATISTIC_TRAINING,
						CURRENT_ITERATION_FAIL + "/"
								+ CURRENT_ITERATION_CORRECT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void changeFaceToStar() {
		imageFace.setImageDrawable(getResources().getDrawable(R.drawable.star));
	}

	private void initGame() {
		loadTrainingOrTesting();
		GAME_MODE = GAME_ON;
	}

	private void loadTrainingOrTesting() {
		if (isTraining()) {
			// Animation animation = new AlphaAnimation(0f, 1f);
			// animation.setDuration(0); // TODO Let duration be set by the user
			// animation.setAnimationListener(this);

			switch (TRAINING_LEVEL) {
			case 0: // Training level A
				FACE_RANGE = 0;
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 1: // Training level B
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 2: // Training level C
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 3: // Training level D
				FACE_RANGE = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 4: // Training level E
				FACE_RANGE = 3;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 5: // Training level F
				FACE_RANGE = 4;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 6: // Training level G
				FACE_RANGE = 6;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 7: // Training level H
				FACE_RANGE = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				break;
			case 8:
				testingLevel = true;
				loadTrainingOrTesting();
				break;
			default:
				break;
			}
			updateImagesAfterAnimation();
		} else {
			if (TESTING_LEVEL < 9) {
				FACE_RANGE = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
			} else if (TESTING_LEVEL == 9) {
				return;
			}

			countDownLevelDuration.start();
		}
	}

	private void hideFace() {
		imageFace.setVisibility(INVISIBLE);
	}

	private void showFace() {
		imageFace.setVisibility(VISIBLE);
	}

	private boolean isTraining() {
		return !testingLevel;
	}

	private void createAndShowEndGameDialog() {
		endGameActivity();

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.dialog_end_game_title);
		builder.setMessage(R.string.dialog_end_game_message);
		builder.setPositiveButton(android.R.string.yes, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(),
						VideoViewActivity.class);
				intent.putExtra(ENDGAME, true);
				startActivityForResult(intent, RESULT_ENDGAME);
			}
		});
		builder.setNeutralButton(R.string.exit, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public void endGameActivity() {
		saveStatestic();
		countDownLevelDuration.cancel();
		countDownBeginGameLevel.cancel();
		if (deleteContinueWhenDone) {
			removeContinueInformation();
		}
	}

	private void saveStatestic() {
		try {
			statistic.put(SharedPreferencesUtil.STATISTIC_DATE_END,
					System.currentTimeMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("JSON",
				"Saving statistics: "
						+ SharedPreferencesUtil.addOrUpdateStatisticOnUser(
								getApplicationContext(),
								SharedPreferencesUtil
										.getCurrentUsersName(getApplicationContext()),
								statistic));
	}

	private void initCircles() {
		imageWestCircle = (ImageView) findViewById(R.id.image_west_circle);
		imageEastCircle = (ImageView) findViewById(R.id.image_east_circle);
	}

	private void setCirclesVisbility(int visibility) {
		imageWestCircle.setVisibility(visibility);
		imageEastCircle.setVisibility(visibility);
	}

	private void updateFaceWithNewImage() {
		imageFace.setImageDrawable(getRandomFace());
	}

	private Drawable getRandomFace() {
		if (keepFace) {
			keepFace = false;
			return faces.get(CURRENT_FACE_DIRECTION);
		}

		while (CURRENT_FACE_DIRECTION == LAST_FACE_DIRECTION) {
			CURRENT_FACE_DIRECTION = getRandomBox();
		}
		LAST_FACE_DIRECTION = CURRENT_FACE_DIRECTION;
		return faces.get(CURRENT_FACE_DIRECTION);
	}

	@Override
	protected int getRandomBox() {
		return random.nextInt(FACE_RANGE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (GAME_MODE == GAME_ON
				&& event.getAction() == MotionEvent.ACTION_DOWN) {
			GAME_MODE = GAME_PAUSE;
			wrongAction();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		countDownLevelDuration.cancel();
		countDownBeginGameLevel.cancel();
		countDownBlankScreen.cancel();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		countDownLevelDuration.cancel();
		countDownBeginGameLevel.cancel();

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.game_pause_title);
		builder.setMessage(R.string.game_pause_message);
		builder.setPositiveButton(R.string.save_quit, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					addStatesticToJson();
					saveStatestic();
					SharedPreferencesUtil.saveContinueInformation(
							getApplicationContext(),
							SharedPreferencesUtil
									.getCurrentUsersName(getApplicationContext()),
							statistic
									.getLong(SharedPreferencesUtil.STATISTIC_DATE),
							TRAINING_LEVEL, TESTING_LEVEL, testingLevel);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.cancel();
				finish();
			}
		});
		builder.setNeutralButton(android.R.string.cancel,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						countDownBeginGameLevel.start();
						keepFace = true;
						dialog.cancel();
					}
				});
		builder.setNegativeButton(R.string.quit, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		updateImagesAfterAnimation();
	}

	private void updateImagesAfterAnimation() {
		switch (TRAINING_LEVEL) {
		case 0:
			if (CURRENT_FACE_DIRECTION == WEST) {
				imageWestCircle.setVisibility(VISIBLE);
				imageFace.setImageDrawable(getCorrectArrowFace(WEST, NORMAL));
			} else if (CURRENT_FACE_DIRECTION == EAST) {
				imageEastCircle.setVisibility(VISIBLE);
				imageFace.setImageDrawable(getCorrectArrowFace(EAST, NORMAL));
			}

			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			break;
		case 1:
			if (CURRENT_FACE_DIRECTION == WEST) {
				// imageWestCircle.setVisibility(INVISIBLE);
				imageWestCircle.setVisibility(VISIBLE);
				imageWestCircle.setImageDrawable(getResources().getDrawable(R.drawable.half_circle_left));
				imageFace.setImageDrawable(getCorrectArrowFace(WEST, NORMAL));
			} else if (CURRENT_FACE_DIRECTION == EAST) {
				// imageEastCircle.setVisibility(INVISIBLE);
				imageEastCircle.setVisibility(VISIBLE);
				imageEastCircle.setImageDrawable(getResources().getDrawable(R.drawable.half_circle_right));
				imageFace.setImageDrawable(getCorrectArrowFace(EAST, NORMAL));
			}

			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			break;
		case 2: // Training level C
			if (CURRENT_FACE_DIRECTION == WEST) {
				imageFace.setImageDrawable(getCorrectArrowFace(WEST, SHORT));
			} else if (CURRENT_FACE_DIRECTION == EAST) {
				imageFace.setImageDrawable(getCorrectArrowFace(EAST, SHORT));
			}
			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			break;
		case 3: // Training level D
			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			break;
		case 4: // Training level E
			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			imageNorth.setVisibility(VISIBLE);
			break;
		case 5: // Training level F
			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			imageNorth.setVisibility(VISIBLE);
			imageSouth.setVisibility(VISIBLE);
			break;
		case 6: // Training level G
			imageWest.setVisibility(VISIBLE);
			imageEast.setVisibility(VISIBLE);
			imageNorth.setVisibility(VISIBLE);
			imageSouth.setVisibility(VISIBLE);
			imageNorthEast.setVisibility(VISIBLE);
			imageNorthWest.setVisibility(VISIBLE);
			break;
		case 7: // Training level H
			setBoxesVisibility(VISIBLE);
			break;
		default:
			break;
		}
		countDownLevelDuration.start();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}
