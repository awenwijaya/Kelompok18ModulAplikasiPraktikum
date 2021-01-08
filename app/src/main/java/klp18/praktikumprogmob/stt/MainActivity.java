package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import klp18.praktikumprogmob.stt.events.ListEventActivity;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnLogout)
    ImageButton logout;

    @BindView(R.id.btnEditProfile)
    ImageButton editProfile;

    @BindView(R.id.btnEvent)
    ImageButton event;

    @BindView(R.id.btnRequestAdmin)
    ImageButton request;

    ApiService service, api;
    TokenManager tokenManager;
    TextView namaAdmin;
    SharedPreferences adminPref;
    String emailAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namaAdmin = (TextView) findViewById(R.id.textViewNamaAdmin);
        FirebaseMessaging.getInstance().subscribeToTopic("alldevices");
        api = RetrofitBuilder.createService(ApiService.class);
        adminPref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        emailAdmin = adminPref.getString("email", " ");
        api.user(emailAdmin).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                namaAdmin.setText(response.body().getData().get(0).getName());
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
    }

    @OnClick(R.id.btnLogout)
    void logout(){
        startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
        tokenManager.deleteToken();
        finish();
    }

    @OnClick(R.id.btnEditProfile)
    void goToEditProfile(){
        startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
        finish();
    }

    @OnClick(R.id.btnEvent)
    void goToEventList(){
        startActivity(new Intent(MainActivity.this, ListEventActivity.class));
        finish();
    }

    @OnClick(R.id.btnRequestAdmin)
    void goToRequest(){
        startActivity(new Intent(MainActivity.this, RequestAcaraAdminActivity.class));
        finish();
    }

}