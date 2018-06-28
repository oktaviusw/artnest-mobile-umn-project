package id.ac.umn.mobile.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oktavius Wiguna on 30/04/2018.
 */

public class FragmentExplore extends Fragment {
    public List<ModelArtistInformation> artistList = new ArrayList<>();
    public List<ModelArtworkInformation> artworkList = new ArrayList<>();

    View myView;

    private RecyclerView recyclerViewArtist; //= findViewById(R.id.recyclerView);
    private RVAListArtist adapterArtistList;

    private RecyclerView recyclerViewArtwork;
    private RVAListArtwork adapterArtworkList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_explore,container,false);

        recyclerViewArtist = myView.findViewById(R.id.RV_artist_list_horizontal);
        adapterArtistList = new RVAListArtist(myView.getContext(),artistList);
        adapterArtistList.notifyDataSetChanged();
        recyclerViewArtist.setAdapter(adapterArtistList);

        recyclerViewArtwork = myView.findViewById(R.id.RV_artwork_list_horizontal);
        adapterArtworkList = new RVAListArtwork(myView.getContext(),artworkList);
        adapterArtworkList.notifyDataSetChanged();
        recyclerViewArtwork.setAdapter(adapterArtworkList);

        seedData();

        return myView;
    }

    public void seedData(){
        /*Get All Artist Data*/
        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);

        Call<JsonElement> callArtists = webServiceAPI.getAllArtistsData();

        callArtists.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray data = obj.get("result").getAsJsonArray();

                    for(int i = 0 ; i < data.size() ; i++){
                        JsonObject singleData = data.get(i).getAsJsonObject();
                        String id = singleData.get("IDArtist").getAsString();
                        String name = singleData.get("DisplayName").getAsString();
                        String email = singleData.get("EMail").getAsString();
                        ModelArtistInformation dataToList = new ModelArtistInformation(id, name, email);
                        artistList.add(dataToList);
                    }

                    adapterArtistList.notifyDataSetChanged();
                    recyclerViewArtist.setAdapter(adapterArtistList);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });



        /*Get All Artwork Data*/
        Call<JsonElement> callArtworks = webServiceAPI.getAllArtworksData();

        callArtworks.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonArray data = obj.get("result").getAsJsonArray();

                    for(int i = 0 ; i < data.size() ; i++){
                        JsonObject singleData = data.get(i).getAsJsonObject();
                        String idArtwork = singleData.get("IDArtWork").getAsString();
                        String titleArtwork = singleData.get("Title").getAsString();
                        String fileArtwork = singleData.get("DirectoryData").getAsString();
                        String idArtist = singleData.get("IDArtist").getAsString();
                        String nameArtist = singleData.get("DisplayName").getAsString();
                        String emailArtist = singleData.get("EMail").getAsString();

                        ModelArtworkInformation dataArtworkToList = new ModelArtworkInformation(idArtwork, titleArtwork, idArtist, nameArtist, emailArtist, fileArtwork);
                        artworkList.add(dataArtworkToList);
                    }

                    adapterArtworkList.notifyDataSetChanged();
                    recyclerViewArtwork.setAdapter(adapterArtworkList);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }
}
