package no.minimon.eyecatch;

import no.minimon.eyecatch.fragment.CreateUserFragment;
import no.minimon.eyecatch.fragment.HomeFragment;
import no.minimon.eyecatch.fragment.SelectStatisticsFragment;
import no.minimon.eyecatch.fragment.SelectUserFragment;
import no.minimon.eyecatch.fragment.SelectVideoFragment;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EyeCatchActivity extends FragmentActivity {

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;

			HomeFragment fragment = new HomeFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
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
				startActivity(new Intent(this, StatisticTestingActivity.class));
				// createAndShowStatisticTestingDialog();
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

	private void createAndShowStatisticTestingDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.dialog_statistic_testing_title);
		alert.setMessage(R.string.dialog_statistic_testing_message);

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				if (name.isEmpty())
					return;

				Intent intent = new Intent(getApplicationContext(),
						StatisticTestingActivity.class);
				intent.putExtra(SharedPreferencesUtil.NAME, name);
				startActivity(intent);
			}
		});

		alert.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		alert.show();
	}

	private boolean isThereASelectedUserAndVideo() {
		return isThereAselectedVideo()
				&& !SharedPreferencesUtil.getCurrentUsersName(this).isEmpty();
	}

	public boolean isThereAselectedVideo() {
		return !SharedPreferencesUtil.getCurrentVideoName(this).isEmpty();
	}

	@Override
	protected void onResume() {
		JSONObject jsonObject = SharedPreferencesUtil.getContinueJson(this);
		if (jsonObject != null) {
			Button button = (Button) findViewById(R.id.eyecatch_continue_game);
			button.setEnabled(true);
			try {
				button.setText("Continue with "
						+ jsonObject.getString(SharedPreferencesUtil.NAME));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onResume();
	}
}
