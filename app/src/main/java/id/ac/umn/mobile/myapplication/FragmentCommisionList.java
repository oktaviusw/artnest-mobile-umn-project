package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentCommisionList extends Fragment {
    List<ModelCommissionInformation> commissionData = new ArrayList<>();

    RecyclerView recyclerViewCommission;
    RVAListCommission adapterCommissionList;

    ProgressDialog progressDialog;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_commision_customer,container,false);

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setMessage("Getting the results");

        recyclerViewCommission = myView.findViewById(R.id.RV_commission_customer);
        adapterCommissionList = new RVAListCommission(myView.getContext(),commissionData,"CommissionArtist");
        adapterCommissionList.notifyDataSetChanged();
        recyclerViewCommission.setAdapter(adapterCommissionList);

        seedData();

        return myView;
    }

    private void seedData(){
        progressDialog.show();
        SharedPreferences pref = this.getActivity().getSharedPreferences("LOGIN_PREFERENCES", Context.MODE_PRIVATE);
        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        String idCustomer = pref.getString("UserID","");
        Call<JsonElement> callData = webServiceAPI.getAllCommissionData(idCustomer,"ARTIST");

        callData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray data = obj.get("result").getAsJsonArray();

                    commissionData.clear();
                    for(int i = 0 ; i < data.size() ; i++){
                        JsonObject singleData = data.get(i).getAsJsonObject();
                        ModelCommissionInformation dataToList;

                        String sIDProject = singleData.get("IDProject").getAsString();
                        String sIDCustomer = singleData.get("IDCustomer").getAsString();
                        String sIDArtist = singleData.get("IDArtist").getAsString();
                        String sCustomerName = singleData.get("CustomerName").getAsString();
                        String sTitleProject = singleData.get("TitleProject").getAsString();
                        Integer sTokenValue = singleData.get("TokenValue").getAsInt();
                        String sArtistName = singleData.get("ArtistName").getAsString();
                        String sRequestStatus = singleData.get("RequestStatus").getAsString();
                        String sStartDate = singleData.get("DateStart").getAsString();
                        String sEndDate = singleData.get("DateEnd").getAsString();
                        Boolean sAnySketch = singleData.get("anySketchBase").getAsBoolean();
                        Date startDate = new Date();
                        Date endDate = new Date();

                        try {
                            startDate = new SimpleDateFormat("yyyy-mm-dd").parse(sStartDate);
                            endDate = new SimpleDateFormat("yyyy-mm-dd").parse(sEndDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(sRequestStatus.equals("ACCEPTED")){
                            String sProjectStatus = singleData.get("CommissionStatus").getAsString();
                            dataToList = new ModelCommissionInformation(sIDProject, sIDCustomer, sCustomerName, sIDArtist, sArtistName, sRequestStatus, sTokenValue, sTitleProject, startDate, endDate, sAnySketch);
                        }
                        else{
                            dataToList = new ModelCommissionInformation(sIDProject, sIDCustomer, sCustomerName, sIDArtist, sArtistName, sRequestStatus, sTokenValue, sTitleProject, startDate, endDate, sAnySketch);
                        }

                        commissionData.add(dataToList);
                    }
                    progressDialog.dismiss();
                    adapterCommissionList.notifyDataSetChanged();
                    recyclerViewCommission.setAdapter(adapterCommissionList);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }


    }


