package no.minimon.eyecatch.game;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

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


	protected ImageView imageFace, imageNorth, imageNorthEast, imageEast,
			imageSouthEast, imageSouth, imageSouthWest, imageWest,
			imageNorthWest;

	protected SparseArray<Drawable> faces;

	protected View contentView;
	protected Random random;


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

	private boolean isRunningJellyBean() {return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;}
}
