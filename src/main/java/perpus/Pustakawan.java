package perpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Pustakawan {
    private int id;
    private String nama;
    private String email;

    public Pustakawan(int id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    void tambahBuku(Buku buku) {
        //
    }

    void hapusBuku(Buku buku) {
        //
    }

    void updateBuku(Buku buku) {
        //
    }

    public static void registerPustakawan(Pustakawan pustakawan) {
        Connection con = DBConnection.getConn();
        String sql = "INSERT INTO pustakawan(id, nama, email) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pustakawan.getId());
            ps.setString(2, pustakawan.getNama());
            ps.setString(3, pustakawan.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Pustakawan> getlistPustakawan() {
        ObservableList<Pustakawan> listPustakawan = FXCollections.observableArrayList();
        Connection con = DBConnection.getConn();
        String sql = "SELECT * FROM pustakawan";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pustakawan pustakawan = new Pustakawan(rs.getInt("id"), rs.getString("nama"), rs.getString("email"));
                listPustakawan.add(pustakawan);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPustakawan;
    }

    public static void updatePustakawan(Pustakawan pustakawan) {
        Connection con = DBConnection.getConn();
        String sql = "UPDATE pustakawan SET nama = ?, email = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pustakawan.getNama());
            ps.setString(2, pustakawan.getEmail());
            ps.setInt(3, pustakawan.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void deletePustakawan(int id) {
        Connection con = DBConnection.getConn();
        String sql = "DELETE FROM pustakawan WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}