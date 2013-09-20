package no.minimon.eyecatch;

import java.util.Random;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class EyeCatchGameActivity extends FragmentActivity {

	private View contentView;
	private boolean GAME_ON;
	private int CURRENT_FACE;
	private SparseArray<Drawable> faces;
	private ImageView faceImageView;
	private Random random;

	public static final int RESULT_VIDEOVIEW = 256;
	public static final int NORTH = 0;
	public static final int NORTH_EAST = 1;
	public static final int EAST = 2;
	public static final int SOUTH_EAST = 3;
	public static final int SOUTH = 4;
	public static final int NORTH_WEST = 5;
	public static final int WEST = 6;
	public static final int SOUTH_WEST = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GAME_ON = false;
		CURRENT_FACE = -1;
		faces = new SparseArray<Drawable>(8);
		loadImagesIntoFaces();
		random = new Random();

		setContentView(R.layout.activity_eyecatch_game);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		faceImageView = (ImageView) findViewById(R.id.image_face);
	}

	public void onImageClicked(View view) {
		if (!GAME_ON) {
			if (view.getId() == R.id.image_face) {
				initGame();
			}
		} else {
			switch (view.getId()) {
			case R.id.image_face:
				break;
			case R.id.image_north:
				checkForCorrectDirection(NORTH);
				break;
			case R.id.image_north_east:
				checkForCorrectDirection(NORTH_EAST);
				break;
			case R.id.image_east:
				checkForCorrectDirection(EAST);
				break;
			case R.id.image_south_east:
				checkForCorrectDirection(SOUTH_EAST);
				break;
			case R.id.image_south:
				checkForCorrectDirection(SOUTH);
				break;
			case R.id.image_south_west:
				checkForCorrectDirection(SOUTH_WEST);
				break;
			case R.id.image_west:
				checkForCorrectDirection(WEST);
				break;
			case R.id.image_north_west:
				checkForCorrectDirection(NORTH_WEST);
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
			// Videotime
			// updateFaceWithNewImage();
		} else {
			// Wrong, buzz and new face
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_VIDEOVIEW) {
			updateFaceWithNewImage();
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		super.onActivityResult(requestCode, requestCode, intent);
	}

	private void initGame() {
		GAME_ON = true;
		/* TODO: Start game
		 - Pick/load level
		 - Load video
		 - Start game
		 */
		updateFaceWithNewImage();
	}

	private void updateFaceWithNewImage() {
		faceImageView.setImageDrawable(getRandomFace());
	}

	private Drawable getRandomFace() {
		CURRENT_FACE = random.nextInt(7);
		return faces.get(CURRENT_FACE);
	}

	private void loadImagesIntoFaces() {
		faces.put(NORTH, getResources().getDrawable(R.drawable.mariama_n));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.mariama_ne));
		faces.put(EAST, getResources().getDrawable(R.drawable.mariama_e));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.mariama_se));
		faces.put(SOUTH, getResources().getDrawable(R.drawable.mariama_s));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.mariama_sw));
		faces.put(WEST, getResources().getDrawable(R.drawable.mariama_w));
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.mariama_nw));
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
