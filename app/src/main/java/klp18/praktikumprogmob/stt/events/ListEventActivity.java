package klp18.praktikumprogmob.stt.events;

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

import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.adapter.ListAdapter;
import klp18.praktikumprogmob.stt.database.Event;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEventActivity extends AppCompatActivity {

    FloatingActionButton tambah;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<Event> items = new ArrayList<>();
    ApiService apiService, service;
    ArrayList<Event> listEvent = new ArrayList<>();
    SQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        recycler = (RecyclerView) findViewById(R.id.listViewEvent);
        service = RetrofitBuilder.getClientPHP().create(ApiService.class);
        apiService = RetrofitBuilder.getClient().create(ApiService.class);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        tambah = (FloatingActionButton) findViewById(R.id.FABTambah);
        helper = new SQLiteHelper(this);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(ListEventActivity.this, InsertEventActivity.class);
                startActivity(tambah);
                finish();
            }
        });

        apiService.getEvent().enqueue(new Callback<ResponsModel>() {
            @Override
            public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                items = response.body().getResult();
                adapter = new ListAdapter(items, ListEventActivity.this);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponsModel> call, Throwable t) {
                Toast.makeText(ListEventActivity.this, t.getMessage().toString(),Toast.LENGTH_SHORT).show();
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
                adapter = new ListAdapter(listEvent, ListEventActivity.this);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}