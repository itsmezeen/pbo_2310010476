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

public class pelangganCrud {

    public String VAR_IDPELANGGAN = null;
    public String VAR_ALAMAT = null;
    public String VAR_HP = null;
    public boolean validasi = false;

    // ===================== SIMPAN =====================
    public void simpanPelanggan(String idpelanggan, String alamat, String hp) {
        try (Connection conn = koneksi.getKoneksi()) {
            String sql = "INSERT INTO pelanggan (idpelanggan, alamat, hp) VALUES (?, ?, ?)";
            String cekPrimary = "SELECT * FROM pelanggan WHERE idpelanggan = ?";

            try (PreparedStatement check = conn.prepareStatement(cekPrimary)) {
                check.setString(1, idpelanggan);
                ResultSet data = check.executeQuery();

                if (data.next()) {
                    JOptionPane.showMessageDialog(null, "ID Pelanggan sudah terdaftar!");
                    this.VAR_ALAMAT = data.getString("alamat");
                    this.VAR_HP = data.getString("hp");
                    this.validasi = true;
                } else {
                    this.validasi = false;

                    try (PreparedStatement perintah = conn.prepareStatement(sql)) {
                        perintah.setString(1, idpelanggan);
                        perintah.setString(2, alamat);
                        perintah.setString(3, hp);
                        perintah.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data pelanggan berhasil disimpan!");
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
    public void ubahPelanggan(String idpelanggan, String alamat, String hp) {
        String sql = "UPDATE pelanggan SET alamat = ?, hp = ? WHERE idpelanggan = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, alamat);
            perintah.setString(2, hp);
            perintah.setString(3, idpelanggan);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data pelanggan berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Pelanggan tidak ditemukan. Data gagal diubah.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Ubah: " + e.getMessage());
        }
    }

    // ===================== HAPUS =====================
    public void hapusPelanggan(String idpelanggan) {
        String sql = "DELETE FROM pelanggan WHERE idpelanggan = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, idpelanggan);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data pelanggan berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Pelanggan tidak ditemukan. Data gagal dihapus.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Hapus: " + e.getMessage());
        }
    }

    // ===================== TAMPIL =====================
    public void tampilDataPelanggan(JTable tabel, String SQL) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID Pelanggan");
        model.addColumn("Alamat");
        model.addColumn("No. HP");

        try (Connection conn = koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet data = stmt.executeQuery(SQL)) {

            while (data.next()) {
                model.addRow(new Object[]{
                    data.getString("idpelanggan"),
                    data.getString("alamat"),
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
