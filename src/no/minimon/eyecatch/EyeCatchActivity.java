package no.minimon.eyecatch;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import no.minimon.eyecatch.fragment.*;
import no.minimon.eyecatch.fragment.StatisticFragment.OnDeletedContinueInfo;
import no.minimon.eyecatch.game.ExtraTestingActivity;
import no.minimon.eyecatch.game.EyeCatchGameActivity;
import no.minimon.eyecatch.game.GeneralizationTestingActivity;
import no.minimon.eyecatch.game.TouchTrainingActivity;
import no.minimon.eyecatch.util.NotificationUtil;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static no.minimon.eyecatch.util.NotificationUtil.alertUser;

public class EyeCatchActivity extends FragmentActivity implements
		OnDeletedContinueInfo {

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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_main_dump_local:
				saveDataToLocal();
				alertUser(this, "Data saved to file");
				return true;
			case R.id.menu_main_dump_share:
				sendDataViaEmail();
				return true;
			default:
				break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void sendDataViaEmail() {
		File attachment = saveDataToLocal();
		if (attachment != null) {
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.putExtra(Intent.EXTRA_SUBJECT, "eyeCatch data");
			share.putExtra(Intent.EXTRA_TEXT,
					"The data is attached to this e-mail.");
			Uri uri = Uri.fromFile(attachment);
			share.putExtra(Intent.EXTRA_STREAM, uri);
			share.setType("text/message");
			startActivity(Intent.createChooser(share, "Send data"));
		}
	}

	private File saveDataToLocal() {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(path, "eyeCatchData-"
					+ new Date(System.currentTimeMillis()).toString() + ".txt");

			try {
				path.mkdirs();

				OutputStream os = new FileOutputStream(file);
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						os);
				outputStreamWriter.write(getDataAsString());
				outputStreamWriter.close();
				os.close();

				MediaScannerConnection.scanFile(this,
						new String[]{file.toString()}, null,
						new OnScanCompletedListener() {

							@Override
							public void onScanCompleted(String path, Uri uri) {
								Log.i("ExternalStorage", "Scanned " + path
										+ ":");
								Log.i("ExternalStorage", "-> uri=" + uri);
							}
						});

				return file;
			} catch (IOException e) {
				Log.w("ExternalStorage", "Error writing " + file, e);
			}
		} else {
			alertUser(this, "Can't save to external storage");
		}

		return null;
	}

	private String getDataAsString() {
		List<String> users = SharedPreferencesUtil.getUsersAsList(this);
		String data = "";
		for (String name : users) {
			JSONObject user = SharedPreferencesUtil.getUser(this, name);
			try {
				data += user.toString(4);
			} catch (JSONException e) {
				data += "User " + name + " was not added.";
				e.printStackTrace();
			}
			data += "\n";
		}
		return data;
	}

	public void onButtonClick(View view) {
		if (view.getId() == R.id.eyecatch_continue_game) {
			if (isThereASelectedVideo()) {
				Intent intent = new Intent(this, EyeCatchGameActivity.class);
				intent.putExtra(SharedPreferencesUtil.CONTINUE, true);
				startActivity(intent);
			} else {
				alertUser(this, R.string.error_missing_video);
			}
		} else if (view.getId() == R.id.eyecatch_start_game) {
			if (isThereASelectedUserAndVideo()) {
				startActivity(new Intent(this, EyeCatchGameActivity.class));
			} else {
				alertUser(this, R.string.error_missing_user_or_video);
			}
		} else if (view.getId() == R.id.eyecatch_touch_training) {
			if (isThereASelectedVideo()) {
				startActivity(new Intent(this, TouchTrainingActivity.class));
			} else {
				alertUser(this, R.string.error_missing_video);
			}
		} else if (view.getId() == R.id.eyecatch_statistic_testing) {
			if (isThereASelectedUserAndVideo()) {
				startActivity(new Intent(this, ExtraTestingActivity.class));
			} else {
				alertUser(this, R.string.error_missing_user_or_video);
			}
		} else if (view.getId() == R.id.eyecatch_generalization_test) {
			if (isThereASelectedUserAndVideo()) {
				startActivity(new Intent(this, GeneralizationTestingActivity.class));
			} else {
				alertUser(this, R.string.error_missing_user_or_video);
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
					alertUser(this, "Fragment not implemented yet!");
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
					alertUser(this, "Activity not implemented yet!");
					return;
			}
			startActivity(intent);
		}
	}

	private boolean isThereASelectedUserAndVideo() {
		return isThereASelectedVideo()
				&& !SharedPreferencesUtil.getCurrentUsersName(this).isEmpty();
	}

	private boolean isThereASelectedVideo() {
		String filename = SharedPreferencesUtil.getCurrentVideoUri(this);
		if (filename.isEmpty()) {
			return false;
		}
		ContentResolver contentResolver = getContentResolver();
		String[] projection = {MediaStore.MediaColumns.DATA};
		Cursor cursor = contentResolver.query(Uri.parse(filename), projection, null, null, null);
		if(cursor != null
				&& cursor.getCount() > 0) {
			cursor.moveToFirst();
			String filePath = cursor.getString(0);
			cursor.close();
			return new File(filePath).exists();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	                                Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		updateContinueButton();
		setHomeFragment();
	}

	private void setHomeFragment() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.item_detail_container, new HomeFragment()).commit();
	}

	@Override
	protected void onResume() {
		updateContinueButton();
		super.onResume();
	}

	@Override
	public void notifyAboutDeletedContinueInfo() {
		updateContinueButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	private void updateContinueButton() {
		if (SharedPreferencesUtil.hasContinueJson(this)) {
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
	}

}
