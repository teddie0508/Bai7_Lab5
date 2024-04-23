import java.util.Random;

class NganHang {
    private double[] taiKhoan;

    public NganHang(int n, double soDuBanDau) {
        taiKhoan = new double[n];
        for (int i = 0; i < taiKhoan.length; i++) {
            taiKhoan[i] = soDuBanDau;
        }
    }

    public synchronized int kichThuoc() {
        return taiKhoan.length;
    }

    public synchronized double getTongSoDu() {
        double tong = 0;
        for (double soDu : taiKhoan) {
            tong += soDu;
        }
        return tong;
    }

    public synchronized void chuyenTien(int tu, int den, double soTien) throws InterruptedException {
        while (taiKhoan[tu] < soTien) {
            System.out.println(Thread.currentThread().getName() + " đang chờ...");
            wait();
        }

        taiKhoan[tu] -= soTien;
        taiKhoan[den] += soTien;

        System.out.println("Chuyển " + soTien + " từ tài khoản " + tu + " sang tài khoản " + den + ". Số dư tổng cộng mới: " + getTongSoDu());

        notifyAll();
    }
}

class ChuyenTien implements Runnable {
    private NganHang nganHang;
    private int tuTaiKhoan;
    private double soTienToiDa;
    private final int tre = 1000;

    public ChuyenTien(NganHang nganHang, int tuTaiKhoan, double soTienToiDa) {
        this.nganHang = nganHang;
        this.tuTaiKhoan = tuTaiKhoan;
        this.soTienToiDa = soTienToiDa;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            while (true) {
                int denTaiKhoan = random.nextInt(nganHang.kichThuoc());
                double soTien = random.nextDouble() * soTienToiDa;
                nganHang.chuyenTien(tuTaiKhoan, denTaiKhoan, soTien);
                Thread.sleep(random.nextInt(tre));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class DongBoNganHang {
    public static void main(String[] args) {
        NganHang nganHang = new NganHang(100, 1000);
        for (int i = 0; i < nganHang.kichThuoc(); i++) {
            ChuyenTien chuyenTien = new ChuyenTien(nganHang, i, 100);
            Thread thread = new Thread(chuyenTien);
            thread.start();
        }
    }
}
