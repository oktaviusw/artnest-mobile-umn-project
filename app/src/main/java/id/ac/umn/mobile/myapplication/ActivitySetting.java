package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySetting extends AppCompatActivity {

    ProgressDialog progressDialog;

    String mediaPathUserProfilePicture;
    ImageView userProfilePicture;
    boolean newProfilePict;

    String mediaPathBackgroundArtistPicture;
    boolean newBackgroundArtistPict;

    List<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data");

        LinearLayout accountSettingHeader = findViewById(R.id.account_setting_header);
        final LinearLayout accountSettingContent = findViewById(R.id.account_setting_content);
        final ImageView accountSettingIndicator = findViewById(R.id.account_setting_header_indicator);

        accountSettingHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountSettingContent.getVisibility() == View.VISIBLE){
                    accountSettingIndicator.setImageResource(R.drawable.ic_setting_drop_down);
                    accountSettingContent.setVisibility(View.GONE);
                }
                else if(accountSettingContent.getVisibility() == View.GONE){
                    accountSettingIndicator.setImageResource(R.drawable.ic_setting_drop_up);
                    accountSettingContent.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayout artistSettingHeader = findViewById(R.id.artist_setting_header);
        final LinearLayout artistSettingContent = findViewById(R.id.artist_setting_content);
        final ImageView artistSettingIndicator = findViewById(R.id.artist_setting_header_indicator);

        artistSettingHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(artistSettingContent.getVisibility() == View.VISIBLE){
                    artistSettingIndicator.setImageResource(R.drawable.ic_setting_drop_down);
                    artistSettingContent.setVisibility(View.GONE);
                }
                else if(artistSettingContent.getVisibility() == View.GONE){
                    artistSettingIndicator.setImageResource(R.drawable.ic_setting_drop_up);
                    artistSettingContent.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayout creditSettingHeader = findViewById(R.id.credit_setting_header);
        final LinearLayout creditSettingContent = findViewById(R.id.credit_setting_content);
        final ImageView creditSettingIndicator = findViewById(R.id.credit_setting_header_indicator);

        creditSettingHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditSettingContent.getVisibility() == View.VISIBLE){
                    creditSettingIndicator.setImageResource(R.drawable.ic_setting_drop_down);
                    creditSettingContent.setVisibility(View.GONE);
                }
                else if(creditSettingContent.getVisibility() == View.GONE){
                    creditSettingIndicator.setImageResource(R.drawable.ic_setting_drop_up);
                    creditSettingContent.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayout thankSettingHeader = findViewById(R.id.thank_setting_header);
        final LinearLayout thankSettingContent = findViewById(R.id.thank_setting_content);
        final ImageView thankSettingIndicator = findViewById(R.id.thank_setting_header_indicator);

        thankSettingHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thankSettingContent.getVisibility() == View.VISIBLE){
                    thankSettingIndicator.setImageResource(R.drawable.ic_setting_drop_down);
                    thankSettingContent.setVisibility(View.GONE);
                }
                else if(thankSettingContent.getVisibility() == View.GONE){
                    thankSettingIndicator.setImageResource(R.drawable.ic_setting_drop_up);
                    thankSettingContent.setVisibility(View.VISIBLE);
                }
            }
        });

        LoadUserData();

        newProfilePict = false;
        newBackgroundArtistPict =false;

        Button updateUserBtn = (Button) findViewById(R.id.btn_update_data_user);
        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateUpdateUserInfo();
            }
        });

        Button updateArtistBtn = (Button) findViewById(R.id.btn_update_data_artist);
        updateArtistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateUpdateArtistInfo();
            }
        });

        userProfilePicture = findViewById(R.id.edit_image_display_picture);
        userProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

        Button selectBackgroundBtn = findViewById(R.id.btn_pick_image_background_pict);
        selectBackgroundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
            }
        });

        Button addArtistCategoryBtn = findViewById(R.id.btn_add_category);
        addArtistCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext() , ActivitySettingAddCategory.class);
                startActivityForResult(addIntent,2);
            }
        });

        Button deleteArtistCategoryBtn = findViewById(R.id.btn_delete_category);
        deleteArtistCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent(getApplicationContext(), ActivitySettingDeleteCategory.class);
                startActivityForResult(deleteIntent,3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){ //Pick Profile Image Event
            try {
                // When an Image is picked
                if (resultCode == RESULT_OK && null != data) {

                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPathUserProfilePicture = cursor.getString(columnIndex);
                    cursor.close();

                    System.out.println(mediaPathUserProfilePicture);

                    // Set the Image in ImageView for Previewing the Media
                    newProfilePict = true;
                    Picasso.get().load(selectedImage).fit().centerCrop().transform(new PicassoCircleTransform()).into(userProfilePicture);
                } else {
                    Toast.makeText(this, "You haven't picked Image for Profile Picture", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == 1){ //Pick Background Artist Image Event
            try {
                // When an Image is picked
                if (resultCode == RESULT_OK && null != data) {

                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPathBackgroundArtistPicture = cursor.getString(columnIndex);
                    cursor.close();

                    // Set the Image in ImageView for Previewing the Media
                    newBackgroundArtistPict = true;

                    ImageView backgroundArtistPict = findViewById(R.id.imageView_background_profile);
                    Picasso.get().load(selectedImage).fit().centerCrop().into(backgroundArtistPict);
                } else {
                    Toast.makeText(this, "You haven't picked Image for Profile Picture", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == 2 || requestCode == 3){  //Add New Category Event
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_LONG).show();
                LoadArtistCategoriesData();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        progressDialog.dismiss();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.setData(Uri.parse(""));
        setResult(RESULT_CANCELED, data);

        progressDialog.dismiss();
        finish();
        super.onBackPressed();
    }

    public void LoadUserData(){
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();
        final EditText editDisplayName = findViewById(R.id.edit_text_display_name);
        final ImageView editImageView = findViewById(R.id.edit_image_display_picture);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idUser = pref.getString("UserID","");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUser = webServiceAPI.getUserdata(idUser);

        callUser.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String userDisplayName = singleData.get("DisplayName").getAsString();
                    String userEmail = singleData.get("EMail").getAsString();
                    boolean userIsArtist = singleData.get("isArtist").getAsBoolean();

                    editDisplayName.setText(userDisplayName);
                    Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+userEmail+"/ProfilePicture.png")
                            .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(editImageView);

                    CardView cardViewArtist = findViewById(R.id.artist_setting_main);
                    if(userIsArtist){
                        cardViewArtist.setVisibility(View.VISIBLE);
                        LoadArtistData();
                    }
                    else{
                        cardViewArtist.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void ValidateUpdateUserInfo(){
        progressDialog.setMessage("Updating Profile");
        progressDialog.show();

        EditText editTextDisplayName = findViewById(R.id.edit_text_display_name);
        EditText editTextNewPassword = findViewById(R.id.edit_text_new_password);
        EditText editTextNewPasswordRetype = findViewById(R.id.edit_text_new_password_retype);
        EditText editTextPasswordAuth = findViewById(R.id.edit_current_password_account);

        if(editTextDisplayName.getText().toString().length() == 0){
            progressDialog.dismiss();
            editTextDisplayName.setError("Display name should not be empty");
        }
        else if(editTextPasswordAuth.getText().toString().length() == 0){
            progressDialog.dismiss();
            editTextPasswordAuth.setError("Password should not be empty");
        }
        else{
            if(editTextNewPassword.getText().toString().length()>0){
                if(editTextNewPassword.getText().toString().length()>=8){
                    if(editTextNewPassword.getText().toString().equals(editTextNewPasswordRetype.getText().toString())){
                        UpdateUserInfoToServer();
                    }
                    else{
                        progressDialog.dismiss();
                        editTextNewPasswordRetype.setError("Password doesn't match with the new password");
                    }
                }
                else{
                    progressDialog.dismiss();
                    editTextNewPassword.setError("Password must be at least 8 characters long");
                }
            }
            else{
                UpdateUserInfoToServer();
            }
        }
    }

    public void LoadArtistCategories(){
        FlowLayout categoryGroup = (FlowLayout) findViewById(R.id.artist_category_group_setting);

        categoryGroup.removeAllViews();

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

    public void UpdateUserInfoToServer(){
        //Toast.makeText(this, "Uploading to Server", Toast.LENGTH_LONG).show();

        final EditText editDisplayName = findViewById(R.id.edit_text_display_name);
        final ImageView editImageView = findViewById(R.id.edit_image_display_picture);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String inputTargetID = pref.getString("UserID","");
        builder.addFormDataPart("IDTarget", inputTargetID);

        EditText editPasswordAuth = findViewById(R.id.edit_current_password_account);
        String inputPasswordAuth = editPasswordAuth.getText().toString();
        builder.addFormDataPart("PasswordAuth", inputPasswordAuth);

        EditText editTextDisplayName = findViewById(R.id.edit_text_display_name);
        String inputNewDisplayName = editTextDisplayName.getText().toString();
        builder.addFormDataPart("NewDisplayName", inputNewDisplayName);

        EditText editTextNewPassword = findViewById(R.id.edit_text_new_password);
        String inputNewPassword = editTextNewPassword.getText().toString();
        if(inputNewPassword.length()==0){
            inputNewPassword = inputPasswordAuth;
        }
        builder.addFormDataPart("NewPassword", inputNewPassword);

        if(newProfilePict){
            File file = new File(mediaPathUserProfilePicture);
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("image/*"), file);

            builder.addFormDataPart("imageData", file.getName(), requestBodyFile);
        }

        MultipartBody fileToPost = builder.build();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUpdateUserData = webServiceAPI.updateUserData(fileToPost);

        callUpdateUserData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();

                    progressDialog.dismiss();
                    System.out.println(obj.get("result").getAsString());
                    Toast.makeText(getApplicationContext(), obj.get("result").getAsString(), Toast.LENGTH_SHORT).show();
                    LoadUserData();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void LoadArtistData(){
        final EditText editTextAboutMe = findViewById(R.id.edit_about_me_artist);
        final EditText editTextFacebookLink = findViewById(R.id.edit_fb_link);
        final EditText editTextTwitterLink = findViewById(R.id.edit_twitter_link);
        final ImageView editBackgroundImageView = findViewById(R.id.imageView_background_profile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idArtist = pref.getString("UserID","");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callArtist = webServiceAPI.getArtist(idArtist);

        callArtist.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String artistID = singleData.get("IDUser").getAsString();
                    String artistName = singleData.get("DisplayName").getAsString();
                    String artistDesc = singleData.get("AboutMe").getAsString();
                    String artistEMail = singleData.get("EMail").getAsString();
                    String artistFBLink = singleData.get("FacebookLink").getAsString();
                    String artistTWLink = singleData.get("TwitterLink").getAsString();
                    categoryList.clear();
                    JsonArray categoryMultipleData = singleData.get("Categories").getAsJsonArray();
                    for(int i = 0; i < categoryMultipleData.size(); i++){
                        categoryList.add(categoryMultipleData.get(i).getAsString());
                    }

                    ModelArtistInformation artistData = new ModelArtistInformation(artistID, artistName, artistEMail, artistDesc, artistFBLink, artistTWLink, 0);

                    String fbLinkEditable = artistData.getFbLink().substring(25);
                    String twitterLinkEditable = artistData.getTwitterLink().substring(24);
                    Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+artistData.getEmail()+"/BackgroundProfile.jpg")
                            .fit().centerCrop().into(editBackgroundImageView);
                    LoadArtistCategories();

                    editTextAboutMe.setText(artistData.getDesc());
                    editTextFacebookLink.setText(fbLinkEditable);
                    editTextTwitterLink.setText(twitterLinkEditable);

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void LoadArtistCategoriesData(){
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

                LoadArtistCategories();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void ValidateUpdateArtistInfo(){
        progressDialog.setMessage("Updating Profile");
        progressDialog.show();

        EditText editTextPasswordAuth = (EditText) findViewById(R.id.edit_current_password_artist);

        if(editTextPasswordAuth.getText().toString().length() == 0){
            progressDialog.dismiss();
            editTextPasswordAuth.setError("Password should not be empty");
        }
        else{
            UploadArtistInfoToServer();
        }
    }

    public void UploadArtistInfoToServer(){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String inputTargetID = pref.getString("UserID","");
        builder.addFormDataPart("IDTarget", inputTargetID);

        EditText editAboutMe = (EditText) findViewById(R.id.edit_about_me_artist);
        String sAboutMe = editAboutMe.getText().toString();
        builder.addFormDataPart("AboutMe", sAboutMe);

        EditText editFBLink = (EditText) findViewById(R.id.edit_fb_link);
        String sFBLink = editFBLink.getText().toString();
        builder.addFormDataPart("FbLink", sFBLink);

        EditText editTWLink = (EditText) findViewById(R.id.edit_twitter_link);
        String sTWLink = editTWLink.getText().toString();
        builder.addFormDataPart("TwLink", sTWLink);

        EditText editPasswordAuth = (EditText) findViewById(R.id.edit_current_password_artist);
        String sPassword = editPasswordAuth.getText().toString();
        builder.addFormDataPart("PasswordAuth", sPassword);

        if(newBackgroundArtistPict){
            File file = new File(mediaPathBackgroundArtistPicture);
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("image/*"), file);

            builder.addFormDataPart("imageData", file.getName(), requestBodyFile);
        }

        MultipartBody fileToPost = builder.build();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUpdateArtistData = webServiceAPI.updateArtistData(fileToPost);

        callUpdateArtistData.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();

                    progressDialog.dismiss();
                    System.out.println(obj.get("result").getAsString());
                    Toast.makeText(getApplicationContext(), obj.get("result").getAsString(), Toast.LENGTH_SHORT).show();
                    LoadUserData();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
