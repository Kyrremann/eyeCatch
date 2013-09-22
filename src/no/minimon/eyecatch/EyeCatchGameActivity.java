package no.minimon.eyecatch;

import java.util.Random;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class EyeCatchGameActivity extends FragmentActivity {

	private static final int VISIBLE = View.VISIBLE;
	private static final int INVISIBLE = View.INVISIBLE;
	private View contentView;
	private boolean GAME_ON, GAME_PAUSE;
	private int CURRENT_FACE;
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

		GAME_ON = false;
		GAME_PAUSE = true;
		CURRENT_FACE = -1;
		faces = new SparseArray<Drawable>(8);
		loadImagesIntoFaces();
		random = new Random(System.currentTimeMillis());

		setContentView(R.layout.activity_eyecatch_game);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		imageFace = (ImageView) findViewById(R.id.image_face);
		imageFace.setImageDrawable(getResources().getDrawable(R.drawable.star));

		initBoxes();
		setBoxesVisibility(INVISIBLE);
	}

	public void onImageClicked(View view) {
		if (!GAME_ON) {
			if (view.getId() == R.id.image_face) {
				initGame();
			}
		} else {
			switch (view.getId()) {
			case R.id.image_face:
				if (GAME_PAUSE) {
					GAME_PAUSE = false;
					nextLevelOrTesting();
				} else if (TESTING_LEVEL == 0 || TRAINING_LEVEL == 0) {
					Toast.makeText(this, "Showing video", Toast.LENGTH_SHORT)
							.show();
					correctAction();
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
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void checkForCorrectDirection(int direction) {
		// TODO: if correct then video else buzz
		if (CURRENT_FACE == direction) {
			startActivityForResult(new Intent(this, VideoViewActivity.class),
					RESULT_VIDEOVIEW);
		} else {
			// Wrong, buzz and new face
			Toast.makeText(this, "Sorry, wrong box", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_VIDEOVIEW) {
			correctAction();
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		super.onActivityResult(requestCode, requestCode, intent);
	}

	private void correctAction() {
		imageFace.setImageDrawable(getResources().getDrawable(R.drawable.star));
		if (testingLevel) {
			TESTING_LEVEL++;
		} else {
			TRAINING_LEVEL++;
		}
		testingLevel = !testingLevel;
		GAME_PAUSE = true;
		hideAllBoxes();
	}

	private void hideAllBoxes() {
		setBoxesVisibility(INVISIBLE);
	}

	private void initGame() {
		GAME_ON = true;
		/* TODO: Start game
		- Pick/load level
		- Load video
		- Start game */
		// TODO: Check if video exist
		loadLevelOrTesting();
		GAME_PAUSE = false;
	}

	void nextLevelOrTesting() {
		loadLevelOrTesting();
	}

	private void loadLevelOrTesting() {
		// showAllBoxes();
		if (testingLevel) {
			switch (TESTING_LEVEL) {
			case 0:
				imageFace.setImageDrawable(getResources().getDrawable(
						R.drawable.mariama_center_brighted));
				break;
			case 1:
				face_range = 1;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				if (CURRENT_FACE == WEST) {
					imageWest.setVisibility(VISIBLE);
				} else if (CURRENT_FACE == EAST) {
					imageEast.setVisibility(VISIBLE);
				}
				break;
			case 2:
				face_range = 3;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				break;
			case 3:
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
			default:
				break;
			}
		} else {
			switch (TRAINING_LEVEL) {
			case 0:
				imageFace.setImageDrawable(getResources().getDrawable(
						R.drawable.mariama_center));
				break;
			case 1:
				face_range = 1;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				break;
			case 2:
				face_range = 4;
				updateFaceWithNewImage();
				setBoxesVisibility(INVISIBLE);
				imageWest.setVisibility(VISIBLE);
				imageEast.setVisibility(VISIBLE);
				imageNorth.setVisibility(VISIBLE);
				imageSouth.setVisibility(VISIBLE);
				break;
			case 3:
				face_range = 8;
				updateFaceWithNewImage();
				setBoxesVisibility(VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	private void showAllBoxes() {
		setBoxesVisibility(VISIBLE);
	}

	private void hideRestOfTheBoxes() {
		for (int i = 0; i < faces.size(); i++) {
			if (i == CURRENT_FACE) {
				continue;
			}
			switch (i) {
			case WEST:
				imageWest.setVisibility(INVISIBLE);
				break;
			case EAST:
				imageEast.setVisibility(INVISIBLE);
				break;
			case NORTH:
				imageNorth.setVisibility(INVISIBLE);
				break;
			case SOUTH:
				imageSouth.setVisibility(INVISIBLE);
				break;
			case NORTH_WEST:
				imageNorthWest.setVisibility(INVISIBLE);
				break;
			case NORTH_EAST:
				imageNorthEast.setVisibility(INVISIBLE);
				break;
			case SOUTH_WEST:
				imageSouthWest.setVisibility(INVISIBLE);
				break;
			case SOUTH_EAST:
				imageSouthEast.setVisibility(INVISIBLE);
				break;
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
		return super.onTouchEvent(event);
	}
}
