package no.minimon.eyecatch.util;

import android.os.Build;

public class AndroidVersionUtil {

	public static boolean isRunningJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean isRunningHoneyComb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
}
