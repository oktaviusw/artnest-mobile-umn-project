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

public class SignInActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameEditText = (EditText) findViewById(R.id.username_edit);
        passwordEditText = (EditText) findViewById(R.id.password_edit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In");


        Button homebuts = (Button) findViewById(R.id.login_button);
        homebuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginCheck();
            }
        });

        TextView  forgetButs= (TextView) findViewById(R.id.forget_password);
        forgetButs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        TextView  signupbuts= (TextView) findViewById(R.id.signup_text);
        signupbuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getBaseContext(),SignUpActivity.class);
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

    public void LoginCheck(){
        progressDialog.show();
        String usernameTxt = usernameEditText.getText().toString();
        String passwordTxt = passwordEditText.getText().toString();

        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callLogin = webServiceAPI.loginArtNest(usernameTxt, passwordTxt);

        callLogin.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();

                    String statusCheck = obj.get("status").getAsString();

                    if(statusCheck.equals("OK")){
                        String idUser = obj.get("result").getAsString();
                        SharedPreferences.Editor prefEdit = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                        prefEdit.putBoolean("LoggedIn",true);
                        prefEdit.putString("UserID",idUser);
                        prefEdit.commit();

                        Intent i = new Intent(getBaseContext(),ActivityMain.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Invalid Login, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went Wrong.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went Wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        if(pref.getBoolean("LoggedIn",false)){
            Intent i = new Intent(getBaseContext(),ActivityMain.class);
            startActivity(i);
        }
    }
}
