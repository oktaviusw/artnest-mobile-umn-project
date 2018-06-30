package id.ac.umn.mobile.myapplication;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySettingDeleteCategory extends AppCompatActivity {
    ProgressDialog progressDialog;

    List<String> categoryList = new ArrayList<>();

    FlowLayout categoryGroup;
    Button btnApplyUpdate;
    EditText editPasswordText;
    Spinner availableCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_delete_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setTitle("Delete Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryGroup = findViewById(R.id.artist_category_group_setting);
        availableCategorySpinner = findViewById(R.id.spinner_delete_category);
        editPasswordText = findViewById(R.id.password_edit);
        btnApplyUpdate = findViewById(R.id.btn_update_add_category);

        btnApplyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateForm();
            }
        });

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
        progressDialog.setMessage("Fetching User Data");
        progressDialog.show();
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
        progressDialog.hide();
        categoryGroup.removeAllViews();

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        progressDialog.hide();
    }

    public void ValidateForm(){
        String newCategory = availableCategorySpinner.getSelectedItem().toString();
        String userPassword = editPasswordText.getText().toString();

        if(newCategory.equals("None")){
            Toast.makeText(this, "Please select your new category", Toast.LENGTH_SHORT);
        }
        else if (userPassword.equals("")){
            editPasswordText.setError("Please input your password to continue");
            Toast.makeText(this, "Please input your password to continue", Toast.LENGTH_SHORT);
        }
        else{
            UpdateToServer();
        }
    }

    public void UpdateToServer(){
        progressDialog.setMessage("Applying Update");
        progressDialog.show();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idArtist = pref.getString("UserID","");
        String userPassword = editPasswordText.getText().toString();
        String targetCategory = availableCategorySpinner.getSelectedItem().toString();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUpdateCategory = webServiceAPI.applyUpdateDeleteCategoryData(idArtist, userPassword, targetCategory);

        callUpdateCategory.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement element = response.body();
                JsonObject obj = element.getAsJsonObject();

                String statusServer = obj.get("status").getAsString();
                String resultServer = obj.get("result").getAsString();

                if(statusServer.equals("OK")){
                    Intent data = new Intent();
                    data.setData(Uri.parse(resultServer));
                    setResult(RESULT_OK, data);

                    progressDialog.dismiss();

                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), resultServer, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }

}
