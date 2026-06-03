import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        // Scanner nesnesini oluşturuyoruz
        Scanner scanner = new Scanner(System.in);

        // Java'ya nokta (.) kullanmasını tam burada söylüyoruz:
        scanner.useLocale(java.util.Locale.US);

        System.out.println("====== BANKACILIK SİSTEMİ CANLI TEST MERKEZİ ======");

        // ==========================================
        // SENARYO 1: VADESİZ HESAP (CHECKING ACCOUNT) TESTİ
        // ==========================================
        System.out.println("\n--- 1. ADIM: Vadesiz Hesap Oluşturma ---");
        System.out.print("Hesap Numarası Giriniz: ");
        String chNo = scanner.nextLine();
        System.out.print("Müşteri Adı Giriniz: ");
        String chName = scanner.nextLine();
        System.out.print("Günlük Para Çekme Limiti Giriniz (Örn: 5000): ");
        double dailyLimit = scanner.nextDouble();

        // Nesneyi Talha Bey'in istediği gibi konsol bilgileriyle dinamik oluşturuyoruz
        CheckingAccount checking = new CheckingAccount(chNo, chName, dailyLimit);
        System.out.println("[SİSTEM]: Vadesiz hesap başarıyla oluşturuldu.");

        // Para Yatırma Testi
        System.out.print("\nHesaba yatırmak istediğiniz tutar: ");
        double chDeposit = scanner.nextDouble();
        checking.deposit(chDeposit);

        // Limit ve Bakiye Testi (Arka arkaya para çekerek limiti zorlayalım)
        System.out.println("\n[TEST]: Şimdi günlük limiti ve bakiyeyi zorlayalım...");
        System.out.print("1. Para Çekme Tutarını Giriniz: ");
        double chWithdraw1 = scanner.nextDouble();
        checking.withdraw(chWithdraw1);

        System.out.print("2. Para Çekme Tutarını Giriniz (Limiti aşmayı deneyin): ");
        double chWithdraw2 = scanner.nextDouble();
        checking.withdraw(chWithdraw2);


        // ==========================================
        // SENARYO 2: VADELİ HESAP (SAVINGS ACCOUNT) TESTİ
        // ==========================================
        System.out.println("\n--- 2. ADIM: Vadeli Hesap Oluşturma ---");
        scanner.nextLine(); // Konsol tamponunu temizlemek için
        System.out.print("Hesap Numarası Giriniz: ");
        String savNo = scanner.nextLine();
        System.out.print("Müşteri Adı Giriniz: ");
        String savName = scanner.nextLine();
        System.out.print("Faiz Oranı Giriniz (Örn: 0.10 -> %10): ");
        double interestRate = scanner.nextDouble();
        System.out.print("Vade Gün Sayısı Giriniz (Örn: 30): ");
        int termDays = scanner.nextInt();

        SavingsAccount savings = new SavingsAccount(savNo, savName, interestRate, termDays);
        System.out.println("[SİSTEM]: Vadeli hesap başarıyla oluşturuldu.");

        // Para Yatırma Testi (Vadeyi başlatır)
        System.out.print("\nHesaba yatırmak istediğiniz vadeli tutar: ");
        double savDeposit = scanner.nextDouble();
        savings.deposit(savDeposit);

        // ERKEN ÇEKİM TESTİ (Bugün çekmeye çalışıyoruz, vade dolmadığı için faiz yanmalı)
        System.out.println("\n[TEST]: Vade dolmadan (Erken Çekim) senaryosunu test ediyoruz...");
        System.out.print("Çekmek istediğiniz tutar (Mevcut bakiyeden az girin): ");
        double savWithdrawEarly = scanner.nextDouble();
        savings.withdraw(savWithdrawEarly);

        // BÜYÜK SINAV: VADE DOLDUĞUNDA FAİZ EKLEME TESTİ
        System.out.println("\n[TEST]: Şimdi sistemdeki tarihi 'vade sonrasına' simüle edelim.");
        System.out.println("Bunun için kodunuzdaki 'today' değişkenini test amaçlı geçmişe veya interestEndDate'i bugüne çekebilirsiniz.");
        System.out.println("Mevcut kodunuza göre tarih kontrolü çalışacak ve duruma göre faiz işletilecektir.");

        System.out.print("\nVade sonu için çekmek istediğiniz tutar: ");
        double savWithdrawLate = scanner.nextDouble();
        savings.withdraw(savWithdrawLate);

        System.out.println("\n====== TEST SENARYOLARI TAMAMLANDI ======");
        scanner.close();
    }
}