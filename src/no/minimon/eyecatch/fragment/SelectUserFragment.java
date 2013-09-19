package no.minimon.eyecatch.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.minimon.eyecatch.EyeCatchActivity;
import no.minimon.eyecatch.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectUserFragment extends ListFragment {

	private List<String> list;
	private SharedPreferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_select_user, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getActivity().getSharedPreferences(
				getActivity().getPackageName(), Context.MODE_PRIVATE);
		String users = preferences.getString(EyeCatchActivity.USERS,
				"");
		list = new ArrayList<String>();
		for (String user : users.split(";")) {
			list.add(user);
		}
		Collections.sort(list);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, list));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// TODO: Do something useful here
		SharedPreferences preferences = getActivity().getSharedPreferences(
				getActivity().getPackageName(), Context.MODE_PRIVATE);
		try {
			JSONObject jsonObject = new JSONObject(preferences.getString(
					list.get(position), new JSONObject().toString()));
			Toast.makeText(getActivity(), jsonObject.toString(),
					Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
