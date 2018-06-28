package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentAccountSetting extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_setting_account,container,false);

        /*Isi Recyclernya Disini*/

        return myView;
    }
}
