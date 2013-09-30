package no.minimon.eyecatch.util;

import java.util.ArrayList;
import java.util.List;

import no.minimon.eyecatch.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {

	public static final String USERS = "eyecatch_users";
	public static final String CURRENT_VIDEO_URI = "eyecatch_current_video_uri";
	public static final String CURRENT_VIDEO_NAME = "eyecatch_current_video_name";
	public static final String CURRENT_USER = "eyecatch_curret_user";
	public static final String CURRENT_ITERATION = "eyecatch_curret_iteration";
	public static final String CURRENT_VIDEO_DURATION = "eyecatch_current_video_duration";
	public static final String CURRENT_SEEK = "eyecatch_current_video_seek";
	public static final int MODE_PRIVATE = 0;
	public static final String NAME = "name";
	public static final String AGE = "age";
	public static final String DURATION_PER_TRIAL = "times_per_trial";
	public static final String NUMBER_OF_TRIALS = "number_of_trials";
	public static final String MASTERY_CRITERIA = "mastery_criteria";
	public static final String VIDEO_DURATION = "video_duration";

	public static JSONObject createAndAddUser(Context context, String name,
			String age, int timesPerTrial, int numberOfTrials,
			int masteryCriteria, int videoDuration) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(NAME, name);
			jsonObject.put(AGE, age);
			jsonObject.put(DURATION_PER_TRIAL, timesPerTrial);
			jsonObject.put(NUMBER_OF_TRIALS, numberOfTrials);
			jsonObject.put(MASTERY_CRITERIA, masteryCriteria);
			jsonObject.put(VIDEO_DURATION, videoDuration);
			addUser(context, jsonObject);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addUser(Context context, JSONObject jsonObject)
			throws JSONException {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		String name = jsonObject.getString("name");
		editor.putString(name, jsonObject.toString());
		addUserToUserlist(preferences, editor, name);
		setCurrentUser(context, name);
		editor.commit();
	}

	private static void addUserToUserlist(SharedPreferences preferences,
			Editor editor, String name) {
		String list = preferences.getString(USERS, "");
		editor.putString(SharedPreferencesUtil.USERS,
				list.concat(";".concat(name)));
	}

	public static String getCurrentVideoUri(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getString(CURRENT_VIDEO_URI, "");
	}

	public static void setCurrentVideoUri(Context context, String uri) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CURRENT_VIDEO_URI, uri);
		editor.commit();
	}

	public static int getDurationOnCurrentVideo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getInt(CURRENT_VIDEO_DURATION, -1);
	}

	public static void setDurationOnCurrentVideo(Context context, int duration) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(CURRENT_VIDEO_DURATION, duration);
		editor.commit();
	}
	
	public static int getLastSeekOnCurrentVideo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getInt(CURRENT_SEEK, 0);
	}

	public static void setLastSeekOnCurrentVideo(Context context, int seek) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(CURRENT_SEEK, seek);
		editor.commit();
	}

	public static List<String> getUsersAsList(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		String users = preferences.getString(USERS, "");
		List<String> list = new ArrayList<String>();
		for (String user : users.split(";")) {
			if (!user.isEmpty()) {
				list.add(user);
			}
		}
		return list;
	}

	public static JSONObject getUser(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		try {
			return new JSONObject(preferences.getString(name, ""));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}

	public static void setCurrentUser(Context context, String user) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CURRENT_USER, user);
		editor.commit();
	}

	public static String getCurrentUsersName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getString(CURRENT_USER, "");
	}
	
	public static JSONObject getCurrentUser(Context context) {
		return getUser(context, getCurrentUsersName(context));
	}

	public static void updateActioBarTitle(Context context, ActionBar actionBar) {
		String title = context.getString(R.string.app_name);
		String user = getCurrentUsersName(context);
		if (!user.isEmpty()) {
			title += " - Selected user: " + user;
		}
		String video = getCurrentVideoName(context);
		if (!video.isEmpty()) {
			title += " - Selected video: " + video;
		}
		actionBar.setTitle(title);
	}

	public static void setCurrentVideoName(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(CURRENT_VIDEO_NAME, name);
		editor.commit();
	}

	public static String getCurrentVideoName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getString(CURRENT_VIDEO_NAME, "");
	}

	public static int getNumberOfTrials(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(NUMBER_OF_TRIALS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10;
	}
	
	public static int getDurationPerTrial(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(DURATION_PER_TRIAL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 5;
	}

	public static int getMasteryCriteria(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(MASTERY_CRITERIA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10;
	}
	
	public static int getAllowedVideoDuration(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(VIDEO_DURATION);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10;
	}

	public static void setCurrentIteration(
			Context context, int iteration) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(CURRENT_ITERATION, iteration);
		editor.commit();	
	}
	
	public static int getCurrentIteration(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences.getInt(CURRENT_ITERATION, 0);
	}
}
