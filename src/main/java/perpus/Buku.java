package perpus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Buku {
    private int idBuku;
    private String judul;
    private String penerbit;
    private String penulis;
    private int tahunTerbit;
    private boolean status;

    // Constructors
    public Buku(int id, String judul, String penerbit, String penulis, int tahunTerbit, Boolean status) {
        this.idBuku = id;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.status = status;
    }

    public Buku(int id, String judul) {
        this.idBuku = id;
        this.judul = judul;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static ObservableList<Buku> getlistBuku() {
        ObservableList<Buku> listBuku = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConn();
        String sql = "SELECT * FROM buku";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Buku buku = new Buku(
                    rs.getInt("idbuku"),
                    rs.getString("judul"),
                    rs.getString("penerbit"),
                    rs.getString("penulis"),
                    rs.getInt("tahun_terbit"),
                    rs.getBoolean("status")
                );
                listBuku.add(buku);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listBuku;
    }
}
