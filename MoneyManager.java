import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MoneyManager {
    public static void main(String[] args) {
        LoginFrame lf=new LoginFrame();
        lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
//登录界面
class LoginFrame extends JFrame implements ActionListener{
    private JLabel l_user,l_pwd; //用户名标签，密码标签
    private JTextField t_user;//用户名文本框
    private JPasswordField t_pwd; //密码文本框
    private JButton b_ok,b_cancel; //登录按钮，退出按钮

    public LoginFrame(){
        super("欢迎使用个人理财账本!");
        l_user=new JLabel("用户名：",JLabel.RIGHT);
        l_pwd=new JLabel("    密码：",JLabel.RIGHT);
        t_user=new JTextField(31);
        t_pwd=new JPasswordField(31);
        b_ok=new JButton("登录");
        b_cancel=new JButton("退出");
        //布局方式FlowLayout，一行排满排下一行
        Container c=this.getContentPane();
        c.setLayout(new FlowLayout());
        c.add(l_user);
        c.add(t_user);
        c.add(l_pwd);
        c.add(t_pwd);
        c.add(b_ok);
        c.add(b_cancel);
        //为按钮添加监听事件
        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        //界面大小不可调整 
        this.setResizable(false);
        this.setSize(455,150);

        //界面显示居中
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if(b_cancel==e.getSource()){
            this.dispose();
        }else if(b_ok==e.getSource()){
            Method m = new Method();
            int i=0;
            ArrayList<User> users = new ArrayList<User>();
            users = m.loadUser(users);
            for (User user : users) {
                if (user.getUserName().equals(t_user.getText())&&user.getPassword().equals(t_pwd.getText())) {
                    new MainFrame(t_user.getText().trim());
                    this.dispose();
                    i=1;
                    break;
                }
            }
            if(i!=1){
                JOptionPane.showMessageDialog(null,"用户名或密码错误！","提示",JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}



//JOptionPane.showMessageDialog(null,"用户名密码出错", "警告", //JOptionPane.ERROR_MESSAGE);


//主界面
class MainFrame extends JFrame implements ActionListener {
    private JMenuBar mb = new JMenuBar();
    private JMenu m_system = new JMenu("系统管理");
    private JMenu m_fm = new JMenu("收支管理");
    private JMenuItem mI[] = {new JMenuItem("密码重置"),new JMenuItem("添加账户") ,new JMenuItem("退出系统")};
    private JMenuItem m_FMEdit = new JMenuItem("收支编辑");
    private JLabel l_type;
    private JLabel l_fromdate;
    private JLabel l_todate;
    private static JLabel l_bal;
    private JLabel l_ps;
    private JTextField t_fromdate, t_todate;
    private JButton b_select1, b_select2;
    private JComboBox<String> c_type;
    private JPanel p_condition, p_detail;
    private String s1[] = {"收入", "支出", "全部"};
    private double bal1, bal2;
    private static JTable table;
    private String username;

    public MainFrame(String username) {
        super(username + ",欢迎使用个人理财账本!");
        this.username = username;
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(mb, "North");
        mb.add(m_system);
        mb.add(m_fm);
        m_system.add(mI[0]);
        m_system.add(mI[1]);
        m_system.add(mI[2]);
        m_fm.add(m_FMEdit);
        m_FMEdit.addActionListener(this);
        mI[0].addActionListener(this);
        mI[1].addActionListener(this);
        mI[2].addActionListener(this);

        l_type = new JLabel("收支类型：");
        c_type = new JComboBox<>(s1);
        b_select1 = new JButton("查询");
        l_fromdate = new JLabel("起始时间");
        t_fromdate = new JTextField(8);
        l_todate = new JLabel("终止时间");
        t_todate = new JTextField(8);
        b_select2 = new JButton("查询");
        l_ps = new JLabel("注意：时间格式为YYYYMMDD，例如：20150901");
        p_condition = new JPanel();
        p_condition.setLayout(new GridLayout(3, 1));
        p_condition.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("输入查询条件"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        p1.add(l_type);
        p1.add(c_type);
        p1.add(b_select1);
        p2.add(l_fromdate);
        p2.add(t_fromdate);
        p2.add(l_todate);
        p2.add(t_todate);
        p2.add(b_select2);
        p3.add(l_ps);
        p_condition.add(p1);
        p_condition.add(p2);
        p_condition.add(p3);
        c.add(p_condition, "Center");

        b_select1.addActionListener(this);
        b_select2.addActionListener(this);

        p_detail = new JPanel();
        p_detail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("收支明细信息"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        l_bal = new JLabel();
        String[] cloum = {"编号", "日期", "类型", "内容", "金额",};
        Object[][] row = new Object[50][5];

        int i = 0;
        try {
            FileReader fr = new FileReader("D:\\IDEAproject\\MoneyManager\\account.txt");
            BufferedReader br = new BufferedReader(fr);
            String str = null;
            String ss[] = new String[5];
            i = 0;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                ss = str.split(",");
                row[i][0] = ss[0];
                row[i][1] = ss[1];
                row[i][2] = ss[2];
                row[i][3] = ss[3];
                row[i][4] = ss[4];
                i++;
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "账本文件不存在", "警告", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "文件出错", "警告", JOptionPane.ERROR_MESSAGE);
        }

        table = new JTable(row, cloum);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(580, 350));
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpane.setViewportView(table);
        p_detail.add(l_bal);
        p_detail.add(scrollpane);
        c.add(p_detail, "South");
        for (int j = 0; j < i; j++) {
            if (row[j][2].equals("IN")) {
                bal1 += Double.parseDouble((String) row[j][4]);
            } else {
                bal1 -= Double.parseDouble((String) row[j][4]);
            }
        }
        if (bal1 < 0)
            l_bal.setText("个人总收支余额为" + bal1 + "元。您已超支，请适度消费！");
        else
            l_bal.setText("个人总收支余额为" + bal1 + "元。");

        this.setResizable(false);
        this.setSize(600, 580);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width - this.getSize().width) / 2, (screen.height - this.getSize().height) / 2);
        this.show();
    }

    public static void updateFrame(ArrayList<Account> a) {
        int i = 0;
        Object[][] row = new Object[50][5];
        double bal = 0;
        for (Account account : a) {
            row[i][0] = account.getId();
            row[i][1] = account.getDate();
            row[i][2] = account.getType();
            row[i][3] = account.getContent();
            row[i][4] = account.getAmount();
            i++;
            if (account.getType().equals("IN")) {
                bal += Double.parseDouble(account.getAmount());
            } else {
                bal -= Double.parseDouble(account.getAmount());
            }
        }
        table.setModel(new DefaultTableModel(row, new String[]{"编号", "日期", "类型", "内容", "金额"}));
        if (bal < 0)
            l_bal.setText("个人总收支余额为" + bal + "元。您已超支，请适度消费！");
        else
            l_bal.setText("个人总收支余额为" + bal + "元。");
    }
    public void actionPerformed(ActionEvent e) {
        Object temp = e.getSource();
        if (temp == mI[0]) {
            new ModifyPwdFrame(username);
        } else if (temp == mI[1]) {
            //new AddFrame(username);
            new AddFrame(username);
        } else if (temp == mI[2]) {
            //退出系统
            System.exit(0);
        } else if (temp == m_FMEdit) {
            new BalEditFrame();
        } else if (temp == b_select1) {  //根据收支类型查询
            ArrayList<Account> a = new ArrayList<Account>();
            ArrayList<Account> aIN = new ArrayList<Account>();
            ArrayList<Account> aOUT = new ArrayList<Account>();
            Method m = new Method();
            a = m.loadData(a);
            for (Account account : a) {
                if (account.getType().equals("IN")||account.getType().equals("收入")) {
                    aIN.add(account);
                } else if (account.getType().equals("out")||account.getType().equals("支出")) {
                    aOUT.add(account);
                }
            }
            if (c_type.getSelectedItem().toString().equals("收入")) {
                m.updateTable(aIN, table);
            } else if(c_type.getSelectedItem().toString().equals("支出")){
                m.updateTable(aOUT, table);
            } else if (c_type.getSelectedItem().toString().equals("全部")) {
                m.updateTable(a, table);
            }
        } else if (temp == b_select2) {   //根据时间范围查询
            ArrayList<Account> a = new ArrayList<Account>();
            String fromdate = t_fromdate.getText();
            String todate = t_todate.getText();
            Method m = new Method();
            a = m.loadData(a);
            ArrayList<Account> aTime = new ArrayList<Account>();
            for (Account account : a) {
                if (account.getDate().compareTo(fromdate) >= 0 && account.getDate().compareTo(todate) <= 0) {
                    aTime.add(account);
                }
            }
            m.updateTable(aTime, table);
        }
    }
}
//修改密码界面
class ModifyPwdFrame extends JFrame implements ActionListener {
    private JLabel l_oldPWD, l_newPWD, l_newPWDAgain;
    private JPasswordField t_oldPWD, t_newPWD, t_newPWDAgain;
    private JButton b_ok, b_cancel;
    private String username;

    public ModifyPwdFrame(String username) {
        super("修改密码");
        this.username = username;
        l_oldPWD = new JLabel("        旧密码：");
        l_newPWD = new JLabel("        新密码：");
        l_newPWDAgain = new JLabel("确认新密码：");
        t_oldPWD = new JPasswordField(15);
        t_newPWD = new JPasswordField(15);
        t_newPWDAgain = new JPasswordField(15);
        b_ok = new JButton("确定");
        b_cancel = new JButton("取消");
        Container c = this.getContentPane();
        c.setLayout(new FlowLayout());
        c.add(l_oldPWD);
        c.add(t_oldPWD);
        c.add(l_newPWD);
        c.add(t_newPWD);
        c.add(l_newPWDAgain);
        c.add(t_newPWDAgain);
        c.add(b_ok);
        c.add(b_cancel);
        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        this.setResizable(false);
        this.setSize(280, 160);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width - this.getSize().width) / 2, (screen.height - this.getSize().height) / 2);
        this.show();
    }

    public void actionPerformed(ActionEvent e) {
        if (b_cancel == e.getSource()) {
            //关闭界面
            this.dispose();
        } else if (b_ok == e.getSource()) {
            ArrayList<User> u = new ArrayList<User>();
            Method m = new Method();
            u = m.loadUser(u);
            for (User user:u){
                if(user.getUserName().equals(username)){
                    if (t_oldPWD.getText().equals(user.getPassword())) {
                        if (t_newPWD.getText().equals(t_newPWDAgain.getText())) {
                            if (t_newPWD.getText().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")){
                                u.get(0).setPassword(t_newPWD.getText());
                                m.updateUser(u);
                                JOptionPane.showMessageDialog(null, "密码修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                                break;
                            }else{
                                JOptionPane.showMessageDialog(null, "密码必须为6-16位数字和字母的组合！", "提示", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "两次输入的新密码不一致！", "警告", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "旧密码错误！", "警告", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
    }
}
//收支编辑界面
class BalEditFrame extends JFrame implements ActionListener{
    private JLabel l_id,l_date,l_bal,l_type,l_item;
    private JTextField t_id,t_date,t_bal;
    private JComboBox<String> c_type;
    private JComboBox<String> c_item;
    private JButton b_update,b_delete,b_select,b_new,b_clear;
    private JPanel p1,p2,p3;
    private JScrollPane scrollpane;
    private JTable table;

    public BalEditFrame(){
        super("收支编辑" );
        l_id=new JLabel("编号：");
        l_date=new JLabel("日期：");
        l_bal=new JLabel("金额：");
        l_type=new JLabel("类型：");
        l_item=new JLabel("内容：");
        t_id=new JTextField(8);
        t_id.setEditable(false);
        t_date=new JTextField(8);
        t_bal=new JTextField(8);

        String s1[]={"收入","支出"};
        String s2[]={"购物","餐饮","居家","交通","娱乐","人情","工资","奖金","其他"};
        c_type=new JComboBox<>(s1);
        c_item=new JComboBox<>(s2);

        b_select=new JButton("查询");
        b_update=new JButton("修改");
        b_delete=new JButton("删除");
        b_new=new JButton("录入");
        b_clear=new JButton("清空");

        Container c=this.getContentPane();
        c.setLayout(new BorderLayout());

        p1=new JPanel();
        p1.setLayout(new GridLayout(5,2,10,10));
        p1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("编辑收支信息"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        p1.add(l_id);
        p1.add(t_id);
        p1.add(l_date);
        p1.add(t_date);
        p1.add(l_type);
        p1.add(c_type);
        p1.add(l_item);
        p1.add(c_item);
        p1.add(l_bal);
        p1.add(t_bal);
        c.add(p1, BorderLayout.WEST);

        p2=new JPanel();
        p2.setLayout(new GridLayout(5,1,10,10));
        p2.add(b_new);
        p2.add(b_update);
        p2.add(b_delete);
        p2.add(b_select);
        p2.add(b_clear);

        c.add(p2,BorderLayout.CENTER);

        p3=new JPanel();
        p3.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("显示收支信息"),
                BorderFactory.createEmptyBorder(5,5,5,5)));

        String[] cloum = { "编号", "日期", "类型","内容", "金额"};
        Object[][] row = new Object[50][5];
        table = new JTable(row, cloum);
        scrollpane = new JScrollPane(table);
        scrollpane.setViewportView(table);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        p3.add(scrollpane);
        c.add(p3,BorderLayout.EAST);

        b_update.addActionListener(this);
        b_delete.addActionListener(this);
        b_select.addActionListener(this);
        b_new.addActionListener(this);
        b_clear.addActionListener(this);

        //添加代码，为table添加鼠标点击事件监听addMouseListener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                t_id.setText(table.getValueAt(row, 0).toString());
                t_date.setText(table.getValueAt(row, 1).toString());
                c_type.setSelectedItem(table.getValueAt(row, 2).toString());
                c_item.setSelectedItem(table.getValueAt(row, 3).toString());
                t_bal.setText(table.getValueAt(row, 4).toString());
            }
        });

        this.setResizable(false);
        this.setSize(800,300);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width-this.getSize().width)/2,(screen.height-this.getSize().height)/2);
        this.show();
        Method m=new Method();
        ArrayList<Account> a=new ArrayList<Account>();
        a=m.loadData(a);
        m.updateTable(a,table);
    }
    public void actionPerformed(ActionEvent e) {
        if(b_select==e.getSource()){  //查询所有符合条件的收支信息
                Method m=new Method();
                ArrayList<Account> a=new ArrayList<Account>();
                a=m.loadData(a);
                m.updateTable(a,table);
        }else if(b_update==e.getSource()){  // 修改某条收支信息
            try {
                Method m=new Method();
                String c_Type;
                ArrayList<Account> a=new ArrayList<Account>();
                a=m.loadData(a);
                m.updateTable(a, table);
                int id=Integer.parseInt(t_id.getText());
                if (c_type.getSelectedItem().equals("收入")) {
                    c_Type="IN";
                } else {
                    c_Type="out";
                }
                a.remove(id-1);
                a.add(id-1,new Account(t_id.getText(),t_date.getText(),c_Type,c_item.getSelectedItem().toString(),t_bal.getText()));
                m.saveData(a);
                JOptionPane.showMessageDialog(null, "修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                m.updateTable(a, table);
                MainFrame.updateFrame(a);
            }catch (NumberFormatException e1){
                JOptionPane.showMessageDialog(null, "请输入正确的编号！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e2){
                JOptionPane.showMessageDialog(null, "修改失败！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(b_delete==e.getSource()){   //删除某条收支信息
            try{
                Method m=new Method();
                ArrayList<Account> a=new ArrayList<Account>();
                a=m.loadData(a);
                m.updateTable(a, table);
                //遍历到指定id
                for (Account account : a) {
                    if (account.getId().equals(t_id.getText())) {
                        a.remove(account);
                        break;
                    }
                }
                m.saveData(a);
                JOptionPane.showMessageDialog(null, "删除成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                m.updateTable(a, table);
                MainFrame.updateFrame(a);
            }catch (NumberFormatException e1){
                t_id.setText("");
                JOptionPane.showMessageDialog(null, "请输入正确的编号！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException e1){
                e1.printStackTrace();
            }catch (Exception e2){
                JOptionPane.showMessageDialog(null, "删除失败！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(b_new==e.getSource()){   //新增某条收支信息
            try{
                Method m=new Method();
                String c_Type;
                ArrayList<Account> a=new ArrayList<Account>();
                a=m.loadData(a);
                m.updateTable(a, table);
                SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd");
                //日期为空自动填写当前日期
                if (t_date.getText().equals("")){
                    t_date.setText(df.format(new Date()));
                }
                //金额必须为数字，可以为小数
                if (!t_bal.getText().matches("^-?([1-9]\\d*|0)(\\.\\d+)?$")||t_bal.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入正确的数字！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    throw new Exception();
                }
                if (c_type.getSelectedItem().equals("收入")) {
                    c_Type="IN";
                } else {
                    c_Type="out";
                }
                a.add(new Account(Integer.toString(Integer.parseInt(a.get(a.size()-1).getId())+1),t_date.getText(),c_Type,c_item.getSelectedItem().toString(),t_bal.getText()));
                m.saveData(a);
                JOptionPane.showMessageDialog(null, "添加成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                m.updateTable(a, table);
                MainFrame.updateFrame(a);
            }catch (NumberFormatException e1){
                JOptionPane.showMessageDialog(null, "请输入正确的编号！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e2){
                JOptionPane.showMessageDialog(null, "添加失败！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(b_clear==e.getSource()){   //清空输入框
            t_bal.setText("");
            t_date.setText("");
            t_id.setText("");
        }
    }
}

class AddFrame extends JFrame implements ActionListener {
    private JLabel l_userName, l_newPWD, l_newPWDAgain;
    private JPasswordField t_newPWD, t_newPWDAgain;
    private JTextField t_userName;
    private JButton b_ok, b_cancel;
    private String username;

    public AddFrame(String username) {
        super("添加用户");
        this.username = username;
        l_userName = new JLabel("    用户名：");
        l_newPWD = new JLabel("        密码：");
        l_newPWDAgain = new JLabel("确认密码：");
        t_userName = new JTextField(15);
        t_newPWD = new JPasswordField(15);
        t_newPWDAgain = new JPasswordField(15);
        b_ok = new JButton("确定");
        b_cancel = new JButton("取消");
        Container c = this.getContentPane();
        c.setLayout(new FlowLayout());
        c.add(l_userName);
        c.add(t_userName);
        c.add(l_newPWD);
        c.add(t_newPWD);
        c.add(l_newPWDAgain);
        c.add(t_newPWDAgain);
        c.add(b_ok);
        c.add(b_cancel);
        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        this.setResizable(false);
        this.setSize(280, 160);
        Dimension screen = this.getToolkit().getScreenSize();
        this.setLocation((screen.width - this.getSize().width) / 2, (screen.height - this.getSize().height) / 2);
        this.show();
    }

    public void actionPerformed(ActionEvent e) {
        if (b_cancel == e.getSource()) {
            //关闭界面
            this.dispose();
        } else if (b_ok == e.getSource()) {
            //添加用户
            try {
                Method m = new Method();
                ArrayList<User> a = new ArrayList<User>();
                a = m.loadUser(a);
                if (t_userName.getText().equals("") || t_newPWD.getText().equals("") || t_newPWDAgain.getText().equals("")) {
                    throw new NumberFormatException();
                }
                if (!t_newPWD.getText().equals(t_newPWDAgain.getText())) {
                    throw new Exception();
                }
                for (User user : a) {
                    if (user.getUserName().equals(t_userName.getText())) {
                        throw new Exception();
                    }
                }
                a.add(new User(t_userName.getText(), t_newPWD.getText()));
                m.updateUser(a);
                JOptionPane.showMessageDialog(null, "添加成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "请输入正确的用户名和密码！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, "两次密码不一致！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}