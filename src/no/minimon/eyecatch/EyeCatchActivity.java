package no.minimon.eyecatch;

import no.minimon.eyecatch.fragment.CreateUserFragment;
import no.minimon.eyecatch.fragment.HomeFragment;
import no.minimon.eyecatch.fragment.SelectStatisticsFragment;
import no.minimon.eyecatch.fragment.SelectUserFragment;
import no.minimon.eyecatch.fragment.SelectVideoFragment;
import no.minimon.eyecatch.fragment.StatisticFragment.OnDeletedContinueInfo;
import no.minimon.eyecatch.game.EyeCatchGameActivity;
import no.minimon.eyecatch.game.ExtraTestingActivity;
import no.minimon.eyecatch.game.TouchTrainingActivity;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EyeCatchActivity extends FragmentActivity implements OnDeletedContinueInfo {

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;

			setHomeFragment();
		}

		SharedPreferencesUtil.updateActioBarTitle(this, getActionBar());
	}

	public void onButtonClick(View view) {
		if (view.getId() == R.id.eyecatch_continue_game) {
			if (isThereAselectedVideo()) {
				Intent intent = new Intent(this, EyeCatchGameActivity.class);
				intent.putExtra(SharedPreferencesUtil.CONTINUE, true);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_missing_video),
						Toast.LENGTH_SHORT).show();
			}
		} else if (view.getId() == R.id.eyecatch_start_game) {
			if (isThereASelectedUserAndVideo()) {
				startActivity(new Intent(this, EyeCatchGameActivity.class));
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_missing_user_or_video),
						Toast.LENGTH_SHORT).show();
			}
		} else if (view.getId() == R.id.eyecatch_touch_training) {
			if (isThereAselectedVideo()) {
				startActivity(new Intent(this, TouchTrainingActivity.class));
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_missing_video),
						Toast.LENGTH_SHORT).show();
			}
		} else if (view.getId() == R.id.eyecatch_statistic_testing) {
			if (isThereASelectedUserAndVideo()) {
				startActivity(new Intent(this, ExtraTestingActivity.class));
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_missing_user_or_video),
						Toast.LENGTH_SHORT).show();
			}
		} else if (mTwoPane) {
			Fragment fragment = null;
			switch (view.getId()) {
			case R.id.eyecatch_select_user:
				fragment = new SelectUserFragment();
				break;
			case R.id.eyecatch_create_user:
				fragment = new CreateUserFragment();
				break;
			case R.id.eyecatch_select_video:
				fragment = new SelectVideoFragment();
				break;
			case R.id.eyecatch_select_statistic:
				fragment = new SelectStatisticsFragment();
				break;
			default:
				Toast.makeText(getApplicationContext(),
						"Fragment not implemented yet!", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
			return;
		} else {
			Intent intent = null;
			switch (view.getId()) {
			case R.id.eyecatch_select_user:
				intent = new Intent(this, SelectUserAcitivity.class);
				break;
			case R.id.eyecatch_create_user:
				intent = new Intent(this, CreateUserActivity.class);
				break;
			case R.id.eyecatch_select_video:
				intent = new Intent(this, SelectVideoActivity.class);
				break;
			case R.id.eyecatch_select_statistic:
				intent = new Intent(this, SelectStatisticAcitivity.class);
				break;
			default:
				Toast.makeText(getApplicationContext(),
						"Activity not implemented yet!", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			startActivity(intent);
		}
	}

	private boolean isThereASelectedUserAndVideo() {
		return isThereAselectedVideo()
				&& !SharedPreferencesUtil.getCurrentUsersName(this).isEmpty();
	}

	private boolean isThereAselectedVideo() {
		return !SharedPreferencesUtil.getCurrentVideoName(this).isEmpty();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		updateContinueButton();
		setHomeFragment();
	}

	private void setHomeFragment() {
		HomeFragment fragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.item_detail_container, fragment).commit();
	}

	@Override
	protected void onResume() {
		updateContinueButton();
		super.onResume();
	}

	private void updateContinueButton() {
		JSONObject jsonObject = SharedPreferencesUtil.getContinueJson(this);
		Button button = (Button) findViewById(R.id.eyecatch_continue_game);
		if (jsonObject.length() > 0) {
			button.setEnabled(true);
			try {
				button.setText("Continue with "
						+ jsonObject.getString(SharedPreferencesUtil.NAME));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			button.setEnabled(false);
			button.setText(R.string.eyecatch_continue_game);
		}
	}

	@Override
	public void notifyAboutDeletedContinueInfo() {
		System.out.println("It works!");
		updateContinueButton();
	}
}
