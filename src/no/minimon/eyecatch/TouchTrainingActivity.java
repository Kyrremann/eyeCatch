package no.minimon.eyecatch;

import java.util.Random;

import no.minimon.eyecatch.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("InlinedApi")
public class TouchTrainingActivity extends FragmentActivity {

	private static final int VISIBLE = View.VISIBLE;
	private static final int INVISIBLE = View.INVISIBLE;

	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
	public static final int NORTH_WEST = 4;
	public static final int NORTH_EAST = 5;
	public static final int SOUTH_WEST = 6;
	public static final int SOUTH_EAST = 7;
	public static final int RESULT_VIDEOVIEW = 256;

	private ImageView imageNorth, imageNorthEast, imageEast, imageSouthEast,
			imageSouth, imageSouthWest, imageWest, imageNorthWest;
	private View contentView;
	private Random random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

		random = new Random();
		contentView = findViewById(R.id.fullscreen_content);

		initBoxes();
		setBoxesVisibility(INVISIBLE);
		

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().hide();
		}

		new CountDownTimer(1000, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				startGame();
			}
		}.start();
	}

	private void startGame() {
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		setBoxesVisibility(INVISIBLE);
		switch (getRandomBox()) {
		case WEST:
			imageWest.setVisibility(VISIBLE);
			break;
		case EAST:
			imageEast.setVisibility(VISIBLE);
			break;
		case NORTH:
			imageNorth.setVisibility(VISIBLE);
			break;
		case SOUTH:
			imageSouth.setVisibility(VISIBLE);
			break;
		case NORTH_WEST:
			imageNorthWest.setVisibility(VISIBLE);
			break;
		case NORTH_EAST:
			imageNorthEast.setVisibility(VISIBLE);
			break;
		case SOUTH_WEST:
			imageSouthWest.setVisibility(VISIBLE);
			break;
		case SOUTH_EAST:
			imageSouthEast.setVisibility(VISIBLE);
			break;
		default:
			break;
		}
	}

	public void onImageClicked(View view) {
		switch (view.getId()) {
		case R.id.image_face:
			break;
		default: // This is a fix, as you can only click on elements that are
					// visible
			showVideo();
			break;
		}
	}

	private void showVideo() {
		// TODO: Show video with bundle
		// startActivityForResult(new Intent(getActivity(),
		// VideoViewActivity.class),
		// RESULT_VIDEOVIEW);
		onActivityResult(0, RESULT_VIDEOVIEW, new Intent());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_VIDEOVIEW) {
			startGame();
		}

		super.onActivityResult(requestCode, resultCode, data);
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

	private int getRandomBox() {
		return random.nextInt(8);
	}

}
