package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import klp18.praktikumprogmob.stt.adapter.ListAdapter;
import klp18.praktikumprogmob.stt.adapter.PenggunaAdapter;
import klp18.praktikumprogmob.stt.database.Event;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.events.ListEventActivity;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity {

    FloatingActionButton requestBaru, logoutUser, editProfileUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<Event> items = new ArrayList<>();
    private TextView namaPengguna;
    String emailPengguna;
    ApiService service;
    ApiService api;
    TokenManager tokenManager;
    SharedPreferences homePref;
    SQLiteHelper helper;
    ArrayList<Event> listEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        recyclerView = (RecyclerView) findViewById(R.id.listViewKegiatanUser);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        requestBaru = (FloatingActionButton) findViewById(R.id.fabRequest);
        logoutUser = (FloatingActionButton) findViewById(R.id.fabLogoutUser);
        editProfileUser = (FloatingActionButton) findViewById(R.id.fabEditProfile);
        namaPengguna = (TextView) findViewById(R.id.namaPenggunaHome);
        service  = RetrofitBuilder.createService(ApiService.class);
        api = RetrofitBuilder.getClientPHP().create(ApiService.class);
        helper = new SQLiteHelper(this);
        homePref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        emailPengguna = homePref.getString("email", " ");
        service.user(emailPengguna).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                namaPengguna.setText(response.body().getData().get(0).getName());
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service.getEvent().enqueue(new Callback<ResponsModel>() {
            @Override
            public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                items = response.body().getResult();
                adapter = new PenggunaAdapter(items, UserHomeActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponsModel> call, Throwable t) {
                Log.d("Retrofit get", "onFailure: " + "Respons gagal");
                Toast.makeText(UserHomeActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                listEvent.clear();
                Cursor res = helper.getDataAll();
                while(res.moveToNext()) {
                    String id = res.getString(0);
                    String nama_acara = res.getString(1);
                    String tanggal_acara = res.getString(2);
                    String tempat_acara = res.getString(3);
                    String alamat = res.getString(4);
                    String keterangan = res.getString(5);
                    listEvent.add(new Event(id, nama_acara, tanggal_acara, tempat_acara, alamat, keterangan));
                }
                adapter = new ListAdapter(listEvent, UserHomeActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        requestBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, RequestAcaraUserActivity.class));
            }
        });

        editProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, EditProfileActivity.class));
            }
        });

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
                builder.setTitle("Logout?")
                        .setIcon(R.drawable.warning)
                        .setMessage("Apakah Anda yakin ingin logout?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(UserHomeActivity.this, LoginUserActivity.class));
                                tokenManager.deleteToken();
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });


    }
}