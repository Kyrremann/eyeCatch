package no.minimon.eyecatch.game;

import static no.minimon.eyecatch.game.EyeCatchGameActivity.EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.NORTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.RESULT_VIDEOVIEW;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_EAST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.SOUTH_WEST;
import static no.minimon.eyecatch.game.EyeCatchGameActivity.WEST;

import java.util.Random;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.VideoViewActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("InlinedApi")
public class TouchTrainingActivity extends AbstractGameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch_game);

		random = new Random();
		contentView = findViewById(R.id.fullscreen_content);
		contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

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
		while (CURRENT_FACE_DIRECTION == LAST_FACE_DIRECTION) {
			CURRENT_FACE_DIRECTION = getRandomBox();
		}
		LAST_FACE_DIRECTION = CURRENT_FACE_DIRECTION;
		switch (CURRENT_FACE_DIRECTION) {
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
		Intent intent = new Intent(this, VideoViewActivity.class);
		startActivityForResult(intent, RESULT_VIDEOVIEW);
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
