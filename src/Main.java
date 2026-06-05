import java.time.LocalDate;

public class Main {

    // =========================================================================
    // 1. ADIM: OTOMATİK HESAP ÜRETİM METOTLARI (FABRİKA MODELİ)
    // =========================================================================
    public static CheckingAccount createCheckingAccount(String accountNo, String customerName) {
        NotificationService sms = new SmsNotification();
        double defaultDailyLimit = 2000.0; // Standart günlük limit otomatik atanıyor
        return new CheckingAccount(accountNo, customerName, defaultDailyLimit, sms);
    }

    public static SavingsAccount createSavingsAccount(String accountNo, String customerName, LocalDate openDate) {
        NotificationService email = new EmailNotification();
        double defaultInterestRate = 0.05; // %5 faiz oranı otomatik atanıyor
        int defaultTermDays = 30;          // 30 gün vade otomatik atanıyor
        return new SavingsAccount(accountNo, customerName, defaultInterestRate, defaultTermDays, openDate, email);
    }

    /**
     * TEST 1: Para Yatırma (Deposit) ve Metot Aşırı Yükleme (Overloading) Kontrolü
     * Vadesiz hesabın tarihsiz, vadeli hesabın tarihli deposit çağrılarını sınar.
     */
    private static void runDepositTest(LocalDate testDate) {
        System.out.println("\n>>> TEST 1: Deposit & Method Overloading Control <<<");

        CheckingAccount checking = createCheckingAccount("VDS-123", "Hatice Nisa");
        SavingsAccount savings = createSavingsAccount("VDL-456", "Hatice Nisa", testDate);

        System.out.println("[ACTION]: Depositing 3000 TL into CheckingAccount (Pure Amount)...");
        checking.deposit(3000.0);

        System.out.println("\n[ACTION]: Depositing 10000 TL into SavingsAccount (Amount + Date)...");
        savings.deposit(10000.0, testDate);
    }

    /**
     * TEST 2: Günlük Para Çekme Limiti Kontrolü (Checking Account)
     * Aynı gün içinde günlük limiti aşan çekim denemelerini otomatik simüle eder.
     */
    private static void runDailyLimitTest(LocalDate testDate) {
        System.out.println("\n>>> TEST 2: Checking Account Daily Limit Control <<<");

        CheckingAccount checking = createCheckingAccount("VDS-789", "Hatice Nisa");
        checking.deposit(5000.0); // Test için bakiye yüklemesi

        System.out.println("[ACTION]: First withdrawal of 1500 TL (Within limit)...");
        checking.withdraw(1500.0, testDate);

        System.out.println("\n[ACTION]: Second withdrawal of 1000 TL (Should FAIL: 1500 + 1000 = 2500 > 2000 Limit)...");
        checking.withdraw(1000.0, testDate);

        System.out.println("\n[ACTION]: Withdrawal of 500 TL on the NEXT DAY (Should SUCCESS - Limit resets)...");
        checking.withdraw(500.0, testDate.plusDays(1));
    }

    /**
     * TEST 3: Kritik Faiz İstismarı ve Vade Öteleme Kontrolü (Savings Account - En Önemli Açık)
     * Vade gününde yetersiz bakiye ile çekim denendiğinde vadenin ötelenmesini ve
     * üst üste haksız faiz binişinin engellendiğini otomatik test eder.
     */
    private static void runInterestExploitTest(LocalDate openDate) {
        System.out.println("\n>>> TEST 3: Critical Interest Accrual Exploitation Control <<<");

        SavingsAccount savings = createSavingsAccount("VDL-555", "Hatice Nisa", openDate);
        savings.deposit(10000.0, openDate); // 10.000 TL anapara

        LocalDate dueDate = openDate.plusDays(30); // Vade bitiş günü (01.07.2026)

        // A Denemesi: Vade günü geldi, faiz eklenecek (10.000 * 0.05 = 500 TL). Toplam bakiye: 10.500 TL.
        // 60.000 TL çekmeye çalışıyoruz, bakiye yetersiz kalacak ve başarısız olacak.
        System.out.println("\n[ACTION]: Attempt 1 (Due Date): Withdrawing 60000 TL (Should FAIL due to balance, but update due date)...");
        savings.withdraw(60000.0, dueDate);

        // B Denemesi: Aynı gün sistemi manipüle etmek için tekrar çekim deneniyor.
        // Eğer bug başarıyla çözüldüyse, vade tarihi A adımında çoktan ertelendiği için bu kez TEKRAR faiz EKLEMEMELİ!
        System.out.println("\n[ACTION]: Attempt 2 (Same Day): Trying again (Should NOT apply interest again!)...");
        savings.withdraw(1000.0, dueDate);
    }

    /**
     * TEST 4: Erken Çekim Uyarısı Konumlandırma Kontrolü
     * Vade dolmadan bakiye yetersizliği yaşandığında erken çekim uyarısının maskelenmesini test eder.
     */
    private static void runEarlyWithdrawalWarningTest(LocalDate openDate) {
        System.out.println("\n>>> TEST 4: Early Withdrawal Warning Placement Control <<<");

        SavingsAccount savings = createSavingsAccount("VDL-999", "Mert", openDate);
        savings.deposit(5000.0, openDate);

        // Vade dolmadan (10 gün sonra), bakiyeden çok büyük para (25.000 TL) çekilmeye çalışılıyor.
        // Bakiye yetersiz olduğu için "Faiziniz yandı" uyarısı basılmamalı, direkt bakiye hatası vermeli.
        System.out.println("\n[ACTION]: Withdrawing 25000 TL before due date (Should fail quietly without burning interest message)...");
        savings.withdraw(25000.0, openDate.plusDays(10));
    }

    // =========================================================================
    // 3. ADIM: TETİKLEYİCİ ANA METOT
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("====== STARTING FULLY AUTOMATED SYSTEM TESTS ======");
        System.out.println("==================================================");

        // Testlerin başlangıç noktası olarak sanal bir tarih belirliyoruz
        LocalDate simulationStartDate = LocalDate.of(2026, 6, 1);

        // Tüm test senaryolarını sırayla ve bağımsızca çalıştırıyoruz
        runDepositTest(simulationStartDate);
        System.out.println("\n--------------------------------------------------");

        runDailyLimitTest(simulationStartDate);
        System.out.println("\n--------------------------------------------------");

        runInterestExploitTest(simulationStartDate);
        System.out.println("\n--------------------------------------------------");

        runEarlyWithdrawalWarningTest(simulationStartDate);

        System.out.println("\n==================================================");
        System.out.println("======    ALL SYSTEM TESTS COMPLETED WITH SUCCESS   ======");
        System.out.println("==================================================");
    }
}