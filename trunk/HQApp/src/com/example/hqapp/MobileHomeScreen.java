package com.example.hqapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MobileHomeScreen extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mobile, null);
		getActivity().getActionBar().show();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.mobile_menu, menu);
		
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		
		MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
}
