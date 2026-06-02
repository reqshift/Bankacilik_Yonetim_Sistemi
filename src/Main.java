public class Main {
    public static void main(String[] args) {

        System.out.println("--- BANKACILIK SİSTEMİ TEST ---");
        Account myAccount = new Account("TR1001", "Ahmet Yılmaz");

        System.out.println("Hesap Sahibi: " + myAccount.getCustomerName());
        System.out.println("Başlangıç Bakiyesi: " + myAccount.getBalance() + " TL\n");

        System.out.println("[İŞLEM]: Hesaba 5000 TL yatırılıyor...");
        myAccount.deposit(5000.0);
        System.out.println("Güncel Bakiye: " + myAccount.getBalance() + " TL\n");

        System.out.println("[İŞLEM]: Hesaba -100 TL yatırılmaya çalışılıyor...");
        myAccount.deposit(-100.0);
        System.out.println("Güncel Bakiye: " + myAccount.getBalance() + " TL\n");

        System.out.println("[İŞLEM]: Hesaptan 2000 TL çekiliyor...");
        myAccount.withdraw(2000.0);
        System.out.println("Güncel Bakiye: " + myAccount.getBalance() + " TL\n");

        System.out.println("[İŞLEM]: Hesaptan 4000 TL çekilmeye çalışılıyor (Yetersiz Bakiye Testi)...");
        myAccount.withdraw(4000.0);
        System.out.println("Son Güncel Bakiye: " + myAccount.getBalance() + " TL");

        System.out.println("====== 2. AŞAMA:  Kalıtım (Inheritance) ve ÇOK BİÇİMLİLİK (POLYMORPHISM) TESTİ BAŞLADI ======\n");

        // 1. VADESİZ HESAP (CHECKING ACCOUNT) TESTİ
        System.out.println("--- Vadesiz Hesap Test Ediliyor ---");
        // Günlük çekim limiti 5000 TL olan bir vadesiz hesap açıyoruz
        CheckingAccount checking = new CheckingAccount("TR-CHECK-101", "Ahmet Yılmaz", 5000.0);

        System.out.println("[İŞLEM]: Hesaba 10.000 TL yatırılıyor...");
        checking.deposit(10000.0);
        System.out.println("Mevcut Bakiye: " + checking.getBalance() + " TL");

        System.out.println("[EYLEM]: Günlük limiti aşacak şekilde 6000 TL çekilmeye çalışılıyor...");
        checking.withdraw(6000.0);

        System.out.println("[İŞLEM]: Limit dahilinde 3000 TL çekiliyor...");
        checking.withdraw(3000.0);
        System.out.println("Vadesiz Hesap Son Bakiyesi: " + checking.getBalance() + " TL\n");

        // 2. VADELİ HESAP (SAVINGS ACCOUNT) TESTİ
        System.out.println("--- Vadeli Hesap Test Ediliyor ---");
        SavingsAccount savings = new SavingsAccount("TR-SAVE-202", "Ahmet Yılmaz", 0.05);

        System.out.println("[İŞLEM]: Hesaba 10.000 TL yatırılıyor...");
        savings.deposit(10000.0);
        System.out.println("Mevcut Bakiye: " + savings.getBalance() + " TL");

        System.out.println("[İŞLEM]: Vadeli hesaptan İLK KEZ 1000 TL çekiliyor (Erken Çekim)...");
        savings.withdraw(1000.0);
        System.out.println("İlk çekim sonrası güncel bakiye: " + savings.getBalance() + " TL\n");

        System.out.println("[İŞLEM]: Vadeli hesaptan İKİNCİ KEZ 500 TL çekiliyor...");
        savings.withdraw(500.0);
        System.out.println("Vadeli Hesap Son Bakiyesi: " + savings.getBalance() + " TL");

        System.out.println("\n====== TEST ÇALIŞTIRMASI BAŞARIYLA TAMAMLANDI ======");
    }
}