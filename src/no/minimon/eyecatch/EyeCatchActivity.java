package no.minimon.eyecatch;

import org.json.JSONException;
import org.json.JSONObject;

import no.minimon.eyecatch.fragment.CreateUserFragment;
import no.minimon.eyecatch.fragment.EyeCatchFragment;
import no.minimon.eyecatch.fragment.HomeFragment;
import no.minimon.eyecatch.fragment.SelectStatisticsFragment;
import no.minimon.eyecatch.fragment.SelectUserFragment;
import no.minimon.eyecatch.fragment.SelectVideoFragment;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class EyeCatchActivity extends FragmentActivity implements
		EyeCatchFragment.Callbacks {

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
		
//		SharedPreferencesUtil.createNewStatisticOnCurrentUser(this);
		// JSONObject jsonObject = SharedPreferencesUtil.getCurrentUser(this);
		// try {
		// // System.out.println(jsonObject.toString(4));
		// jsonObject.getJSONObject(SharedPreferencesUtil.STATISTIC).remove("1381425403492");
		// SharedPreferencesUtil.updateUserInfo(this, jsonObject);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}

	public void onButtonClick(View view) {
		if (view.getId() == R.id.eyecatch_start_game) {
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

	public boolean isThereAselectedVideo() {
		return !SharedPreferencesUtil.getCurrentVideoName(this).isEmpty();
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, CreateUserActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
