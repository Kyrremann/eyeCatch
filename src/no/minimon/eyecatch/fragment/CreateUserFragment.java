package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.NotificationUtil.alertUser;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CreateUserFragment extends AbstractUserInfoFragment {

	private EditText editName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_create_user,
				container, false);
		
		initSeekbars(rootView);
		initTextViews(rootView);
		initFaces(rootView);
		initBoxes(rootView);
		
		setDefaultSeekbarValue();
		setDefaultTextViewValue();
		
		face = getString(R.string.face_mariama);
		editName = (EditText) rootView.findViewById(R.id.edit_username);
		editAge = (EditText) rootView.findViewById(R.id.edit_age);
		rootView.findViewById(R.id.create_user_create_user)
				.setOnClickListener(this);

		return rootView;
	}

	private boolean isNameAndAgeSet() {
		return editName.getText().length() > 0
				&& editAge.getText().length() > 0;
	}

	private void setDefaultSeekbarValue() {
		barDurationPerTrail.setProgress(getResources().getInteger(
				R.integer.default_time_per_trial));
		barNumberOfTrials.setProgress(getResources().getInteger(
				R.integer.default_number_of_trials));
		barMasteryCriteria.setProgress(getResources().getInteger(
				R.integer.default_mastery_criteria));
		barVideoDuration.setProgress(getResources().getInteger(
				R.integer.default_video_duration));
		barErrorDuration.setProgress(getResources().getInteger(
				R.integer.default_error_duration));
	}

	private void setDefaultTextViewValue() {
		textDurationPerTrail.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_time_per_trial)));
		textNumberOfTrials.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_number_of_trials)));
		textMasteryCriteria.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_mastery_criteria)));
		textVideoDuration.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_video_duration)));
		textErrorDuration.setText(String.format("%02d", getResources()
				.getInteger(R.integer.default_error_duration)));
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.create_user_create_user:
			createUser();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	private void createUser() {
		if (!isNameAndAgeSet()) {
			alertUser(getActivity(), R.string.error_missing_name_or_age);
		} else {
			String name = editName.getText().toString();
			String age = editAge.getText().toString();
			SharedPreferencesUtil
					.createAndAddUser(getActivity(), name, age,
							Integer.valueOf(textDurationPerTrail.getText()
									.toString()) * 1000, Integer
									.valueOf(textNumberOfTrials.getText()
											.toString()), Integer
									.valueOf(textMasteryCriteria.getText()
											.toString()), Integer
									.valueOf(textVideoDuration.getText()
											.toString()) * 1000, Integer
									.valueOf(textErrorDuration.getText()
											.toString()) * 1000, face, box);
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());

			alertUser(getActivity(), R.string.info_user_created);

			if (isMultipane()) {
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.item_detail_container, new HomeFragment())
						.commit();
			} else {
				getActivity().finish();
			}
		}
	}

	private boolean isMultipane() {
		return getActivity().findViewById(R.id.item_list) != null;
	}
}
