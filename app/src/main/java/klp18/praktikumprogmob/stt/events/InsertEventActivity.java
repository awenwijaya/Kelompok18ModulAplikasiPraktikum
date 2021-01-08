package klp18.praktikumprogmob.stt.events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.network.ApiService;
import klp18.praktikumprogmob.stt.network.ResponsModel;
import klp18.praktikumprogmob.stt.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertEventActivity extends AppCompatActivity {

    private EditText nama, tanggal, tempat, alamat, keterangan;
    private Button simpan, batal, galeri;
    private ImageView imageKegiatan;
    private SQLiteHelper helper;
    public static final String UPLOAD_URL_GAMBAR = "http://192.168.18.10/newfolder2/laravel-progmob-api-test/crud-php/upload-image.php";
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_RESULT = 1;
    private Uri filepath;
    ApiService apiService, api;
    DatePickerDialog.OnDateSetListener setListener;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_event);
        nama = findViewById(R.id.NamaAcaraTambah);
        tanggal = findViewById(R.id.TanggalAcaraTambah);
        tempat = findViewById(R.id.tempatAcaraTambah);
        alamat = findViewById(R.id.alamatAcaraTambah);
        keterangan = findViewById(R.id.keteranganTambah);
        simpan = (Button) findViewById(R.id.btnSimpanTambah);
        batal = (Button) findViewById(R.id.btnBatalTambah);
        galeri = (Button) findViewById(R.id.btnGaleriTambah);
        imageKegiatan = (ImageView) findViewById(R.id.imageKegiatanTambah);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InsertEventActivity.this,
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
        storagePermission();
        apiService = RetrofitBuilder.getClient().create(ApiService.class);
        api = RetrofitBuilder.createService(ApiService.class);
        helper = new SQLiteHelper(this);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_acara = nama.getText().toString();
                String tanggal_acara = tanggal.getText().toString();
                String tempat_acara = tempat.getText().toString();
                String alamat_acara = alamat.getText().toString();
                String keterangan_acara = keterangan.getText().toString();
                if (TextUtils.isEmpty(nama_acara)) {
                    nama.setError("Data tidak boleh kosong!");
                    nama.requestFocus();
                } else if (TextUtils.isEmpty(tanggal_acara)) {
                    tanggal.setError("Data tidak boleh kosong");
                    tanggal.requestFocus();
                } else if (TextUtils.isEmpty(tempat_acara)) {
                    tempat.setError("Data tidak boleh kosong!");
                    tempat.requestFocus();
                } else if (TextUtils.isEmpty(alamat_acara)) {
                    alamat.setError("Data tidak boleh kosong!");
                    alamat.requestFocus();
                } else if (TextUtils.isEmpty(keterangan_acara)) {
                    keterangan.setError("Data tidak boleh kosong!");
                    keterangan.requestFocus();
                } else {
                    Call<ResponsModel> postEvent = apiService.postEvent(nama_acara, tanggal_acara, tempat_acara, alamat_acara, keterangan_acara);
                    postEvent.enqueue(new Callback<ResponsModel>() {
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
                                String path = getPath(filepath);
                                try {
                                    String uploadId = UUID.randomUUID().toString();
                                    new MultipartUploadRequest(InsertEventActivity.this, uploadId, UPLOAD_URL_GAMBAR)
                                            .addFileToUpload(path, "image")
                                            .addParameter("nama_acara", nama_acara)
                                            .setMaxRetries(3)
                                            .startUpload();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                boolean isInsert = helper.tambahDataEvent(nama_acara, tanggal_acara, tempat_acara, alamat_acara, keterangan_acara, imageViewToByte(imageKegiatan));
                                if(isInsert) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InsertEventActivity.this);
                                    builder.setTitle("Data berhasil ditambahkan!")
                                            .setMessage("Apakah Anda ingin menambahkan data lagi?")
                                            .setIcon(R.drawable.ask)
                                            .setCancelable(false)
                                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    nama.setText(null);
                                                    tanggal.setText(null);
                                                    tempat.setText(null);
                                                    alamat.setText(null);
                                                    keterangan.setText(null);
                                                    imageKegiatan.setImageDrawable(null);
                                                }
                                            })
                                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(InsertEventActivity.this, ListEventActivity.class));
                                                    nama.setText(null);
                                                    tanggal.setText(null);
                                                    tempat.setText(null);
                                                    alamat.setText(null);
                                                    keterangan.setText(null);
                                                    finish();
                                                }
                                            }).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsModel> call, Throwable t) {
                            Toast.makeText(InsertEventActivity.this, "Gagal menghubungi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertEventActivity.this);
                builder.setTitle("Batal?")
                        .setMessage("Apakah anda yakin ingin batal mengisi data kegiatan?")
                        .setCancelable(true)
                        .setIcon(R.drawable.ask)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent home = new Intent(InsertEventActivity.this, ListEventActivity.class);
                                startActivity(home);
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
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
                imageKegiatan.setImageBitmap(bitmap);
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