package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.NotificationUtil.alertUser;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.AGE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.DURATION_PER_TRIAL;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.ERROR_DURATION;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.FACE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.BOX;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.MASTERY_CRITERIA;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NAME;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NUMBER_OF_TRIALS;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.VIDEO_DURATION;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.getUser;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoFragment extends AbstractUserInfoFragment {

	private JSONObject user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String name = getArguments().getString(NAME);
		user = getUser(getActivity(), name);
		View rootView = inflater.inflate(R.layout.fragment_user_info,
				container, false);

		initSeekbars(rootView);
		initTextViews(rootView);
		initFaces(rootView);
		initBoxes(rootView);
		initButtons(rootView);

		try {
			initAndSetNameAndAge(user, rootView);
			setUserValues(user);
			setUsersFace(user, rootView);
			setUsersBox(user, rootView);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootView;
	}

	private void setUsersBox(JSONObject user, View rootView) throws JSONException {
		removeBackgroundFromBoxes();
		String box = user.getString(BOX);
		ImageView imageView = null;
		if (box.equals(getString(R.string.box_icon))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_icon);
		} else if (box.equals(getString(R.string.box_gift))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_gift_box_icon);
		} else if (box.equals(getString(R.string.box_cat))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_cat);
		} else if (box.equals(getString(R.string.box_dino_1))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_dino_1);
		} else if (box.equals(getString(R.string.box_dino_2))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_dino_2);
		} else if (box.equals(getString(R.string.box_elephant))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_elephant);
		} else if (box.equals(getString(R.string.box_gorilla))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_gorilla);
		} else if (box.equals(getString(R.string.box_monster))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_box_monster);
		} else {
			imageView = (ImageView) rootView.findViewById(R.id.image_box);
		}
		imageView.setBackgroundResource(R.drawable.image_border_selector);
	}

	private void setUsersFace(JSONObject user, View rootView)
			throws JSONException {
		removeBackgroundFromFaces();
		String face = user.getString(FACE);
		ImageView imageView = null;
		if (face.equals(getString(R.string.face_mariama))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_mariama);
		} else if (face.equals(getString(R.string.face_aurelien))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_aurelien);
		} else if (face.equals(getString(R.string.face_joelle))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_joelle);
		} else if (face.equals(getString(R.string.face_mike))) {
			imageView = (ImageView) rootView.findViewById(R.id.image_mike);
		}
		imageView.setBackgroundResource(R.drawable.image_border_selector);
	}

	private void initButtons(View rootView) {
		((ImageView) rootView.findViewById(R.id.user_delete))
				.setOnClickListener(this);
		((Button) rootView.findViewById(R.id.button_cancel))
				.setOnClickListener(this);
		((Button) rootView.findViewById(R.id.button_save))
				.setOnClickListener(this);
		((Button) rootView.findViewById(R.id.button_select_user))
				.setOnClickListener(this);
	}

	private void initAndSetNameAndAge(JSONObject user, View rootView)
			throws JSONException {
		TextView textView = (TextView) rootView.findViewById(R.id.username);
		textView.setText(user.getString(NAME));

		editAge = (EditText) rootView.findViewById(R.id.age_edit);
		editAge.setText(user.getString(AGE));

	}

	private void setUserValues(JSONObject user) throws JSONException {
		barDurationPerTrail.setProgress(user.getInt(DURATION_PER_TRIAL) / 1000);
		textDurationPerTrail.setText(String.format("%02d",
				user.getInt(DURATION_PER_TRIAL) / 1000));
		barNumberOfTrials.setProgress(user.getInt(NUMBER_OF_TRIALS));
		textNumberOfTrials.setText(String.format("%02d",
				user.getInt(NUMBER_OF_TRIALS)));
		barMasteryCriteria.setProgress(user.getInt(MASTERY_CRITERIA));
		textMasteryCriteria.setText(String.format("%02d",
				user.getInt(MASTERY_CRITERIA)));
		barVideoDuration.setProgress(user.getInt(VIDEO_DURATION) / 1000);
		textVideoDuration.setText(String.format("%02d",
				user.getInt(VIDEO_DURATION) / 1000));
		barErrorDuration.setProgress(user.getInt(ERROR_DURATION) / 1000);
		textErrorDuration.setText(String.format("%02d",
				user.getInt(ERROR_DURATION) / 1000));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_save:
			saveData();
			alertUser(getActivity(), "Information saved");
			break;
		case R.id.button_cancel:
			finishActivity();
			break;
		case R.id.button_select_user:
			selectUser();
			alertUser(getActivity(), "User selected");
			break;
		case R.id.user_delete:
			deleteUser();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	private void deleteUser() {
		try {
			String name = user.getString(NAME);
			SharedPreferencesUtil.removeUser(getActivity(), name);
			SharedPreferencesUtil.removeContinueInfoIfSameName(getActivity(),
					name);
			SharedPreferencesUtil.removeSelectedUserIfSameName(getActivity(),
					name);
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());
			finishActivity();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
			user.put(AGE, editAge.getText().toString());
			user.put(
					DURATION_PER_TRIAL,
					Integer.valueOf(textDurationPerTrail.getText().toString()) * 1000);
			user.put(NUMBER_OF_TRIALS,
					Integer.valueOf(textNumberOfTrials.getText().toString()));
			user.put(MASTERY_CRITERIA,
					Integer.valueOf(textMasteryCriteria.getText().toString()));
			user.put(
					VIDEO_DURATION,
					Integer.valueOf(textVideoDuration.getText().toString()) * 1000);
			user.put(
					ERROR_DURATION,
					Integer.valueOf(textVideoDuration.getText().toString()) * 1000);
			user.put(FACE, face);
			user.put(BOX, box);
			SharedPreferencesUtil.updateUserInfo(getActivity(), user);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
