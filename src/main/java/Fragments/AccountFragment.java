package Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.saboorsalaam.veed10.R;

import Adapters.AccountListViewAdapter;
import ObservableScrollView.BaseFragment;

/**
 * Created by Saboor Salaam on 4/27/2015.
 */
public class AccountFragment extends BaseFragment {
    Context context;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_account, container, false);

        ListView account_list = (ListView) rootView.findViewById(R.id.accountlistView), settings_list = (ListView) rootView.findViewById(R.id.settingslistView);
        AccountListViewAdapter accountListViewAdapter = new AccountListViewAdapter(getActivity());
        account_list.setAdapter(accountListViewAdapter);
        settings_list.setAdapter(accountListViewAdapter);

        return rootView;
    }

}
