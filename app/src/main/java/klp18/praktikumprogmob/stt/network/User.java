package klp18.praktikumprogmob.stt.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("alamat_rumah")
    @Expose
    private String alamat_rumah;

    @SerializedName("no_telepon")
    @Expose
    private String no_telepon;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("role")
    @Expose
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat_rumah() {
        return alamat_rumah;
    }

    public void setAlamat_rumah(String alamat_rumah) {
        this.alamat_rumah = alamat_rumah;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
