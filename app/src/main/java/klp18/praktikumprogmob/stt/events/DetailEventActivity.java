package klp18.praktikumprogmob.stt.events;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEventActivity extends AppCompatActivity {

    TextView nama, tanggal, tempat, alamat, keterangan, imageURL;
    Button edit, hapus;
    SQLiteHelper helper;
    ImageView detailGambar;
    private String id, nama_acara, tanggal_acara, tempat_acara, alamat_acara, keterangan_acara;
    String IMAGE_URL = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/";
    ApiService api;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        nama = (TextView) findViewById(R.id.NamaAcaraDetailAdmin);
        tanggal = (TextView) findViewById(R.id.TanggalAcaraDetailAdmin);
        tempat = (TextView) findViewById(R.id.TempatAcaraDetailAdmin);
        alamat = (TextView) findViewById(R.id.AlamatAcaraDetailAdmin);
        detailGambar = (ImageView) findViewById(R.id.imageViewDetailKegiatanAdmin);
        imageURL = (TextView) findViewById(R.id.imageURLDetail);
        keterangan = (TextView) findViewById(R.id.KeteranganAcaraDetailAdmin);
        edit = (Button) findViewById(R.id.btnEditKegiatanDetailAdmin);
        hapus = (Button) findViewById(R.id.btnDeleteKegiatanAdmin);
        context = DetailEventActivity.this;
        helper = new SQLiteHelper(this);
        api = RetrofitBuilder.createService(ApiService.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            id = bundle.getString("id");
            nama_acara = bundle.getString("nama_acara");
            tanggal_acara = bundle.getString("tanggal_acara");
            tempat_acara = bundle.getString("tempat_acara");
            alamat_acara = bundle.getString("alamat");
            keterangan_acara = bundle.getString("keterangan");
            nama.setText(nama_acara);
        }

        api.lihatEvent(id).enqueue(new Callback<ResponsModel>() {
            @Override
            public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                tanggal.setText(response.body().getResult().get(0).getTanggal_acara());
                tempat.setText(response.body().getResult().get(0).getTempat_acara());
                alamat.setText(response.body().getResult().get(0).getAlamat());
                keterangan.setText(response.body().getResult().get(0).getKeterangan());
                imageURL.setText(response.body().getResult().get(0).getPhoto());
                String gambar_url = imageURL.getText().toString();
                Picasso.get().load(IMAGE_URL + gambar_url).into(detailGambar);
            }

            @Override
            public void onFailure(Call<ResponsModel> call, Throwable t) {
                Toast.makeText(DetailEventActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                tanggal.setText(tanggal_acara);
                tempat.setText(tempat_acara);
                alamat.setText(alamat_acara);
                keterangan.setText(keterangan_acara);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoEdit = new Intent(context, EditEventActivity.class);
                gotoEdit.putExtra("id", id);
                gotoEdit.putExtra("nama_acara", nama_acara);
                gotoEdit.putExtra("tanggal_acara", tanggal.getText().toString());
                gotoEdit.putExtra("tempat_acara", tempat.getText().toString());
                gotoEdit.putExtra("alamat", alamat.getText().toString());
                gotoEdit.putExtra("keterangan", keterangan.getText().toString());
                context.startActivity(gotoEdit);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Yakin hapus?")
                        .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                api.deleteEvent(id).enqueue(new Callback<ResponsModel>() {
                                    @Override
                                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                                        Log.d("Retrofit get", "berhasil");
                                        Integer isDelete = helper.deleteData(nama_acara);
                                        Toast.makeText(DetailEventActivity.this, "Data acara terhapus", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DetailEventActivity.this, ListEventActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                                        Toast.makeText(context, "Maaf! Terdapat kesalahan pada server kami " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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