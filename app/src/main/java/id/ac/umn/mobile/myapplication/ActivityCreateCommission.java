package id.ac.umn.mobile.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCreateCommission extends AppCompatActivity {

    boolean startDatePicked;
    boolean endDatePicked;
    boolean sketchBasePicked;

    EditText editTitleCommission;
    EditText editCustomerCommission;
    EditText editDescCommission;
    EditText editPriceCommission;
    EditText editStartDate;
    EditText editEndDate;

    ImageView sketchPreview;
    ImageView startDatePicker;
    ImageView endDatePicker;

    Button btnImagePicker;
    Button btnCreateCommissionOrder;

    String mediaPathSketch;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_commission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sketchBasePicked = false;
        startDatePicked = false;
        endDatePicked = false;

        editTitleCommission = findViewById(R.id.edit_text_title_commission);
        editCustomerCommission = findViewById(R.id.edit_text_email_customer);
        editDescCommission = findViewById(R.id.edit_text_desc_commission);
        editPriceCommission = findViewById(R.id.edit_text_price_commission);
        editStartDate = findViewById(R.id.edit_text_start_date);
        editEndDate = findViewById(R.id.edit_text_end_date);

        sketchPreview = findViewById(R.id.imageView_sketch_base);
        startDatePicker = findViewById(R.id.btn_start_date_picker);
        endDatePicker = findViewById(R.id.btn_end_date_picker);

        btnImagePicker = findViewById(R.id.pick_image_button);
        btnCreateCommissionOrder = findViewById(R.id.button_create_commission_order);

        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickStartDate();
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickEndDate();
            }
        });

        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageFromGallery();
            }
        });

        btnCreateCommissionOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateUploadOrder();
            }
        });
    }

    public void PickStartDate(){
        FragmentStartDatePicker dateFragment = new FragmentStartDatePicker();
        dateFragment.show(getSupportFragmentManager(), "startDatePicker");
        //Toast.makeText(this, "Function for Start Date", Toast.LENGTH_LONG).show();
    }

    public void PickEndDate(){
        FragmentEndDatePicker dateFragment = new FragmentEndDatePicker();
        dateFragment.show(getSupportFragmentManager(), "endDatePicker");
        //Toast.makeText(this, "Function for End Date", Toast.LENGTH_LONG).show();
    }

    public void PickImageFromGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);
    }

    public void ValidateUploadOrder(){
        Date startDate = new Date();
        Date endDate = new Date();
        String sTitle = editTitleCommission.getText().toString();
        String sCustomer = editCustomerCommission.getText().toString();
        String sPrice = editPriceCommission.getText().toString();
        String sDesc = editDescCommission.getText().toString();
        String sStartDate = editStartDate.getText().toString();
        String sEndDate = editEndDate.getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        String sArtist = pref.getString("UserID","");

        if(sTitle.replace(" ","").length()==0){
            editTitleCommission.setError("Please input this field");
        }
        else if(sCustomer.length()==0){
            editCustomerCommission.setError("Please customer's email");
        }
        else if(sPrice.length()==0){
            editPriceCommission.setError("Please input commission's price");
        }
        else if(startDatePicked == false){
            editStartDate.setError("Please pick a Date");
        }
        else if(endDatePicked == false){
            editEndDate.setError("Please pick a Date");
        }
        else{
            try {
                startDate = new SimpleDateFormat("dd-MM-yyyy").parse(sStartDate);
                endDate = new SimpleDateFormat("dd-MM-yyyy").parse(sEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date currentDate = new Date();

            if(currentDate.after(startDate)){
                Toast.makeText(this, "Pick Start Date at least a day after current day.", Toast.LENGTH_LONG).show();
            }
            else{
                if(startDate.after(endDate)){
                    Toast.makeText(this, "Invalid End Date.", Toast.LENGTH_LONG).show();
                }
                else {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String strStartDate = dateFormat.format(startDate);
                    String strEndDate = dateFormat.format(endDate);

                    UploadDataToServer(sArtist, sCustomer, sTitle, sPrice, sDesc, strStartDate, strEndDate);
                    //Toast.makeText(this, sTitle+"\n"+sCustomer+"\n"+sPrice+"\n"+sDesc+"\n"+sStartDate+"\n"+sEndDate, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void UploadDataToServer(String artistID, String customerEmail, String titleProject, String price, String description, String startDate, String endDate){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your order");
        progressDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("IDArtist", artistID);
        builder.addFormDataPart("EmailCustomer", customerEmail);
        builder.addFormDataPart("TitleProject", titleProject);
        builder.addFormDataPart("TokenValue", price);
        builder.addFormDataPart("Description",description);
        builder.addFormDataPart("DateStart", startDate);
        builder.addFormDataPart("DateEnd", endDate);

        if(sketchBasePicked){
            File file = new File(mediaPathSketch);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("imageData", file.getName(), requestBody);
        }


        MultipartBody fileToPost = builder.build();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callAddNewCommission = webServiceAPI.addNewCommission(fileToPost);

        callAddNewCommission.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()){
                    JsonElement jsonElement = response.body();
                    JsonObject object = jsonElement.getAsJsonObject();
                    String status = object.get("status").getAsString();
                    if(status.equals("OK")){
                        Toast.makeText(getApplicationContext(), "Commission added", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }, 1000);
                    }
                    else {
                        progressDialog.hide();
                        System.out.println(object.get("result").getAsString());
                        Toast.makeText(getApplicationContext(), object.get("result").getAsString(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                sketchBasePicked = true;

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPathSketch = cursor.getString(columnIndex);
                cursor.close();

                System.out.println(mediaPathSketch);

                Picasso.get().load(selectedImage).resize(1000, 0).into(sketchPreview);
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ProcessStartDatePickerResult(int year, int month, int day) {
        startDatePicked = true;
        String month_string = String.format("%02d",month+1);
        String day_string = String.format("%02d",day);
        String year_string = String.format("%02d",year);

        String dateMessage = (day_string + "-" + month_string + "-" + year_string);
        editStartDate.setText(dateMessage);
    }

    public void ProcessEndDatePickerResult(int year, int month, int day) {
        endDatePicked = true;
        String month_string = String.format("%02d",month+1);
        String day_string = String.format("%02d",day);
        String year_string = String.format("%02d",year);

        String dateMessage = (day_string + "-" + month_string + "-" + year_string);
        editEndDate.setText(dateMessage);
    }

}
