package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentPageProfile extends Fragment {
    ModelArtistInformation artistData;
    List<ModelArtworkInformation> artworkList = new ArrayList<>();
    List<String> categoryList = new ArrayList<>();
    RecyclerView recyclerViewArtwork;
    RVAListArtworkProfilePage adapterArtworkList;

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_page_profile,container,false);

        recyclerViewArtwork = myView.findViewById(R.id.RV_artwork_list_horizontal);
        adapterArtworkList = new RVAListArtworkProfilePage(myView.getContext(),artworkList);
        adapterArtworkList.notifyDataSetChanged();
        recyclerViewArtwork.setAdapter(adapterArtworkList);

        seedArtistData();

        CardView aboutMeCard = (CardView) myView.findViewById(R.id.artist_desc_card);
        final TextView artistDesc= (TextView) myView.findViewById(R.id.artist_about_me);

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

        com.github.clans.fab.FloatingActionButton fabCreateCommission = (com.github.clans.fab.FloatingActionButton) myView.findViewById(R.id.fab_create_commission);
        fabCreateCommission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myView.getContext() , ActivityCreateCommission.class);
                startActivityForResult(intent, 1);
            }
        });

        com.github.clans.fab.FloatingActionButton fabAddArtwork = (com.github.clans.fab.FloatingActionButton) myView.findViewById(R.id.fab_add_artwork);
        fabAddArtwork.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(myView.getContext() , ActivityAddArtwork.class);
                                                 startActivityForResult(intent, 1);
                                             }
                                         }
        );

        com.github.clans.fab.FloatingActionButton fabEditPortfolio = (com.github.clans.fab.FloatingActionButton) myView.findViewById(R.id.fab_edit_portfolio);
        fabEditPortfolio.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(myView.getContext() , ActivitySetting.class);
                                                 startActivityForResult(intent, 1);
                                             }
                                         }
        );

        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            seedArtistData();
        }
    }

    public void seedArtistData(){
        SharedPreferences pref = myView.getContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
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
                    Integer artistWorks = singleData.get("CompletedWorks").getAsInt();

                    artistData = new ModelArtistInformation(artistID, artistName, artistEMail, artistDesc, artistFBLink, artistTWLink, artistWorks);

                    categoryList.clear();
                    JsonArray categoryMultipleData = singleData.get("Categories").getAsJsonArray();
                    for(int i = 0; i < categoryMultipleData.size(); i++){
                        categoryList.add(categoryMultipleData.get(i).getAsString());
                    }

                    artworkList.clear();
                    JsonArray artworkMultipleData = singleData.get("Artworks").getAsJsonArray();
                    for(int i = 0; i < artworkMultipleData.size(); i++){
                        JsonObject singleArtworkData = artworkMultipleData.get(i).getAsJsonObject();

                        String artworkID = singleArtworkData.get("IDArtwork").getAsString();
                        String artworkTitle = singleArtworkData.get("Title").getAsString();
                        String artworkDataDirectory = singleArtworkData.get("DirectoryData").getAsString();

                        ModelArtworkInformation dataArtworkToList = new ModelArtworkInformation(artworkID,artworkTitle,artistID,artistName,artistEMail,artworkDataDirectory);
                        artworkList.add(dataArtworkToList);
                    }

                    LoadDataToFragment();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    public void LoadDataToFragment(){
        ImageView backgroundImage = (ImageView) myView.findViewById(R.id.artist_display_background_picture);
        ImageView profileImage = (ImageView) myView.findViewById(R.id.artist_display_picture);
        TextView completeProject = (TextView) myView.findViewById(R.id.completed_project);
        TextView displayName = (TextView) myView.findViewById(R.id.artist_display_name_text);
        TextView email = (TextView) myView.findViewById(R.id.artist_email_text);
        TextView descArtist = (TextView) myView.findViewById(R.id.artist_about_me);
        TextView fbLink = (TextView) myView.findViewById(R.id.facebook_link_text);
        TextView twitterLink = (TextView) myView.findViewById(R.id.twitter_link_text);
        FlowLayout categoryGroup = (FlowLayout) myView.findViewById(R.id.category_group);

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+artistData.getEmail()+"/BackgroundProfile.jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(backgroundImage);

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+artistData.getEmail()+"/ProfilePicture.png")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(profileImage);

        displayName.setText(artistData.getName());
        email.setText(artistData.getEmail());
        completeProject.setText(artistData.getTotalCompletedCommission().toString()+" Completed Works");
        if(artistData.getFbLink().substring(25).equals("")){
            fbLink.setVisibility(View.GONE);
        }
        else{
            fbLink.setText(artistData.getFbLink());
        }

        if(artistData.getTwitterLink().substring(24).equals("")){
            twitterLink.setVisibility(View.GONE);
        }
        else{
            twitterLink.setText(artistData.getTwitterLink());
        }
        descArtist.setText(artistData.getDesc());

        float conversionDP = myView.getContext().getResources().getDisplayMetrics().density;

        categoryGroup.removeAllViews();
        for(int i = 0; i < categoryList.size() ; i++){
            CardView cardCategory = new CardView(myView.getContext());

            CardView.LayoutParams cardParams= new CardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(5*(int)conversionDP, 5*(int)conversionDP, 5*(int)conversionDP, 5*(int)conversionDP);

            cardCategory.setLayoutParams(cardParams);
            cardCategory.setCardBackgroundColor(Color.parseColor("#D9A069"));
            cardCategory.setRadius(10*conversionDP);
            cardCategory.setContentPadding(8*(int)conversionDP,8*(int)conversionDP,8*(int)conversionDP,8*(int)conversionDP);
            cardCategory.setCardElevation(5*(int)conversionDP);

            ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textCategory = new TextView(myView.getContext());
            textCategory.setLayoutParams(textParams);
            textCategory.setText(categoryList.get(i));
            textCategory.setTextColor(Color.parseColor("#f0f0f0"));
            textCategory.setTypeface(null, Typeface.BOLD);
            cardCategory.addView(textCategory);

            categoryGroup.addView(cardCategory);
        }

        adapterArtworkList.notifyDataSetChanged();
        recyclerViewArtwork.setAdapter(adapterArtworkList);

        fbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistData.getFbLink()));
                startActivity(browserIntent);
            }
        });

        twitterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistData.getTwitterLink()));
                startActivity(browserIntent);
            }
        });

        ScrollView scrollView = (ScrollView) myView.findViewById(R.id.scroll_view_portfolio);
        scrollView.requestFocus(View.FOCUS_UP);
        scrollView.scrollTo(0,0);
    }
}
