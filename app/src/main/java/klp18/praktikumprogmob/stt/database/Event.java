package klp18.praktikumprogmob.stt.database;

public class Event {

    private String id;
    private String nama_acara;
    private String tanggal_acara;
    private String tempat_acara;
    private String alamat;
    private String keterangan;
    private String photo;

    public Event(String id, String nama_acara, String tanggal_acara, String tempat_acara, String alamat, String keterangan) {
        this.id = id;
        this.nama_acara = nama_acara;
        this.tanggal_acara = tanggal_acara;
        this.tempat_acara = tempat_acara;
        this.alamat = alamat;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_acara() {
        return nama_acara;
    }

    public void setNama_acara(String nama_acara) {
        this.nama_acara = nama_acara;
    }

    public String getTanggal_acara() {
        return tanggal_acara;
    }

    public void setTanggal_acara(String tanggal_acara) {
        this.tanggal_acara = tanggal_acara;
    }

    public String getTempat_acara() {
        return tempat_acara;
    }

    public void setTempat_acara(String tempat_acara) {
        this.tempat_acara = tempat_acara;
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
