package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class EyeCatchGameActivity extends FragmentActivity {

	private static final int VISIBLE = View.VISIBLE;
	private static final int INVISIBLE = View.INVISIBLE;
	private static final int PAUSE = 2;
	private static final int GAME_ON = 1;
	private static final int BEFORE_GAME = 0;
	private View contentView;
	private int CURRENT_FACE, GAME_MODE, LEVEL_ITERATION, LEVEL_DURATION,
			CURRENT_ITERATION, MASTERY_CRITERIA;
	private SparseArray<Drawable> faces;
	private ImageView imageFace, imageNorth, imageNorthEast, imageEast,
			imageSouthEast, imageSouth, imageSouthWest, imageWest,
			imageNorthWest;
	private Random random;
	private int TESTING_LEVEL = 0;
	private int TRAINING_LEVEL = 0;
	private int face_range = 0;
	private boolean testingLevel = true;

	public static final int RESULT_VIDEOVIEW = 256;
	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	public static final int NORTH_WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int SOUTH_WEST = 6;
	public static final int SOUTH_EAST = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TRAINING_LEVEL = 2; // uncomment to start direct with boxes

		CURRENT_FACE = -1;
		CURRENT_ITERATION = 0;
		GAME_MODE = BEFORE_GAME;
		LEVEL_ITERATION = SharedPreferencesUtil.getNumberOfTrials(this);
		LEVEL_DURATION = SharedPreferencesUtil.getDurationPerTrial(this);
		SharedPreferencesUtil.setLastSeekOnCurrentVideo(this, 0);
		MASTERY_CRITERIA = SharedPreferencesUtil.getMasteryCriteria(this);

		faces = new SparseArray<Drawable>(8);
		loadImagesIntoFaces();
		random = new Random();

		setContentView(R.layout.activity_eyecatch_game);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		imageFace = (ImageView) findViewById(R.id.image_face);
		// imageFace.setImageDrawable(getResources().getDrawable(R.drawable.star));

		initBoxes();
		setBoxesVisibility(INVISIBLE);
	}

	public void onImageClicked(View view) {
		int id = view.getId();
		switch (GAME_MODE) {
		case BEFORE_GAME:
			if (view.getId() == R.id.image_face) {
				initGame();
			}
		case PAUSE:
			if (id == R.id.image_face) {
				loadTrainingOrTesting();
				GAME_MODE = GAME_ON;
			}
			break;
		case GAME_ON:
			switch (id) {
			case R.id.image_face:
				if (testingLevel) {
					wrongAction();
				} else if (TRAINING_LEVEL == 0 || TRAINING_LEVEL == 1) {
					// Kun de to første traingin level som bruker image_face
					// I den første levelen er det kun tiden som kan gå ut
					CURRENT_ITERATION++;
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
		if (!testingLevel) {
			CURRENT_ITERATION = 0;
			continueOrNext();
		} else {
			CURRENT_ITERATION++;
			continueOrNext();
		}
	}

	private void checkForCorrectDirection(int direction) {
		// TODO: if correct then video else buzz
		if (CURRENT_FACE == direction) {
			CURRENT_ITERATION++;
			correctActionVideoOrNext();
		} else {
			// Wrong, buzz and new face
			// Toast.makeText(this, "Sorry, wrong box",
			// Toast.LENGTH_SHORT).show();
			wrongAction();
		}
	}

	private void correctActionVideoOrNext() {
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
		if (CURRENT_ITERATION == LEVEL_ITERATION) {
			doneWithLastRound();
		} else {
			continueWithCurrent();
		}
	}

	private void continueWithCurrent() {
		// loadTrainingOrTesting();
		GAME_MODE = PAUSE;
		changeFaceToCenter();
	}

	private void doneWithLastRound() {
		if (!testingLevel) {
			TRAINING_LEVEL++;
		} else {
			TESTING_LEVEL++;
		}
		CURRENT_ITERATION = 0;
		GAME_MODE = PAUSE;
		testingLevel = !testingLevel;
		setBoxesVisibility(INVISIBLE);
		changeFaceToCenter();
	}

	public void changeFaceToCenter() {
		imageFace.setImageDrawable(getResources().getDrawable(
				R.drawable.mariama_center));
	}

	private void initGame() {
		/* TODO: Start game
		- Pick/load level
		- Load video
		- Start game
		- Check if video exist */
		loadTrainingOrTesting();
		GAME_MODE = GAME_ON;
	}

	private void loadTrainingOrTesting() {
		if (!testingLevel) {
			switch (TRAINING_LEVEL) {
			case 0: // Training level A
				imageFace.setImageDrawable(getResources().getDrawable(
						R.drawable.mariama_center_brighted));
				break;
			case 1: // Training level B
				changeFaceToCenter();
				break;
			case 2: // Training level C
				face_range = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				if (CURRENT_FACE == WEST) {
					imageWest.setVisibility(VISIBLE);
				} else if (CURRENT_FACE == EAST) {
					imageEast.setVisibility(VISIBLE);
				}
				break;
			case 3: // Training level D
				face_range = 2;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				break;
			case 4: // Training level E
				face_range = 3;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				break;
			case 5: // Training level F
				face_range = 4;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				imageSouth.setVisibility(VISIBLE);
			case 6: // Training level G
				face_range = 6;
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
				face_range = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
				break;
			case 8:
				finish();
				break;
			default:
				break;
			}
		} else {
			if (TESTING_LEVEL < 8) {
				face_range = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
			} else if (TESTING_LEVEL == 8) {
				finish();
			}
		}
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
		CURRENT_FACE = random.nextInt(face_range);
		// System.out.printf("Range: %d - Got: %d", face_range, CURRENT_FACE);
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
		wrongAction();
		return super.onTouchEvent(event);
	}
}
