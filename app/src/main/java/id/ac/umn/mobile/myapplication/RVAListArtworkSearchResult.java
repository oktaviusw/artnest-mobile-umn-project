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

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Oktavius Wiguna on 11/05/2018.
 */



public class RVAListArtworkSearchResult extends RecyclerView.Adapter<RVAListArtworkSearchResult.ViewHolder>{

    private Context context;
    private List<ModelArtworkInformation> dataArtworks;

    public RVAListArtworkSearchResult(Context context, List<ModelArtworkInformation> data){
        this.context = context;
        this.dataArtworks = data;
    }

    @Override
    public RVAListArtworkSearchResult.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_search_result_artwork, parent,false);
        RVAListArtworkSearchResult.ViewHolder holder = new RVAListArtworkSearchResult.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAListArtworkSearchResult.ViewHolder holder, final int position) {
        final ModelArtworkInformation singleData = dataArtworks.get(position);

        holder.artworkTitle.setText(singleData.getTitleArtwork());
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+singleData.getEmailArtist()+"/artwork/"+singleData.getDirectoryData()).fit().centerCrop().into(holder.artworkPreview);

        holder.artistName.setText(singleData.getArtistName());
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+singleData.getEmailArtist()+"/ProfilePicture.png").fit().centerCrop().transform(new PicassoCircleTransform()).into(holder.artistProfilePicture);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityDisplayArtwork.class);
                intent.putExtra( "idArtwork", singleData.getId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataArtworks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //CardView backgroundCard;
        ImageView artistProfilePicture, artworkPreview;
        TextView artworkTitle, artistName;
        FrameLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            parentLayout = (FrameLayout) itemView.findViewById(R.id.parentCardLayout);

            artworkPreview = (ImageView) parentLayout.findViewById(R.id.artwork_display_preview);
            artistProfilePicture = (ImageView) parentLayout.findViewById(R.id.artist_profile_picture);
            artworkTitle = (TextView) parentLayout.findViewById(R.id.artwork_title_text);
            artistName = (TextView) parentLayout.findViewById(R.id.artist_display_name_text);
        }
    }
}
