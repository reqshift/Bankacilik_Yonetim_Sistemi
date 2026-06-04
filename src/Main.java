import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Konsoldan double (ondalıklı) sayıları hatasız okumak için Locale.US set ediyoruz
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("=== BANKACILIK SİSTEMİ OTOMATİK TEST PANELİ ===");

        // 1. Simülasyonun Başlayacağı İlk Tarihi Kullanıcıdan Alıyoruz
        System.out.print("Sistem başlangıç tarihini giriniz (Format: YYYY-AA-GG, Örn: 2026-06-01): ");
        String baslangicTarihMetni = scanner.next();
        LocalDate ilkIslemTarihi = LocalDate.parse(baslangicTarihMetni);

        // 2. Hesap Nesnelerini bu ilk işlem tarihine göre oluşturuyoruz (now() tamamen kalktı!)
        CheckingAccount checking = new CheckingAccount("VDS-123", "Hatice Nisa", 1000.0); // 1000 TL günlük limit
        SavingsAccount savings = new SavingsAccount("VDL-456", "Hatice Nisa", 0.05, 30, ilkIslemTarihi); // %5 faiz, 30 gün vade

        // Hesaplara ilk paraları da bu başlangıç tarihinde yatırıyoruz
        checking.deposit(5000.0, ilkIslemTarihi);
        savings.deposit(10000.0, ilkIslemTarihi);

        System.out.println("\n[SİSTEM]: Hesaplar başarıyla kuruldu ve ilk bakiye yüklendi.");
        System.out.println("Vadesiz Bakiye: " + checking.getBalance() + " TL (Günlük Limit: 1000 TL)");
        System.out.println("Vadeli Bakiye: " + savings.getBalance() + " TL (Vade Bitiş Tarihi otomatik: " + ilkIslemTarihi.plusDays(30) + ")");

        // 3. Dinamik Test Döngüsü
        boolean devam = true;
        while (devam) {
            System.out.println("\n------------------------------------------------");
            System.out.print("Şu an işlem yapmak istediğiniz tarihi girin (Format: YYYY-AA-GG): ");
            String hamTarih = scanner.next();

            // Kullanıcının klavyeden girdiği o anki dinamik işlem tarihi (today/transactionDate)
            LocalDate anlikIslemTarihi = LocalDate.parse(hamTarih);
            System.out.println("[SİSTEM AKTİF TARİH]: " + anlikIslemTarihi);

            System.out.println("\n[1] Vadesiz Hesaptan Para Çek (CheckingAccount)");
            System.out.println("[2] Vadeli Hesaptan Para Çek (SavingsAccount)");
            System.out.println("[3] Vadeli Hesaba Para Yatır (SavingsAccount)");
            System.out.println("[4] Mevcut Bakiyeleri ve Durumu Görüntüle");
            System.out.println("[5] Simülasyonu Bitir");
            System.out.print("Yapmak istediğiniz işlem numarasını seçin: ");
            int secim = scanner.nextInt();

            switch (secim) {
                case 1:
                    System.out.print("Vadesiz hesaptan çekilecek tutar: ");
                    double vadesizMiktar = scanner.nextDouble();
                    // Klavyeden girilen tarihi parametre olarak fırlatıyoruz
                    checking.withdraw(vadesizMiktar, anlikIslemTarihi);
                    break;

                case 2:
                    System.out.print("Vadeli hesaptan çekilecek tutar: ");
                    double vadeliMiktar = scanner.nextDouble();
                    // Klavyeden girilen tarihi parametre olarak fırlatıyoruz
                    savings.withdraw(vadeliMiktar, anlikIslemTarihi);
                    break;

                case 3:
                    System.out.print("Vadeli hesaba yatırılacak tutar: ");
                    double yatirilacakMiktar = scanner.nextDouble();
                    // Klavyeden girilen tarihi parametre olarak fırlatıyoruz (Vadeyi buna göre güncelleyecek)
                    savings.deposit(yatirilacakMiktar, anlikIslemTarihi);
                    break;

                case 4:
                    System.out.println("\n--- GÜNCEL HESAP DURUMLARI ---");
                    System.out.println("Vadesiz Hesap Bakiye: " + checking.getBalance() + " TL");
                    System.out.println("Vadeli Hesap Bakiye: " + savings.getBalance() + " TL");
                    break;

                case 5:
                    devam = false;
                    System.out.println("Simülasyon başarıyla sonlandırıldı. Kod kalitesi testi tamamlandı!");
                    break;

                default:
                    System.out.println("[UYARI]: Geçersiz seçim yaptınız, tekrar deneyin.");
            }
        }
        scanner.close();
    }
}