package com.core.lib.application;

import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public abstract class BaseFragment extends Fragment {

    private static View mView = null;

    public View findViewById(int id) {
        if (mView == null) {
            mView = getView();
        }
        return mView.findViewById(id);
    }

    public void finishFragment() {
        FragmentTransaction transaction = this.getActivity()
                .getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.remove(this).commit();
    }

    public void switchFragment(int resOldFragmentId, BaseFragment newFragment) {

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(resOldFragmentId, newFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
