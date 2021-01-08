package klp18.praktikumprogmob.stt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRequestActivity extends AppCompatActivity {

    private EditText namaAcara, tanggalAcara, lokasiAcara, alamatAcara, keteranganAcara;
    private Button simpan, batal;
    private TextView namaPengguna;
    ApiService api;
    private SQLiteHelper helper;
    SharedPreferences editRequestPref;
    private String idRequest, nama_acara, tanggal_acara, lokasi_acara, alamat_acara, keterangan_acara, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
        namaAcara = (EditText) findViewById(R.id.NamaAcaraRequestEditPengguna);
        tanggalAcara = (EditText) findViewById(R.id.tanggalAcaraRequestEditPengguna);
        lokasiAcara = (EditText) findViewById(R.id.lokasiAcaraRequestEditPengguna);
        alamatAcara = (EditText) findViewById(R.id.alamatAcaraRequestEditPengguna);
        keteranganAcara = (EditText) findViewById(R.id.keteranganAcaraRequestEditPengguna);
        simpan = (Button) findViewById(R.id.btnSimpanRequestEditPengguna);
        batal = (Button) findViewById(R.id.btnBatalRequestEditPengguna);
        namaPengguna = (TextView) findViewById(R.id.namaPenggunaRequestEdit);
        helper = new SQLiteHelper(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            idRequest = bundle.getString("id");
            nama_acara = bundle.getString("nama");
            tanggal_acara = bundle.getString("tanggal");
            lokasi_acara = bundle.getString("lokasi");
            keterangan_acara = bundle.getString("keterangan");
            alamat_acara = bundle.getString("alamat");
            namaAcara.setText(nama_acara);
            tanggalAcara.setText(tanggal_acara);
            lokasiAcara.setText(lokasi_acara);
            keteranganAcara.setText(keterangan_acara);
            alamatAcara.setText(alamat_acara);
        }

        editRequestPref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        email = editRequestPref.getString("email", " ");
        api = RetrofitBuilder.createService(ApiService.class);
        api.user(email).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                namaPengguna.setText(response.body().getData().get(0).getName());
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_acara_request = namaAcara.getText().toString();
                String tanggal_acara_request = tanggalAcara.getText().toString();
                String lokasi_acara_request = lokasiAcara.getText().toString();
                String keterangan_acara_request = keteranganAcara.getText().toString();
                String alamat_acara_request = alamatAcara.getText().toString();
                String nama_pengguna_request = namaPengguna.getText().toString();
                api.updateRequest(idRequest, nama_acara_request, tanggal_acara_request, lokasi_acara_request, keterangan_acara_request, nama_pengguna_request, alamat_acara_request).enqueue(new Callback<ResponsModelRequest>() {
                    @Override
                    public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                        boolean isUpdate = helper.updateDataRequest(nama_acara, idRequest, nama_acara_request, tanggal_acara_request, lokasi_acara_request, keterangan_acara_request, nama_pengguna_request, alamat_acara_request);
                        if(isUpdate) {
                            Toast.makeText(EditRequestActivity.this, "Edit Request Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditRequestActivity.this, RequestAcaraUserActivity.class));
                            finish();
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModelRequest> call, Throwable t) {

                    }
                });

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditRequestActivity.this);
                builder.setTitle("Batal?")
                        .setMessage("Apakah Anda yakin ingin batal mengedit data?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(EditRequestActivity.this, RequestAcaraUserActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

    }
}