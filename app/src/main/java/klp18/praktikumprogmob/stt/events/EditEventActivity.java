package klp18.praktikumprogmob.stt.events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEventActivity extends AppCompatActivity {

    private EditText nama, tanggal, tempat, alamat, keterangan;
    private Button simpan, batal;
    SQLiteHelper helper;
    private String id, namaAcara, tanggalAcara, tempatAcara, alamatAcara, keteranganAcara;
    ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        nama = (EditText) findViewById(R.id.NamaAcaraEdit);
        tanggal = (EditText) findViewById(R.id.TanggalAcaraEdit);
        tempat = (EditText) findViewById(R.id.tempatAcaraEdit);
        alamat = (EditText) findViewById(R.id.alamatAcaraEdit);
        keterangan = (EditText) findViewById(R.id.keteranganEdit);
        simpan = (Button) findViewById(R.id.btnSimpanEdit);
        batal = (Button) findViewById(R.id.btnBatalEdit);
        helper = new SQLiteHelper(this);
        api = RetrofitBuilder.createService(ApiService.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            id = bundle.getString("id");
            namaAcara = bundle.getString("nama_acara");
            tanggalAcara = bundle.getString("tanggal_acara");
            tempatAcara = bundle.getString("tempat_acara");
            alamatAcara = bundle.getString("alamat");
            keteranganAcara = bundle.getString("keterangan");

            nama.setText(namaAcara);
            tanggal.setText(tanggalAcara);
            tempat.setText(tempatAcara);
            alamat.setText(alamatAcara);
            keterangan.setText(keteranganAcara);
        }

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_acara_stt = nama.getText().toString();
                String tanggal_acara_stt = tanggal.getText().toString();
                String tempat_acara_stt = tempat.getText().toString();
                String alamat_acara_stt = alamat.getText().toString();
                String keterangan_acara_stt = keterangan.getText().toString();

                if(TextUtils.isEmpty(nama_acara_stt)) {
                    nama.setError("Data tidak boleh kosong!");
                    nama.requestFocus();
                } else if(TextUtils.isEmpty(tanggal_acara_stt)) {
                    tanggal.setError("Data tidak boleh kosong!");
                    tanggal.requestFocus();
                } else if(TextUtils.isEmpty(tempat_acara_stt)) {
                    tempat.setError("Data tidak boleh kosong!");
                    tempat.requestFocus();
                } else if(TextUtils.isEmpty(alamat_acara_stt)) {
                    alamat.setError("Data tidak boleh kosong!");
                    alamat.requestFocus();
                } else if(TextUtils.isEmpty(keterangan_acara_stt)) {
                    keterangan.setError("Data tidak boleh kosong!");
                    keterangan.requestFocus();
                } else {
                    api.updateEvent(id, nama_acara_stt, tanggal_acara_stt, tempat_acara_stt, alamat_acara_stt, keterangan_acara_stt).enqueue(new Callback<ResponsModel>() {
                        @Override
                        public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                            boolean isUpdate = helper.updateData(namaAcara, nama_acara_stt, tanggal_acara_stt, tempat_acara_stt, alamat_acara_stt, keterangan_acara_stt);
                            if(isUpdate) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);
                                builder.setTitle("Berhasil diubah!")
                                        .setMessage("Apakah Anda ingin melakukan perubahan lagi?")
                                        .setCancelable(false)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(EditEventActivity.this, ListEventActivity.class));
                                                finish();
                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsModel> call, Throwable t) {
                            Toast.makeText(EditEventActivity.this, "Terjadi kesalahan pada server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kembali = new Intent(EditEventActivity.this, ListEventActivity.class);
                startActivity(kembali);
            }
        });
    }
}