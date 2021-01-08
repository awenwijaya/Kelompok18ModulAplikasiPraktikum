package klp18.praktikumprogmob.stt.network;

import klp18.praktikumprogmob.stt.entities.AccessToken;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name,
                               @Field("email") String email,
                               @Field("password") String password,
                               @Field("alamat_rumah") String alamat_rumah,
                               @Field("no_telepon") String no_telepon,
                               @Field("role") String role);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("update/{emailOld}")
    Call<UpdateProfileResponse> editProfile(
            @Path("emailOld") String emailOld,
            @Field("name") String name,
            @Field("alamat_rumah") String alamat_rumah,
            @Field("no_telepon") String no_telepon,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user")
    Call<UserModelResponse> user(@Field("username") String username);

    /*crud*/
    @GET("event")
    Call<ResponsModel> getEvent();

    @FormUrlEncoded
    @POST("event/store")
    Call<ResponsModel> postEvent(@Field("nama_acara") String nama_acara,
                             @Field("tanggal_acara") String tanggal_acara,
                             @Field("tempat_acara") String tempat_acara,
                             @Field("alamat") String alamat,
                             @Field("keterangan") String keterangan);

    @FormUrlEncoded
    @POST("event/update")
    Call<ResponsModel> updateEvent(@Field("id") String id,
                               @Field("nama_acara") String nama_acara,
                               @Field("tanggal_acara") String tanggal_acara,
                               @Field("tempat_acara") String tempat_acara,
                               @Field("alamat") String alamat,
                               @Field("keterangan") String keterangan);

    @FormUrlEncoded
    @POST("event/delete")
    Call<ResponsModel> deleteEvent(@Field("id") String id);

    @FormUrlEncoded
    @POST("event-show")
    Call<ResponsModel> lihatEvent(@Field("id") String id);

    @FormUrlEncoded
    @POST("approve")
    Call<ResponsModel> approve(@Field("nama_acara") String nama_acara,
                               @Field("tanggal_acara") String tanggal_acara,
                               @Field("tempat_acara") String tempat_acara,
                               @Field("alamat") String alamat,
                               @Field("keterangan") String keterangan,
                               @Field("photo") String photo);

    /*crud request acara*/
    @GET("request")
    Call<ResponsModelRequest> getRequest();

    @FormUrlEncoded
    @POST("request/store")
    Call<ResponsModelRequest> postRequest(@Field("nama_acara_request") String nama_acara_request,
                                          @Field("tanggal") String tanggal,
                                          @Field("lokasi") String lokasi,
                                          @Field("keterangan") String keterangan,
                                          @Field("nama_pengguna") String nama_pengguna,
                                          @Field("alamat") String alamat);

    @FormUrlEncoded
    @POST("request/update")
    Call<ResponsModelRequest> updateRequest(@Field("id") String id,
                                            @Field("nama_acara") String nama_acara_request,
                                            @Field("tanggal") String tanggal,
                                            @Field("lokasi") String lokasi,
                                            @Field("keterangan") String keterangan,
                                            @Field("nama_pengguna") String nama_pengguna,
                                            @Field("alamat") String alamat);

    @FormUrlEncoded
    @POST("request/delete")
    Call<ResponsModelRequest> deleteRequest(@Field("id") String id);

    @FormUrlEncoded
    @POST("request-show")
    Call<ResponsModelRequest> lihatRequest(@Field("id") String id);

    @GET("notificationSTT")
    Call<ResponseBody> notificationSTT();

}
