package id.ac.umn.mobile.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ModelUserInformation dataUserLogin;
    public static String CURRENT_STATE = "";
    public static String PREV_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        Drawable logo = getDrawable(R.drawable.application_logo_header);
        toolbar.setLogo(logo);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child != null)
                if (child.getClass() == ImageView.class) {
                    ImageView iv2 = (ImageView) child;
                    if ( iv2.getDrawable() == logo ) {
                        iv2.setAdjustViewBounds(true);
                    }
                }
        }
        */

        getSupportActionBar().setLogo(R.drawable.application_logo);
        //getSupportActionBar().setIcon(R.drawable.application_logo);
        //getSupportActionBar().setTitle("ArtNest");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //toggle.setDrawerIndicatorEnabled(false);
        //toggle.setHomeAsUpIndicator(R.drawable.application_logo);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LoadUserData();

        if(CURRENT_STATE.equals("")){
            CURRENT_STATE = "HOME";
        }
        LoadMainFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(CURRENT_STATE.equals("HOME")){
            getMenuInflater().inflate(R.menu.explore_menu, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.general_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            CURRENT_STATE = "SEARCH_DATA";
            LoadMainFragment();
        }
        else if (id == R.id.action_home) {
            CURRENT_STATE = "HOME";
            LoadMainFragment();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        PREV_STATE = CURRENT_STATE;

        if (id == R.id.nav_explore) {
            CURRENT_STATE = "HOME";
        } else if (id == R.id.nav_messages) {
            CURRENT_STATE = "MESSAGE";
        } else if (id == R.id.nav_request_list) {
            CURRENT_STATE = "COMMISSION_CUSTOMER";
        } else if (id == R.id.nav_artist_profile_page) {
            CURRENT_STATE = "PROFILE_PAGE_ARTIST";
        } else if (id == R.id.nav_artist_commission_list) {
            CURRENT_STATE = "COMMISSION_ARTIST";
        } else if (id == R.id.nav_artist_become_artist) {
            CURRENT_STATE = "BECOME_ARTIST";
        } else if (id == R.id.nav_setting_account) {
            CURRENT_STATE = "SETTING_ACCOUNT";
        } else if (id == R.id.nav_info_about_us) {
            CURRENT_STATE = "INFO_ABOUT_US";
        } else if (id == R.id.nav_log_out) {
            SharedPreferences.Editor prefEdit = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
            prefEdit.clear();
            prefEdit.commit();

            finish();
        }
        LoadMainFragment();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LoadUserData(){
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        APIService webServiceAPI = APIClient.getApiClient().create(APIService.class);
        Call<JsonElement> callUser = webServiceAPI.getUserdata(pref.getString("UserID",""));

        callUser.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    JsonElement element = response.body();
                    JsonObject obj = element.getAsJsonObject();
                    JsonObject singleData = obj.get("result").getAsJsonObject();

                    String userLoginID = singleData.get("IDUser").getAsString();
                    String userLoginName = singleData.get("DisplayName").getAsString();
                    String userEmail = singleData.get("EMail").getAsString();
                    int userLoginToken = singleData.get("Token").getAsInt();
                    boolean userLoginIsArtist = singleData.get("isArtist").getAsBoolean();

                    dataUserLogin = new ModelUserInformation(userLoginID, userLoginName, userEmail, userLoginToken, userLoginIsArtist);
                    ValidateNavigationDrawer();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void ValidateNavigationDrawer(){
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*Ngubah Data di nav view*/
        View headerNavView = navigationView.getHeaderView(0);

        ImageView imageViewProfile = (ImageView) headerNavView.findViewById(R.id.imageViewProfile);
        Picasso.get().load("https://artnest-umn.000webhostapp.com/assets/userdata/"+dataUserLogin.getUserEmail()+"/ProfilePicture.png")
                .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().transform(new PicassoCircleTransform()).into(imageViewProfile);

        TextView nameUser = (TextView) headerNavView.findViewById(R.id.fullnameProfileNav);
        nameUser.setText(dataUserLogin.getUserName());

        TextView tokenUser = (TextView) headerNavView.findViewById(R.id.tokenProfileNav);
        tokenUser.setText(dataUserLogin.getUserToken() + " Token");

        Menu menu = navigationView.getMenu();
        MenuItem menuBecomeArtist = menu.findItem(R.id.nav_artist_become_artist);
        MenuItem menuPageProfile = menu.findItem(R.id.nav_artist_profile_page);
        MenuItem menuCommissionList = menu.findItem(R.id.nav_artist_commission_list);

        if(dataUserLogin.isArtist()){
            menuPageProfile.setVisible(true);
            menuCommissionList.setVisible(true);
            menuBecomeArtist.setVisible(false);
        }
        else{
            menuPageProfile.setVisible(false);
            menuCommissionList.setVisible(false);
            menuBecomeArtist.setVisible(true);
        }

        navigationView.invalidate();
    }

    public void LoadMainFragment(){
        FragmentManager fragmentManager = getFragmentManager();

        if(CURRENT_STATE.equals("HOME")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentExplore()).commit();
        }
        else if(CURRENT_STATE.equals("MESSAGE")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentMessageList()).commit();
        }
        else if(CURRENT_STATE.equals("COMMISSION_CUSTOMER")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentRequestList()).commit();
        }
        else if(CURRENT_STATE.equals("PROFILE_PAGE_ARTIST")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentPageProfile()).commit();
        }
        else if(CURRENT_STATE.equals("COMMISSION_ARTIST")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentCommisionList()).commit();
        }
        else if(CURRENT_STATE.equals("BECOME_ARTIST")){
            Intent intent = new Intent(getApplicationContext() , ActivityBecomeArtist.class);
            startActivityForResult(intent, 2);
            CURRENT_STATE = PREV_STATE;
        }
        else if(CURRENT_STATE.equals("SETTING_ACCOUNT")){
            //fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentAccountSetting()).commit();
            Intent intent = new Intent(getApplicationContext() , ActivitySetting.class);
            startActivityForResult(intent, 1);
            CURRENT_STATE = PREV_STATE;
        }
        else if(CURRENT_STATE.equals("SEARCH_DATA")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentSearch()).commit();
        }
        else if(CURRENT_STATE.equals("INFO_ABOUT_US")){
            fragmentManager.beginTransaction().replace(R.id.main_content, new FragmentInfoAboutUs()).commit();
        }
        invalidateOptionsMenu();
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        recreate();
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){

        }

        ValidateNavigationDrawer();
        invalidateOptionsMenu();
    }
}
