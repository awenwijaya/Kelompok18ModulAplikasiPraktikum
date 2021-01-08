package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import klp18.praktikumprogmob.stt.adapter.RequestAdapter;
import klp18.praktikumprogmob.stt.adapter.RequestAdminAdapter;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.Request;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAcaraAdminActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_request_acara_admin);
        recyclerView = (RecyclerView) findViewById(R.id.listRequestAcaraAdmin);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        api = RetrofitBuilder.getClient().create(ApiService.class);
        helper = new SQLiteHelper(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        Call<ResponsModelRequest> getRequest = api.getRequest();
        getRequest.enqueue(new Callback<ResponsModelRequest>() {
            @Override
            public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                if(response.isSuccessful()) {
                    items = response.body().getResult();
                    adapter = new RequestAdminAdapter(items, RequestAcaraAdminActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                Log.d("Retrofit get", "onFailure: " + "Respons gagal");
                Toast.makeText(RequestAcaraAdminActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                adapter = new RequestAdminAdapter(listRequest, RequestAcaraAdminActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}