package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySettingAddCategory extends AppCompatActivity {

    ProgressDialog progressDialog;

    List<String> categoryList = new ArrayList<>();
    List<String> availableCategoryList  = new ArrayList<>();
    Spinner availableCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_add_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        setTitle("Add New Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        availableCategorySpinner = findViewById(R.id.spinner_available_category);

        FetchUserCategoriesData();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent data = new Intent();
        data.setData(Uri.parse("Add Category canceled"));
        setResult(RESULT_CANCELED, data);

        progressDialog.dismiss();
        finish();

        return true;
    }

    public void FetchUserCategoriesData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idArtist = pref.getString("UserID","");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callArtist = webServiceAPI.getArtist(idArtist);

        callArtist.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement element = response.body();
                JsonObject obj = element.getAsJsonObject();
                JsonObject singleData = obj.get("result").getAsJsonObject();

                categoryList.clear();
                JsonArray categoryMultipleData = singleData.get("Categories").getAsJsonArray();
                for(int i = 0; i < categoryMultipleData.size(); i++){
                    categoryList.add(categoryMultipleData.get(i).getAsString());
                }

                FetchAvailableCategoryData();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Intent data = new Intent();
                data.setData(Uri.parse("Request Timed Out"));
                setResult(RESULT_CANCELED, data);

                progressDialog.dismiss();
                finish();
            }
        });
    }

    public void FetchAvailableCategoryData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idArtist = pref.getString("UserID","");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callData = webServiceAPI.getAvailableCategoryData(idArtist);

        callData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement element = response.body();
                JsonObject obj = element.getAsJsonObject();
                JsonArray categoryMultipleData = obj.get("result").getAsJsonArray();

                availableCategoryList.clear();
                availableCategoryList.add("None");
                for(int i = 0; i < categoryMultipleData.size(); i++){
                    JsonObject singleCategoryData = categoryMultipleData.get(i).getAsJsonObject();
                    System.out.println(singleCategoryData.get("CategoryName").getAsString());
                    availableCategoryList.add(singleCategoryData.get("CategoryName").getAsString());
                }
                LoadDataToIntent();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Intent data = new Intent();
                data.setData(Uri.parse("Request Timed Out"));
                setResult(RESULT_CANCELED, data);

                progressDialog.dismiss();
                finish();
            }
        });
    }

    public void LoadDataToIntent(){
        FlowLayout categoryGroup = (FlowLayout) findViewById(R.id.artist_category_group_setting);

        categoryGroup.removeAllViews();

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, availableCategoryList);
        adapterCategory.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        availableCategorySpinner.setAdapter(adapterCategory);

        float conversionDP = this.getResources().getDisplayMetrics().density;
        for(int i = 0; i < categoryList.size() ; i++){
            CardView cardCategory = new CardView(this);

            CardView.LayoutParams cardParams= new CardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(5*(int)conversionDP, 5*(int)conversionDP, 5*(int)conversionDP, 5*(int)conversionDP);

            cardCategory.setLayoutParams(cardParams);
            cardCategory.setCardBackgroundColor(Color.parseColor("#D9A069"));
            cardCategory.setRadius(10*conversionDP);
            cardCategory.setContentPadding(8*(int)conversionDP,8*(int)conversionDP,8*(int)conversionDP,8*(int)conversionDP);
            cardCategory.setCardElevation(5*(int)conversionDP);

            ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textCategory = new TextView(this);
            textCategory.setLayoutParams(textParams);
            textCategory.setText(categoryList.get(i));
            textCategory.setTextColor(Color.parseColor("#f0f0f0"));
            textCategory.setTypeface(null, Typeface.BOLD);
            cardCategory.addView(textCategory);

            categoryGroup.addView(cardCategory);
        }
    }
}
