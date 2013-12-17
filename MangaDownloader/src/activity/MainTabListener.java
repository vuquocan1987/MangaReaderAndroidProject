package activity;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;


public class MainTabListener<T> implements TabListener {
	private final Activity mActivity;
	private final Class<T> mClass;
	private final String mTag;
	private final Bundle mAgrs;
	private Fragment mFragment;

	public MainTabListener(Activity mActivity,String mTag, Class<T> mClass ) {
		this(mActivity, mClass, mTag, null);
	}

	public MainTabListener(Activity mActivity, Class<T> mClass, String mTag,
			Bundle mAgrs) {
		super();
		this.mActivity = mActivity;
		this.mClass = mClass;
		this.mTag = mTag;
		this.mAgrs = mAgrs;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(mActivity, "Reselected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mFragment == null) {
			mFragment = Fragment
					.instantiate(mActivity, mClass.getName(), mAgrs);
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			ft.attach(mFragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null)
			ft.detach(mFragment);
	}

}
