package no.minimon.eyecatch.util;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtil {

	public static void alertUser(Context context, int resId) {
		alertUser(context, context.getString(resId));
	}

	public static void alertUser(Context context, String message) {
		Toast.makeText(context.getApplicationContext(),
				message,
				Toast.LENGTH_SHORT).show();
	}
}
