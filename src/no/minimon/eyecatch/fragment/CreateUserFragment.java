package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.NotificationUtil.alertUser;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
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

public class CreateUserFragment extends Fragment implements OnSeekBarChangeListener, OnClickListener {

	private TextView durationPerTrail, numberOfTrials, masteryCriteria,
			videoDuration, errorDuration;
	private EditText editName, editAge;
	private ImageView mariama, mike, joelle, aurelien;
	private String face;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_create_user, container, false);
		initSeekbars(rootView);
		initFaces(rootView);
		face = "mariama";
		editName = (EditText) rootView.findViewById(R.id.edit_username);
		editAge = (EditText) rootView.findViewById(R.id.edit_age);
		((Button) rootView.findViewById(R.id.create_user_create_user)).setOnClickListener(this);

		return rootView;
	}

	private void initFaces(View rootView) {
		mariama = (ImageView) rootView.findViewById(R.id.image_mariama);
		mariama.setOnClickListener(this);
		
		mike = (ImageView) rootView.findViewById(R.id.image_mike);
		mike.setOnClickListener(this);
		
		joelle = (ImageView) rootView.findViewById(R.id.image_joelle);
		joelle.setOnClickListener(this);
		
		aurelien = (ImageView) rootView.findViewById(R.id.image_aurelien);
		aurelien.setOnClickListener(this);
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

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_error_duration);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_error_duration));
		errorDuration = (TextView) root
				.findViewById(R.id.textView_error_duration);
		errorDuration.setText(String.format("%02d",
				getResources().getInteger(R.integer.default_error_duration)));
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
				case R.id.seekBar_error_duration:
					errorDuration.setText(String.format("%02d", progress));
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
			case R.id.image_aurelien:
				removeBackgroundFromFaces();
				aurelien.setBackgroundResource(R.drawable.image_border_selector);
				face = "aurelien";
				break;
			case R.id.image_joelle:
				removeBackgroundFromFaces();
				joelle.setBackgroundResource(R.drawable.image_border_selector);
				face = "joelle";
				break;
			case R.id.image_mariama:
				removeBackgroundFromFaces();
				mariama.setBackgroundResource(R.drawable.image_border_selector);
				face = "mariama";
				break;
			case R.id.image_mike:
				removeBackgroundFromFaces();
				mike.setBackgroundResource(R.drawable.image_border_selector);
				face = "mike";
				break;
		}
	}
	
	private void removeBackgroundFromFaces() {
		mariama.setBackgroundResource(0);
		mike.setBackgroundResource(0);
		joelle.setBackgroundResource(0);
		aurelien.setBackgroundResource(0);
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
					Integer.valueOf(videoDuration.getText().toString()) * 1000,
					Integer.valueOf(errorDuration.getText().toString()) * 1000,
					face);
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
