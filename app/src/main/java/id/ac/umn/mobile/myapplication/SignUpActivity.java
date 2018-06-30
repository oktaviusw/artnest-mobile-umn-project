package id.ac.umn.mobile.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.r0adkll.slidr.Slidr;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);





        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Signing Up");


        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        TextView signinButton= (TextView) findViewById(R.id.signin_text);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),SignInActivity.class);
                startActivity(i);
            }
        });


        Slidr.attach(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerUser(){
        progressDialog.show();
        EditText usernameEdit = (EditText) findViewById(R.id.username_edit);
        EditText emailEdit = (EditText) findViewById(R.id.email_edit);
        EditText passwordEdit = (EditText) findViewById(R.id.password_edit);

       String username = usernameEdit.getText().toString();
        System.out.println(username);
       String email = emailEdit.getText().toString();
       String password = passwordEdit.getText().toString();

       APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
       Call<JsonElement> callRegister = webServiceAPI.registerArtNest(username, email, password);

       callRegister.enqueue(new Callback<JsonElement>() {
           @Override
           public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
               progressDialog.dismiss();
               if(response.isSuccessful()){
                   JsonElement element = response.body();
                   JsonObject obj = element.getAsJsonObject();

                   String statusCheck = obj.get("status").getAsString();

                   if(statusCheck.equals("OK")){
                       String message = obj.get("result").getAsString();


                       Intent i = new Intent(getBaseContext(), SignInActivity.class);
                       startActivity(i);
                       Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                   }
                   else if(statusCheck.equals("ERROR")){
                       Toast.makeText(getApplicationContext(), "Email already exists, please try again.", Toast.LENGTH_SHORT).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Success! You can now sign in.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getBaseContext(), SignInActivity.class);
                startActivity(i);
           }
       });

    }




}
