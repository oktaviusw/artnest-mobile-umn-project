package id.ac.umn.mobile.myapplication;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Oktavius Wiguna on 10/05/2018.
 */

public class RVAListArtistSearchResult extends RecyclerView.Adapter<RVAListArtistSearchResult.ViewHolder>{
    private Context context;
    private List<ModelArtistInformation> dataArtists;

    public RVAListArtistSearchResult(Context context, List<ModelArtistInformation> data){
        this.context = context;
        this.dataArtists = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_search_result_artist, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ModelArtistInformation singleData = dataArtists.get(position);


        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+singleData.getEmail()+"/ProfilePicture.png").fit().centerCrop()
                .transform(new PicassoCircleTransform()).into(holder.profilePicture);

        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+singleData.getEmail()+"/BackgroundProfile.jpg").fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.profileBackground);

        holder.profileName.setText(singleData.getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityArtistPortfolio.class);
                intent.putExtra( "idArtist", singleData.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArtists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //CardView backgroundCard;
        ImageView profilePicture, profileBackground;
        TextView profileName;
        FrameLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            parentLayout = (FrameLayout) itemView.findViewById(R.id.parentCardLayout);
            profileBackground = (ImageView) itemView.findViewById(R.id.result_search_profile_display_background);
            profilePicture = (ImageView) parentLayout.findViewById(R.id.profile_display_picture);
            profileName = (TextView) parentLayout.findViewById(R.id.profile_display_name);
        }
    }
}