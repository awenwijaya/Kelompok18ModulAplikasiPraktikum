package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAcaraPenggunaActivity extends AppCompatActivity {

    TextView nama, tanggal, tempat, alamat, keterangan, imageURL;
    private String id, nama_acara, tanggal_acara, tempat_acara, alamat_acara, keterangan_acara;
    ApiService api;
    ImageView gambarDetailKegiatanPengguna;
    String IMAGE_URL = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acara_pengguna);
        nama = (TextView) findViewById(R.id.NamaAcaraDetailPengguna);
        tanggal = (TextView) findViewById(R.id.TanggalAcaraDetailPengguna);
        tempat = (TextView) findViewById(R.id.TempatAcaraDetailPengguna);
        alamat = (TextView) findViewById(R.id.AlamatAcaraDetailPengguna);
        keterangan = (TextView) findViewById(R.id.KeteranganAcaraDetailPengguna);
        imageURL = (TextView) findViewById(R.id.imageURLPengguna);
        gambarDetailKegiatanPengguna = (ImageView) findViewById(R.id.imageViewDetailKegiatanPengguna);
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
                String gambar = imageURL.getText().toString();
                Picasso.get().load(IMAGE_URL + gambar).into(gambarDetailKegiatanPengguna);
            }

            @Override
            public void onFailure(Call<ResponsModel> call, Throwable t) {
                tanggal.setText(tanggal_acara);
                tempat.setText(tempat_acara);
                alamat.setText(alamat_acara);
                keterangan.setText(keterangan_acara);
            }
        });
    }
}