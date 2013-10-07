package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.SharedPreferencesUtil.*;
import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticFragment extends Fragment {

	private String name;
	private JSONObject jsonUser;

	private View rootView;
	private TextView trainingA, trainingB, trainingC, trainingD, trainingE,
			trainingF, trainingG, trainingH;
	private TextView testingPreA, testingPreB, testingPreC, testingPreD,
			testingPreE, testingPreF, testingPreG, testingPreH, testingPostH;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		name = getArguments().getString(NAME);
		rootView = inflater.inflate(R.layout.fragment_statistic, container,
				false);

		jsonUser = SharedPreferencesUtil.getUser(getActivity(), name);

		try {
			populateUserData();

			initTrainingCells();
			initTestingCells();

			populateTrainingCellData();
			populateTestingCellData();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rootView;
	}

	public void populateUserData() throws JSONException {
		TextView textView = (TextView) rootView
				.findViewById(R.id.statistic_name);
		textView.setText(jsonUser.getString(NAME));

		textView = (TextView) rootView.findViewById(R.id.statistic_age);
		textView.setText(jsonUser.getString(AGE));

		textView = (TextView) rootView
				.findViewById(R.id.statistic_testing_number);
		textView.setText(jsonUser.getString(NUMBER_OF_TRIALS));

		textView = (TextView) rootView
				.findViewById(R.id.statistic_training_number);
		textView.setText(jsonUser.getString(MASTERY_CRITERIA));

		// TODO: Need to set date for this particular dataset
	}

	private void initTrainingCells() {
		trainingA = (TextView) rootView.findViewById(R.id.statistic_training_A);
		trainingB = (TextView) rootView.findViewById(R.id.statistic_training_B);
		trainingC = (TextView) rootView.findViewById(R.id.statistic_training_C);
		trainingD = (TextView) rootView.findViewById(R.id.statistic_training_D);
		trainingE = (TextView) rootView.findViewById(R.id.statistic_training_E);
		trainingF = (TextView) rootView.findViewById(R.id.statistic_training_F);
		trainingG = (TextView) rootView.findViewById(R.id.statistic_training_G);
		trainingH = (TextView) rootView.findViewById(R.id.statistic_training_H);
	}

	private void initTestingCells() {
		testingPreA = (TextView) rootView
				.findViewById(R.id.statistic_testing_A);
		testingPreB = (TextView) rootView
				.findViewById(R.id.statistic_testing_B);
		testingPreC = (TextView) rootView
				.findViewById(R.id.statistic_testing_C);
		testingPreD = (TextView) rootView
				.findViewById(R.id.statistic_testing_D);
		testingPreE = (TextView) rootView
				.findViewById(R.id.statistic_testing_E);
		testingPreF = (TextView) rootView
				.findViewById(R.id.statistic_testing_F);
		testingPreG = (TextView) rootView
				.findViewById(R.id.statistic_testing_G);
		testingPreH = (TextView) rootView
				.findViewById(R.id.statistic_testing_H);
		testingPostH = (TextView) rootView
				.findViewById(R.id.statistic_testing_post_H);
	}

	private void populateTestingCellData() {
		// TODO Auto-generated method stub
		
	}

	private void populateTrainingCellData() {
		// TODO Auto-generated method stub
		
	}

}
