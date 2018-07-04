package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBecomeArtist extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText editAboutMe;
    EditText editFBLink;
    EditText editTWLink;
    ImageView imageBackgroundArtist;
    Button btnPickBackgroundImage;
    Button btnRegisterArtist;

    String mediaPathBackgroundArtistPicture;
    boolean newBackgroundArtistPict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_artist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Become Artist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        newBackgroundArtistPict = false;

        editAboutMe = findViewById(R.id.about_me_text);
        editFBLink = findViewById(R.id.facebook_text);
        editTWLink = findViewById(R.id.twitter_text);
        imageBackgroundArtist = findViewById(R.id.imageView_background_profile);
        btnPickBackgroundImage = findViewById(R.id.btn_pick_image_background_pict);
        btnRegisterArtist = findViewById(R.id.btn_confirm_become_artist);

        btnPickBackgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

        btnRegisterArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
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
                    Picasso.get().load(selectedImage).fit().centerCrop().into(imageBackgroundArtist);
                } else {
                    Toast.makeText(this, "You haven't picked Image for Profile Picture", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ValidateData(){
        String sAboutMe = editAboutMe.getText().toString();

        if(sAboutMe.equals("")){
            editAboutMe.setError("This Field is required");
        }
        else{
            SubmitDataToServer();
        }
    }

    public void SubmitDataToServer(){
        progressDialog.setMessage("Processing your request");
        progressDialog.show();

        String sAboutMe = editAboutMe.getText().toString();
        String sFBLink = editFBLink.getText().toString();
        String sTWLink = editTWLink.getText().toString();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String inputTargetID = pref.getString("UserID","");

        builder.addFormDataPart("idArtist", inputTargetID);
        builder.addFormDataPart("aboutMe", sAboutMe);
        builder.addFormDataPart("facebookLink", sFBLink.replace(" ",""));
        builder.addFormDataPart("twitterLink", sTWLink.replace(" ",""));

        if(newBackgroundArtistPict){
            File file = new File(mediaPathBackgroundArtistPicture);
            RequestBody requestBodyFile = RequestBody.create(MediaType.parse("image/*"), file);

            builder.addFormDataPart("imageData", file.getName(), requestBodyFile);
        }

        MultipartBody fileToPost = builder.build();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callBecomeArtist = webServiceAPI.becomeArtist(fileToPost);

        callBecomeArtist.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();

                    if(status.equals("OK")){
                        Intent data = new Intent();
                        data.setData(Uri.parse("SUCCESSFUL"));
                        setResult(RESULT_OK, data);

                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent data = new Intent();
        data.setData(Uri.parse(""));
        setResult(RESULT_CANCELED, data);

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
}
