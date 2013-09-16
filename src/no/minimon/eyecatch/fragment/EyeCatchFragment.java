package no.minimon.eyecatch.fragment;

import no.minimon.eyecatch.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EyeCatchFragment extends Fragment {

	private Callbacks mCallbacks = sDummyCallbacks;

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	public EyeCatchFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_eyecatch, container, false);
	}
}
