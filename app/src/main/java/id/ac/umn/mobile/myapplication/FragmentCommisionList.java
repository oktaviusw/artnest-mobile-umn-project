package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentCommisionList extends Fragment {
    List<ModelCommissionInformation> commissionDatas = new ArrayList<>();

    RecyclerView recyclerViewCommission;
    RVAListCommission adapterCommissionList;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_commision_artist,container,false);

        seedData();


        return myView;
    }

    private void seedData(){
        Date startDate = new Date(2018, 6 ,1);
        Date deadlineDate = new Date(2018, 6 ,30);
        ModelCommissionInformation data1 = new ModelCommissionInformation("1", "1", "Oktavius Wiguna", "2", "Anna Jeane", 2, "Sagiri Commmisssion", startDate, deadlineDate);
        ModelCommissionInformation data2 = new ModelCommissionInformation("2", "1", "Oktavius Wiguna", "2", "Anna Jeane", 2, "Sagiri Commmisssion", startDate, deadlineDate);
        Log.d("oktaGanteng",data1.getIdCommission().toString());
        commissionDatas.add(data1);
        commissionDatas.add(data2);
    }
}
