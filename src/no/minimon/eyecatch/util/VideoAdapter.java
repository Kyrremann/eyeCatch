package no.minimon.eyecatch.util;

import java.util.Locale;

import no.minimon.eyecatch.R;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private ContentResolver contentResolver;

	public VideoAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);
		inflater = LayoutInflater.from(context);
		contentResolver = context.getContentResolver();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView thumbnail = (ImageView) view
				.findViewById(R.id.video_thumbnail);
		TextView title = (TextView) view.findViewById(R.id.video_title);
		TextView duration = (TextView) view.findViewById(R.id.video_duration);

		Bitmap bm = MediaStore.Video.Thumbnails
				.getThumbnail(contentResolver, cursor.getLong(0),
						MediaStore.Video.Thumbnails.MICRO_KIND, null);
		thumbnail.setImageBitmap(bm);
		title.setText(cursor.getString(1));
		duration.setText(convertMillisToTime(cursor.getInt(3)));
	}

	private String convertMillisToTime(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		return String.format(Locale.UK, "%02d:%02d:%02d", hour, minute, second);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.view_row_select_video, null);
	}

}
