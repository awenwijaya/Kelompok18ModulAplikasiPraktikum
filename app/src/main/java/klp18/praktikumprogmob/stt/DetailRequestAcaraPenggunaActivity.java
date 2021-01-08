package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestAcaraPenggunaActivity extends AppCompatActivity {

    private TextView nama, tanggal, lokasi, alamat, keterangan, nama_user, gambar, text_nama_pengguna;
    private Button edit, hapus;
    ImageView imageKegiatanRequest;
    SQLiteHelper helper;
    private String idRequest, nama_acara, tanggal_acara, lokasi_acara, alamat_acara, keterangan_acara, nama_pengguna, namaPenggunaOld, emailOld;
    ApiService api;
    SharedPreferences requestPref;
    String IMAGE_URL = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_acara_pengguna);
        nama = (TextView) findViewById(R.id.NamaAcaraRequestDetailUser);
        tanggal = (TextView) findViewById(R.id.TanggalAcaraRequestDetailUser);
        lokasi = (TextView) findViewById(R.id.LokasiAcaraRequestDetailUser);
        alamat = (TextView) findViewById(R.id.AlamatAcaraRequestDetailUser);
        keterangan = (TextView) findViewById(R.id.KeteranganAcaraRequestDetailUser);
        nama_user = (TextView) findViewById(R.id.NamaPenggunaRequestDetailUser);
        gambar = (TextView) findViewById(R.id.imageURLDetailRequestUser);
        edit = (Button) findViewById(R.id.btnEditRequestAcaraUserDetail);
        hapus = (Button) findViewById(R.id.btnDeleteRequestAcaraUserDetail);
        requestPref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        text_nama_pengguna = (TextView) findViewById(R.id.emailPengguna);
        imageKegiatanRequest = (ImageView) findViewById(R.id.imageViewRequestAcaraPenggunaDetail);
        helper = new SQLiteHelper(this);
        context = DetailRequestAcaraPenggunaActivity.this;
        api = RetrofitBuilder.createService(ApiService.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            idRequest = bundle.getString("id");
            nama_acara = bundle.getString("nama");
            tanggal_acara = bundle.getString("tanggal");
            lokasi_acara = bundle.getString("lokasi");
            keterangan_acara = bundle.getString("keterangan");
            alamat_acara = bundle.getString("alamat");
            nama_pengguna = bundle.getString("namaUser");
            nama.setText(nama_acara);
        }
        emailOld = requestPref.getString("email", " ");
        api.user(emailOld).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                text_nama_pengguna.setText(response.body().getData().get(0).getName());
                namaPenggunaOld = text_nama_pengguna.getText().toString();
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });

        api.lihatRequest(idRequest).enqueue(new Callback<ResponsModelRequest>() {
            @Override
            public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                tanggal.setText(response.body().getResult().get(0).getTanggal());
                lokasi.setText(response.body().getResult().get(0).getLokasi());
                alamat.setText(response.body().getResult().get(0).getAlamat());
                keterangan.setText(response.body().getResult().get(0).getKeterangan());
                nama_user.setText(response.body().getResult().get(0).getNama_pengguna());
                gambar.setText(response.body().getResult().get(0).getPhoto());
                String urlGambar = gambar.getText().toString();
                Picasso.get().load(IMAGE_URL + urlGambar).into(imageKegiatanRequest);
            }

            @Override
            public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                tanggal.setText(tanggal_acara);
                lokasi.setText(lokasi_acara);
                keterangan.setText(keterangan_acara);
                alamat.setText(alamat_acara);
                nama_user.setText(nama_pengguna);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaPenggunaOld.equals(nama_pengguna)) {
                    Intent editRequest = new Intent(context, EditRequestActivity.class);
                    editRequest.putExtra("id", idRequest);
                    editRequest.putExtra("nama", nama_acara);
                    editRequest.putExtra("tanggal", tanggal.getText().toString());
                    editRequest.putExtra("lokasi", lokasi.getText().toString());
                    editRequest.putExtra("keterangan", keterangan.getText().toString());
                    editRequest.putExtra("namaUser", nama_user.getText().toString());
                    editRequest.putExtra("alamat", alamat.getText().toString());
                    context.startActivity(editRequest);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestAcaraPenggunaActivity.this);
                    builder.setTitle("Tidak terverifikasi!")
                            .setMessage("Maaf! Anda tidak bisa melakukan perubahan data")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaPenggunaOld.equals(nama_pengguna)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestAcaraPenggunaActivity.this);
                    builder.setTitle("Hapus?")
                            .setIcon(R.drawable.ask)
                            .setCancelable(false)
                            .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    api.deleteRequest(idRequest).enqueue(new Callback<ResponsModelRequest>() {
                                        @Override
                                        public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                                            Integer isDelete = helper.deleteDataRequest(nama_acara);
                                            Toast.makeText(DetailRequestAcaraPenggunaActivity.this, "Berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(DetailRequestAcaraPenggunaActivity.this, RequestAcaraUserActivity.class));
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                                            Toast.makeText(DetailRequestAcaraPenggunaActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestAcaraPenggunaActivity.this);
                    builder.setTitle("Tidak terverifikasi!")
                            .setMessage("Maaf! Anda tidak bisa melakukan penghapusan data")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }
        });
    }
}