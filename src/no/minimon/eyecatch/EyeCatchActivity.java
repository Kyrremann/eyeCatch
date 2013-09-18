package no.minimon.eyecatch;

import no.minimon.eyecatch.fragment.CreateUserFragment;
import no.minimon.eyecatch.fragment.EyeCatchFragment;
import no.minimon.eyecatch.fragment.SelectUserFragment;
import no.minimon.eyecatch.fragment.SelectVideoFragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.RemoteControlClient.MetadataEditor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class EyeCatchActivity extends FragmentActivity implements
		EyeCatchFragment.Callbacks {

	public static final String EYECATCH_USERS = "eyecatch_users";
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eyecatch);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;

			CreateUserFragment fragment = new CreateUserFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
		}

		// Creating test user to start the app with
		// First mock up JSONObject
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", "Bao");
			jsonObject.put("age", "12");
			jsonObject.put("timer_per_trial", "5");
			jsonObject.put("number_of_trials", "10");
			jsonObject.put("mastery_criteria", "10");

			// Then create preference and save it
			SharedPreferences preferences = getSharedPreferences(
					getPackageName(), Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.putString(jsonObject.getString("name"),
					jsonObject.toString());
			editor.putString(EYECATCH_USERS, "Bao;Lars;Kyrre;Per;Pål;Espen");
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onButtonClick(View view) {
		if (mTwoPane) {
			if (view.getId() == R.id.create_user_create_user) {
				// TODO: Check if everything is typed in correctly and create a
				// new
				// user
				Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT)
						.show();
			} else {
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
				case R.id.eyecatch_touch_training:

					// break;

				default:
					Toast.makeText(this, "Not implemented yet!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, fragment).commit();
				return;
			}
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

				// break;
			case R.id.eyecatch_touch_training:

				// break;

			default:
				Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			startActivity(intent);
		}
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
