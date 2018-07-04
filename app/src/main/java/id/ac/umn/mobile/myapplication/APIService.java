package id.ac.umn.mobile.myapplication;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Oktavius Wiguna on 09/05/2018.
 */

/* ini ngelist function apa aja yang bisa diambil dari web apinya, 
*  kalo mau masukin parameter pake post kurang tau deh
*  paling baca2 dokumentasi
*/
public interface APIService {
    @POST("LoginUser")
    @FormUrlEncoded
    Call<JsonElement> loginArtNest(@Field("Email") String emailUser,
                                   @Field("Password") String passwordUser);
    @POST("RegisterUser")
    @FormUrlEncoded
    Call<JsonElement> registerArtNest(@Field("DisplayName") String username,
                                        @Field("Email") String email,
                                        @Field("Password") String password);

    @POST("UpdateUserData")
    Call<JsonElement> updateUserData(@Body RequestBody form);

    @GET("LoadSingleUserData/{id}")
    Call<JsonElement> getUserdata(@Path("id") String id);

    @GET("ShowAllArtist")
    Call<JsonElement> getAllArtistsData();

    @GET("LoadArtistData/{id}")
    Call<JsonElement> getArtist(@Path("id") String id);

    @POST("UpdateArtistData")
    Call<JsonElement> updateArtistData(@Body RequestBody form);

    @GET("ShowAllArtworks")
    Call<JsonElement> getAllArtworksData();

    @GET("LoadArtworkData/{id}")
    Call<JsonElement> getArtwork(@Path("id") String id);

    @POST("AddNewArtwork")
    Call<JsonElement> addNewArtwork(@Body RequestBody file);

    @POST("UpdateArtworkData")
    @FormUrlEncoded
    Call<JsonElement> updateArtwork(@Field("IDTarget") String idTarget,
                                    @Field("NewTitle") String newTitle,
                                    @Field("NewDesc") String newDesc);

    @GET("DeleteArtwork/{id}")
    Call<JsonElement> deleteArtwork(@Path("id") String id);

    @GET("GetAllCategories")
    Call<JsonElement> getCategories();

    @POST("SearchData")
    @FormUrlEncoded
    Call<JsonElement> searchData(@Field("keywordSearch") String keywordSearch,
                                 @Field("categorySearch") String categorySearch,
                                 @Field("typeSearch") String typeSearch);

    @GET("ShowAllRequest/{id}/{role}")
    Call<JsonElement> getAllCommissionData(@Path("id") String id,
                                           @Path("role") String role);

    @GET("LoadCommissionData/{id}")
    Call<JsonElement> getCommission(@Path("id") String id);

    @POST("AddNewCommission") Call<JsonElement> addNewCommission(@Body RequestBody file);

    @GET("GetAvailableCategory/{id}")
    Call<JsonElement> getAvailableCategoryData(@Path("id") String id);

    @POST("UpdateAddCategory")
    @FormUrlEncoded
    Call<JsonElement> applyUpdateAddCategoryData(@Field("idArtist") String IDArtist,
                                                 @Field("passwordArtist") String passwordArtist,
                                                 @Field("newCategoryArtist") String newCategoryArtist);

    @POST("UpdateDeleteCategory")
    @FormUrlEncoded
    Call<JsonElement> applyUpdateDeleteCategoryData(@Field("idArtist") String IDArtist,
                                                    @Field("passwordArtist") String passwordArtist,
                                                    @Field("targetCategoryArtist") String newCategoryArtist);

    @POST("BecomeArtist") Call<JsonElement> becomeArtist(@Body RequestBody file);
}
