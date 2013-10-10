package no.minimon.eyecatch.fragment;

import static no.minimon.eyecatch.util.SharedPreferencesUtil.AGE;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.MASTERY_CRITERIA;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NAME;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.NUMBER_OF_TRIALS;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_TESTING;
import static no.minimon.eyecatch.util.SharedPreferencesUtil.STATISTIC_TRAINING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StatisticFragment extends Fragment implements OnClickListener,
		AnimationListener {

	private String name;
	private JSONObject jsonUser;

	private View rootView, viewToBeDeleted;
	private ViewGroup container;
	private LayoutInflater inflater;
	private LinearLayout parent;

	private TextView trainingA, trainingB, trainingC, trainingD, trainingE,
			trainingF, trainingG, trainingH;
	private TextView testingPreA, testingPreB, testingPreC, testingPreD,
			testingPreE, testingPreF, testingPreG, testingPreH, testingPostH;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.name = getArguments().getString(NAME);
		this.inflater = inflater;
		this.container = container;

		rootView = inflater.inflate(R.layout.fragment_statistic, container,
				false);
		parent = (LinearLayout) rootView.findViewById(R.id.statistic_parent);

		jsonUser = SharedPreferencesUtil.getUser(getActivity(), name);
		// try {
		// Toast.makeText(getActivity(), jsonUser.toString(4),
		// Toast.LENGTH_SHORT).show();
		// System.out.println(jsonUser.toString(4));
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

		try {
			populateUserData();
			populateData();

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
	}

	private void initTrainingCells(View rootView) {
		trainingA = (TextView) rootView.findViewById(R.id.statistic_training_A);
		trainingB = (TextView) rootView.findViewById(R.id.statistic_training_B);
		trainingC = (TextView) rootView.findViewById(R.id.statistic_training_C);
		trainingD = (TextView) rootView.findViewById(R.id.statistic_training_D);
		trainingE = (TextView) rootView.findViewById(R.id.statistic_training_E);
		trainingF = (TextView) rootView.findViewById(R.id.statistic_training_F);
		trainingG = (TextView) rootView.findViewById(R.id.statistic_training_G);
		trainingH = (TextView) rootView.findViewById(R.id.statistic_training_H);
	}

	private void initTestingCells(View rootView) {
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

	@SuppressWarnings("unchecked")
	private void populateData() throws JSONException {
		JSONObject statistics = jsonUser.getJSONObject(STATISTIC);
		Iterator<String> keys = statistics.keys();
		ArrayList<String> sortedKeys = new ArrayList<String>();
		while (keys.hasNext()) {
			sortedKeys.add(keys.next());
		}
		Collections.sort(sortedKeys);

		for (String key : sortedKeys) {
			JSONObject stat = statistics.getJSONObject(key);

			View table = inflater.inflate(R.layout.view_stat_table, container,
					false);
			table.setTag(key);

			JSONArray array = stat.getJSONArray(STATISTIC_TRAINING);
			populateTrainingCellData(table, array);
			array = stat.getJSONArray(STATISTIC_TESTING);
			populateTestingCellData(table, array);

			TextView textView = (TextView) table
					.findViewById(R.id.statistic_date);
			textView.setText(new Date(Long.valueOf(key)).toString());

			textView = (TextView) table
					.findViewById(R.id.statistic_testing_number);
			textView.setText(jsonUser.getString(NUMBER_OF_TRIALS));

			textView = (TextView) table
					.findViewById(R.id.statistic_training_number);
			textView.setText(jsonUser.getString(MASTERY_CRITERIA));

			textView = (TextView) table.findViewById(R.id.statistic_delete);
			textView.setClickable(true);
			textView.setOnClickListener(this);

			parent.addView(table);
		}

	}

	private void populateTestingCellData(View root, JSONArray array)
			throws JSONException {
		if (array.length() == 0) {
			return;
		}

		initTestingCells(root);

		testingPreA.setText((array.isNull(0)) ? "-" : array.getString(0));
		testingPreB.setText((array.isNull(1)) ? "-" : array.getString(1));
		testingPreC.setText((array.isNull(2)) ? "-" : array.getString(2));
		testingPreD.setText((array.isNull(3)) ? "-" : array.getString(3));
		testingPreE.setText((array.isNull(4)) ? "-" : array.getString(4));
		testingPreF.setText((array.isNull(5)) ? "-" : array.getString(5));
		testingPreG.setText((array.isNull(6)) ? "-" : array.getString(6));
		testingPreH.setText((array.isNull(7)) ? "-" : array.getString(7));
		testingPostH.setText((array.isNull(8)) ? "-" : array.getString(8));
	}

	private void populateTrainingCellData(View root, JSONArray array)
			throws JSONException {
		if (array.length() == 0) {
			return;
		}

		initTrainingCells(root);

		trainingA.setText((array.isNull(0)) ? "-" : array.getString(0));
		trainingB.setText((array.isNull(1)) ? "-" : array.getString(1));
		trainingC.setText((array.isNull(2)) ? "-" : array.getString(2));
		trainingD.setText((array.isNull(3)) ? "-" : array.getString(3));
		trainingE.setText((array.isNull(4)) ? "-" : array.getString(4));
		trainingF.setText((array.isNull(5)) ? "-" : array.getString(5));
		trainingG.setText((array.isNull(6)) ? "-" : array.getString(6));
		trainingH.setText((array.isNull(7)) ? "-" : array.getString(7));
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.statistic_delete) {
			view = (View) view.getParent().getParent();
			// Animation animation = new ScaleAnimation(1f, 0f, 1f, 0f,
			// Animation.RELATIVE_TO_PARENT, 0f,
			// Animation.RELATIVE_TO_PARENT, 0f);
			Animation animation = new AlphaAnimation(1f, 0f);
			// Animation animation = new TranslateAnimation();
			animation.setDuration(1200);
			animation.setAnimationListener(this);
			// view.setAnimation(animation)
			// animation.start();;
			view.startAnimation(animation);

			String key = (String) view.getTag();
			SharedPreferencesUtil.removeStatestic(getActivity(), name, key);
			viewToBeDeleted = view;
			// parent.removeView(view);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		parent.post(new Runnable() {

			@Override
			public void run() {
				if (viewToBeDeleted != null) {
					parent.removeView(viewToBeDeleted);
					viewToBeDeleted = null;
				}
			}
		});
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}
