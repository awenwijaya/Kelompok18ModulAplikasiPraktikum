package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import klp18.praktikumprogmob.stt.adapter.RequestAdapter;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.Request;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAcaraUserActivity extends AppCompatActivity {

    FloatingActionButton requestTambah;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Request> items = new ArrayList<>();
    ArrayList<Request> listRequest = new ArrayList<>();
    ApiService api;
    TokenManager tokenManager;
    SQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acara_user);
        recyclerView = (RecyclerView) findViewById(R.id.listRequestAcaraPengguna);
        requestTambah = (FloatingActionButton) findViewById(R.id.fabRequestBaruNew);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        api = RetrofitBuilder.getClient().create(ApiService.class);
        helper = new SQLiteHelper(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        Call<ResponsModelRequest> getRequest = api.getRequest();

        requestTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestAcaraUserActivity.this, RequestBaruActivity.class));
                finish();
            }
        });

        getRequest.enqueue(new Callback<ResponsModelRequest>() {
            @Override
            public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                if(response.isSuccessful()) {
                    items = response.body().getResult();
                    adapter = new RequestAdapter(items, RequestAcaraUserActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                Log.d("Retrofit get", "onFailure: " + "Respons gagal");
                Toast.makeText(RequestAcaraUserActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                listRequest.clear();
                Cursor res = helper.getDataRequestAll();
                while(res.moveToNext()) {
                    String id = res.getString(0);
                    String nama_acara_request = res.getString(1);
                    String nama_pengguna = res.getString(2);
                    String tanggal = res.getString(3);
                    String lokasi = res.getString(4);
                    String alamat = res.getString(5);
                    String keterangan = res.getString(6);
                    listRequest.add(new Request(id, nama_acara_request, nama_pengguna, tanggal, lokasi, alamat, keterangan));
                }
                adapter = new RequestAdapter(listRequest, RequestAcaraUserActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}