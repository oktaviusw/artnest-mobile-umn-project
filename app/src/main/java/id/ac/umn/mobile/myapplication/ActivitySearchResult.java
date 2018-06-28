package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySearchResult extends AppCompatActivity {

    String searchKeyword;
    String searchCategory;
    String searchType;
    ProgressDialog progressDialog;

    RecyclerView RVResult;
    private RVAListArtistSearchResult adapterArtistList;
    private RVAListArtworkSearchResult adapterArtworkList;

    public List<ModelArtistInformation> artistList = new ArrayList<>();
    public List<ModelArtworkInformation> artworkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting the results");

        RVResult = findViewById(R.id.RV_search_result);

        GetResultSearch();
    }

    public void GetResultSearch(){
        progressDialog.show();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("searchKeyword")) {
                searchKeyword = extras.getString("searchKeyword");
            }
            else{
                searchKeyword = "";
            }
            if(extras.containsKey("searchCategory")){
                searchCategory = extras.getString("searchCategory");
                if(searchCategory.equals("None")){
                    searchCategory = "";
                }
            }
            else {
                searchCategory = "";
            }
            searchType = extras.getString("searchType").toUpperCase();
        }

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callSearchData = webServiceAPI.searchData(searchKeyword,searchCategory,searchType);
        callSearchData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();

                    if(status.equals("OK")){
                        JsonArray data = obj.get("result").getAsJsonArray();
                        if(searchType.equals("ARTIST")){
                            artistList.clear();
                            for(int i = 0 ; i < data.size() ; i++){
                                JsonObject singleData = data.get(i).getAsJsonObject();

                                String artistID = singleData.get("IDUser").getAsString();
                                String artistName = singleData.get("DisplayName").getAsString();
                                String artistDesc = singleData.get("AboutMe").getAsString();
                                String artistEMail = singleData.get("EMail").getAsString();
                                String artistFBLink = singleData.get("FacebookLink").getAsString();
                                String artistTWLink = singleData.get("TwitterLink").getAsString();

                                ModelArtistInformation dataToList = new ModelArtistInformation(artistID, artistName, artistEMail, artistDesc, artistFBLink, artistTWLink, 0);

                                artistList.add(dataToList);
                            }
                        }
                        else if(searchType.equals("ARTWORK")){

                            artworkList.clear();
                            for(int i = 0 ; i < data.size() ; i++){
                                JsonObject singleData = data.get(i).getAsJsonObject();
                                String idArtwork = singleData.get("IDArtWork").getAsString();
                                String titleArtwork = singleData.get("Title").getAsString();
                                String fileArtwork = singleData.get("DirectoryData").getAsString();
                                String idArtist = singleData.get("IDArtist").getAsString();
                                String nameArtist = singleData.get("DisplayName").getAsString();
                                String emailArtist = singleData.get("EMail").getAsString();

                                ModelArtworkInformation dataArtworkToList = new ModelArtworkInformation(idArtwork, titleArtwork, idArtist, nameArtist, emailArtist, fileArtwork);
                                artworkList.add(dataArtworkToList);
                            }
                        }
                        LoadDataToRVASearch();
                    }
                    else if(status.equals("EMPTY")){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No data match", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 1000);
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 1000);
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }, 1000);
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, 1000);
            }
        });
    }

    public void LoadDataToRVASearch(){
        progressDialog.dismiss();
        if(searchType.equals("ARTIST")){
            RVResult.setLayoutManager(new GridLayoutManager(this, 2));

            adapterArtistList = new RVAListArtistSearchResult(ActivitySearchResult.this, artistList);
            adapterArtistList.notifyDataSetChanged();
            //RVAListArtist mAdapterArtistList = new RVAListArtist(ActivitySearchResult.this,artistList);
            //mAdapterArtistList.notifyDataSetChanged();
            RVResult.setAdapter(adapterArtistList);
        }
        else if(searchType.equals("ARTWORK")){
            RVResult.setLayoutManager(new GridLayoutManager(this, 1));

            adapterArtworkList = new RVAListArtworkSearchResult(ActivitySearchResult.this,artworkList);
            adapterArtworkList.notifyDataSetChanged();
            RVResult.setAdapter(adapterArtworkList);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }
}
