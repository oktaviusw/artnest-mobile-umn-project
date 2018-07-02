package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ActivityAddArtwork extends AppCompatActivity {

    String mediaPath;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artwork);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading the Artwork");

        Button pickImageButton = (Button)findViewById(R.id.pick_image_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

        Button submitArtwork = (Button)findViewById(R.id.confirm_upload_artwork);
        submitArtwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitArtworkData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println(mediaPath);

                // Set the Image in ImageView for Previewing the Media
                ImageView artworkPreview = (ImageView) findViewById(R.id.imageView_artwork_preview);
                Picasso.get().load(selectedImage).resize(1000,0).into(artworkPreview);
                //artworkPreview.setImageBitmap(BitmapFactory.decodeFile(mediaPath));


            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void submitArtworkData() {
        progressDialog.show();

        EditText titleArtworkEdit = (EditText) findViewById(R.id.title_new_artwork);
        final String titleArtwork = titleArtworkEdit.getText().toString();

        EditText descArtworkEdit = (EditText) findViewById(R.id.desc_new_artwork);
        final String descArtwork = descArtworkEdit.getText().toString();

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUser = webServiceAPI.getUserdata(pref.getString("UserID",""));

        callUser.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String artistID = singleData.get("IDUser").getAsString();
                    String artistEmail = singleData.get("EMail").getAsString();

                    UploadToServerAPI(artistID, artistEmail, titleArtwork, descArtwork);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void UploadToServerAPI(String artistID, String artistEmail, String titleArtwork, String descArtwork){
        File file = new File(mediaPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("artistEmail", artistEmail);
        builder.addFormDataPart("artworkTitle", titleArtwork);
        builder.addFormDataPart("artworkDesc", descArtwork);
        builder.addFormDataPart("artistID", artistID);
        builder.addFormDataPart("imageData", file.getName(), requestBody);

        MultipartBody fileToPost = builder.build();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callAddNewArtwork = webServiceAPI.addNewArtwork(fileToPost);

        callAddNewArtwork.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();
                    if (status.equals("OK")) {
                        Toast.makeText(getApplicationContext(), "Upload Artwork Successful", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 1000);

                    } else {
                        System.out.println(obj.get("result").getAsString());
                        Toast.makeText(getApplicationContext(), obj.get("result").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }
}
