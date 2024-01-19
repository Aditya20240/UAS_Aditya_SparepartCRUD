/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sparepartcrud;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SparepartCRUD {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sparepart";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTable(connection); // Membuat tabel jika belum ada

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Pilih operasi:");
                System.out.println("1. Tambah Sparepart");
                System.out.println("2. Tampilkan Semua Sparepart");
                System.out.println("3. Perbarui Sparepart");
                System.out.println("4. Hapus Sparepart");
                System.out.println("0. Keluar");
                System.out.print("Pilihan Anda: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addSparepart(connection, scanner);
                        break;
                    case 2:
                        displayAllSpareparts(connection);
                        break;
                    case 3:
                        updateSparepart(connection, scanner);
                        break;
                    case 4:
                        deleteSparepart(connection, scanner);
                        break;
                }
            } while (choice != 0);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS spareparts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nama VARCHAR(255)," +
                "stok INT," +
                "harga DOUBLE)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void addSparepart(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Masukkan nama sparepart: ");
        String nama = scanner.next();
        System.out.print("Masukkan stok sparepart: ");
        int stok = scanner.nextInt();
        System.out.print("Masukkan harga sparepart: ");
        double harga = scanner.nextDouble();

        String insertQuery = "INSERT INTO spareparts (nama, stok, harga) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, nama);
            preparedStatement.setInt(2, stok);
            preparedStatement.setDouble(3, harga);
            preparedStatement.executeUpdate();

            System.out.println("Sparepart berhasil ditambahkan!");
        }
    }

    private static void displayAllSpareparts(Connection connection) throws SQLException {
        String selectAllQuery = "SELECT * FROM spareparts";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Nama: " + resultSet.getString("nama"));
                System.out.println("Stok: " + resultSet.getInt("stok"));
                System.out.println("Harga: " + resultSet.getDouble("harga"));
                System.out.println("---------------");
            }
        }
    }

    private static void updateSparepart(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Masukkan ID sparepart yang akan diperbarui: ");
        int id = scanner.nextInt();

        String selectQuery = "SELECT * FROM spareparts WHERE id = ?";
        String updateQuery = "UPDATE spareparts SET nama = ?, stok = ?, harga = ? WHERE id = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            selectStatement.setInt(1, id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.print("Masukkan nama baru: ");
                    String nama = scanner.next();
                    System.out.print("Masukkan stok baru: ");
                    int stok = scanner.nextInt();
                    System.out.print("Masukkan harga baru: ");
                    double harga = scanner.nextDouble();

                    updateStatement.setString(1, nama);
                    updateStatement.setInt(2, stok);
                    updateStatement.setDouble(3, harga);
                    updateStatement.setInt(4, id);

                    updateStatement.executeUpdate();
                    System.out.println("Sparepart berhasil diperbarui!");
                } else {
                    System.out.println("Sparepart dengan ID tersebut tidak ditemukan.");
                }
            }
        }
    }

    private static void deleteSparepart(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Masukkan ID sparepart yang akan dihapus: ");
        int id = scanner.nextInt();

        String deleteQuery = "DELETE FROM spareparts WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sparepart berhasil dihapus!");
            } else {
                System.out.println("Sparepart dengan ID tersebut tidak ditemukan.");
            }
        }
    }
}
