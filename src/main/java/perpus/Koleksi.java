package perpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Koleksi {
    Buku buku;
    String lokasi;
    int jumlah = 0;

    public Koleksi(Buku buku, String lokasi) {
        this.buku = buku;
        this.lokasi = lokasi;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getIdKoleksi() {
        int idBuku = getBuku().getIdBuku();
        String idBukuString = String.valueOf(idBuku);
        String idKoleksi = idBukuString.substring(0, 2);
        return idKoleksi;
    }

    public static void addKoleksi(String idKoleksi, String lokasi) {
        Connection conn = DBConnection.getConn();
        int jumlah = calculateJumlah(idKoleksi); // Function to calculate the number of books with the same two-digit prefix
        String sql = "INSERT INTO koleksi (id_koleksi, lokasi, jumlah) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, idKoleksi);
            st.setString(2, lokasi);
            st.setInt(3, jumlah);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int calculateJumlah(String idKoleksi) {
        Connection conn = DBConnection.getConn();
        String sql = "SELECT COUNT(*) FROM buku WHERE SUBSTRING(idbuku, 1, 2) = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, idKoleksi);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void updateKoleksi(Koleksi koleksi, String lokasi) {
        Connection conn = DBConnection.getConn();
        int idBuku = koleksi.getBuku().getIdBuku();
        String idBukuString = String.valueOf(idBuku);
        int jumlah = calculateJumlah(idBukuString.substring(0, 2));
        String sql = "UPDATE koleksi SET lokasi = ?, jumlah = ? WHERE id_koleksi = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, lokasi);
            st.setInt(2, jumlah);
            st.setString(3, String.valueOf(koleksi.getBuku().getIdBuku()).substring(0, 2));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Koleksi> getListKoleksi() {
        ObservableList<Koleksi> koleksiList = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConn();
        String sql = "SELECT * FROM koleksi";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Buku buku = new Buku(Integer.parseInt(rs.getString("id_koleksi") + "0000"), ""); 
                Koleksi koleksi = new Koleksi(
                    buku,
                    rs.getString("lokasi")
                );
                koleksi.setJumlah(rs.getInt("jumlah"));
                koleksiList.add(koleksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return koleksiList;
    }

    public static void deleteKoleksi(Koleksi koleksi) {
        Connection conn = DBConnection.getConn();
        String sql = "DELETE FROM koleksi WHERE id_koleksi = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, String.valueOf(koleksi.getBuku().getIdBuku()).substring(0, 2));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateJumlahKoleksi(int idBuku) {
        String idKoleksi = String.valueOf(idBuku).substring(0, 2);
        int jumlah = calculateJumlah(idKoleksi);
        Connection conn = DBConnection.getConn();
        String sql = "UPDATE koleksi SET jumlah = ? WHERE id_koleksi = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, jumlah);
            st.setString(2, idKoleksi);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
