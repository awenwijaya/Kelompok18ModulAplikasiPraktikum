package klp18.praktikumprogmob.stt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_STT = "stt";

    private static final String TABEL_EVENT = "event";
    private static final String KOLOM_ID = "id";
    private static final String KOLOM_NAMA_ACARA = "nama_acara";
    private static final String KOLOM_TANGGAL_ACARA = "tanggal_acara";
    private static final String KOLOM_TEMPAT_ACARA = "tempat_acara";
    private static final String KOLOM_ALAMAT = "alamat";
    private static final String KOLOM_KETERANGAN = "keterangan";
    private static final String KOLOM_GAMBAR = "gambar";

    private static final String TABEL_REQUEST_USER = "request_user";
    private static final String KOLOM_ID_REQUEST = "id";
    private static final String KOLOM_NAMA_ACARA_REQUEST = "nama_acara_request";
    private static final String KOLOM_TANGGAL_REQUEST = "tanggal";
    private static final String KOLOM_LOKASI_REQUEST = "lokasi";
    private static final String KOLOM_KETERANGAN_REQUEST = "keterangan";
    private static final String KOLOM_NAMA_PENGGUNA = "nama_penguna";
    private static final String KOLOM_ALAMAT_REQUEST = "alamat";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_STT, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABEL_EVENT + " (" +
                KOLOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KOLOM_NAMA_ACARA + " TEXT, " +
                KOLOM_TANGGAL_ACARA + " TEXT, " +
                KOLOM_TEMPAT_ACARA + " TEXT, " +
                KOLOM_ALAMAT + " TEXT, " +
                KOLOM_KETERANGAN + " TEXT," +
                KOLOM_GAMBAR + " BLOB);");

        db.execSQL("CREATE TABLE " + TABEL_REQUEST_USER + " (" +
                KOLOM_ID_REQUEST + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KOLOM_NAMA_ACARA_REQUEST + " TEXT, " +
                KOLOM_TANGGAL_REQUEST + " TEXT, " +
                KOLOM_LOKASI_REQUEST + " TEXT, " +
                KOLOM_KETERANGAN_REQUEST + " TEXT, " +
                KOLOM_NAMA_PENGGUNA + " TEXT, " +
                KOLOM_ALAMAT_REQUEST + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_REQUEST_USER);
    }

    public boolean tambahDataEvent(String nama_acara, String tanggal_acara, String tempat_acara, String alamat, String keterangan, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_NAMA_ACARA, nama_acara);
        values.put(KOLOM_TANGGAL_ACARA, tanggal_acara);
        values.put(KOLOM_TEMPAT_ACARA, tempat_acara);
        values.put(KOLOM_ALAMAT, alamat);
        values.put(KOLOM_KETERANGAN, keterangan);
        values.put(KOLOM_GAMBAR, image);
        long result = db.insert(TABEL_EVENT, null, values);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getDataAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABEL_EVENT, null);
    }

    public Cursor showEvent(String nama_acara) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABEL_EVENT + " WHERE " + KOLOM_NAMA_ACARA + " =? ", new String[]{nama_acara});
    }

    public boolean updateData(String nama_acara_old, String nama_acara, String tanggal_acara, String tempat_acara, String alamat, String keterangan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_NAMA_ACARA, nama_acara);
        values.put(KOLOM_TANGGAL_ACARA, tanggal_acara);
        values.put(KOLOM_TEMPAT_ACARA, tempat_acara);
        values.put(KOLOM_ALAMAT, alamat);
        values.put(KOLOM_KETERANGAN, keterangan);
        db.update(TABEL_EVENT, values, KOLOM_NAMA_ACARA + " =? ", new String[]{nama_acara_old});
        return true;
    }

    public Integer deleteData(String nama_acara_old) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABEL_EVENT, KOLOM_NAMA_ACARA + " =? ", new String[]{nama_acara_old});
    }

    public Cursor getDataRequestAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABEL_REQUEST_USER, null);
    }

    public boolean tambahDataRequest(String nama_acara_request, String tanggal_acara_request, String lokasi_acara_request, String keterangan_acara_request, String nama_pengguna, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_NAMA_ACARA_REQUEST, nama_acara_request);
        values.put(KOLOM_TANGGAL_REQUEST, tanggal_acara_request);
        values.put(KOLOM_LOKASI_REQUEST, lokasi_acara_request);
        values.put(KOLOM_KETERANGAN_REQUEST, keterangan_acara_request);
        values.put(KOLOM_NAMA_PENGGUNA, nama_pengguna);
        values.put(KOLOM_ALAMAT, alamat);
        long result = db.insert(TABEL_REQUEST_USER, null, values);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateDataRequest(String nama_acara_old, String id, String nama_acara_request, String tanggal_acara_request, String lokasi_acara_request, String keterangan_acara_request, String nama_pengguna, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KOLOM_ID_REQUEST, id);
        values.put(KOLOM_NAMA_ACARA_REQUEST, nama_acara_request);
        values.put(KOLOM_TANGGAL_REQUEST, tanggal_acara_request);
        values.put(KOLOM_LOKASI_REQUEST, lokasi_acara_request);
        values.put(KOLOM_KETERANGAN_REQUEST, keterangan_acara_request);
        values.put(KOLOM_NAMA_PENGGUNA, nama_pengguna);
        values.put(KOLOM_ALAMAT, alamat);
        db.update(TABEL_REQUEST_USER, values, KOLOM_NAMA_ACARA_REQUEST + " =? ", new String[]{nama_acara_old});
        return true;
    }

    public Integer deleteDataRequest(String nama_acara_old) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABEL_REQUEST_USER, KOLOM_NAMA_ACARA_REQUEST + " =? ", new String[]{nama_acara_old});
    }

}
