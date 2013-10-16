package no.minimon.eyecatch;

import static no.minimon.eyecatch.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.EyeCatchGameActivity.WEST;

import java.util.Random;

import no.minimon.eyecatch.util.SharedPreferencesUtil;

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
public class StatisticTestingActivity extends FragmentActivity {

	private static final int VISIBLE = View.VISIBLE;

	public static final String TRAINING = "training";

	private int CURRENT_FACE = -1;
	private int NUMBER_OF_TRIALS = 5;
	private int CURRENT_ITERATION;
	private int CLICKS_CORRECT = 0, CLICKS_FAIL = 0;

	private ImageView imageFace, imageNorth, imageNorthEast, imageEast,
			imageSouthEast, imageSouth, imageSouthWest, imageWest,
			imageNorthWest;
	private View contentView;
	private Random random;
	private SparseArray<Drawable> faces;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		name = SharedPreferencesUtil.getCurrentUsersName(this);
		NUMBER_OF_TRIALS = SharedPreferencesUtil.getNumberOfTrials(this);

		random = new Random();
		faces = new SparseArray<Drawable>(8);

		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		initBoxes();
		initFace();
		loadImagesIntoFaces();
		setBoxesVisibility(VISIBLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				newGameRound();
			}
		}.start();
	}

	private void newGameRound() {
		if (CURRENT_ITERATION >= NUMBER_OF_TRIALS) {
			finish();
		} else {
			CURRENT_ITERATION++;
		}
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		CURRENT_FACE = getRandomBox();
		imageFace.setImageDrawable(faces.get(CURRENT_FACE));
		imageFace.setVisibility(VISIBLE);
		setBoxesVisibility(VISIBLE);
	}

	public void onImageClicked(View view) {
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

	private void checkForCorrectDirection(int direction) {
		if (CURRENT_FACE == direction) {
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

	private void saveStatistic() {
		SharedPreferencesUtil.createAndAddStatisticTestingUser(this, name);
		SharedPreferencesUtil.addStatisticToStatisticTestingUser(this, name,
				CLICKS_CORRECT, CLICKS_FAIL);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			CLICKS_FAIL++;
			newGameRound();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		saveStatistic();
		super.onDestroy();
	}

}
