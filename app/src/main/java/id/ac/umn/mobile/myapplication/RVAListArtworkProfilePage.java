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

public class RVAListArtworkProfilePage extends RecyclerView.Adapter<RVAListArtworkProfilePage.ViewHolder>{

    private Context context;
    private List<ModelArtworkInformation> dataArtworks;

    public RVAListArtworkProfilePage(Context context, List<ModelArtworkInformation> data){
        this.context = context;
        this.dataArtworks = data;
    }

    @Override
    public RVAListArtworkProfilePage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_artwork_profile_page, parent,false);
        RVAListArtworkProfilePage.ViewHolder holder = new RVAListArtworkProfilePage.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAListArtworkProfilePage.ViewHolder holder, final int position) {
        final ModelArtworkInformation singleData = dataArtworks.get(position);

        /*
        Picasso.get().load("http://172.16.9.209/API/assets/userdata/"+singleData.getEmail().toString()+"/ProfilePicture.png").resize(80,80).centerCrop()
                .transform(new PicassoCircleTransform()).into(holder.profilePicture);
        */
        /*
        Picasso.get().load("https://cdn2.iconfinder.com/data/icons/design-creativity/512/magic-wand-128.png").resize(80,80).centerCrop()
                .transform(new PicassoCircleTransform()).into(holder.profilePicture);
        Picasso.get().load("https://cdn.pixabay.com/photo/2017/08/30/01/05/milky-way-2695569_960_720.jpg").resize(250,200).centerCrop()
                .into(holder.profileBackground);
        holder.profileName.setText(singleData.getName());
        holder.profileDesc.setText(singleData.getDesc());
        */
        /*
        holder.artworkTitle.setText(singleData.getTitleArtwork());
        Picasso.get().load(singleData.getDirectoryData()).fit().centerCrop().into(holder.artworkPreview);
        */

        holder.artworkTitle.setText(singleData.getTitleArtwork());
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+singleData.getEmailArtist()+"/artwork/"+singleData.getDirectoryData()).fit().centerCrop().into(holder.artworkPreview);
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
        ImageView artworkPreview;
        TextView artworkTitle;
        FrameLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);

            parentLayout = (FrameLayout) itemView.findViewById(R.id.parentCardLayout);
            artworkPreview = (ImageView) parentLayout.findViewById(R.id.artwork_display_preview);
            artworkTitle = (TextView) parentLayout.findViewById(R.id.artwork_title_text);
        }
    }
}
