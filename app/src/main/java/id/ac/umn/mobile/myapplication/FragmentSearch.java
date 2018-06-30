package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentSearch extends Fragment {
    EditText keywordSearch;
    Spinner categorySpinner;
    Spinner typeSpinner;
    Button btnSearch;

    List<String> listCategories  = new ArrayList<>();
    View myView;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_search,container,false);

        progressDialog = new ProgressDialog(myView.getContext());
        progressDialog.setMessage("Fetching Data");

        keywordSearch = myView.findViewById(R.id.keyword_search);
        categorySpinner = myView.findViewById(R.id.spinner_category);
        typeSpinner = myView.findViewById(R.id.spinner_search_type);
        btnSearch = myView.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSearch();
            }
        });

        FetchCategoryData();

        return myView;
    }

    public void FetchCategoryData(){
        progressDialog.show();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callCategoryData = webServiceAPI.getCategories();

        callCategoryData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray multipleCategories = obj.get("result").getAsJsonArray();

                    listCategories.clear();
                    listCategories.add("None");
                    for(int i = 0 ; i < multipleCategories.size(); i++){
                        JsonObject singleData = multipleCategories.get(i).getAsJsonObject();

                        String categoryName = singleData.get("CategoryName").getAsString();
                        listCategories.add(categoryName);
                    }

                    LoadSearchFragment();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(myView.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LoadSearchFragment(){
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(myView.getContext(), android.R.layout.simple_spinner_item, listCategories);
        adapterCategory.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);

        List<String> typeSearch = new ArrayList<>();
        typeSearch.add("Artist");
        typeSearch.add("Artwork");

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(myView.getContext(), android.R.layout.simple_spinner_item, typeSearch);
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapterType);

        progressDialog.dismiss();
    }

    public void StartSearch(){
        String searchKeyWord = keywordSearch.getText().toString();
        String searchCategory = categorySpinner.getSelectedItem().toString();
        String searchType = typeSpinner.getSelectedItem().toString();

        Intent intent = new Intent(myView.getContext(), ActivitySearchResult.class);
        Bundle extras = new Bundle();
        if(!searchKeyWord.equals("")){
            extras.putString("searchKeyword", searchKeyWord);
        }
        if(!searchCategory.equals("None")){
            extras.putString("searchCategory", searchCategory);
        }
        extras.putString("searchType",searchType);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
