package perpus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Anggota {

    private int id;
    private String nama;
    private String alamat;
    private String telepon;
    private String email;
    private Date tanggalDaftar;

    @SuppressWarnings("exports")
    public Anggota(int id, String nama, String alamat, String telepon, String email, Date tanggalDaftar) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.email = email;
        this.tanggalDaftar = tanggalDaftar;
    }

    public Anggota(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SuppressWarnings("exports")
    public Date getTanggalDaftar() {
        return tanggalDaftar;
    }

    @SuppressWarnings("exports")
    public void setTanggalDaftar(Date tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }private ObservableList<Peminjaman> peminjamanList = FXCollections.observableArrayList();


     public static void registerAnggota(Anggota anggota) {
        Connection conn = DBConnection.getConn();
        String sql = "INSERT INTO anggota(id, nama, alamat, telepon, email, tanggalDaftar) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, anggota.getId());
            st.setString(2, anggota.getNama());
            st.setString(3, anggota.getAlamat());
            st.setString(4, anggota.getTelepon());
            st.setString(5, anggota.getEmail());
            st.setDate(6, anggota.getTanggalDaftar());
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ObservableList<Anggota> getlistAnggota() {
        ObservableList<Anggota> listAnggota = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConn();
        String sql = "SELECT * FROM anggota";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Anggota anggota = new Anggota(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("alamat"),
                    rs.getString("telepon"),
                    rs.getString("email"),
                    rs.getDate("tanggalDaftar")
                );
                listAnggota.add(anggota);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listAnggota;
    }

    public static void updateAnggota(Anggota anggota) {
        Connection conn = DBConnection.getConn();
        String sql = "UPDATE anggota SET nama = ?, alamat = ?, telepon = ?, email = ?, tanggalDaftar = ? WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, anggota.getNama());
            st.setString(2, anggota.getAlamat());
            st.setString(3, anggota.getTelepon());
            st.setString(4, anggota.getEmail());
            st.setDate(5, anggota.getTanggalDaftar());
            st.setInt(6, anggota.getId());
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void deleteAnggota(int id) {
        Connection conn = DBConnection.getConn();
        String sql = "DELETE FROM anggota WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
