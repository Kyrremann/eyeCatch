package no.minimon.eyecatch.game;

import java.util.Random;

import no.minimon.eyecatch.R;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import no.minimon.eyecatch.util.AndroidVersionUtil;

import static no.minimon.eyecatch.util.AndroidVersionUtil.isRunningJellyBean;

public abstract class AbstractGameActivity extends FragmentActivity {

	protected static final String TRAINING = "training";
	public static final String ENDGAME = "end_game";

	protected static final int VISIBLE = View.VISIBLE;
	protected static final int INVISIBLE = View.INVISIBLE;
	protected static final int PAUSE = 0;
	protected static final int GAME_ON = 1;

	public static final int RESULT_VIDEOVIEW = 256;
	public static final int RESULT_ENDGAME = 512;
	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	public static final int NORTH_WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int SOUTH_WEST = 6;
	public static final int SOUTH_EAST = 7;
	public static final int SHORT = 32;
	public static final int NORMAL = 33;

	protected int CURRENT_FACE_DIRECTION = -1;
	protected int LAST_FACE_DIRECTION = -1;
	protected int NUMBER_OF_TRIALS = 5;
	protected int CURRENT_ITERATION;
	protected int CLICKS_CORRECT = 0, CLICKS_FAIL = 0;
	protected int ROUND_COUNTER = 0;
	protected int GAME_MODE = PAUSE;
	protected String FACE_TYPE, BOX_TYPE;

	protected ImageView imageFace;
	protected ImageView imageNorth;
	protected ImageView imageNorthEast;
	protected ImageView imageEast;
	protected ImageView imageSouthEast;
	protected ImageView imageSouth;
	protected ImageView imageSouthWest;
	protected ImageView imageWest;
	protected ImageView imageNorthWest;

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

	protected Drawable getCorrectArrowFace(int direction, int type) {
		if (FACE_TYPE.equals(getString(R.string.face_mariama))) {
			if (direction == WEST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.mariama_w_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.mariama_w_arrow);
				}
			} else if (direction == EAST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.mariama_e_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.mariama_e_arrow);
				}
			}
		} else if (FACE_TYPE.equals(getString(R.string.face_aurelien))) {
			if (direction == WEST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.aurelien_w_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.aurelien_w_arrow);
				}
			} else if (direction == EAST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.aurelien_e_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.aurelien_e_arrow);
				}
			}
		} else if (FACE_TYPE.equals(getString(R.string.face_joelle))) {
			if (direction == WEST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.joelle_w_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.joelle_w_arrow);
				}
			} else if (direction == EAST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.joelle_e_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.joelle_e_arrow);
				}
			}
		} else if (FACE_TYPE.equals(getString(R.string.face_mike))) {
			if (direction == WEST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.mike_w_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.mike_w_arrow);
				}
			} else if (direction == EAST) {
				if (type == SHORT) {
					return getResources().getDrawable(
							R.drawable.mike_e_short_arrow);
				} else if (type == NORMAL) {
					return getResources().getDrawable(
							R.drawable.mike_e_arrow);
				}
			}
		}

		return null;
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
		faces.put(NORTH_WEST, getResources().getDrawable(R.drawable.aurelien_nw));
		faces.put(NORTH_EAST, getResources().getDrawable(R.drawable.aurelien_ne));
		faces.put(SOUTH_WEST, getResources().getDrawable(R.drawable.aurelien_sw));
		faces.put(SOUTH_EAST, getResources().getDrawable(R.drawable.aurelien_se));
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
	
	protected void loadBoxImages() {
		if (BOX_TYPE.equals(getString(R.string.box))) {
			loadBox();
		} else if (BOX_TYPE.equals(getString(R.string.box_icon))) {
			loadBoxIcon();
		} else if (BOX_TYPE.equals(getString(R.string.box_cat))) {
			loadBoxCat();
		} else if (BOX_TYPE.equals(getString(R.string.box_elephant))) {
			loadBoxElephant();
		} else if (BOX_TYPE.equals(getString(R.string.box_monster))) {
			loadBoxMonster();
		} else if (BOX_TYPE.equals(getString(R.string.box_gorilla))) {
			loadBoxGorilla();
		} else if (BOX_TYPE.equals(getString(R.string.box_dino_1))) {
			loadBoxDino_1();
		} else if (BOX_TYPE.equals(getString(R.string.box_dino_2))) {
			loadBoxDino_2();
		}
	}

	private void loadBox() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box));
	}

	private void loadBoxIcon() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_box));
	}

	private void loadBoxCat() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_cat));
	}
	
	private void loadBoxMonster() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_monster));
	}
	
	private void loadBoxGorilla() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_gorilla));
	}

	private void loadBoxElephant() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_elephant));
	}
	
	private void loadBoxDino_1() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_1));
	}
	
	private void loadBoxDino_2() {
		imageWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageNorth.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageSouth.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageNorthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageNorthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageSouthWest.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
		imageSouthEast.setImageDrawable(getResources().getDrawable(R.drawable.box_dino_2));
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
}
