package klp18.praktikumprogmob.stt.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nama_acara_request")
    @Expose
    private String nama_acara_request;

    @SerializedName("nama_pengguna")
    @Expose
    private String nama_pengguna;

    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    @SerializedName("lokasi")
    @Expose
    private String lokasi;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("photo")
    @Expose
    private String photo;

    public Request(String id, String nama_acara_request, String nama_pengguna, String tanggal, String lokasi, String alamat, String keterangan) {
        this.id = id;
        this.nama_acara_request = nama_acara_request;
        this.nama_pengguna = nama_pengguna;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.alamat = alamat;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_acara_request() {
        return nama_acara_request;
    }

    public void setNama_acara_request(String nama_acara_request) {
        this.nama_acara_request = nama_acara_request;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
