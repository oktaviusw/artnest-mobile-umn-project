package id.ac.umn.mobile.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDisplayArtwork extends AppCompatActivity {

    ModelArtworkInformation data;

    ImageView artworkImage;
    ImageView profileImage;
    TextView titleArtwork;
    TextView descArtwork;
    TextView artistName;
    FloatingActionMenu artworkEditMenu;
    FloatingActionButton artworkEditButton;
    FloatingActionButton artworkDeleteButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_artwork);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artworkImage = (ImageView) findViewById(R.id.display_artwork);
        profileImage = (ImageView) findViewById(R.id.artist_display_picture);
        titleArtwork = (TextView) findViewById(R.id.title_artwork);
        descArtwork = (TextView) findViewById(R.id.artwork_description);
        artistName = (TextView) findViewById(R.id.artist_display_name);
        artworkEditMenu = (FloatingActionMenu) findViewById(R.id.fab_menu_artwork);
        artworkEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_artwork);
        artworkDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_artwork);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Artwork Data");

        progressDialog.show();
        seedDataArtwork();
    }

    @Override
    public boolean onSupportNavigateUp(){
        progressDialog.dismiss();
        finish();
        return true;
    }

    public void seedDataArtwork(){
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
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
            }
        });

    }

    public void loadDataToActivity(){
        titleArtwork.setText(data.getTitleArtwork());
        descArtwork.setText(data.getDescArtwork());
        artistName.setText(data.getArtistName());

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+data.getEmailArtist()+"/artwork/"+data.getDirectoryData()).resize(1000,0).centerCrop().into(artworkImage);

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+data.getEmailArtist()+"/ProfilePicture.png").fit().centerCrop()
                .transform(new PicassoCircleTransform()).into(profileImage);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idUser = pref.getString("UserID","");

        if(idUser.equals(data.getIdArtist())){
            artworkEditMenu.setVisibility(View.VISIBLE);
            artworkEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext() , ActivityEditArtwork.class);
                    intent.putExtra( "idArtwork", data.getId());
                    startActivityForResult(intent, 1);
                }
            });
            artworkDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext() , ActivityDeleteArtwork.class);
                    intent.putExtra( "idArtwork", data.getId());
                    startActivityForResult(intent, 2);
                }
            });
        }

        progressDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            Toast.makeText(this, data.getData().toString(), Toast.LENGTH_LONG).show();
            if(resultCode==RESULT_OK){
                seedDataArtwork();
            }
        }
        else if(requestCode==2){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
