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

public class kurirCrud {

    public String VAR_IDKURIR = null;
    public String VAR_NAMAKURIR = null;
    public String VAR_HP = null;
    public String VAR_WILAYAH = null;
    public String VAR_ALAMAT = null;
    public String VAR_JENISKELAMIN = null;
    public boolean validasi = false;
    private String jeniskelamin;

    // ===================== SIMPAN =====================
    public void simpanKurir(String idkurir, String namakurir, String hp, String wilayah, String alamat) {

        try (Connection conn = koneksi.getKoneksi()) {
            String sql = "INSERT INTO kurir (idkurir, namakurir, hp, wilayah, alamat, jeniskelamin) VALUES (?, ?, ?, ?, ?, ?)";
            String cekPrimary = "SELECT * FROM kurir WHERE idkurir = ?";

            try (PreparedStatement check = conn.prepareStatement(cekPrimary)) {
                check.setString(1, idkurir);
                ResultSet data = check.executeQuery();

                if (data.next()) {
                    JOptionPane.showMessageDialog(null, "ID Kurir sudah terdaftar!");
                    this.VAR_NAMAKURIR = data.getString("namakurir");
                    this.VAR_HP = data.getString("hp");
                    this.VAR_WILAYAH = data.getString("wilayah");
                    this.VAR_ALAMAT = data.getString("alamat");
                    this.VAR_JENISKELAMIN = data.getString("jeniskelamin");
                    this.validasi = true;
                } else {
                    this.validasi = false;
                    try (PreparedStatement perintah = conn.prepareStatement(sql)) {
                        perintah.setString(1, idkurir);
                        perintah.setString(2, namakurir);
                        perintah.setString(3, hp);
                        perintah.setString(4, wilayah);
                        perintah.setString(5, alamat);
                        perintah.setString(6, jeniskelamin);
                        perintah.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data kurir berhasil disimpan!");
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
    public void ubahKurir(String idkurir, String namakurir, String hp, String wilayah, String alamat) {

        String sql = "UPDATE kurir SET namakurir = ?, hp = ?, wilayah = ?, alamat = ?, jeniskelamin = ? WHERE idkurir = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, namakurir);
            perintah.setString(2, hp);
            perintah.setString(3, wilayah);
            perintah.setString(4, alamat);
            perintah.setString(5, jeniskelamin);
            perintah.setString(6, idkurir);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data kurir berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Kurir tidak ditemukan. Data gagal diubah.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Ubah: " + e.getMessage());
        }
    }

    // ===================== HAPUS =====================
    public void hapusKurir(String idkurir) {
        String sql = "DELETE FROM kurir WHERE idkurir = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {
            perintah.setString(1, idkurir);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data kurir berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID Kurir tidak ditemukan. Data gagal dihapus.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Hapus: " + e.getMessage());
        }
    }

    // ===================== TAMPIL =====================
    public void tampilDataKurir(JTable tabel, String SQL) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID Kurir");
        model.addColumn("Nama Kurir");
        model.addColumn("No. HP");
        model.addColumn("Wilayah");
        model.addColumn("Alamat");
        model.addColumn("Jenis Kelamin");

        try (Connection conn = koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet data = stmt.executeQuery(SQL)) {

            while (data.next()) {
                model.addRow(new Object[]{
                    data.getString("idkurir"),
                    data.getString("namakurir"),
                    data.getString("hp"),
                    data.getString("wilayah"),
                    data.getString("alamat"),
                    data.getString("jeniskelamin")
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
