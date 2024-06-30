package perpus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Peminjaman {
    private int id;
    private Anggota anggota;
    private Buku buku;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private String status;

    public Peminjaman(int id, Anggota anggota, Buku buku, LocalDate tanggalPinjam) {
        this.id = id;
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalPinjam.plusDays(7);
        this.status = "belum dikembalikan";
    }

    public Peminjaman(int id, Anggota anggota, Buku buku, LocalDate tanggalPinjam, LocalDate tanggalKembali, String status) {
        this.id = id;
        this.anggota = anggota;
        this.buku = buku;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
    }

    public int getId() { return id; }
    public Anggota getAnggota() { return anggota; }
    public Buku getBuku() { return buku; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void save() {
        Connection conn = DBConnection.getConn();
        String sql = "INSERT INTO peminjaman (id_pinjam, id_anggota, id_buku, tanggal_pinjam, tanggal_kembali, status) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdateBuku = "UPDATE buku SET status = '0' WHERE idBuku = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            PreparedStatement stUpdateBuku = conn.prepareStatement(sqlUpdateBuku);
            st.setInt(1, this.getId());
            st.setInt(2, this.anggota.getId());
            st.setInt(3, this.buku.getIdBuku());
            st.setDate(4, Date.valueOf(this.tanggalPinjam));
            st.setDate(5, Date.valueOf(this.tanggalKembali));
            st.setString(6, this.status);
            stUpdateBuku.setInt(1, this.buku.getIdBuku());
            st.executeUpdate();
            stUpdateBuku.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Peminjaman> getListPeminjaman() {
        ObservableList<Peminjaman> listPeminjaman = FXCollections.observableArrayList();
        Connection conn = DBConnection.getConn();
        String sql = "SELECT p.id_pinjam, p.tanggal_pinjam, p.tanggal_kembali, p.status, " +
                     "a.id AS id_anggota, a.nama AS nama_anggota, " +
                     "b.idBuku AS id_buku, b.judul AS judul_buku " +
                     "FROM peminjaman p " +
                     "JOIN anggota a ON p.id_anggota = a.id " +
                     "JOIN buku b ON p.id_buku = b.idBuku";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Anggota anggota = new Anggota(rs.getInt("id_anggota"), rs.getString("nama_anggota"));
                Buku buku = new Buku(rs.getInt("id_buku"), rs.getString("judul_buku"));
                Peminjaman peminjaman = new Peminjaman(
                        rs.getInt("id_pinjam"),
                        anggota,
                        buku,
                        rs.getDate("tanggal_pinjam").toLocalDate(),
                        rs.getDate("tanggal_kembali").toLocalDate(),
                        rs.getString("status")
                );
                listPeminjaman.add(peminjaman);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPeminjaman;
    }

static void updatePeminjamanStatus(Peminjaman peminjaman) {
        Connection conn = DBConnection.getConn();
        String sql = "UPDATE peminjaman SET status = ? WHERE id_pinjam = ?";
        String sqlUpdateBuku = "UPDATE buku SET status = '1' WHERE idBuku = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            PreparedStatement stUpdateBuku = conn.prepareStatement(sqlUpdateBuku);
            st.setString(1, peminjaman.getStatus());
            st.setInt(2, peminjaman.getId());
            stUpdateBuku.setInt(1, peminjaman.getBuku().getIdBuku());
            st.executeUpdate();
            stUpdateBuku.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
}
