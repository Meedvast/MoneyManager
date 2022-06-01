import java.util.ArrayList;

public class Monitor extends Thread{
    double bal = 0;
    public void run() {
        while (true) {
            try {
                Method m = new Method();
                ArrayList<Account> accounts = new ArrayList<>();
                accounts = m.loadData(accounts);
                for (Account account:accounts) {
                    if (account.getType().equals("IN")) {
                        bal += Double.parseDouble(account.getAmount());
                    } else {
                        bal -= Double.parseDouble(account.getAmount());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
