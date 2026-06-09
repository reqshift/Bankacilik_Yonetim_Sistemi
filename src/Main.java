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
                    String vadesizIsim = "";
                    String vadesizNo = "";
                    while(true) {
                        System.out.print("Müşteri Adı Soyadı: ");
                        vadesizIsim = scanner.nextLine().trim(); //.trim used to delete spaces (not in the word)
                        if (vadesizIsim.isEmpty()) {
                            System.out.println("[ERROR]: Müşteri adı boş olamaz.");
                            continue;
                        } else {
                            break;
                        }
                    }
                    while(true) {
                        System.out.print("Hesap Numarası Girin (Örn: TR1001): ");
                        vadesizNo = scanner.nextLine().trim().toUpperCase();
                        if (vadesizNo.isEmpty()) {
                            System.out.println("[ERROR]: Hesap numarası boş olamaz.");
                            continue;
                        } else {
                            break;
                        }
                    }

                    CheckingAccount yeniVadesiz = new CheckingAccount(vadesizNo, vadesizIsim, 2000.0, new SmsNotification());
                    banka.addAccount(yeniVadesiz);
                    break;

                case 2:
                    String vadeliIsim = "";
                    String vadeliNo = "";
                    double faiz = 0.0;
                    int gun = 0;

                    while(true) {
                        System.out.print("Müşteri Adı Soyadı: ");
                        vadeliIsim = scanner.nextLine().trim();
                        if (vadeliIsim.isEmpty()) {
                            System.out.println("[ERROR]: Müşteri adı boş olamaz.");
                            continue;
                        } else {
                            break;
                        }
                    }
                    while(true) {
                        System.out.print("Hesap Numarası Girin (Örn: TR1001): ");
                        vadeliNo = scanner.nextLine().trim().toUpperCase();
                        if (vadeliNo.isEmpty()) {
                            System.out.println("[ERROR]: Hesap numarası boş olamaz.");
                            continue;
                        } else {
                            break;
                        }
                    }
                    while(true) {
                        System.out.print("Faiz Oranı (Örn: %45 için 0.45 girin): ");
                        java.lang.String faizGirdisi = scanner.nextLine().replace(",", ".").trim();
                            if (faizGirdisi.isEmpty()) {
                                System.out.println("[ERROR]: Faiz oranı boş bırakılamaz.");
                                continue;
                            }
                            try {
                                faiz = Double.parseDouble(faizGirdisi);

                                if (faiz < 0) {
                                    System.out.println("[ERROR]: Faiz oranı negatif bir değer olamaz.");
                                    continue;
                                }
                                break;
                            } catch (NumberFormatException e){ // Eğer Double.parseDouble("abc") gibi bir hata oluşursa program çökmez, buraya düşer:
                                System.out.println("[ERROR]: Lütfen geçerli bir sayısal faiz oranı girin (Örn: 0.45)!");
                            }
                    }
                    while(true){
                    System.out.print("Vade Gün Sayısı (Örn: 30): ");
                    java.lang.String gunGirdisi = scanner.nextLine().trim();
                        if(gunGirdisi.isEmpty()){
                            System.out.println("[ERROR]: Vade gün sayısı boş bırakılamaz.");
                            continue;
                        }
                        try {
                            gun = Integer.parseInt(gunGirdisi);
                            if (gun <= 0) {
                                System.out.println("[ERROR]: Vade gün sayısı 0 veya negatif bir değer olamaz.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("[ERROR]: Lütfen geçerli bir tam sayı girin (Örn: 30)!");
                        }
                    }
                    System.out.println("-> Hesap Açılış Tarihini Girmeniz Bekleniyor.");
                    LocalDate acilisTarihi = tarihAl(scanner);

                    SavingsAccount yeniVadeli = new SavingsAccount(vadeliNo, vadeliIsim, faiz, gun, acilisTarihi, new EmailNotification());
                    banka.addAccount(yeniVadeli);
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
                        // --- AKILLI BAKİYE VE FAİZ ÖNİZLEME ALANI ---
                        if (cekilecekHesap instanceof SavingsAccount) {
                            SavingsAccount vHesap = (SavingsAccount) cekilecekHesap;
                            double mevcutAnaPara = vHesap.getBalance();

                            System.out.println("[ÖNİZLEME]: Girilen tarih itibarıyla vade durumu kontrol ediliyor...");

                            if (islemTarihi.isBefore(vHesap.getInterestEndDate())) {
                                // Vade henüz dolmadıysa (Erken Çekim Önizlemesi)
                                System.out.printf("[UYARI]: Girilen tarih (%s), vade bitiş tarihinden (%s) önceden bir gündür.\n", islemTarihi, vHesap.getInterestEndDate());
                                System.out.printf("[BİLGİ]: Erken çekim yapacağınız için faiz hakkınız yanacaktır. Çekilebilir mevcut ana paranız: %,.2f TL\n", mevcutAnaPara);
                            } else {
                                // Vade dolduysa veya geçtiyse (Normal Faizli Çekim Önizlemesi)
                                double kazanilacakFaiz = mevcutAnaPara * vHesap.getInterestRate();
                                double faizliToplamBakiye = mevcutAnaPara + kazanilacakFaiz;

                                // Silinen mesaj satırını buraya ekledik:
                                System.out.printf("[BİLGİ]: %s tarihi itibarıyla vade dolmuş olacağı için tahmini faiz dahil toplam çekilebilir bakiyeniz: %,.2f TL (Kazanılacak Faiz: %,.2f TL)\n",
                                        islemTarihi, faizliToplamBakiye, kazanilacakFaiz);
                            }

                        } else {
                            System.out.printf("[BİLGİ]: Mevcut bakiyeniz: %,.2f TL (Günlük Limit: 2.000,00 TL)\n", cekilecekHesap.getBalance());
                        }

                        // Kullanıcı artık toplam faizli bakiyeyi görerek tam olarak çekmek istediği miktarı yazıyor
                        System.out.print("Çekmek istediğiniz tutar (TL cinsinden nokta veya virgül kullanmadan giriniz): ");
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
                    double toplamBankaBakiyesi = 0.0;

                    if (detayNo.equalsIgnoreCase("ALL")) {
                        System.out.println("\n=== BANKA GENEL DURUM RAPORU ===");
                        for (Account hesap : banka.getAllAccounts()) {
                            if(hesap instanceof SavingsAccount){
                                System.out.println("Hesap türü: Vadeli");
                            } else {
                                System.out.println("Hesap türü: Vadesiz");
                            }
                                System.out.println("Hesap No: " + hesap.getAccountNo());
                                System.out.println("Müşteri: " + hesap.getCustomerName());
                                System.out.println("Güncel Bakiye: " + hesap.getBalance() + " TL");
                                System.out.println("-------------------------");
                                toplamBankaBakiyesi += hesap.getBalance();
                        }
                        System.out.printf("BANKADAKİ TOPLAM HESAP SAYISI: %d\n", banka.getAllAccounts().size());
                        System.out.printf("TOPLAM BANKA BAKİYESİ: %,.2f TL\n", toplamBankaBakiyesi);
                        System.out.println("=================================");

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
        while (true) {
            System.out.print("İşlem Tarihi (Format: YYYY-MM-DD, Örn: 2026-06-06): ");
            String tarihYazisi = scanner.nextLine().trim();

            if (tarihYazisi.isEmpty()) {
                System.out.println("[ERROR]: Tarih alanı boş bırakılamaz!");
                continue;
            }
            try {
                // Eğer girdi "2026-06-06" formatına tam uyuyorsa başarıyla parse edilir
                // 'return' komutu çalıştığı anda metot o tarihi geri döndürür ve döngü de kendiliğinden biter
                return LocalDate.parse(tarihYazisi);

            } catch (Exception e) {
                System.out.println("[ERROR]: Geçersiz tarih formatı! Lütfen YYYY-MM-DD formatına uyun (Yıl-Ay-Gün).");
            }
        }
    }
}