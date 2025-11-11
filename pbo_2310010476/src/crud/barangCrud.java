package crud;

import db_config.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class barangCrud {

    public String VAR_KODEBARANG = null;
    public String VAR_NAMABARANG = null;
    public String VAR_JENISBARANG = null;
    public int VAR_JUMLAHBARANG = 0;
    public double VAR_BERATBARANG = 0.0;
    public double VAR_HARGABARANG = 0.0;
    public boolean validasi = false;

    // ===================== SIMPAN =====================
    public void simpanBarang(String kodebarang, String namabarang, String jenisbarang,
                             int jumlahbarang, double beratbarang, double hargabarang) {

        try (Connection conn = koneksi.getKoneksi()) {
            String sql = "INSERT INTO barang (kodebarang, namabarang, jenisbarang, jumlahbarang, beratbarang, hargabarang) VALUES (?, ?, ?, ?, ?, ?)";
            String cekPrimary = "SELECT * FROM barang WHERE kodebarang = ?";

            try (PreparedStatement check = conn.prepareStatement(cekPrimary)) {
                check.setString(1, kodebarang);
                ResultSet data = check.executeQuery();

                if (data.next()) {
                    JOptionPane.showMessageDialog(null, "Kode Barang sudah terdaftar!");
                    this.VAR_NAMABARANG = data.getString("namabarang");
                    this.VAR_JENISBARANG = data.getString("jenisbarang");
                    this.VAR_JUMLAHBARANG = data.getInt("jumlahbarang");
                    this.VAR_BERATBARANG = data.getDouble("beratbarang");
                    this.VAR_HARGABARANG = data.getDouble("hargabarang");
                    this.validasi = true;
                } else {
                    this.validasi = false;

                    try (PreparedStatement perintah = conn.prepareStatement(sql)) {
                        perintah.setString(1, kodebarang);
                        perintah.setString(2, namabarang);
                        perintah.setString(3, jenisbarang);
                        perintah.setInt(4, jumlahbarang);
                        perintah.setDouble(5, beratbarang);
                        perintah.setDouble(6, hargabarang);
                        perintah.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data barang berhasil disimpan!");
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
    public void ubahBarang(String kodebarang, String namabarang, String jenisbarang,
                           int jumlahbarang, double beratbarang, double hargabarang) {

        String sql = "UPDATE barang SET namabarang = ?, jenisbarang = ?, jumlahbarang = ?, beratbarang = ?, hargabarang = ? WHERE kodebarang = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {

            perintah.setString(1, namabarang);
            perintah.setString(2, jenisbarang);
            perintah.setInt(3, jumlahbarang);
            perintah.setDouble(4, beratbarang);
            perintah.setDouble(5, hargabarang);
            perintah.setString(6, kodebarang);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data barang berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "Kode Barang tidak ditemukan. Data gagal diubah.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Ubah: " + e.getMessage());
        }
    }

    // ===================== HAPUS =====================
    public void hapusBarang(String kodebarang) {
        String sql = "DELETE FROM barang WHERE kodebarang = ?";

        try (Connection conn = koneksi.getKoneksi(); PreparedStatement perintah = conn.prepareStatement(sql)) {

            perintah.setString(1, kodebarang);

            int rowsAffected = perintah.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data barang berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "Kode Barang tidak ditemukan. Data gagal dihapus.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat Hapus: " + e.getMessage());
        }
    }

    // ===================== TAMPIL =====================
    public void tampilDataBarang(JTable tabel, String SQL) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Jenis Barang");
        model.addColumn("Jumlah Barang");
        model.addColumn("Berat Barang");
        model.addColumn("Harga Barang");

        try (Connection conn = koneksi.getKoneksi();
             Statement stmt = conn.createStatement();
             ResultSet data = stmt.executeQuery(SQL)) {

            while (data.next()) {
                model.addRow(new Object[]{
                    data.getString("kodebarang"),
                    data.getString("namabarang"),
                    data.getString("jenisbarang"),
                    data.getInt("jumlahbarang"),
                    data.getDouble("beratbarang"),
                    data.getDouble("hargabarang")
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
