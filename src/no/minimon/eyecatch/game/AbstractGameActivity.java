package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.game.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.WEST;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import no.minimon.eyecatch.R;

public abstract class AbstractGameActivity extends FragmentActivity {

	protected static final int VISIBLE = View.VISIBLE;
	protected static final int INVISIBLE = View.INVISIBLE;
	protected static final int PAUSE = 0;
	protected static final int GAME_ON = 1;
	protected static final String TRAINING = "training";
	public static final String ENDGAME = "end_game";

	protected int CURRENT_FACE_DIRECTION = -1;
	protected int LAST_FACE_DIRECTION = -1;
	protected int NUMBER_OF_TRIALS = 5;
	protected int CURRENT_ITERATION;
	protected int CLICKS_CORRECT = 0, CLICKS_FAIL = 0;
	protected int ROUND_COUNTER = 0;
	protected int GAME_MODE = PAUSE;
	protected String FACE_TYPE;

	protected ImageView imageFace, imageNorth, imageNorthEast, imageEast,
			imageSouthEast, imageSouth, imageSouthWest, imageWest,
			imageNorthWest;

	protected SparseArray<Drawable> faces;

	protected View contentView;
	protected Random random;
	
	protected Drawable getCorrectCenterImage() {
		if (FACE_TYPE.equals(getString(R.string.face_mariama))) {
			return getResources().getDrawable(R.drawable.mariama_center); 
		} else if (FACE_TYPE.equals(getString(R.string.face_aurelien))) {
			return getResources().getDrawable(R.drawable.aurelien_center);
		} else if (FACE_TYPE.equals(getString(R.string.face_joelle))) {
			return getResources().getDrawable(R.drawable.joelle_center);
		} else if (FACE_TYPE.equals(getString(R.string.face_mike))) {
			return getResources().getDrawable(R.drawable.mike_center);
		} else {
			return getResources().getDrawable(R.drawable.mariama_center);
		}
	}

	protected void initFace() {
		imageFace = (ImageView) findViewById(R.id.image_face);
	}

	protected void loadImagesIntoFaces() {
		if (FACE_TYPE.equals(getString(R.string.face_mariama))) {
			loadFacesOfMariama();
		} else if (FACE_TYPE.equals(getString(R.string.face_aurelien))) {
			loadFacesOfAurelien();
		} else if (FACE_TYPE.equals(getString(R.string.face_joelle))) {
			loadFacesOfJoelle();
		} else if (FACE_TYPE.equals(getString(R.string.face_mike))) {
			loadFacesOfMike();
		}
	}

	public void changeFaceToCenter() {
		if (FACE_TYPE.equals(getString(R.string.face_mariama))) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.mariama_center));
		} else if (FACE_TYPE.equals(getString(R.string.face_aurelien))) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.aurelien_center));
		} else if (FACE_TYPE.equals(getString(R.string.face_joelle))) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.joelle_center));
		} else if (FACE_TYPE.equals(getString(R.string.face_mike))) {
			imageFace.setImageDrawable(getResources().getDrawable(
					R.drawable.mike_center));
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

	protected void initBoxes() {
		imageWest = (ImageView) findViewById(R.id.image_west);
		imageEast = (ImageView) findViewById(R.id.image_east);
		imageNorth = (ImageView) findViewById(R.id.image_north);
		imageSouth = (ImageView) findViewById(R.id.image_south);
		imageNorthWest = (ImageView) findViewById(R.id.image_north_west);
		imageNorthEast = (ImageView) findViewById(R.id.image_north_east);
		imageSouthWest = (ImageView) findViewById(R.id.image_south_west);
		imageSouthEast = (ImageView) findViewById(R.id.image_south_east);
	}

	protected void setBoxesVisibility(int visibility) {
		imageWest.setVisibility(visibility);
		imageEast.setVisibility(visibility);
		imageNorth.setVisibility(visibility);
		imageSouth.setVisibility(visibility);
		imageNorthWest.setVisibility(visibility);
		imageNorthEast.setVisibility(visibility);
		imageSouthWest.setVisibility(visibility);
		imageSouthEast.setVisibility(visibility);
	}

	protected int getRandomBox() {
		return random.nextInt(8);
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

	private boolean isRunningJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
}
