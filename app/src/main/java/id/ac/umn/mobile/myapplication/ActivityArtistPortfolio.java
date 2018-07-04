package id.ac.umn.mobile.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityArtistPortfolio extends AppCompatActivity {
    ModelArtistInformation artistData;
    List<ModelArtworkInformation> artworkList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();
    RecyclerView recyclerViewArtwork;
    RVAListArtworkProfilePage adapterArtworkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_portofolio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewArtwork = findViewById(R.id.RV_artwork_list_horizontal);
        adapterArtworkList = new RVAListArtworkProfilePage(this,artworkList);
        adapterArtworkList.notifyDataSetChanged();
        recyclerViewArtwork.setAdapter(adapterArtworkList);

        seedArtistData();

        CardView aboutMeCard = (CardView) findViewById(R.id.artist_desc_card);
        final TextView artistDesc= (TextView) findViewById(R.id.artist_about_me);

        aboutMeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(artistDesc.getVisibility()==View.GONE){
                    artistDesc.setVisibility(View.VISIBLE);
                }
                else if(artistDesc.getVisibility()==View.VISIBLE){
                    artistDesc.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void seedArtistData(){
        String idArtist = getIntent().getStringExtra("idArtist");

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

                    artistData = new ModelArtistInformation(artistID, artistName, artistEMail, artistDesc, artistFBLink, artistTWLink, 0);

                    JsonArray categoryMultipleData = singleData.get("Categories").getAsJsonArray();
                    for(int i = 0; i < categoryMultipleData.size(); i++){
                        categoryList.add(categoryMultipleData.get(i).getAsString());
                    }

                    JsonArray artworkMultipleData = singleData.get("Artworks").getAsJsonArray();
                    for(int i = 0; i < artworkMultipleData.size(); i++){
                        JsonObject singleArtworkData = artworkMultipleData.get(i).getAsJsonObject();

                        String artworkID = singleArtworkData.get("IDArtwork").getAsString();
                        String artworkTitle = singleArtworkData.get("Title").getAsString();
                        String artworkDataDirectory = singleArtworkData.get("DirectoryData").getAsString();

                        ModelArtworkInformation dataArtworkToList = new ModelArtworkInformation(artworkID,artworkTitle,artistID,artistName,artistEMail,artworkDataDirectory);
                        artworkList.add(dataArtworkToList);
                    }

                    LoadDataToActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void LoadDataToActivity(){
        ImageView backgroundImage = (ImageView) findViewById(R.id.artist_display_background_picture);
        ImageView profileImage = (ImageView) findViewById(R.id.artist_display_picture);
        TextView completeProject = (TextView) findViewById(R.id.completed_project);
        TextView displayName = (TextView) findViewById(R.id.artist_display_name_text);
        TextView email = (TextView) findViewById(R.id.artist_email_text);
        TextView descArtist = (TextView) findViewById(R.id.artist_about_me);
        TextView fbLink = (TextView) findViewById(R.id.facebook_link_text);
        TextView twitterLink = (TextView) findViewById(R.id.twitter_link_text);
        FlowLayout categoryGroup = (FlowLayout) findViewById(R.id.category_group);

        float conversionDP = this.getResources().getDisplayMetrics().density;

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+artistData.getEmail()+"/BackgroundProfile.jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(backgroundImage);

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+artistData.getEmail()+"/ProfilePicture.png")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(profileImage);

        displayName.setText(artistData.getName());
        email.setText(artistData.getEmail());
        completeProject.setText(artistData.getTotalCompletedCommission().toString()+" Completed Works");
        fbLink.setText(artistData.getFbLink());
        twitterLink.setText(artistData.getTwitterLink());
        descArtist.setText(artistData.getDesc());

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

        adapterArtworkList.notifyDataSetChanged();
        recyclerViewArtwork.setAdapter(adapterArtworkList);

        Button askCommissionButton = (Button) findViewById(R.id.ask_commission_button);
        askCommissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailto = "mailto:"+artistData.getEmail();

                Intent mailClient = new Intent(Intent.ACTION_SENDTO);
                mailClient.setData(Uri.parse(mailto));
                //mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                startActivity(mailClient);
            }
        });

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view_portfolio);
        scrollView.requestFocus(View.FOCUS_UP);
        scrollView.scrollTo(0,0);
    }

}
