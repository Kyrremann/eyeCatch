package no.minimon.eyecatch.fragment;

import java.util.Random;

import no.minimon.eyecatch.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.VideoView;

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
		// MediaStore.Video.VideoColumns.DATA
		cursor = MediaStore.Video.query(getActivity().getContentResolver(),
				external, columns);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, cursor,
				new String[] { MediaStore.Video.Media.DURATION },
				new int[] { android.R.id.text1 }, CursorAdapter.NO_SELECTION);
		setListAdapter(adapter);
		/*System.out.println("SIZE: " + cursor.getCount());
		while (cursor.moveToNext()) {
			System.out.printf("ID: %s - TITLE: %s - ARTIST: %s\n",
					cursor.getString(0), cursor.getString(1),
					cursor.getString(2));
		}
		cursor.moveToFirst();
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		Bitmap bm = MediaStore.Video.Thumbnails.getThumbnail(
				getContentResolver(), cursor.getLong(0),
				MediaStore.Video.Thumbnails.MICRO_KIND, null);
		imageView.setImageBitmap(bm);*/
	}

	VideoView videoView;
	Dialog dialog;

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		cursor.moveToPosition(position);
		Uri uri = Uri.parse(cursor.getString(0));
		Uri external = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		Uri complete = Uri.parse(external.toString() + "/" + uri.toString());

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		videoView = new VideoView(getActivity());
		builder.setView(videoView);
		videoView.setVideoURI(complete);
		videoView.setZOrderOnTop(true);
		dialog = builder.create();
		dialog.show();
		new ASync().execute(cursor.getInt(3));
	}

	private class ASync extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			Random random = new Random();
			int seek = random.nextInt(params[0] - 5000);
			videoView.seekTo(seek);
			videoView.start();
			while (videoView.getCurrentPosition() < seek + 5000) {
			}
			videoView.stopPlayback();
			dialog.cancel();
			return null;

		}

	}
}
