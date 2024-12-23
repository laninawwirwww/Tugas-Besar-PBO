import java.sql.*;
import java.util.*;
import java.text.*;

// Interface Hewan
interface Hewan {
    // Interface yang mengharuskan implementasi method tampilkanDetail() di kelas yang mengimplementasikannya
    void tampilkanDetail();
}

// Superclass HewanAir (Superclass)
abstract class HewanAir implements Hewan {
    // Properti umum untuk semua hewan air
    protected String nama;
    protected int id;
    protected String tipe;
    protected double berat;
    protected double panjang;
    protected int usia;
    protected String tipeKolam;

    // Konstruktor untuk menginisialisasi objek HewanAir
    public HewanAir(String nama, int id, String tipe, double berat, double panjang, int usia, String tipeKolam) {
        this.nama = nama;
        this.id = id;
        this.tipe = tipe;
        this.berat = berat;
        this.panjang = panjang;
        this.usia = usia;
        this.tipeKolam = tipeKolam;
    }

    // Implementasi method dari interface Hewan untuk menampilkan detail
    @Override
    public void tampilkanDetail() {
        System.out.println("ID: " + id);
        System.out.println("Nama: " + nama);
        System.out.println("Tipe: " + tipe);
        System.out.println("Berat: " + berat + " kg");
        System.out.println("Panjang: " + panjang + " cm");
        System.out.println("Usia: " + usia + " bulan");
        System.out.println("Tipe Kolam: " + tipeKolam);
    }
}

// Subclass Ikan (Subclass)
class Ikan extends HewanAir {
    // Properti tambahan untuk ikan
    private double jumlahPakan;
    private String tanggalPanen;
    private int jumlahAnak;
    private double jumlahPakanPerAnak;

    // Konstruktor untuk menginisialisasi objek Ikan
    public Ikan(String nama, int id, String tipe, double berat, double panjang, int usia, String tipeKolam, double jumlahPakan, String tanggalPanen, int jumlahAnak, double jumlahPakanPerAnak) {
        super(nama, id, tipe, berat, panjang, usia, tipeKolam); // Memanggil konstruktor superclass HewanAir
        this.jumlahPakan = jumlahPakan;
        this.tanggalPanen = tanggalPanen;
        this.jumlahAnak = jumlahAnak;
        this.jumlahPakanPerAnak = jumlahPakanPerAnak;
    }

    // Implementasi method tampilkanDetail() untuk menampilkan informasi ikan
    @Override
    public void tampilkanDetail() {
        super.tampilkanDetail(); // Memanggil method tampilkanDetail() dari superclass
        System.out.println("Jumlah Pakan: " + jumlahPakan + " gram");
        System.out.println("Tanggal Panen: " + tanggalPanen);
        System.out.println("Jumlah Anakan Ikan (1 bungkus 15 anakan): " + jumlahAnak);
        System.out.println("Jumlah Pakan per Anak Ikan: " + jumlahPakanPerAnak + " gram");
        System.out.println("-----------------------------");
    }

    // Method untuk memperbarui detail ikan
    public void perbaruiDetail(String nama, String tipe, double berat, double panjang, int usia, String tipeKolam, double jumlahPakan, String tanggalPanen, int jumlahAnak, double jumlahPakanPerAnak) {
        super.nama = nama;
        super.tipe = tipe;
        super.berat = berat;
        super.panjang = panjang;
        super.usia = usia;
        super.tipeKolam = tipeKolam;
        this.jumlahPakan = jumlahPakan;
        this.tanggalPanen = tanggalPanen;
        this.jumlahAnak = jumlahAnak;
        this.jumlahPakanPerAnak = jumlahPakanPerAnak;
    }

    // Getter untuk ID ikan
    public int getId() {
        return id;
    }

    // Getter untuk jumlah pakan per anak ikan
    public double getJumlahPakanPerAnak() {
        return jumlahPakanPerAnak;
    }
}

public class ManajemenIkan {
    // Variabel untuk koneksi ke database MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/manajemen_ikan"; // Ganti dengan URL database Anda
    private static final String USER = "root"; // Username MySQL
    private static final String PASSWORD = ""; // Kosongkan jika tidak pakai password
    private static Scanner scanner = new Scanner(System.in); // Scanner untuk input dari user

    // Koleksi untuk menyimpan objek Ikan
    private static List<Ikan> daftarIkan = new ArrayList<>(); 

    public static void main(String[] args) {
        // Perulangan untuk menampilkan menu
        while (true) {
            System.out.println("\nSistem Manajemen Ikan");
            System.out.println("1. Tambah Ikan");
            System.out.println("2. Lihat Semua Ikan");
            System.out.println("3. Perbarui Ikan");
            System.out.println("4. Hapus Ikan");
            System.out.println("5. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt(); // Membaca input pilihan dari user
            scanner.nextLine(); // consume newline

            switch (pilihan) {
                case 1:
                    tambahIkan(); // Memanggil method untuk menambah ikan
                    break;
                case 2:
                    lihatSemuaIkan(); // Memanggil method untuk melihat semua ikan
                    break;
                case 3:
                    perbaruiIkan(); // Memanggil method untuk memperbarui data ikan
                    break;
                case 4:
                    hapusIkan(); // Memanggil method untuk menghapus ikan
                    break;
                case 5:
                    System.out.println("Keluar... Sampai jumpa!");
                    return; // Keluar dari program
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

    // Method untuk membuka koneksi ke database
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method untuk menambah ikan
    private static void tambahIkan() {
        int id = -1; // Inisialisasi dengan nilai yang tidak valid
        String nama, tipe, tipeKolam, tanggalPanen;
        double berat, panjang, jumlahPakan;
        int usia, jumlahAnak;
    
        // Input ID ikan dengan pengecekan validitas
        while (id <= 0) {
            System.out.print("Masukkan ID Ikan: ");
            try {
                id = Integer.parseInt(scanner.nextLine()); // Mengambil input dan mengonversi ke integer
                if (id <= 0) {
                    System.out.println("ID ikan harus berupa angka positif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID ikan harus berupa angka.");
            }
        }
    
        // Input data ikan lainnya
        System.out.print("Masukkan Nama Ikan: ");
        nama = scanner.nextLine();
        while (nama.isEmpty()) {
            System.out.println("Nama ikan tidak boleh kosong.");
            System.out.print("Masukkan Nama Ikan: ");
            nama = scanner.nextLine();
        }
    
        System.out.print("Masukkan Tipe Ikan: ");
        tipe = scanner.nextLine();
        while (tipe.isEmpty()) {
            System.out.println("Tipe ikan tidak boleh kosong.");
            System.out.print("Masukkan Tipe Ikan: ");
            tipe = scanner.nextLine();
        }
    
        System.out.print("Masukkan Berat Ikan (kg): ");
        berat = scanner.nextDouble();
        while (berat <= 0) {
            System.out.println("Berat ikan harus lebih dari 0.");
            System.out.print("Masukkan Berat Ikan (kg): ");
            berat = scanner.nextDouble();
        }
    
        System.out.print("Masukkan Panjang Ikan (cm): ");
        panjang = scanner.nextDouble();
        while (panjang <= 0) {
            System.out.println("Panjang ikan harus lebih dari 0.");
            System.out.print("Masukkan Panjang Ikan (cm): ");
            panjang = scanner.nextDouble();
        }
    
        System.out.print("Masukkan Usia Ikan (bulan): ");
        usia = scanner.nextInt();
        while (usia <= 0) {
            System.out.println("Usia ikan harus lebih dari 0.");
            System.out.print("Masukkan Usia Ikan (bulan): ");
            usia = scanner.nextInt();
        }
        scanner.nextLine(); // consume newline
    
        System.out.print("Masukkan Tipe Kolam: ");
        tipeKolam = scanner.nextLine();
        while (tipeKolam.isEmpty()) {
            System.out.println("Tipe kolam tidak boleh kosong.");
            System.out.print("Masukkan Tipe Kolam: ");
            tipeKolam = scanner.nextLine();
        }
    
        System.out.print("Masukkan Jumlah Pakan (kg): ");
jumlahPakan = scanner.nextDouble();
while (jumlahPakan <= 1) {
    System.out.println("Jumlah pakan harus lebih dari 1 kg.");
    System.out.print("Masukkan Jumlah Pakan (kg): ");
    jumlahPakan = scanner.nextDouble();
}
    
        System.out.print("Masukkan Jumlah Anakan Ikan (1 bungkus 15 anakan): ");
        jumlahAnak = scanner.nextInt();
        while (jumlahAnak <= 0) {
            System.out.println("Jumlah anak ikan harus lebih dari 0.");
            System.out.print("Masukkan Jumlah Anakan Ikan (1 bungkus 15 anakan): ");
            jumlahAnak = scanner.nextInt();
        }
    
        // Mengalikan jumlah anak ikan dengan 15
        jumlahAnak = jumlahAnak * 15;
    
        // Menghitung jumlah pakan dalam gram
        jumlahPakan = jumlahPakan * 1000; // Convert ke gram
    
        // Menghitung total pakan yang dibutuhkan (dikalikan 15 untuk jumlah anak ikan)
        double totalPakanDiperlukan = jumlahAnak * 15;
    
        // Menghitung jumlah pakan per anak ikan
        double jumlahPakanPerAnak = jumlahPakan / totalPakanDiperlukan;
        jumlahPakanPerAnak = Math.round(jumlahPakanPerAnak); // Membulatkan hasil pakan per anak ikan
    
        System.out.print("Masukkan Tanggal Panen (YYYY-MM-DD): ");
        tanggalPanen = scanner.nextLine();
        while (tanggalPanen.isEmpty()) {
           
            tanggalPanen = scanner.nextLine();
        }
    
        // Simpan ikan ke dalam koleksi
        Ikan ikan = new Ikan(nama, id, tipe, berat, panjang, usia, tipeKolam, jumlahPakan, tanggalPanen, jumlahAnak, jumlahPakanPerAnak);
        daftarIkan.add(ikan);
    
        // Simpan ikan ke database
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO ikan (id, nama, tipe, berat, panjang, usia, tipeKolam, jumlahPakan, tanggalPanen, jumlahAnak, jumlahPakanPerAnak) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setString(2, nama);
                stmt.setString(3, tipe);
                stmt.setDouble(4, berat);
                stmt.setDouble(5, panjang);
                stmt.setInt(6, usia);
                stmt.setString(7, tipeKolam);
                stmt.setDouble(8, jumlahPakan);
                stmt.setString(9, tanggalPanen);
                stmt.setInt(10, jumlahAnak);
                stmt.setDouble(11, jumlahPakanPerAnak); // Menyimpan hasil perhitungan jumlah pakan per anak ikan
                stmt.executeUpdate();
            }
            System.out.println("Ikan berhasil ditambahkan!");
        } catch (SQLException e) {
            e.printStackTrace(); // Exception handling untuk error database
        }
    }
    
    // Method untuk melihat semua ikan yang tersimpan
    private static void lihatSemuaIkan() {
        daftarIkan.clear(); // Membersihkan daftar ikan sebelum menambah data baru

        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM ikan";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nama = rs.getString("nama");
                    String tipe = rs.getString("tipe");
                    double berat = rs.getDouble("berat");
                    double panjang = rs.getDouble("panjang");
                    int usia = rs.getInt("usia");
                    String tipeKolam = rs.getString("tipeKolam");
                    double jumlahPakan = rs.getDouble("jumlahPakan");
                    String tanggalPanen = rs.getString("tanggalPanen");
                    int jumlahAnak = rs.getInt("jumlahAnak");
                    double jumlahPakanPerAnak = rs.getDouble("jumlahPakanPerAnak");

                    Ikan ikan = new Ikan(nama, id, tipe, berat, panjang, usia, tipeKolam, jumlahPakan, tanggalPanen, jumlahAnak, jumlahPakanPerAnak);
                    daftarIkan.add(ikan);
                }
            }
            if (daftarIkan.isEmpty()) {
                System.out.println("Tidak ada ikan dalam database.");
            } else {
                for (Ikan ikan : daftarIkan) {
                    ikan.tampilkanDetail(); // Menampilkan detail ikan yang ada di koleksi
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Exception handling untuk error database
        }
    }

    // Method untuk memperbarui data ikan
    private static void perbaruiIkan() {
        System.out.print("Masukkan ID Ikan yang akan diperbarui: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Ikan ikanUntukDiperbarui = null;
        for (Ikan ikan : daftarIkan) {
            if (ikan.getId() == id) {
                ikanUntukDiperbarui = ikan;
                break;
            }
        }

        if (ikanUntukDiperbarui == null) {
            System.out.println("Ikan dengan ID " + id + " tidak ditemukan.");
            return;
        }

        // Memperbarui data ikan sesuai input pengguna
        System.out.print("Masukkan Nama Ikan Baru: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Tipe Ikan Baru: ");
        String tipe = scanner.nextLine();
        System.out.print("Masukkan Berat Ikan Baru (kg): ");
        double berat = scanner.nextDouble();
        System.out.print("Masukkan Panjang Ikan Baru (cm): ");
        double panjang = scanner.nextDouble();
        System.out.print("Masukkan Usia Ikan Baru (bulan): ");
        int usia = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Masukkan Tipe Kolam Baru: ");
        String tipeKolam = scanner.nextLine();

        System.out.print("Masukkan Jumlah Pakan Baru (kg): ");
        double jumlahPakan = scanner.nextDouble();

        System.out.print("Masukkan Jumlah Anakan Ikan Baru (1 bungkus 15 anakan): ");
        int jumlahAnak = scanner.nextInt();
        scanner.nextLine(); // consume newline

        jumlahAnak = jumlahAnak * 15;
        jumlahPakan = jumlahPakan * 1000; // Convert ke gram
        double totalPakanDiperlukan = jumlahAnak * 15;
        double jumlahPakanPerAnak = jumlahPakan / totalPakanDiperlukan;
        jumlahPakanPerAnak = Math.round(jumlahPakanPerAnak); // Membulatkan hasil pakan per anak ikan

        System.out.print("Masukkan Tanggal Panen Baru (YYYY-MM-DD): ");
        String tanggalPanen = scanner.nextLine();

        // Memperbarui data ikan yang ada di koleksi dan di database
        ikanUntukDiperbarui.perbaruiDetail(nama, tipe, berat, panjang, usia, tipeKolam, jumlahPakan, tanggalPanen, jumlahAnak, jumlahPakanPerAnak);

        try (Connection conn = getConnection()) {
            String query = "UPDATE ikan SET nama = ?, tipe = ?, berat = ?, panjang = ?, usia = ?, tipeKolam = ?, jumlahPakan = ?, tanggalPanen = ?, jumlahAnak = ?, jumlahPakanPerAnak = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nama);
                stmt.setString(2, tipe);
                stmt.setDouble(3, berat);
                stmt.setDouble(4, panjang);
                stmt.setInt(5, usia);
                stmt.setString(6, tipeKolam);
                stmt.setDouble(7, jumlahPakan);
                stmt.setString(8, tanggalPanen);
                stmt.setInt(9, jumlahAnak);
                stmt.setDouble(10, jumlahPakanPerAnak);
                stmt.setInt(11, id);
                stmt.executeUpdate();
            }
            System.out.println("Ikan berhasil diperbarui!");
        } catch (SQLException e) {
            e.printStackTrace(); // Exception handling untuk error database
        }
    }

    // Method untuk menghapus ikan
    private static void hapusIkan() {
        System.out.print("Masukkan ID Ikan yang akan dihapus: ");
        int id = scanner.nextInt();

        // Mencari ikan berdasarkan ID
        Ikan ikanUntukDihapus = null;
        for (Ikan ikan : daftarIkan) {
            if (ikan.getId() == id) {
                ikanUntukDihapus = ikan;
                break;
            }
        }

        if (ikanUntukDihapus == null) {
            System.out.println("Ikan dengan ID " + id + " tidak ditemukan.");
            return;
        }

        // Menghapus ikan dari koleksi dan database
        daftarIkan.remove(ikanUntukDihapus);

        try (Connection conn = getConnection()) {
            String query = "DELETE FROM ikan WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            System.out.println("Ikan dengan ID " + id + " berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace(); // Exception handling untuk error database
}
}
}