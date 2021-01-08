package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.User;
import klp18.praktikumprogmob.stt.network.UpdateProfileResponse;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    EditText nama, alamat, noTelp, password;
    TextView role;
    Button simpan;
    SharedPreferences editPref;
    String email_pengguna_stt;
    ApiService api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        nama = (EditText) findViewById(R.id.editNamaLengkap);
        alamat = (EditText) findViewById(R.id.editAlamatRumah);
        noTelp = (EditText) findViewById(R.id.editNomorTelepon);
        password = (EditText) findViewById(R.id.edit_Password);
        simpan = (Button) findViewById(R.id.edit_SimpanButton);
        role = (TextView) findViewById(R.id.RolePenggunaTextView);
        editPref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        email_pengguna_stt = editPref.getString("email", " ");
        api = RetrofitBuilder.createService(ApiService.class);
        api.user(email_pengguna_stt).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                nama.setText(response.body().getData().get(0).getName());
                alamat.setText(response.body().getData().get(0).getAlamat_rumah());
                noTelp.setText(response.body().getData().get(0).getNo_telepon());
                role.setText(response.body().getData().get(0).getRole());
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService api = RetrofitBuilder.createService(ApiService.class);
                Call<UpdateProfileResponse> updateProfileResponseCall = api.editProfile(email_pengguna_stt, nama.getText().toString(), alamat.getText().toString(), noTelp.getText().toString(), password.getText().toString());
                updateProfileResponseCall.enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        if(response.body().getMessage() != null) {
                            SharedPreferences.Editor editor = editPref.edit();
                            String email = response.body().getData().get(0).getEmail();
                            editor.putString("email", email);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



}