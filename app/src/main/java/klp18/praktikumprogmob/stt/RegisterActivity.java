package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import klp18.praktikumprogmob.stt.entities.AccessToken;
import klp18.praktikumprogmob.stt.entities.ApiError;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    ApiService service;
    Call<AccessToken> call;
    TokenManager tokenManager;
    String role = "Pengguna";
    EditText tilName, tilEmail, tilPassword, alamatrumah, nomortelepon, rolePenggunaPendaftaran;
    Button login, regis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tilName = findViewById(R.id.registerNamePengguna);
        tilEmail = findViewById(R.id.registerEmailPengguna);
        tilPassword = findViewById(R.id.registerPasswordPengguna);
        alamatrumah = findViewById(R.id.registerAlamatRumahPengguna);
        nomortelepon = findViewById(R.id.registerNomorTeleponText);
        login = findViewById(R.id.btnLogin_regis);
        regis = findViewById(R.id.btnDaftar_regis);
        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tilName.getText().toString();
                String email = tilEmail.getText().toString();
                String password = tilPassword.getText().toString();
                String no_telepon = nomortelepon.getText().toString();
                String alamatRumah = alamatrumah.getText().toString();
                tilName.setError(null);
                tilEmail.setError(null);
                tilPassword.setError(null);
                nomortelepon.setError(null);
                alamatrumah.setError(null);
                call = service.register(name, email, password, alamatRumah, no_telepon, role);
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                        Log.w(TAG, "onResponse: " + response );

                        if(response.isSuccessful()) {
                            Log.w(TAG, "onResponse: " + response.body() );
                            tokenManager.saveToken(response.body());
                            finish();
                        } else {
                            handleErrors(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {

                        Log.w(TAG, "onFailure: " + t.getMessage() );

                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginUserActivity.class));
            }
        });

    }

    private void handleErrors(ResponseBody response) {

        ApiError apiError = Utils.converErrors(response);

        for(Map.Entry<String, List<String>> error: apiError.getErrors().entrySet()) {

            if(error.getKey().equals("name")) {
                tilName.setError(error.getValue().get(0));
                Toast.makeText(RegisterActivity.this, "Mohon diperiksa kembali", Toast.LENGTH_SHORT).show();
            }

            if(error.getKey().equals("email")) {
                tilEmail.setError(error.getValue().get(0));
                Toast.makeText(RegisterActivity.this, "Mohon diperiksa kembali", Toast.LENGTH_SHORT).show();
            }

            if(error.getKey().equals("password")) {
                tilPassword.setError(error.getValue().get(0));
                Toast.makeText(RegisterActivity.this, "Mohon diperiksa kembali", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null) {
            call.cancel();
            call = null;
        }
    }
}