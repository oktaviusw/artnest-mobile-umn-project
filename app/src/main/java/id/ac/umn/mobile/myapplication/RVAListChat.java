package id.ac.umn.mobile.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Oktavius Wiguna on 11/05/2018.
 */

public class RVAListChat extends RecyclerView.Adapter<RVAListChat.ViewHolder>{


    private Context context;
    private List<ModelChatInformation> dataChat;

    public RVAListChat(Context context, List<ModelChatInformation> data){
        this.context = context;
        this.dataChat = data;
    }

    @Override
    public RVAListChat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_list_chat, parent,false);
        RVAListChat.ViewHolder holder = new RVAListChat.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAListChat.ViewHolder holder, final int position) {
        ModelChatInformation singleData = dataChat.get(position);

        Picasso.get().load("https://cdnb.artstation.com/p/assets/images/images/008/005/831/large/anna-jeane-astolfo.jpg?1509884215")
                .fit().centerCrop().transform(new PicassoCircleTransform()).into(holder.targetProfileImage);

        holder.targetName.setText(singleData.getNameSender());
        holder.contextMessage.setText(singleData.getContextChat());
    }

    @Override
    public int getItemCount() {
        return dataChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parentLayout;
        ImageView targetProfileImage;
        TextView targetName, contextMessage;

        public ViewHolder(View itemView){
            super(itemView);

            parentLayout = (LinearLayout) itemView.findViewById(R.id.parentCardLayout);
            targetProfileImage = (ImageView) parentLayout.findViewById(R.id.target_display_picture);
            targetName = (TextView) parentLayout.findViewById(R.id.target_display_name);
            contextMessage = (TextView) parentLayout.findViewById(R.id.last_message_context);
        }
    }
}
