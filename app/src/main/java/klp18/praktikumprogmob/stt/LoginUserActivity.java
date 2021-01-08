package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import klp18.praktikumprogmob.stt.entities.AccessToken;
import klp18.praktikumprogmob.stt.entities.ApiError;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUserActivity extends AppCompatActivity {

    private static final String TAG = "LoginUserActivity";
    Button register, login;
    EditText email, password;
    ApiService api;
    TokenManager tokenManager;
    Call<AccessToken> call;
    SharedPreferences loginPrefUser;
    String emailUser, passwordUser;
    TextView role_user;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        api = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        loginPrefUser = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        email = findViewById(R.id.loginEmailPengguna);
        password = findViewById(R.id.loginPasswordPengguna);
        login = (Button) findViewById(R.id.btnLogin_loginPengguna);
        register = (Button) findViewById(R.id.btnRegister_loginPengguna);
        role_user = (TextView) findViewById(R.id.textViewCekRoleUser);

        if(tokenManager.getToken().getAccessToken() != null) {
            role = loginPrefUser.getString("role", " ");
            if(role.equals("Pengguna")) {
                startActivity(new Intent(LoginUserActivity.this, UserHomeActivity.class));
                finish();
            } else if(role.equals("Pengurus")) {
                startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                finish();
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                password.setError(null);
                emailUser = email.getText().toString();
                passwordUser = password.getText().toString();
                api.user(emailUser).enqueue(new Callback<UserModelResponse>() {
                    @Override
                    public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                        role_user.setText(response.body().getData().get(0).getRole());
                        role = role_user.getText().toString();
                        if(role.equals("Pengguna")) {
                            api.login(emailUser, passwordUser).enqueue(new Callback<AccessToken>() {
                                @Override
                                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                                    Log.w(TAG, "onResponse: " + response );
                                    if(response.isSuccessful()) {
                                        tokenManager.saveToken(response.body());
                                        startActivity(new Intent(LoginUserActivity.this, UserHomeActivity.class));
                                        SharedPreferences.Editor editor = loginPrefUser.edit();
                                        editor.putString("email", emailUser);
                                        editor.putString("role", role);
                                        editor.apply();
                                        finish();
                                    } else {
                                        if(response.code() == 422) {
                                            handleErrors(response.errorBody());
                                        }
                                        if(response.code() == 401) {
                                            ApiError apiError = Utils.converErrors(response.errorBody());
                                            Toast.makeText(LoginUserActivity.this, apiError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AccessToken> call, Throwable t) {
                                    Log.w(TAG, "onFailure: " + t.getMessage() );
                                    Toast.makeText(LoginUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if(role.equals("Pengurus")) {
                            api.login(emailUser, passwordUser).enqueue(new Callback<AccessToken>() {
                                @Override
                                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                                    Log.w(TAG, "onResponse: " + response );
                                    if(response.isSuccessful()) {
                                        tokenManager.saveToken(response.body());
                                        startActivity(new Intent(LoginUserActivity.this, MainActivity.class));
                                        SharedPreferences.Editor editor = loginPrefUser.edit();
                                        editor.putString("email", emailUser);
                                        editor.putString("role", role);
                                        editor.apply();
                                        finish();
                                    } else {
                                        if(response.code() == 422) {
                                            handleErrors(response.errorBody());
                                        }
                                        if(response.code() == 401) {
                                            ApiError apiError = Utils.converErrors(response.errorBody());
                                            Toast.makeText(LoginUserActivity.this, apiError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AccessToken> call, Throwable t) {
                                    Log.w(TAG, "onFailure: " + t.getMessage() );
                                    Toast.makeText(LoginUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginUserActivity.this);
                            builder.setTitle("Tidak terverifikasi!")
                                    .setIcon(R.drawable.warning)
                                    .setMessage("Maaf! Kami tidak bisa login dengan akun Anda yang anda masukkan pada kolom login dikarenakan akun Anda tidak terverifikasi. Silahkan mencoba lagi")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModelResponse> call, Throwable t) {
                        Toast.makeText(LoginUserActivity.this, "Maaf! Ada kesalahan dengan server kami: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUserActivity.this, RegisterActivity.class));
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Utils.converErrors(response);
        for(Map.Entry<String, List<String>> error: apiError.getErrors().entrySet()) {
            if(error.getKey().equals("username")) {
                email.setError(error.getValue().get(0));
            }
            if(error.getKey().equals("password")) {
                password.setError(error.getValue().get(0));
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