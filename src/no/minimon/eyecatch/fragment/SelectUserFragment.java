package no.minimon.eyecatch.fragment;

import java.util.Collections;
import java.util.List;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectUserFragment extends ListFragment {

	private List<String> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_select_user, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = SharedPreferencesUtil.getUsersAsList(getActivity());
		Collections.sort(list);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, list));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			JSONObject jsonObject = SharedPreferencesUtil.getUser(
					getActivity(), list.get(position));
			SharedPreferencesUtil.setCurrentUser(getActivity(),
					jsonObject.getString(SharedPreferencesUtil.NAME));
			SharedPreferencesUtil.updateActioBarTitle(getActivity(),
					getActivity().getActionBar());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
