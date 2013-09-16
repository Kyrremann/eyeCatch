package no.minimon.eyecatch.fragment;

import no.minimon.eyecatch.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CreateUserFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_user, container,
				false);
		initSeekbars(view);
		return view;
	}

	private void initSeekbars(View root) {
		SeekBar seekBar = (SeekBar) root
				.findViewById(R.id.seekBar_times_per_trail);
		seekBar.setOnSeekBarChangeListener(createOnSeekBarListener());
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_time_per_trial));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_number_of_trails);
		seekBar.setOnSeekBarChangeListener(createOnSeekBarListener());
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_number_of_trials));

		seekBar = (SeekBar) root.findViewById(R.id.seekBar_mastery_criteria);
		seekBar.setOnSeekBarChangeListener(createOnSeekBarListener());
		seekBar.setProgress(getResources().getInteger(
				R.integer.default_mastery_criteria));
	}

	private OnSeekBarChangeListener createOnSeekBarListener() {
		return new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					switch (seekBar.getId()) {
					case R.id.seekBar_times_per_trail:
						((TextView) seekBar.getRootView().findViewById(
								R.id.textView_times_per_trail)).setText(String
								.valueOf(progress));
						break;
					case R.id.seekBar_number_of_trails:
						((TextView) seekBar.getRootView().findViewById(
								R.id.textView_number_of_trails)).setText(String
								.valueOf(progress));
						break;
					case R.id.seekBar_mastery_criteria:
						((TextView) seekBar.getRootView().findViewById(
								R.id.textView_mastery_criteria)).setText(String
								.valueOf(progress));
						break;
					default:
						break;
					}
				}
			}
		};
	}
}
