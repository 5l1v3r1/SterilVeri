package com.syagbasan.sterilveri.adapter;
import com.syagbasan.sterilveri.EmpomedAnadolFragment;
import com.syagbasan.sterilveri.MasraflarFragment;
import com.syagbasan.sterilveri.OdemelerFragment;
import com.syagbasan.sterilveri.OfficeFragment;
import com.syagbasan.sterilveri.PersonalFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new PersonalFragment();
		case 1:
			return new OfficeFragment();
		case 2:
			return new EmpomedAnadolFragment();
		case 3: 
			return new MasraflarFragment();
		case 4:
			return new OdemelerFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;
	}

}
