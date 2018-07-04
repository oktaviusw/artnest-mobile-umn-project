package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCommissionDetail extends AppCompatActivity {

    ModelCommissionInformation data;

    String emailCustomer;
    String emailArtist;

    TextView commissionTitle;
    TextView commissionDate;
    TextView commissionStatus;
    TextView commissionPrice;

    LinearLayout commissionDescCard;
    TextView commissionDescText;

    LinearLayout commissionSketchCard;
    ImageView commissionSketchPict;

    LinearLayout artistCard;
    ImageView artistProfilePict;
    TextView artistName;

    ImageView customerProfilePict;
    TextView customerName;

    Button acceptCommission;
    Button declineCommission;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commissionTitle = (TextView) findViewById(R.id.commission_title);
        commissionDate = (TextView) findViewById(R.id.commission_date);
        commissionStatus = (TextView) findViewById(R.id.commission_status);
        commissionPrice = (TextView) findViewById(R.id.commission_price);
        commissionDescCard = (LinearLayout) findViewById(R.id.commission_description_card);
        commissionDescText = (TextView) findViewById(R.id.commission_description);
        commissionSketchCard = (LinearLayout) findViewById(R.id.commission_sketch_card);
        commissionSketchPict = (ImageView) findViewById(R.id.commission_sketch);

        artistCard = (LinearLayout) findViewById(R.id.commission_artist_card);
        artistProfilePict = (ImageView) findViewById(R.id.commission_artist_pict);
        artistName = (TextView) findViewById(R.id.commission_artist_name);

        customerProfilePict = (ImageView)  findViewById(R.id.commission_customer_pict);
        customerName = (TextView) findViewById(R.id.commission_customer_name);

        acceptCommission = (Button) findViewById(R.id.btn_accept);
        declineCommission = (Button) findViewById(R.id.btn_decline);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Commission Data");
        progressDialog.setCancelable(false);

        progressDialog.show();
        SeedData();
    }

    @Override
    public boolean onSupportNavigateUp(){
        progressDialog.dismiss();
        finish();
        return true;
    }

    public void SeedData(){
        String idCommission = getIntent().getStringExtra("idCommission");

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callCommission = webServiceAPI.getCommission(idCommission);

        callCommission.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String sIDProject = singleData.get("IDProject").getAsString();
                    String sIDCustomer = singleData.get("IDCustomer").getAsString();
                    String sIDArtist = singleData.get("IDArtist").getAsString();
                    String sCustomerName = singleData.get("CustomerName").getAsString();
                    String sTitleProject = singleData.get("TitleProject").getAsString();
                    Integer sTokenValue = singleData.get("TokenValue").getAsInt();
                    String sArtistName = singleData.get("ArtistName").getAsString();
                    String sRequestStatus = singleData.get("RequestStatus").getAsString();
                    String sStartDate = singleData.get("DateStart").getAsString();
                    String sEndDate = singleData.get("DateEnd").getAsString();
                    String sDescProject = singleData.get("Description").getAsString();
                    Boolean sAnySketch = singleData.get("anySketchBase").getAsBoolean();
                    Date startDate = new Date();
                    Date endDate = new Date();

                    try {
                        startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sStartDate);
                        endDate = new SimpleDateFormat("yyyy-MM-dd").parse(sEndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String sProjectStatus;
                    if(sRequestStatus.equals("ACCEPTED")){
                        sProjectStatus = singleData.get("CommissionStatus").getAsString();
                        data = new ModelCommissionInformation(sIDProject, sIDCustomer, sCustomerName, sIDArtist, sArtistName, sRequestStatus, sProjectStatus, sTokenValue, sTitleProject, startDate, endDate, sDescProject, sAnySketch);
                    }
                    else{
                        data = new ModelCommissionInformation(sIDProject, sIDCustomer, sCustomerName, sIDArtist, sArtistName, sRequestStatus, sTokenValue, sTitleProject, startDate, endDate, sDescProject, sAnySketch);
                    }

                    emailArtist = singleData.get("ArtistEMail").getAsString();
                    emailCustomer = singleData.get("CustomerEMail").getAsString();



                    LoadToActivity();

                }else{
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    public void LoadToActivity(){
        commissionTitle.setText(data.getTitleCommission());

        String sStartDate = new SimpleDateFormat("dd MMMM yyyy").format(data.getStartDate());
        String sEndDate = new SimpleDateFormat("dd MMMM yyyy").format(data.getDeadlineDate());

        commissionDate.setText(sStartDate+" - "+sEndDate);

        if(data.getStatusRequest().equals("ACCEPTED")){
            String projectStatus = data.getStatusProject();
            if(projectStatus.equals("PROGRESS")){
                commissionStatus.setText("Work in Progress");
            }
            else if (projectStatus.equals("FINISHED")){
                commissionStatus.setText("Completed");
            }
            else if(projectStatus.equals("CANCELLED")){
                commissionStatus.setText("Terminated");
            }
        }
        else{
            commissionStatus.setText("Waiting for Confirmation");
        }

        commissionPrice.setText("Rp. "+String.valueOf(data.getTokenValue())+",-");

        if(data.getDescriptionProject().equals("")){
            commissionDescCard.setVisibility(View.GONE);
        }
        else{
            commissionDescText.setText(data.getDescriptionProject());
        }

        if(data.isSketchBaseAvailable()){
            Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/projectdata/"+data.getIdCommission()+"/SketchBase.jpg").resize(1000,0).centerCrop().into(commissionSketchPict);
        }
        else{
            commissionSketchCard.setVisibility(View.GONE);
        }

        artistName.setText(data.getNameArtist());
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+emailArtist+"/ProfilePicture.png")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(artistProfilePict);
        artistCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityArtistPortfolio.class);
                intent.putExtra( "idArtist", data.getIdArtist());
                startActivity(intent);
            }
        });

        customerName.setText(data.getNameCustomer());
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+emailCustomer+"/ProfilePicture.png")
                .memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(customerProfilePict);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String idUser = pref.getString("UserID","");

        if(idUser.equals(data.getIdArtist())){
            acceptCommission.setVisibility(View.GONE);
            declineCommission.setVisibility(View.GONE);
        }

        if(data.getStatusRequest().equals("PENDING")){
            acceptCommission.setText("Accept Commission");
            declineCommission.setText("Reject Commission");
        }
        else if(data.getStatusRequest().equals("ACCEPTED")){
            if(data.getStatusProject().equals("PROGRESS")){
                acceptCommission.setText("Complete Commission");
                declineCommission.setText("Cancel Commission");
            }else{
                acceptCommission.setVisibility(View.GONE);
                declineCommission.setVisibility(View.GONE);
            }
        }

        acceptCommission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToCommission("ACCEPT");
            }
        });

        declineCommission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseToCommission("DECLINE");
            }
        });
        progressDialog.hide();
    }

    public void ResponseToCommission(String response){
        progressDialog.setMessage("Sending your response");
        progressDialog.show();

        String sID = data.getIdCommission();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callCommission = webServiceAPI.responseCommission(sID,response);

        callCommission.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    String status = obj.get("status").getAsString();
                    if(status.equals("OK")){
                        Toast.makeText(getApplicationContext(), "Response Submitted", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 1000);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT);
            }
        });
    }
}
