package no.minimon.eyecatch.fragment;

import no.minimon.eyecatch.R;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AbstractUserInfoFragment extends Fragment implements
		OnSeekBarChangeListener, OnClickListener {

	protected TextView textDurationPerTrail, textNumberOfTrials,
			textMasteryCriteria, textVideoDuration, textErrorDuration;
	protected SeekBar barDurationPerTrail, barNumberOfTrials,
			barMasteryCriteria, barVideoDuration, barErrorDuration;
	protected ImageView mariama, mike, joelle, aurelien, imageBox, imageBoxIcon,
			imageBoxCat, imageBoxDino1, imageBoxDino2, imageBoxElephant,
			imageBoxGorilla, imageBoxMonster;
	protected EditText editAge;
	protected String face, box;

	protected void initSeekbars(View root) {
		barDurationPerTrail = (SeekBar) root
				.findViewById(R.id.seekBar_times_per_trail);
		barDurationPerTrail.setOnSeekBarChangeListener(this);

		barNumberOfTrials = (SeekBar) root
				.findViewById(R.id.seekBar_number_of_trails);
		barNumberOfTrials.setOnSeekBarChangeListener(this);

		barMasteryCriteria = (SeekBar) root
				.findViewById(R.id.seekBar_mastery_criteria);
		barMasteryCriteria.setOnSeekBarChangeListener(this);

		barVideoDuration = (SeekBar) root
				.findViewById(R.id.seekBar_video_duration);
		barVideoDuration.setOnSeekBarChangeListener(this);

		barErrorDuration = (SeekBar) root
				.findViewById(R.id.seekBar_error_duration);
		barErrorDuration.setOnSeekBarChangeListener(this);
	}

	protected void initTextViews(View root) {
		textDurationPerTrail = (TextView) root
				.findViewById(R.id.textView_times_per_trail);
		textNumberOfTrials = (TextView) root
				.findViewById(R.id.textView_number_of_trails);
		textMasteryCriteria = (TextView) root
				.findViewById(R.id.textView_mastery_criteria);
		textVideoDuration = (TextView) root
				.findViewById(R.id.textView_video_duration);
		textErrorDuration = (TextView) root
				.findViewById(R.id.textView_error_duration);
	}

	protected void initBoxes(View rootView) {
		imageBox = (ImageView) rootView.findViewById(R.id.image_box);
		imageBox.setOnClickListener(this);

		imageBoxIcon= (ImageView) rootView.findViewById(R.id.image_box_icon);
		imageBoxIcon.setOnClickListener(this);
		
		imageBoxCat = (ImageView) rootView.findViewById(R.id.image_box_cat);
		imageBoxCat.setOnClickListener(this);
		
		imageBoxDino1 = (ImageView) rootView
				.findViewById(R.id.image_box_dino_1);
		imageBoxDino1.setOnClickListener(this);
		
		imageBoxDino2 = (ImageView) rootView
				.findViewById(R.id.image_box_dino_2);
		imageBoxDino2.setOnClickListener(this);
		
		imageBoxElephant = (ImageView) rootView
				.findViewById(R.id.image_box_elephant);
		imageBoxElephant.setOnClickListener(this);
		
		imageBoxGorilla = (ImageView) rootView
				.findViewById(R.id.image_box_gorilla);
		imageBoxGorilla.setOnClickListener(this);
		
		imageBoxMonster = (ImageView) rootView
				.findViewById(R.id.image_box_monster);
		imageBoxMonster.setOnClickListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			switch (seekBar.getId()) {
			case R.id.seekBar_times_per_trail:
				textDurationPerTrail.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_number_of_trails:
				textNumberOfTrials.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_mastery_criteria:
				textMasteryCriteria.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_video_duration:
				textVideoDuration.setText(String.format("%02d", progress));
				break;
			case R.id.seekBar_error_duration:
				textErrorDuration.setText(String.format("%02d", progress));
				break;
			}
		}
	}

	protected void removeBackgroundFromFaces() {
		mariama.setBackgroundResource(0);
		mike.setBackgroundResource(0);
		joelle.setBackgroundResource(0);
		aurelien.setBackgroundResource(0);
	}

	protected void removeBackgroundFromBoxes() {
		imageBox.setBackgroundResource(0);
		imageBoxCat.setBackgroundResource(0);
		imageBoxDino1.setBackgroundResource(0);
		imageBoxDino2.setBackgroundResource(0);
		imageBoxElephant.setBackgroundResource(0);
		imageBoxGorilla.setBackgroundResource(0);
		imageBoxMonster.setBackgroundResource(0);
	}

	protected void initFaces(View rootView) {
		mariama = (ImageView) rootView.findViewById(R.id.image_mariama);
		mariama.setOnClickListener(this);

		mike = (ImageView) rootView.findViewById(R.id.image_mike);
		mike.setOnClickListener(this);

		joelle = (ImageView) rootView.findViewById(R.id.image_joelle);
		joelle.setOnClickListener(this);

		aurelien = (ImageView) rootView.findViewById(R.id.image_aurelien);
		aurelien.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
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
		case R.id.image_box:
			removeBackgroundFromBoxes();
			imageBox.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box);
			break;
			case R.id.image_box_icon:
				removeBackgroundFromBoxes();
				imageBoxIcon.setBackgroundResource(R.drawable.image_border_selector);
				box = getString(R.string.box_icon);
				break;
		case R.id.image_box_cat:
			removeBackgroundFromBoxes();
			imageBoxCat.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_cat);
			break;
		case R.id.image_box_dino_1:
			removeBackgroundFromBoxes();
			imageBoxDino1.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_dino_1);
			break;
		case R.id.image_box_dino_2:
			removeBackgroundFromBoxes();
			imageBoxDino2.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_dino_2);
			break;
		case R.id.image_box_elephant:
			removeBackgroundFromBoxes();
			imageBoxElephant.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_elephant);
			break;
		case R.id.image_box_gorilla:
			removeBackgroundFromBoxes();
			imageBoxGorilla.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_gorilla);
			break;
		case R.id.image_box_monster:
			removeBackgroundFromBoxes();
			imageBoxMonster.setBackgroundResource(R.drawable.image_border_selector);
			box = getString(R.string.box_monster);
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
