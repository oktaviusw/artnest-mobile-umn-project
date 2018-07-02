package id.ac.umn.mobile.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDeleteArtwork extends AppCompatActivity {

    ModelArtworkInformation data;

    ImageView artworkImage;
    EditText titleArtwork;
    EditText descArtwork;
    Button btnConfirmDelete;
    Button btnCancelDelete;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_artwork);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Edit Artwork");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artworkImage = (ImageView) findViewById(R.id.artwork_display);
        titleArtwork = (EditText) findViewById(R.id.artwork_title);
        descArtwork = (EditText) findViewById(R.id.artwork_description);
        btnConfirmDelete = (Button) findViewById(R.id.btn_confirm_delete);
        btnCancelDelete = (Button) findViewById(R.id.btn_cancel_delete);

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

        btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.setData(Uri.parse("Canceled"));
                setResult(RESULT_CANCELED,data);
                progressDialog.dismiss();
                finish();
            }
        });
        progressDialog.hide();
    }

}
