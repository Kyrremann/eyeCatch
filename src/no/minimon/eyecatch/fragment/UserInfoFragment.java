package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.SharedPreferencesUtil.AGE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.DURATION_PER_TRIAL;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.MASTERY_CRITERIA;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NAME;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NUMBER_OF_TRIALS;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.VIDEO_DURATION;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.getUser;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	private JSONObject user;
	private View root;
	private TextView durationPerTrail, numberOfTrials, masteryCriteria,
			videoDuration;
	private EditText age;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String name = getArguments().getString(NAME);
		user = getUser(getActivity(), name);
		root = inflater.inflate(R.layout.fragment_user_info, container, false);

		try {
			initInformation();
			initSeekbars(root);
			initButtons();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return root;
	}

	private void initButtons() {
		((ImageView) root.findViewById(R.id.user_delete))
				.setOnClickListener(this);
		((Button) root.findViewById(R.id.button_cancel))
				.setOnClickListener(this);
		((Button) root.findViewById(R.id.button_save)).setOnClickListener(this);
		((Button) root.findViewById(R.id.button_select_user))
				.setOnClickListener(this);
	}

	private void initInformation() throws JSONException {
		TextView textView = (TextView) root.findViewById(R.id.username);
		textView.setText(user.getString(NAME));

		age = (EditText) root.findViewById(R.id.age_edit);
		age.setText(user.getString(AGE));

	}

	private void initSeekbars(View root) throws JSONException {
		SeekBar seekBar = (SeekBar) root
				.findViewById(R.id.seekBar_times_per_trail);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(user.getInt(DURATION_PER_TRIAL) / 1000);
		durationPerTrail = (TextView) root
				.findViewById(R.id.textView_times_per_trail);
		durationPerTrail.setText(String.format("%02d",
				user.getInt(DURATION_PER_TRIAL) / 1000));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_number_of_trails);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(user.getInt(NUMBER_OF_TRIALS));
		numberOfTrials = (TextView) root
				.findViewById(R.id.textView_number_of_trails);
		numberOfTrials.setText(String.format("%02d",
				user.getInt(NUMBER_OF_TRIALS)));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_mastery_criteria);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(user.getInt(MASTERY_CRITERIA));
		masteryCriteria = (TextView) root
				.findViewById(R.id.textView_mastery_criteria);
		masteryCriteria.setText(String.format("%02d",
				user.getInt(MASTERY_CRITERIA)));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_video_duration);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(user.getInt(VIDEO_DURATION) / 1000);
		videoDuration = (TextView) root
				.findViewById(R.id.textView_video_duration);
		videoDuration.setText(String.format("%02d",
				user.getInt(VIDEO_DURATION) / 1000));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			switch (seekBar.getId()) {
			case R.id.seekBar_times_per_trail:
				durationPerTrail.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_number_of_trails:
				numberOfTrials.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_mastery_criteria:
				masteryCriteria.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_video_duration:
				videoDuration.setText(String.format("%02d", progress));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_save:
			saveData();
			toast("Information saved");
			break;
		case R.id.button_cancel:
			finishActivity();
			break;
		case R.id.button_select_user:
			selectUser();
			toast("User selected");
			break;
		case R.id.user_delete:
			deleteUser();
			break;
		default:
			break;
		}
	}

	private void deleteUser() {
		try {
			String name = user.getString(NAME);
			SharedPreferencesUtil.removeUser(getActivity(),
					name);
			SharedPreferencesUtil.removeContinueInfoIfSameName(getActivity(), name);
			SharedPreferencesUtil.removeSelectedUserIfSameName(getActivity(), name);
			SharedPreferencesUtil.updateActioBarTitle(getActivity(), getActivity().getActionBar());
			finishActivity();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void toast(String message) {
		Toast.makeText(getActivity().getApplicationContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	private void finishActivity() {
		if (getActivity().findViewById(R.id.item_detail_container) != null) {
			HomeFragment fragment = new HomeFragment();
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
		} else {
			getActivity().finish();
		}
	}

	private void selectUser() {
		try {
			JSONObject jsonObject = SharedPreferencesUtil.getUser(
					getActivity(), user.getString(NAME));
			SharedPreferencesUtil.setCurrentUser(getActivity(),
					jsonObject.getString(SharedPreferencesUtil.NAME));
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void saveData() {
		try {
			user.put(AGE, age.getText().toString());
			user.put(
					DURATION_PER_TRIAL,
					Integer.valueOf(durationPerTrail.getText().toString()) * 1000);
			user.put(NUMBER_OF_TRIALS,
					Integer.valueOf(numberOfTrials.getText().toString()));
			user.put(MASTERY_CRITERIA,
					Integer.valueOf(masteryCriteria.getText().toString()));
			user.put(VIDEO_DURATION,
					Integer.valueOf(videoDuration.getText().toString()) * 1000);
			SharedPreferencesUtil.updateUserInfo(getActivity(), user);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
