package id.ac.umn.mobile.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Oktavius Wiguna on 11/05/2018.
 */

public class RVAListCommission extends RecyclerView.Adapter<RVAListCommission.ViewHolder>{

    private Context context;
    private List<ModelCommissionInformation> dataCommmisions;
    private String type;

    public RVAListCommission(Context context, List<ModelCommissionInformation> data, String type){
        this.context = context;
        this.dataCommmisions = data;
        this.type = type;
    }

    @Override
    public RVAListCommission.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_template_list_commission, parent,false);
        RVAListCommission.ViewHolder holder = new RVAListCommission.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RVAListCommission.ViewHolder holder, final int position) {
        final ModelCommissionInformation singleData = dataCommmisions.get(position);

        if(singleData.isSketchBaseAvailable()){
            Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/projectdata/"+singleData.getIdCommission()+"/SketchBase.jpg").fit().centerCrop().into(holder.commissionImage);
        }
        else if (!singleData.isSketchBaseAvailable()){
            Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/template/ProfilePicture.png").fit().centerCrop().into(holder.commissionImage);
        }

        holder.commissionTitle.setText(singleData.getTitleCommission());

        String sStartDate = new SimpleDateFormat("dd MMM yyyy").format(singleData.getStartDate());
        String sEndDate = new SimpleDateFormat("dd MMM yyyy").format(singleData.getDeadlineDate());
        holder.commissionDate.setText(sStartDate+" - "+sEndDate);
        if(type.equals("CommissionCustomer")){
            holder.userName.setText("Drawn by "+singleData.getNameArtist());
        }else if(type.equals("CommissionArtist")){
            holder.userName.setText("For "+singleData.getNameCustomer());
        }
        if(singleData.getStatusRequest().equals("PENDING")){
            holder.commissionStatus.setText("Waiting for Confirmation");
        }
        else if(singleData.getStatusRequest().equals("ACCEPTED")){
            if(singleData.getStatusProject().equals("PROGRESS")){
                holder.commissionStatus.setText("On Going");
            }
            else if(singleData.getStatusProject().equals("FINISHED")){
                holder.commissionStatus.setText("Completed");
            }
            else if(singleData.getStatusProject().equals("CANCELED")){
                holder.commissionStatus.setText("Terminated");
            }
        }
        else if(singleData.getStatusRequest().equals("DECLINED")){
            holder.commissionStatus.setText("Expired");
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityCommissionDetail.class);
                intent.putExtra( "idCommission", singleData.getIdCommission());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataCommmisions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        FrameLayout parentLayout;
        ImageView commissionImage;
        TextView commissionTitle, userName, commissionDate, commissionStatus;

        public ViewHolder(View itemView){
            super(itemView);

            parentLayout = (FrameLayout) itemView.findViewById(R.id.parentCardLayout);
            commissionImage = (ImageView) parentLayout.findViewById(R.id.commission_sketch_base_project);
            commissionTitle = (TextView) parentLayout.findViewById(R.id.commission_title);
            commissionStatus = (TextView) parentLayout.findViewById(R.id.commission_status);
            commissionDate = (TextView) parentLayout.findViewById(R.id.commission_date);
            userName = (TextView) parentLayout.findViewById(R.id.user_name);
        }
    }

}
