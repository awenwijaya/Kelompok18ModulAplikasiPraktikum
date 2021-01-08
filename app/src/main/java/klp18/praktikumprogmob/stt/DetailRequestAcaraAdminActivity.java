package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestAcaraAdminActivity extends AppCompatActivity {

    private TextView nama, tanggal, lokasi, alamat, keterangan, nama_user, gambar;
    private Button approve, tolak;
    ImageView imageKegiatanRequest;
    SQLiteHelper helper;
    private String id, nama_acara_request, tanggal_acara, lokasi_acara, alamat_acara, keterangan_acara, nama_pengguna;
    ApiService api;
    String IMAGE_URL = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_acara_admin);
        nama = (TextView) findViewById(R.id.NamaAcaraRequestDetailAdmin);
        tanggal = (TextView) findViewById(R.id.TanggalAcaraRequestDetailAdmin);
        lokasi = (TextView) findViewById(R.id.LokasiAcaraRequestDetailAdmin2);
        alamat = (TextView) findViewById(R.id.AlamatAcaraRequestDetailAdmin);
        gambar = (TextView) findViewById(R.id.imageURLDetailRequestAdmin);
        imageKegiatanRequest = (ImageView) findViewById(R.id.imageViewKegiatanRequestAdmin);
        keterangan = (TextView) findViewById(R.id.KeteranganAcaraRequestDetailAdmin);
        approve = (Button) findViewById(R.id.btnApproveDetailAcaraAdmin);
        tolak = (Button) findViewById(R.id.btnTolakDetailAcaraAdmin2);
        context = DetailRequestAcaraAdminActivity.this;
        nama_user = (TextView) findViewById(R.id.NamaPenggunaRequestDetailAdmin);
        helper = new SQLiteHelper(this);
        api = RetrofitBuilder.createService(ApiService.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            id = bundle.getString("id");
            nama_acara_request = bundle.getString("nama");
            tanggal_acara = bundle.getString("tanggal");
            lokasi_acara = bundle.getString("lokasi");
            alamat_acara = bundle.getString("alamat");
            keterangan_acara = bundle.getString("keterangan");
            nama_pengguna = bundle.getString("nama_pengguna");
            nama.setText(nama_acara_request);
        }

        api.lihatRequest(id).enqueue(new Callback<ResponsModelRequest>() {
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

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestAcaraAdminActivity.this);
                builder.setTitle("Konfirmasi approve")
                        .setCancelable(false)
                        .setMessage("Apakah Anda yakin ingin menyetujui acara ini?")
                        .setIcon(R.drawable.ask)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                api.approve(nama.getText().toString(),
                                        tanggal.getText().toString(),
                                        lokasi.getText().toString(),
                                        alamat.getText().toString(),
                                        keterangan.getText().toString(),
                                        gambar.getText().toString()).enqueue(new Callback<ResponsModel>() {
                                    @Override
                                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                                        Log.d("Retrofit get", response.body().toString());
                                        if(response.isSuccessful()) {
                                            api.notificationSTT().enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });
                                            boolean isInsert = helper.tambahDataEvent(nama.getText().toString(),
                                                    tanggal.getText().toString(),
                                                    lokasi.getText().toString(),
                                                    alamat.getText().toString(),
                                                    keterangan.getText().toString(),
                                                    imageViewToByte(imageKegiatanRequest));
                                            if(isInsert) {
                                                api.deleteRequest(id).enqueue(new Callback<ResponsModelRequest>() {
                                                    @Override
                                                    public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                                                        Log.d("Retrofit get", response.body().toString());
                                                        helper.deleteDataRequest(nama.getText().toString());
                                                        Toast.makeText(DetailRequestAcaraAdminActivity.this, "Acara berhasil di setujui!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(DetailRequestAcaraAdminActivity.this, RequestAcaraAdminActivity.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                                                        Toast.makeText(DetailRequestAcaraAdminActivity.this, "Maaf! Terjadi kesalahan pada server kami " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                                        Toast.makeText(DetailRequestAcaraAdminActivity.this, "Maaf! Ada kesalahan saat menghubungi server kami : " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailRequestAcaraAdminActivity.this);
                builder.setTitle("Tolak?")
                        .setCancelable(false)
                        .setMessage("Apakah Anda yakin ingin menolak permintaan acara ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                api.deleteRequest(id).enqueue(new Callback<ResponsModelRequest>() {
                                    @Override
                                    public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                                        Log.d("Retrofit get", "Berhasil!");
                                        Integer isDelete = helper.deleteDataRequest(nama_acara_request);
                                        startActivity(new Intent(DetailRequestAcaraAdminActivity.this, RequestAcaraAdminActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                                        Log.d("Retrofit get", "Maaf! Terjadi kesalahan pada server kami " + t.getMessage());
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

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}