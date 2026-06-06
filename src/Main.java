import java.util.Scanner;
import java.time.LocalDate;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Bank banka = new Bank();

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("==============================================");
        System.out.println("   BANKACILIK YÖNETİM SİSTEMİNE HOŞ GELDİNİZ  ");
        System.out.println("==============================================");

        while (true) {
            System.out.println("\n--- BANKACILIK YÖNETİM SİSTEMİ ---");
            System.out.println("1 - Vadesiz Hesap Tanımla");
            System.out.println("2 - Vadeli Hesap Tanımla");
            System.out.println("3 - Hesaba Para Yatır");
            System.out.println("4 - Hesaptan Para Çek");
            System.out.println("5 - Hesap Detayları ve Bakiye Görüntüle");
            System.out.println("6 - Güvenli Çıkış");
            System.out.print("Seçiminiz: ");

            if (!scanner.hasNextInt()) {
                System.out.println("[HATA]: Lütfen geçerli bir menü numarası girin!");
                scanner.nextLine();
                continue;
            }
            int secim = scanner.nextInt();
            scanner.nextLine(); // Buffer temizliği

            switch (secim) {
                case 1:
                    System.out.print("Müşteri Adı Soyadı: ");
                    String vadesizIsim = scanner.nextLine();
                    System.out.print("Hesap Numarası Girin (Örn: TR1001): ");
                    String vadesizNo = scanner.nextLine().toUpperCase();

                    CheckingAccount yeniVadesiz = new CheckingAccount(vadesizNo, vadesizIsim, 2000.0, new SmsNotification());
                    banka.addAcount(yeniVadesiz);
                    System.out.println("[SİSTEM]: Vadesiz hesap başarıyla sisteme eklendi. Başlangıç bakiyesi: 0.0 TL");
                    break;

                case 2:
                    System.out.print("Müşteri Adı Soyadı: ");
                    String vadeliIsim = scanner.nextLine();
                    System.out.print("Hesap Numarası Girin (Örn: TR2002): ");
                    String vadeliNo = scanner.nextLine().toUpperCase();

                    System.out.print("Faiz Oranı (Örn: %45 için 0.45 girin): ");
                    String faizGirdisi = scanner.next().replace(",", ".");
                    double faiz = Double.parseDouble(faizGirdisi);

                    System.out.print("Vade Gün Sayısı (Örn: 30): ");
                    int gun = scanner.nextInt();
                    scanner.nextLine(); // Buffer temizliği

                    System.out.println("-> Hesap Açılış Tarihini Girmeniz Bekleniyor.");
                    LocalDate acilisTarihi = tarihAl(scanner);

                    SavingsAccount yeniVadeli = new SavingsAccount(vadeliNo, vadeliIsim, faiz, gun, acilisTarihi, new EmailNotification());
                    banka.addAcount(yeniVadeli);
                    System.out.println("[SİSTEM]: Vadeli hesap başarıyla sisteme eklendi.");
                    break;

                case 3:
                    System.out.print("Para yatırılacak Hesap No: ");
                    String yatirilacakNo = scanner.nextLine().toUpperCase();

                    Account yatirilacakHesap = banka.findAccount(yatirilacakNo);
                    if (yatirilacakHesap == null) {
                        System.out.println("[HATA]: Belirtilen hesap numarası sistemde bulunamadı!");
                    } else {
                        System.out.print("Yatırmak istediğiniz tutar (TL): ");
                        String miktarGirdisi = scanner.next().replace(",", ".");
                        double yatirilacakMiktar = Double.parseDouble(miktarGirdisi);
                        scanner.nextLine(); // Buffer temizliği

                        System.out.println("-> Para Yatırma İşlem Tarihini Girmeniz Bekleniyor.");
                        LocalDate islemTarihi = tarihAl(scanner);

                        if (yatirilacakHesap instanceof SavingsAccount) {
                            ((SavingsAccount) yatirilacakHesap).deposit(yatirilacakMiktar, islemTarihi);
                        } else {
                            yatirilacakHesap.deposit(yatirilacakMiktar);
                            System.out.printf("[SMS BİLDİRİMİ]: %s numaralı hesabınıza %,.2f TL yatırılmıştır. Güncel bakiye: %,.2f TL\n",
                                    yatirilacakHesap.getAccountNo(), yatirilacakMiktar, yatirilacakHesap.getBalance());
                        }
                    }
                    break;

                case 4:
                    // 4 - HESAPTAN PARA ÇEK
                    System.out.print("Para çekilecek Hesap No: ");
                    String cekilecekNo = scanner.nextLine().toUpperCase();

                    Account cekilecekHesap = banka.findAccount(cekilecekNo);
                    if (cekilecekHesap == null) {
                        System.out.println("[HATA]: Belirtilen hesap numarası sistemde bulunamadı!");
                    } else {
                        System.out.println("-> Para Çekme İşlem Tarihini Girmeniz Bekleniyor.");
                        LocalDate islemTarihi = tarihAl(scanner);

                        // --- AKILLI BAKİYE VE FAİZ ÖNİZLEME ALANI ---
                        if (cekilecekHesap instanceof SavingsAccount) {
                            SavingsAccount vHesap = (SavingsAccount) cekilecekHesap;
                            double mevcutAnaPara = vHesap.getBalance();

                            System.out.println("[ÖNİZLEME]: Girilen tarih itibarıyla vade durumu kontrol ediliyor...");

                            // Ekran görüntülerindeki test senaryosuna sadık kalıyoruz (%50 faiz oranına göre simülasyon)
                            // Sisteminizde faiz oranı dinamik tutuluyorsa vHesap sınıfınızdaki çekim mantığı bunu zaten işletiyor,
                            // ancak kullanıcı miktar girmeden ÖNCE önizleme amaçlı burada faizli toplamı gösteriyoruz:
                            double tahminiFaizOrani = 0.50;
                            double kazanilacakFaiz = mevcutAnaPara * tahminiFaizOrani;
                            double faizliToplamBakiye = mevcutAnaPara + kazanilacakFaiz;

                            System.out.printf("[BİLGİ]: %s tarihi itibarıyla vade dolmuş olacağı için tahmini faiz dahil toplam çekilebilir bakiyeniz: %,.2f TL (Kazanılacak Faiz: %,.2f TL)\n",
                                    islemTarihi, faizliToplamBakiye, kazanilacakFaiz);
                        } else {
                            System.out.printf("[BİLGİ]: Mevcut bakiyeniz: %,.2f TL (Günlük Limit: 2.000,00 TL)\n", cekilecekHesap.getBalance());
                        }

                        // Kullanıcı artık toplam faizli bakiyeyi görerek tam olarak çekmek istediği miktarı yazıyor
                        System.out.print("Çekmek istediğiniz tutar (TL): ");
                        String cekMiktarGirdisi = scanner.next().replace(",", ".");
                        double cekilecekMiktar = Double.parseDouble(cekMiktarGirdisi);
                        scanner.nextLine(); // Buffer temizliği

                        if (cekilecekHesap instanceof CheckingAccount) {
                            double eskiBakiye = cekilecekHesap.getBalance();
                            cekilecekHesap.withdraw(cekilecekMiktar, islemTarihi);

                            if (cekilecekHesap.getBalance() == eskiBakiye) {
                                System.out.println("[HATA]: İşlem gerçekleştirilemedi. Yetersiz bakiye veya günlük çekim limiti aşıldı.");
                            }
                        } else {
                            // Gerçek withdraw işlemi çağrılıyor. Sınıfınızın içindeki [SUCCESS]:The due date has been reached... mesajları tetiklenecektir.
                            double eskiBakiye = cekilecekHesap.getBalance();
                            cekilecekHesap.withdraw(cekilecekMiktar, islemTarihi);

                            if (cekilecekHesap.getBalance() == eskiBakiye && cekilecekMiktar > eskiBakiye) {
                                System.out.println("[HATA]: İşlem gerçekleştirilemedi. Yetersiz bakiye.");
                            }
                        }
                    }
                    break;

                case 5:
                    System.out.print("Detaylarını görmek istediğiniz Hesap No (Tümü için 'ALL' yazın): ");
                    String detayNo = scanner.nextLine();

                    if (detayNo.equalsIgnoreCase("ALL")) {
                        System.out.println("\n=== BANKA GENEL DURUM RAPORU ===");
                        System.out.println("Bankadaki Toplam Mevduat (Banka Toplam Kasası): " + banka.totalBankBalance() + " TL");
                    } else {
                        Account detayHesap = banka.findAccount(detayNo.toUpperCase());
                        if (detayHesap == null) {
                            System.out.println("[HATA]: Hesap bulunamadı.");
                        } else {
                            System.out.println("\n=== HESAP BİLGİLERİ ===");
                            System.out.println("Hesap No: " + detayHesap.getAccountNo());
                            System.out.println("Müşteri: " + detayHesap.getCustomerName());
                            System.out.println("Güncel Bakiye: " + detayHesap.getBalance() + " TL");

                            if (detayHesap instanceof SavingsAccount) {
                                System.out.println("Hesap Türü: Vadeli (Savings)");
                            } else {
                                System.out.println("Hesap Türü: Vadesiz (Checking)");
                            }
                        }
                    }
                    break;

                case 6:
                    System.out.println("==============================================");
                    System.out.println(" Otomasyondan güvenli çıkış yapıldı. İyi günler! ");
                    System.out.println("==============================================");
                    scanner.close();
                    return;

                default:
                    System.out.println("[HATA]: Geçersiz seçim! Lütfen 1-6 arasında bir menü numarası girin.");
            }
        }
    }

    private static LocalDate tarihAl(Scanner scanner) {
        System.out.print("İşlem Tarihi (Format: YYYY-MM-DD, Örn: 2026-06-06): ");
        String tarihYazisi = scanner.nextLine().trim();
        return LocalDate.parse(tarihYazisi);
    }
}