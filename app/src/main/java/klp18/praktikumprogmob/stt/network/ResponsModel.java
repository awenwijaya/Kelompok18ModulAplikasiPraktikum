package klp18.praktikumprogmob.stt.network;

import java.util.List;

import klp18.praktikumprogmob.stt.database.Event;

public class ResponsModel {
    String kode, pesan;
    List<Event> result;

    public List<Event> getResult() {
        return result;
    }

    public void setResult(List<Event> result) {
        this.result = result;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
