package klp18.praktikumprogmob.stt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.events.InsertEventActivity;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModelRequest;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import klp18.praktikumprogmob.stt.network.UserModelResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestBaruActivity extends AppCompatActivity {

    EditText nama, lokasi, alamat, keterangan, tanggal;
    Button simpan, batal, galeri;
    SharedPreferences requestPref;
    ApiService service;
    ImageView gambarRequestAcaraPengguna;
    private SQLiteHelper helper;
    private String namaRequest, lokasiRequest, alamatRequest, keteranganRequest, tanggalRequest, namaPengguna, emailPengguna;
    TextView namaPenggunaReq, idPenggunaReq;
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_RESULT = 1;
    private Uri filepath;
    DatePickerDialog.OnDateSetListener setListener;
    Bitmap bitmap;
    public static final String UPLOAD_URL_GAMBAR = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/upload-image-request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_baru);
        nama = (EditText) findViewById(R.id.NamaAcaraRequestEditPengguna);
        lokasi = (EditText) findViewById(R.id.lokasiAcaraRequestEditPengguna);
        alamat = (EditText) findViewById(R.id.alamatAcaraRequestEditPengguna);
        keterangan = (EditText) findViewById(R.id.keteranganAcaraRequestEditPengguna);
        tanggal = (EditText) findViewById(R.id.tanggalAcaraRequestEditPengguna);
        simpan = (Button) findViewById(R.id.btnSimpanRequestEditPengguna);
        batal = (Button) findViewById(R.id.btnBatalRequestEditPengguna);
        galeri = (Button) findViewById(R.id.btnBukaGaleriRequestKegiatanPenggunaTambah);
        gambarRequestAcaraPengguna = (ImageView) findViewById(R.id.imageViewRequestKegiatanPenggunaNew);
        namaPenggunaReq = (TextView) findViewById(R.id.namaPenggunaRequestEdit);
        idPenggunaReq = (TextView) findViewById(R.id.idPenggunaRequestBaruNew);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RequestBaruActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tanggal.setText(date);
            }
        };
        helper = new SQLiteHelper(this);
        requestPref = getSharedPreferences("loginPref", Context.MODE_PRIVATE);
        emailPengguna = requestPref.getString("email", " ");
        service = RetrofitBuilder.createService(ApiService.class);
        service.user(emailPengguna).enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                namaPenggunaReq.setText(response.body().getData().get(0).getName());
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {

            }
        });

        galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaPengguna = namaPenggunaReq.getText().toString();
                namaRequest = nama.getText().toString();
                tanggalRequest = tanggal.getText().toString();
                lokasiRequest = lokasi.getText().toString();
                keteranganRequest = keterangan.getText().toString();
                alamatRequest = alamat.getText().toString();
                if(TextUtils.isEmpty(namaRequest)) {
                    nama.setError("Data tidak boleh kosong!");
                    nama.requestFocus();
                } else if(TextUtils.isEmpty(tanggalRequest)) {
                    tanggal.setError("Data tidak boleh kosong!");
                    tanggal.requestFocus();
                } else if(TextUtils.isEmpty(lokasiRequest)) {
                    lokasi.setError("Data tidak boleh kosong!");
                    lokasi.requestFocus();
                } else if(TextUtils.isEmpty(keteranganRequest)) {
                    keterangan.setError("Data tidak boleh kosong!");
                    keterangan.requestFocus();
                } else if(TextUtils.isEmpty(alamatRequest)) {
                    alamat.setError("Data tidak boleh kosong!");
                    alamat.requestFocus();
                } else {
                    service.postRequest(namaRequest, tanggalRequest, lokasiRequest, keteranganRequest, namaPengguna, alamatRequest).enqueue(new Callback<ResponsModelRequest>() {
                        @Override
                        public void onResponse(Call<ResponsModelRequest> call, Response<ResponsModelRequest> response) {
                            if(response.isSuccessful()) {
                                String path = getPath(filepath);
                                try {
                                    String uploadId = UUID.randomUUID().toString();
                                    new MultipartUploadRequest(RequestBaruActivity.this, uploadId, UPLOAD_URL_GAMBAR)
                                            .addFileToUpload(path, "image")
                                            .addParameter("nama_acara_request", namaRequest)
                                            .setMaxRetries(3)
                                            .startUpload();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                boolean isInsert = helper.tambahDataRequest(namaRequest, tanggalRequest, lokasiRequest, keteranganRequest, namaPengguna, alamatRequest);
                                if(isInsert) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestBaruActivity.this);
                                    builder.setTitle("Berhasil ditambahkan!")
                                            .setMessage("Apakah Anda ingin menambahkan data lagi?")
                                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    nama.setText("");
                                                    lokasi.setText("");
                                                    alamat.setText("");
                                                    keterangan.setText("");
                                                    tanggal.setText("");
                                                    gambarRequestAcaraPengguna.setImageDrawable(null);
                                                    dialog.cancel();
                                                }
                                            })
                                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    nama.setText("");
                                                    lokasi.setText("");
                                                    alamat.setText("");
                                                    keterangan.setText("");
                                                    tanggal.setText("");
                                                    startActivity(new Intent(RequestBaruActivity.this, RequestAcaraUserActivity.class));
                                                    finish();
                                                }
                                            }).show();
                                } else {
                                    Toast.makeText(RequestBaruActivity.this, "data gagal disimpan ke dalam database", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RequestBaruActivity.this, "Terdapat kesalahan dalam server kami! " + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsModelRequest> call, Throwable t) {
                            Toast.makeText(RequestBaruActivity.this, "Gagal menghubungi ke server " + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestBaruActivity.this);
                builder.setTitle("Batal?")
                        .setMessage("Apakah anda yakin ingin batal mengisi data ini?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(RequestBaruActivity.this, RequestAcaraUserActivity.class));
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

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void storagePermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ijin akses diberikan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ijin akses ditolak", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_RESULT && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                gambarRequestAcaraPengguna.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID+"=?",new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

}