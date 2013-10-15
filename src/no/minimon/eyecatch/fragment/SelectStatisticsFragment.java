package no.minimon.eyecatch.fragment;

import java.util.Collections;
import java.util.List;

import no.minimon.eyecatch.R;
import no.minimon.eyecatch.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectStatisticsFragment extends ListFragment {

	private List<String> usersList, statisticTestingUsersList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_select_user, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		usersList = SharedPreferencesUtil.getUsersAsList(getActivity());
		statisticTestingUsersList = SharedPreferencesUtil.getStatisticTestingUsersAsList(getActivity());
		usersList.addAll(statisticTestingUsersList);
		Collections.sort(usersList);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, usersList));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		JSONObject jsonObject = SharedPreferencesUtil.getUser(getActivity(),
				usersList.get(position));

		if (getActivity().findViewById(R.id.item_detail_container) != null) {

			Fragment fragment = new StatisticFragment();
			Bundle args = new Bundle();
			try {
				args.putString(SharedPreferencesUtil.NAME,
						jsonObject.getString(SharedPreferencesUtil.NAME));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			fragment.setArguments(args);
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
			// TODO: Shift the user list to where the menu is
		} else {
			// TODO: Create activity for mobilephones
		}
	}

}
