package id.ac.umn.mobile.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEditArtwork extends AppCompatActivity {

    ModelArtworkInformation data;

    ImageView artworkImage;
    EditText titleArtwork;
    EditText descArtwork;
    Button btnApplyUpdate;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artwork);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Edit Artwork");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artworkImage = (ImageView) findViewById(R.id.artwork_display);
        titleArtwork = (EditText) findViewById(R.id.artwork_title);
        descArtwork = (EditText) findViewById(R.id.artwork_description);
        btnApplyUpdate = (Button) findViewById(R.id.btn_apply_update);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Artwork Data");
        progressDialog.setCancelable(false);

        seedDataArtwork();
    }

    @Override
    public boolean onSupportNavigateUp(){
        progressDialog.dismiss();
        Intent data = new Intent();
        data.setData(Uri.parse("Canceled"));
        setResult(RESULT_CANCELED,data);
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

    public void seedDataArtwork(){
        progressDialog.show();
        String idArtwork = getIntent().getStringExtra("idArtwork");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callArtwork = webServiceAPI.getArtwork(idArtwork);

        callArtwork.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String idArtwork = singleData.get("IDArtWork").getAsString();
                    String titleArtwork = singleData.get("Title").getAsString();
                    String descArtwork = singleData.get("DescriptionArtwork").getAsString();
                    String fileArtwork = singleData.get("DirectoryData").getAsString();
                    String idArtist = singleData.get("IDArtist").getAsString();
                    String nameArtist = singleData.get("DisplayName").getAsString();
                    String emailArtist = singleData.get("EMail").getAsString();

                    data = new ModelArtworkInformation(idArtwork, titleArtwork, descArtwork, idArtist, nameArtist, emailArtist, fileArtwork);

                    loadDataToActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Intent data = new Intent();
                data.setData(Uri.parse("Something went wrong"));
                setResult(RESULT_CANCELED,data);
                progressDialog.dismiss();
                finish();
            }
        });

    }

    public void loadDataToActivity(){
        titleArtwork.setText(data.getTitleArtwork());
        descArtwork.setText(data.getDescArtwork());

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+data.getEmailArtist()+"/artwork/"+data.getDirectoryData()).resize(1000,0).centerCrop().into(artworkImage);

        btnApplyUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDataUpdate();
            }
        });
        progressDialog.hide();
    }

    public void validateDataUpdate(){
        String newTitle = titleArtwork.getText().toString();

        if(newTitle.replace(" ","").equals("")){
            titleArtwork.setError("Title should not be empty");
        }
        else{
            UploadToServer();
        }
    }

    public void UploadToServer(){
        progressDialog.setMessage("Applying Update");
        progressDialog.show();

        String idTarget = data.getId();
        String newTitle = titleArtwork.getText().toString();
        String newDesc = descArtwork.getText().toString();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callArtwork = webServiceAPI.updateArtwork(idTarget,newTitle,newDesc);

        callArtwork.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();
                    String result = obj.get("result").getAsString();

                    if(status.equals("OK")){
                        Intent data = new Intent();
                        data.setData(Uri.parse(result));
                        setResult(RESULT_OK,data);
                        progressDialog.dismiss();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });
    }
}
