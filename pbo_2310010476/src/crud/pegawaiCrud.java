package crud;

import db_config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class pegawaiCrud {

    public String VAR_IDPEGAWAI = null;
    public String VAR_NAMAPEGAWAI = null;
    public String VAR_ALAMAT = null;
    public String VAR_TTL = null;
    public String VAR_HP = null;
    public boolean validasi = false;

    // ===================== SIMPAN =====================
    public void simpanPegawai(String idpegawai, String namapegawai, String alamat, String ttl, String hp) {
        try (Connection conn = koneksi.getKoneksi()) {
            String sql = "INSERT INTO pegawai (idpegawai, namapegawai, alamat, ttl, hp) VALUES (?, ?, ?, ?, ?)";
            String cekPrimary = "SELECT * FROM pegawai WHERE idpegawai = ?";

            try (PreparedStatement check = conn.prepareStatement(cekPrimary)) {
                check.setString(1, idpegawai);
                ResultSet data = check.executeQuery();

                if (data.next()) {
                    JOptionPane.showMessageDialog(null, "ID Pegawai sudah terdaftar!");
                    this.VAR_NAMAPEGAWAI = data.getString("namapegawai");
                    this.VAR_ALAMAT = data.getString("alamat");
                    this.VAR_TTL = data.getString("ttl");
                    this.VAR_HP = data.getString("hp");
                    this.validasi = true;
                } else {
                    this.validasi = false;

                    try (PreparedStatement perintah = conn.prepareStatement(sql)) {
                        perintah.setString(1, idpegawai);
                        perintah.setString(2, namapegawai);
                        perintah.setString(3, alamat);
                        perintah.setString(4, ttl);
                        perintah.setString(5, hp);
                        perintah.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data pegawai berhasil disimpan!");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL saat Simpan: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Umum saat Simpan: " + e.getMessage());
        }
    }

    // ===================== UBAH =====================
    public void ubahPegawai(String idpegawai, String namapegawai, String alamat, String ttl, String hp) {
        String sql = "UPDATE pegawai SET namapegawai = ?, alamat = ?, ttl = ?, hp = ? WHERE idpegawai = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, namapegawai);
            perintah.setString(2, alamat);
            perintah.setString(3, ttl);
            perintah.setString(4, hp);
            perintah.setString(5, idpegawai);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data pegawai berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Pegawai tidak ditemukan. Data gagal diubah.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Ubah: " + e.getMessage());
        }
    }

    // ===================== HAPUS =====================
    public void hapusPegawai(String idpegawai) {
        String sql = "DELETE FROM pegawai WHERE idpegawai = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, idpegawai);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data pegawai berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Pegawai tidak ditemukan. Data gagal dihapus.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Hapus: " + e.getMessage());
        }
    }

    // ===================== TAMPIL =====================
    public void tampilDataPegawai(JTable tabel, String SQL) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID Pegawai");
        model.addColumn("Nama Pegawai");
        model.addColumn("Alamat");
        model.addColumn("Tempat/Tanggal Lahir");
        model.addColumn("No. HP");

        try (Connection conn = koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet data = stmt.executeQuery(SQL)) {

            while (data.next()) {
                model.addRow(new Object[]{
                    data.getString("idpegawai"),
                    data.getString("namapegawai"),
                    data.getString("alamat"),
                    data.getString("ttl"),
                    data.getString("hp")
                });
            }

            tabel.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL saat Tampil Data: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Umum saat Tampil Data: " + e.getMessage());
        }
    }
}
