package klp18.praktikumprogmob.stt.network;

import java.util.List;

public class ResponsModelRequest {
    String kode, pesan;
    List<Request> result;

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

    public List<Request> getResult() {
        return result;
    }

    public void setResult(List<Request> result) {
        this.result = result;
    }
}
