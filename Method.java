import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;

public class Method {
    //从文件中读取数据
    public ArrayList<Account> loadData(ArrayList<Account> accounts) {
        try {
            FileReader fr = new FileReader("D:\\IDEAproject\\MoneyManager\\account.txt");
            BufferedReader br = new BufferedReader(fr);
            String str = null;
            String ss[] = new String[5];
            int i = 0;
            while ((str = br.readLine()) != null) {
                ss = str.split(",");
                accounts.add(new Account(ss[0], ss[1], ss[2], ss[3], ss[4]));
                i++;
            }
            br.close();
            fr.close();
        } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
    //更新Table上的数据
    public void updateTable(ArrayList<Account> a, JTable table) {
        Object[][] row = new Object[a.size()][5];
        for (int i = 0; i < a.size(); i++) {
             row[i][0] = a.get(i).getId();
             row[i][1] = a.get(i).getDate();
             row[i][2] = a.get(i).getType();
             row[i][3] = a.get(i).getContent();
             row[i][4] = a.get(i).getAmount();
             }
             table.setModel(new DefaultTableModel(row, new String[]{"编号", "日期", "类型", "内容", "金额"}));
    }

    public ArrayList<User> loadUser(ArrayList<User> users) {
        try {
            FileReader fr=new FileReader("D:\\IDEAproject\\MoneyManager\\pwd.txt");
            BufferedReader br=new BufferedReader(fr);
            while (true) {
                String str=br.readLine();
                if (str==null) break;
                String[] ss=str.split(",");
                users.add(new User(ss[0],ss[1]));
            }
            br.close();
            fr.close();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"读取失败", "警告", JOptionPane.ERROR_MESSAGE);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(ArrayList<User> a) {
        try {
            FileWriter fw = new FileWriter("D:\\IDEAproject\\MoneyManager\\pwd.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for (User user : a) {
                bw.write(user.getUserName() + "," + user.getPassword());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(ArrayList<Account> a) {
        try {
            FileWriter fw = new FileWriter("D:\\IDEAproject\\MoneyManager\\account.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for (Account account : a) {
                bw.write(account.getId() + "," + account.getDate() + "," + account.getType() + "," + account.getContent() + "," + account.getAmount());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(ArrayList<Account> a, String text) {
        for (Account account : a) {
            if (account.getId().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public double updateBalance(ArrayList<Account> a){
        double bal = 0;
        for (Account account : a) {
            if (account.getType().equals("IN")) {
                bal += Double.parseDouble(account.getAmount());
            } else {
                bal -= Double.parseDouble(account.getAmount());
            }
        }
        return bal;
    }

}
