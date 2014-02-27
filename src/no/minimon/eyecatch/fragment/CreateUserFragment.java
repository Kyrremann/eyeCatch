package no.minimon.eyecatch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.NotificationUtil;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import static no.minimon.eyecatch.util.NotificationUtil.alertUser;

public class CreateUserFragment extends Fragment implements OnSeekBarChangeListener, OnClickListener {

	private TextView durationPerTrail, numberOfTrials, masteryCriteria,
			videoDuration;
	private EditText editName, editAge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_create_user, container, false);
		initSeekbars(rootView);
		editName = (EditText) rootView.findViewById(R.id.edit_username);
		editAge = (EditText) rootView.findViewById(R.id.edit_age);
		((Button) rootView.findViewById(R.id.create_user_create_user)).setOnClickListener(this);

		return rootView;
	}

	private boolean isNameAndAgeSet() {
		return editName.getText().length() > 0
				&& editAge.getText().length() > 0;
	}

	private void initSeekbars(View root) {
		SeekBar seekBar = (SeekBar) root
				.findViewById(R.id.seekBar_times_per_trail);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_time_per_trial));
		durationPerTrail = (TextView) root
				.findViewById(R.id.textView_times_per_trail);
		durationPerTrail.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_time_per_trial)));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_number_of_trails);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_number_of_trials));
		numberOfTrials = (TextView) root
				.findViewById(R.id.textView_number_of_trails);
		numberOfTrials.setText(String.format("%02d",
				getResources().getInteger(R.integer.default_number_of_trials)));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_mastery_criteria);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_mastery_criteria));
		masteryCriteria = (TextView) root
				.findViewById(R.id.textView_mastery_criteria);
		masteryCriteria.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_mastery_criteria)));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_video_duration);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_video_duration));
		videoDuration = (TextView) root
				.findViewById(R.id.textView_video_duration);
		videoDuration.setText(String.format("%02d",
				getResources().getInteger(R.integer.default_video_duration)));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			switch (seekBar.getId()) {
				case R.id.seekBar_times_per_trail:
					durationPerTrail.setText(String
							.format("%02d", progress));
					break;
				case R.id.seekBar_number_of_trails:
					numberOfTrials.setText(String.format("%02d", progress));
					break;
				case R.id.seekBar_mastery_criteria:
					masteryCriteria
							.setText(String.format("%02d", progress));
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
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.create_user_create_user:
				createUser();
				break;
		}
	}

	private void createUser() {
		if (!isNameAndAgeSet()) {
			alertUser(getActivity(), R.string.error_missing_name_or_age);
		} else {
			String name = editName.getText().toString();
			String age = editAge.getText().toString();
			SharedPreferencesUtil.createAndAddUser(
					getActivity(),
					name,
					age,
					Integer.valueOf(durationPerTrail.getText()
							.toString()) * 1000,
					Integer.valueOf(numberOfTrials.getText().toString()),
					Integer.valueOf(masteryCriteria.getText()
							.toString()),
					Integer.valueOf(videoDuration.getText().toString()) * 1000);
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());

			alertUser(getActivity(), R.string.info_user_created);

			if (isMultipane()) {
				getFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, new HomeFragment())
						.commit();
			}
		}
	}

	private boolean isMultipane() {
		return getActivity().findViewById(R.id.item_detail_container) != null;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
