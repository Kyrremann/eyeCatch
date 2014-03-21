package no.minimon.eyecatch.util;

import java.util.ArrayList;
import java.util.List;

import no.minimon.eyecatch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreferencesUtil {

	public static final String USERS = "eyecatch_users";
	public static final String CURRENT_VIDEO_URI = "eyecatch_current_video_uri";
	public static final String CURRENT_VIDEO_NAME = "eyecatch_current_video_name";
	public static final String CURRENT_USER = "eyecatch_curret_user";
	public static final String CURRENT_ITERATION = "eyecatch_curret_iteration";
	public static final String CURRENT_VIDEO_DURATION = "eyecatch_current_video_duration";
	public static final String CURRENT_SEEK = "eyecatch_current_video_seek";
	public static final String STATISTIC = "eyecatch_statistic";
	public static final String STATISTIC_DATE = "eyecatch_statistic_date";
	public static final String STATISTIC_DATE_END = "eyecatch_statistic_date_end";
	public static final String STATISTIC_TRAINING = "eyecatch_statistic_training";
	public static final String STATISTIC_TESTING = "eyecatch_statistic_testing";
	public static final String STATISTIC_TYPE = "eyecatch_statistic_type";
	public static final String STATISTIC_FACE = "eyecatch_statistic_face";
	public static final String STATISTIC_NORMAL = "NORMAL";
	public static final String STATISTIC_EXTRA = "EXTRA";
	public static final String STATISTIC_GENERALIZATION = "GENERALIZATION";
	public static final int MODE_PRIVATE = 0;
	public static final String NAME = "name";
	public static final String AGE = "age";
	public static final String DURATION_PER_TRIAL = "times_per_trial";
	public static final String NUMBER_OF_TRIALS = "number_of_trials";
	public static final String MASTERY_CRITERIA = "mastery_criteria";
	public static final String VIDEO_DURATION = "video_duration";
	private static final String ERROR_DURATION = "error_duration";
	public static final String CONTINUE = "eyecatch_continue";
	public static final String CONTINUE_DATE = "eyecatch_continue_date";
	public static final String CONTINUE_TRAINING = "eye_catch_training";
	public static final String CONTINUE_TESTING = "eyecatch_testing";
	public static final String CONTINUE_TESTING_OR_TRAINING = "eyecatch_testing_or_training";
	public static final String CORRECT = "correct";
	public static final String FAIL = "fail";
	public static final String STATISTIC_TESTING_USERS = "statistic_testing_users";

	private static final String LOG_SPU = "SPU";

	public static JSONObject createAndAddUser(Context context, String name,
	                                          String age, int timesPerTrial, int numberOfTrials,
	                                          int masteryCriteria, int videoDuration, int errorDuration) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(NAME, name);
			jsonObject.put(AGE, age);
			jsonObject.put(DURATION_PER_TRIAL, timesPerTrial);
			jsonObject.put(NUMBER_OF_TRIALS, numberOfTrials);
			jsonObject.put(MASTERY_CRITERIA, masteryCriteria);
			jsonObject.put(VIDEO_DURATION, videoDuration);
			jsonObject.put(ERROR_DURATION, errorDuration);
			addUser(context, jsonObject);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addUser(Context context, JSONObject jsonObject)
			throws JSONException {
		Editor editor = getEditor(context);
		String name = jsonObject.getString("name");
		editor.putString(name, jsonObject.toString());
		addUserToUserlist(context, editor, name);
		setCurrentUser(context, name);
		editor.commit();
	}

	private static void addUserToUserlist(Context context, Editor editor,
			String name) {
		SharedPreferences preferences = getSharedPreference(context);
		String list = preferences.getString(USERS, "");
		editor.putString(SharedPreferencesUtil.USERS,
				list.concat(";".concat(name)));
	}

	public static String getCurrentVideoUri(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		return preferences.getString(CURRENT_VIDEO_URI, "");
	}

	public static void setCurrentVideoUri(Context context, String uri) {
		Editor editor = getEditor(context);
		editor.putString(CURRENT_VIDEO_URI, uri);
		editor.commit();
	}

	public static int getDurationOnCurrentVideo(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		return preferences.getInt(CURRENT_VIDEO_DURATION, -1);
	}

	public static void setCurrentVideoDuration(Context context, int duration) {
		Editor editor = getEditor(context);
		editor.putInt(CURRENT_VIDEO_DURATION, duration);
		editor.commit();
	}

	public static int getLastSeekOnCurrentVideo(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		return preferences.getInt(CURRENT_SEEK, 0);
	}

	public static void setLastSeekOnCurrentVideo(Context context, int seek) {
		Editor editor = getEditor(context);
		editor.putInt(CURRENT_SEEK, seek);
		editor.commit();
	}

	public static List<String> getUsersAsList(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
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
			return new JSONObject(preferences.getString(name,
					new JSONObject().toString()));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}

	public static boolean hasUser(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		return !preferences.getString(name, "").isEmpty();
	}

	public static void setCurrentUser(Context context, String user) {
		Editor editor = getEditor(context);
		editor.putString(CURRENT_USER, user);
		editor.commit();
	}

	public static String getCurrentUsersName(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
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
		Editor editor = getEditor(context);
		editor.putString(CURRENT_VIDEO_NAME, name);
		editor.commit();
	}

	public static String getCurrentVideoName(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		return preferences.getString(CURRENT_VIDEO_NAME, "");
	}

	public static int getNumberOfTrials(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(NUMBER_OF_TRIALS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_SPU, "Didn't not find number of trails. Returning default");
		return 5;
	}

	public static int getErrorDuration(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(ERROR_DURATION);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_SPU, "Didn't not find error duration. Returning default");
		return 2000;
	}

	public static int getDurationPerTrial(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(DURATION_PER_TRIAL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_SPU, "Didn't not find duration per trial. Returning default");
		return 5000;
	}

	public static int getMasteryCriteria(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(MASTERY_CRITERIA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(LOG_SPU, "Didn't not find mastery criteria. Returning default");
		return 10;
	}

	public static int getAllowedVideoDuration(Context context) {
		JSONObject jsonObject = getCurrentUser(context);
		try {
			return jsonObject.getInt(VIDEO_DURATION);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 10000;
	}

	public static void setCurrentIteration(Context context, int iteration) {
		Editor editor = getEditor(context);
		editor.putInt(CURRENT_ITERATION, iteration);
		editor.commit();
	}

	@SuppressLint("CommitPrefEdits")
	private static Editor getEditor(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		Editor editor = preferences.edit();
		return editor;
	}

	private static SharedPreferences getSharedPreference(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				context.getPackageName(), MODE_PRIVATE);
		return preferences;
	}

	public static int getCurrentIteration(Context context) {
		SharedPreferences preferences = getSharedPreference(context);
		return preferences.getInt(CURRENT_ITERATION, 0);
	}

	public static boolean addOrUpdateStatisticOnUser(Context context,
			String userName, JSONObject statistic) {
		JSONObject user = getUser(context, userName);

		if (user.has(STATISTIC)) {
			try {
				((JSONObject) user.get(STATISTIC)).put(
						statistic.getString(STATISTIC_DATE), statistic);
				return updateUserInfo(context, user);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			try {
				user.put(STATISTIC, new JSONObject());
				updateUserInfo(context, user);
				return addOrUpdateStatisticOnUser(context, userName, statistic);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public static boolean updateUserInfo(Context context, JSONObject user) {
		Editor editor = getEditor(context);
		try {
			editor.putString(user.getString(NAME), user.toString());
			editor.commit();
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static JSONObject createNewStatistic(Context context) {
		JSONObject statistic = new JSONObject();
		try {
			statistic.put(STATISTIC_DATE, System.currentTimeMillis());
			statistic.put(STATISTIC_TESTING, new JSONArray());
			statistic.put(STATISTIC_TRAINING, new JSONArray());
			statistic.put(STATISTIC_TYPE, STATISTIC_NORMAL);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return statistic;
	}

	public static boolean removeStatestic(Context context, String username,
			String key) {
		JSONObject user = getUser(context, username);
		try {
			JSONObject stats = user.getJSONObject(STATISTIC);
			stats.remove(key);
			user.put(STATISTIC, stats);
			return updateUserInfo(context, user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void saveContinueInformation(Context context, String name,
			long date, int training_level, int testing_level,
			boolean testingLevel) {
		// Editor editor = getEditor(context);
		try {
			JSONObject continueObject = new JSONObject();
			continueObject.put(NAME, name);
			continueObject.put(CONTINUE_DATE, date);
			continueObject.put(CONTINUE_TESTING, testing_level);
			continueObject.put(CONTINUE_TRAINING, training_level);
			continueObject.put(CONTINUE_TESTING_OR_TRAINING, testingLevel);
			JSONObject user = getCurrentUser(context);
			user.put(CONTINUE, continueObject);
			updateUserInfo(context, user);
			// editor.putString(CONTINUE, jsonObject.toString());
			// editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static JSONObject getContinueJson(Context context) {
		// SharedPreferences preferences = getSharedPreference(context);
		JSONObject user = getCurrentUser(context);
		try {
			return user.getJSONObject(CONTINUE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// String jsonString = preferences.getString(CONTINUE, "");
		/*String jsonString = 
		if (!jsonString.isEmpty()) {
			try {
				return new JSONObject(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
*/
		return new JSONObject();
	}

	public static JSONObject getStatesticFromCurrentUser(Context context,
			long date) {
		JSONObject user = getCurrentUser(context);
		try {
			JSONObject stats = user.getJSONObject(STATISTIC);
			return stats.getJSONObject(String.valueOf(date));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean createAndAddStatisticForExtraTesting(Context context,
			String name, int correct, int fail) {
		JSONObject statistic = new JSONObject();
		try {
			statistic.put(STATISTIC_DATE, System.currentTimeMillis());
			statistic.put(CORRECT, correct);
			statistic.put(FAIL, fail);
			statistic.put(STATISTIC_TYPE, STATISTIC_EXTRA);
			return addOrUpdateStatisticOnUser(context, name, statistic);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void removeContinueJson(Context context) {
		Editor editor = getEditor(context);
		editor.remove(CONTINUE);
		editor.commit();
	}

	public static boolean removeContinueInfoIfSameDate(Context context,
			String statDate) {
		JSONObject jsonObject = SharedPreferencesUtil.getContinueJson(context);
		try {
			String continueDate = jsonObject.getString(CONTINUE_DATE);
			if (continueDate == null) {
				return false;
			}

			if (continueDate.equals(statDate)) {
				removeContinueJson(context);
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean removeContinueInfoIfSameName(Context context,
			String name) {
		JSONObject jsonObject = SharedPreferencesUtil.getContinueJson(context);
		try {
			String continueName = jsonObject.getString(NAME);
			if (continueName == null) {
				return false;
			}

			if (continueName.equals(name)) {
				removeContinueJson(context);
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void removeUser(Context context, String name) {
		SharedPreferences preferences = getSharedPreference(context);
		String users = preferences.getString(USERS, "");
		System.out.println(users);
		users = users.replace(name, "");
		System.out.println(users);
		Editor editor = getEditor(context);
		editor.putString(USERS, users);
		editor.commit();
	}

	public static void removeSelectedUserIfSameName(Context context,
			String name) {
		String selectedUser = getCurrentUsersName(context);
		if (selectedUser.equals(name)) {
			Editor editor = getEditor(context);
			editor.putString(CURRENT_USER, "");
			editor.commit();
		}
	}
}
