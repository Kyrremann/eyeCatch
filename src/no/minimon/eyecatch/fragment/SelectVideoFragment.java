package no.minimon.eyecatch.fragment;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;
import no.minimon.eyecatch.util.VideoAdapter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class SelectVideoFragment extends ListFragment {

	private Cursor cursor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_video, container,
				false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri external = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		// Uri internal = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
		String[] columns = new String[] { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.TITLE, MediaStore.Video.Media.ARTIST,
				MediaStore.Video.Media.DURATION };
		cursor = MediaStore.Video.query(getActivity().getContentResolver(),
				external, columns);
		VideoAdapter adapter = new VideoAdapter(getActivity(), cursor, 0);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		cursor.moveToPosition(position);
		if (cursor.getInt(3) > 1000) {
			Uri uri = Uri.parse(cursor.getString(0));
			Uri external = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
			SharedPreferencesUtil.setCurrentVideoUri(getActivity(),
					external.toString() + "/" + uri.toString());
			SharedPreferencesUtil.setCurrentVideoName(getActivity(),
					cursor.getString(1));
			SharedPreferencesUtil.setCurrentVideoDuration(getActivity(),
					cursor.getInt(3));
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());

			if (getActivity().findViewById(R.id.item_detail_container) != null) {

				HomeFragment fragment = new HomeFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, fragment).commit();
			}
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					getString(R.string.error_short_video_duration),
					Toast.LENGTH_SHORT).show();
		}
	}
}
