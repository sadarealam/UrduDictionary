package seatech.alam.urdudictionary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seatech.alam.urdudictionary.R;

/**
 * Created by root on 27/9/15.
 */
public class History extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        return  view ;
    }
}
