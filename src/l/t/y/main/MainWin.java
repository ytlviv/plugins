package l.t.y.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import l.t.y.bean.LoginUserInfo;
import l.t.y.bean.TicketSearch;
import l.t.y.bean.UserInfo;
import l.t.y.helper.DateHelper;
import l.t.y.helper.HttpHeader;
import l.t.y.helper.ResManager;
import l.t.y.helper.StringHelper;
import l.t.y.helper.ToolHelper;
import l.t.y.thread.LoginThread;
import l.t.y.thread.TicketThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWin extends JFrame{
	 private static final long serialVersionUID = 4187140706289820520L;
	  private static final Logger logger = LoggerFactory.getLogger(MainWin.class);
	  private JFrame frame;
	  public JTextField username;
	  public JPasswordField password;
	  public JLabel code;
	  private JButton loginBtn;
	  public JTextField authcode;
	  private JTextField linkman1_name;
	  private JTextField linkman1_cardNo;
	  private JTextField linkman1_mobile;
	  private JTextField linkman2_name;
	  private JTextField linkman2_cardNo;
	  private JTextField linkman2_mobile;
	  private JTextField linkman3_name;
	  private JTextField linkman3_cardNo;
	  private JTextField linkman3_mobile;
	  private JTextField linkman4_name;
	  private JTextField linkman4_cardNo;
	  private JTextField linkman4_mobile;
	  private JTextField linkman5_name;
	  private JTextField linkman5_cardNo;
	  private JTextField linkman5_mobile;
	  public JCheckBox boxkTwoSeat;
	  public JCheckBox hardSleePer;
	  private JFormattedTextField txtStartDate;
	  private JTextField formCode;
	  private JTextField toCode;
	  public JButton startButton;
	  public JTextArea messageOut;
	  public List<UserInfo> userInfoList = null;
	  private TicketSearch req;
	  public static String path = System.getProperty("user.dir") + File.separator;

	  public MainWin mainWin = this;
	  public String loginUrl;
	  public String submitUrl;
	  public boolean isLogin = false;

	  public boolean isRunThread = false;

	  public boolean isStopRun = false;

	  public ClientCore client = new ClientCore();

	  public void initLayout()
	  {
	    this.frame = new JFrame(ResManager.getString("RobotTicket.main.msg"));
	    ImageIcon ico = ResManager.createImageIcon("logo.jpg");
	    this.frame.setIconImage(ico.getImage());
	    this.frame.setBounds(50, 50, 670, 640);
	    this.frame.setResizable(false);
	    this.frame.setDefaultCloseOperation(3);
	    this.frame.setLocationRelativeTo(null);
	    ToolTipManager.sharedInstance().setInitialDelay(0);
	    this.frame.getContentPane().setLayout(null);

	    this.frame.addWindowListener(new WindowAdapter()
	    {
	      public void windowClosing(WindowEvent e) {
	        try {
	          ToolHelper.getUserInfo(MainWin.path, "data" + File.separator + "UI.dat", new Object[] { MainWin.this.username, MainWin.this.password, MainWin.this.linkman1_name, MainWin.this.linkman1_cardNo, MainWin.this.linkman1_mobile, MainWin.this.linkman2_name, MainWin.this.linkman2_cardNo, MainWin.this.linkman2_mobile, MainWin.this.linkman3_name, MainWin.this.linkman3_cardNo, MainWin.this.linkman3_mobile, MainWin.this.linkman4_name, MainWin.this.linkman4_cardNo, MainWin.this.linkman4_mobile, MainWin.this.linkman5_name, MainWin.this.linkman5_cardNo, MainWin.this.linkman5_mobile, MainWin.this.formCode, MainWin.this.toCode });
	        }
	        catch (Exception ex)
	        {
	          MainWin.logger.error("save user data file Failure!", ex);
	        }
	      }
	    });
	    JPanel panel_o = new JPanel();
	    panel_o.setBounds(10, 12, 650, 54);
	    this.frame.getContentPane().add(panel_o);
	    panel_o.setLayout(null);
	    panel_o.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.userinfo")));

	    JLabel label_o = new JLabel(ResManager.getString("RobotTicket.label.user_name"));
	    label_o.setBounds(10, 26, 40, 15);
	    panel_o.add(label_o);
	    label_o.setHorizontalAlignment(4);

	    this.username = new JTextField();
	    this.username.setName("username");
	    this.username.setToolTipText(ResManager.getString("RobotTicket.label.user_name"));
	    this.username.setBounds(60, 23, 100, 21);
	    panel_o.add(this.username);
	    this.username.setColumns(10);

	    JLabel label_o1 = new JLabel(ResManager.getString("RobotTicket.label.password"));
	    label_o1.setBounds(170, 26, 40, 15);
	    panel_o.add(label_o1);
	    label_o1.setHorizontalAlignment(4);

	    this.password = new JPasswordField();
	    this.password.setName("password");
	    this.password.setToolTipText(ResManager.getString("RobotTicket.label.password"));
	    this.password.setBounds(220, 23, 100, 21);
	    panel_o.add(this.password);
	    this.password.setColumns(10);

	    this.code = new JLabel();
	    this.code.setBounds(341, 18, 78, 28);
	    this.code.setToolTipText("点我刷新验证码！");
	    panel_o.add(this.code);
	    this.code.setHorizontalAlignment(4);

	    this.code.addMouseListener(new MouseAdapter() {
	      public void mouseClicked(MouseEvent e) {
	        MainWin.this.initLoginImage();
	      }
	    });
	    this.authcode = new JTextField();
	    this.authcode.setToolTipText(ResManager.getString("RobotTicket.label.codename"));
	    this.authcode.setBounds(440, 23, 40, 21);
	    panel_o.add(this.authcode);
	    this.authcode.setColumns(10);

	    this.loginBtn = new JButton();
	    this.loginBtn.setText(ResManager.getString("RobotTicket.btn.login"));
	    this.loginBtn.setBounds(560, 18, 65, 28);
	    panel_o.add(this.loginBtn);
	    this.loginBtn.addActionListener(new LoginBtn());

	    JPanel panel2 = new JPanel();
	    panel2.setBounds(10, 76, 650, 315);
	    this.frame.getContentPane().add(panel2);
	    panel2.setLayout(null);
	    panel2.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkmaninfo")));

	    JPanel panel3 = new JPanel();
	    panel3.setBounds(20, 20, 610, 54);
	    panel2.add(panel3);
	    panel3.setLayout(null);
	    panel3.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkman1")));

	    JLabel label_2 = new JLabel(ResManager.getString("RobotTicket.label.username"));
	    label_2.setBounds(55, 26, 30, 15);
	    panel3.add(label_2);
	    label_2.setHorizontalAlignment(4);

	    this.linkman1_name = new JTextField();
	    this.linkman1_name.setName("linkman1_name");
	    this.linkman1_name.setToolTipText(ResManager.getString("RobotTicket.label.username"));
	    this.linkman1_name.setBounds(95, 23, 40, 21);
	    panel3.add(this.linkman1_name);
	    this.linkman1_name.setColumns(10);

	    JLabel label_3 = new JLabel(ResManager.getString("RobotTicket.label.cardno"));
	    label_3.setBounds(155, 26, 50, 15);
	    panel3.add(label_3);
	    label_3.setHorizontalAlignment(4);

	    this.linkman1_cardNo = new JTextField();
	    this.linkman1_cardNo.setName("linkman1_cardNo");
	    this.linkman1_cardNo.setToolTipText(ResManager.getString("RobotTicket.label.cardno"));
	    this.linkman1_cardNo.setBounds(215, 23, 150, 21);
	    panel3.add(this.linkman1_cardNo);
	    this.linkman1_cardNo.setColumns(10);

	    JLabel label_4 = new JLabel(ResManager.getString("RobotTicket.label.mobilephone"));
	    label_4.setBounds(375, 26, 40, 15);
	    panel3.add(label_4);
	    label_4.setHorizontalAlignment(4);

	    this.linkman1_mobile = new JTextField();
	    this.linkman1_mobile.setName("linkman1_mobile");
	    this.linkman1_mobile.setToolTipText(ResManager.getString("RobotTicket.label.mobilephone"));
	    this.linkman1_mobile.setBounds(425, 23, 100, 21);
	    panel3.add(this.linkman1_mobile);
	    this.linkman1_mobile.setColumns(10);

	    JPanel panel4 = new JPanel();
	    panel4.setBounds(20, 80, 610, 54);
	    panel2.add(panel4);
	    panel4.setLayout(null);
	    panel4.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkman2")));

	    JLabel label_5 = new JLabel(ResManager.getString("RobotTicket.label.username"));
	    label_5.setBounds(55, 26, 30, 15);
	    panel4.add(label_5);
	    label_5.setHorizontalAlignment(4);

	    this.linkman2_name = new JTextField();
	    this.linkman2_name.setName("linkman2_name");
	    this.linkman2_name.setToolTipText(ResManager.getString("RobotTicket.label.username"));
	    this.linkman2_name.setBounds(95, 23, 40, 21);
	    panel4.add(this.linkman2_name);
	    this.linkman2_name.setColumns(10);

	    JLabel label_6 = new JLabel(ResManager.getString("RobotTicket.label.cardno"));
	    label_6.setBounds(155, 26, 50, 15);
	    panel4.add(label_6);
	    label_6.setHorizontalAlignment(4);

	    this.linkman2_cardNo = new JTextField();
	    this.linkman2_cardNo.setName("linkman2_cardNo");
	    this.linkman2_cardNo.setToolTipText(ResManager.getString("RobotTicket.label.cardno"));
	    this.linkman2_cardNo.setBounds(215, 23, 150, 21);
	    panel4.add(this.linkman2_cardNo);
	    this.linkman2_cardNo.setColumns(10);

	    JLabel label_7 = new JLabel(ResManager.getString("RobotTicket.label.mobilephone"));
	    label_7.setBounds(375, 26, 40, 15);
	    panel4.add(label_7);
	    label_7.setHorizontalAlignment(4);

	    this.linkman2_mobile = new JTextField();
	    this.linkman2_mobile.setName("linkman2_mobile");
	    this.linkman2_mobile.setToolTipText(ResManager.getString("RobotTicket.label.mobilephone"));
	    this.linkman2_mobile.setBounds(425, 23, 100, 21);
	    panel4.add(this.linkman2_mobile);
	    this.linkman2_mobile.setColumns(10);

	    JPanel panel5 = new JPanel();
	    panel5.setBounds(20, 140, 610, 54);
	    panel2.add(panel5);
	    panel5.setLayout(null);
	    panel5.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkman3")));

	    JLabel label_8 = new JLabel(ResManager.getString("RobotTicket.label.username"));
	    label_8.setBounds(55, 26, 30, 15);
	    panel5.add(label_8);
	    label_8.setHorizontalAlignment(4);

	    this.linkman3_name = new JTextField();
	    this.linkman3_name.setName("linkman3_name");
	    this.linkman3_name.setToolTipText(ResManager.getString("RobotTicket.label.username"));
	    this.linkman3_name.setBounds(95, 23, 40, 21);
	    panel5.add(this.linkman3_name);
	    this.linkman3_name.setColumns(10);

	    JLabel label_9 = new JLabel(ResManager.getString("RobotTicket.label.cardno"));
	    label_9.setBounds(155, 26, 50, 15);
	    panel5.add(label_9);
	    label_9.setHorizontalAlignment(4);

	    this.linkman3_cardNo = new JTextField();
	    this.linkman3_cardNo.setName("linkman3_cardNo");
	    this.linkman3_cardNo.setToolTipText(ResManager.getString("RobotTicket.label.cardno"));
	    this.linkman3_cardNo.setBounds(215, 23, 150, 21);
	    panel5.add(this.linkman3_cardNo);
	    this.linkman3_cardNo.setColumns(10);

	    JLabel label_10 = new JLabel(ResManager.getString("RobotTicket.label.mobilephone"));
	    label_10.setBounds(375, 26, 40, 15);
	    panel5.add(label_10);
	    label_10.setHorizontalAlignment(4);

	    this.linkman3_mobile = new JTextField();
	    this.linkman3_mobile.setName("linkman3_mobile");
	    this.linkman3_mobile.setToolTipText(ResManager.getString("RobotTicket.label.mobilephone"));
	    this.linkman3_mobile.setBounds(425, 23, 100, 21);
	    panel5.add(this.linkman3_mobile);
	    this.linkman3_mobile.setColumns(10);

	    JPanel panel7 = new JPanel();
	    panel7.setLayout(null);
	    panel7.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkman4")));
	    panel7.setBounds(20, 193, 610, 54);
	    panel2.add(panel7);

	    JLabel label = new JLabel(ResManager.getString("RobotTicket.label.username"));
	    label.setHorizontalAlignment(4);
	    label.setBounds(55, 26, 30, 15);
	    panel7.add(label);

	    this.linkman4_name = new JTextField();
	    this.linkman4_name.setToolTipText(ResManager.getString("RobotTicket.label.username"));
	    this.linkman4_name.setName("linkman4_name");
	    this.linkman4_name.setColumns(10);
	    this.linkman4_name.setBounds(95, 23, 40, 21);
	    panel7.add(this.linkman4_name);

	    JLabel label_1 = new JLabel(ResManager.getString("RobotTicket.label.cardno"));
	    label_1.setHorizontalAlignment(4);
	    label_1.setBounds(155, 26, 50, 15);
	    panel7.add(label_1);

	    this.linkman4_cardNo = new JTextField();
	    this.linkman4_cardNo.setToolTipText(ResManager.getString("RobotTicket.label.cardno"));
	    this.linkman4_cardNo.setName("linkman4_cardNo");
	    this.linkman4_cardNo.setColumns(10);
	    this.linkman4_cardNo.setBounds(215, 23, 150, 21);
	    panel7.add(this.linkman4_cardNo);

	    JLabel label_11 = new JLabel(ResManager.getString("RobotTicket.label.mobilephone"));
	    label_11.setHorizontalAlignment(4);
	    label_11.setBounds(375, 26, 40, 15);
	    panel7.add(label_11);

	    this.linkman4_mobile = new JTextField();
	    this.linkman4_mobile.setName("linkman4_mobile");
	    this.linkman4_mobile.setBounds(425, 23, 100, 21);
	    this.linkman4_mobile.setToolTipText(ResManager.getString("RobotTicket.label.mobilephone"));
	    this.linkman4_mobile.setColumns(10);
	    panel7.add(this.linkman4_mobile);

	    JPanel panel8 = new JPanel();
	    panel8.setLayout(null);
	    panel8.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.linkman5")));
	    panel8.setBounds(20, 251, 610, 54);
	    panel2.add(panel8);

	    JLabel label_12 = new JLabel(ResManager.getString("RobotTicket.label.username"));
	    label_12.setHorizontalAlignment(4);
	    label_12.setBounds(55, 26, 30, 15);
	    panel8.add(label_12);

	    this.linkman5_name = new JTextField();
	    this.linkman5_name.setToolTipText(ResManager.getString("RobotTicket.label.username"));
	    this.linkman5_name.setName("linkman5_name");
	    this.linkman5_name.setColumns(10);
	    this.linkman5_name.setBounds(95, 23, 40, 21);
	    panel8.add(this.linkman5_name);

	    JLabel label_13 = new JLabel(ResManager.getString("RobotTicket.label.cardno"));
	    label_13.setHorizontalAlignment(4);
	    label_13.setBounds(155, 26, 50, 15);
	    panel8.add(label_13);

	    this.linkman5_cardNo = new JTextField();
	    this.linkman5_cardNo.setToolTipText(ResManager.getString("RobotTicket.label.cardno"));
	    this.linkman5_cardNo.setName("linkman5_cardNo");
	    this.linkman5_cardNo.setColumns(10);
	    this.linkman5_cardNo.setBounds(215, 23, 150, 21);
	    panel8.add(this.linkman5_cardNo);

	    JLabel label_14 = new JLabel(ResManager.getString("RobotTicket.label.mobilephone"));
	    label_14.setHorizontalAlignment(4);
	    label_14.setBounds(375, 26, 40, 15);
	    panel8.add(label_14);

	    this.linkman5_mobile = new JTextField();
	    this.linkman5_mobile.setToolTipText(ResManager.getString("RobotTicket.label.mobilephone"));
	    this.linkman5_mobile.setName("linkman5_mobile");
	    this.linkman5_mobile.setColumns(10);
	    this.linkman5_mobile.setBounds(425, 23, 100, 21);
	    panel8.add(this.linkman5_mobile);

	    JPanel panel6 = new JPanel();
	    panel6.setBounds(10, 394, 650, 63);
	    this.frame.getContentPane().add(panel6);
	    panel6.setLayout(null);
	    panel6.setBorder(new TitledBorder(ResManager.getString("RobotTicket.panel.configuration")));

	    this.boxkTwoSeat = new JCheckBox(ResManager.getString("RobotTicket.label.boxkTwoSeat"));
	    this.boxkTwoSeat.setBounds(82, 29, 74, 15);
	    panel6.add(this.boxkTwoSeat);
	    this.boxkTwoSeat.setHorizontalAlignment(4);

	    this.hardSleePer = new JCheckBox(ResManager.getString("RobotTicket.label.hardSleePer"));
	    this.hardSleePer.setBounds(6, 29, 74, 15);
	    panel6.add(this.hardSleePer);
	    this.hardSleePer.setHorizontalAlignment(4);

	    JLabel label_15 = new JLabel(ResManager.getString("RobotTicket.label.txtStartDate"));
	    label_15.setBounds(162, 30, 60, 13);
	    panel6.add(label_15);
	    label_15.setHorizontalAlignment(4);

	    MaskFormatter mf = null;
	    try {
	      mf = new MaskFormatter("####-##-##");
	    } catch (ParseException e1) {
	      e1.printStackTrace();
	    }
	    this.txtStartDate = new JFormattedTextField(mf);
	    this.txtStartDate.setToolTipText(ResManager.getString("RobotTicket.label.txtStartDate"));
	    this.txtStartDate.setBounds(225, 26, 84, 21);
	    panel6.add(this.txtStartDate);
	    this.txtStartDate.setColumns(10);

	    JLabel label_16 = new JLabel(ResManager.getString("RobotTicket.label.formCode"));
	    label_16.setBounds(307, 26, 40, 17);
	    panel6.add(label_16);
	    label_16.setHorizontalAlignment(4);

	    this.formCode = new JTextField();
	    this.formCode.setToolTipText(ResManager.getString("RobotTicket.label.formCode"));
	    this.formCode.setName("formCode");
	    this.formCode.setBounds(357, 26, 60, 21);
	    panel6.add(this.formCode);
	    this.formCode.setColumns(10);

	    JLabel label_17 = new JLabel(ResManager.getString("RobotTicket.label.toCode"));
	    label_17.setBounds(405, 26, 60, 17);
	    panel6.add(label_17);
	    label_17.setHorizontalAlignment(4);

	    this.toCode = new JTextField();
	    this.toCode.setToolTipText(ResManager.getString("RobotTicket.label.toCode"));
	    this.toCode.setName("toCode");
	    this.toCode.setBounds(475, 26, 60, 21);
	    panel6.add(this.toCode);
	    this.toCode.setColumns(10);

	    this.startButton = new JButton();
	    this.startButton.setText(ResManager.getString("RobotTicket.btn.start"));
	    this.startButton.setBounds(559, 22, 70, 28);
	    panel6.add(this.startButton);
	    this.startButton.addActionListener(new StartButton());

	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(15, 460, 640, 145);
	    scrollPane.setViewportBorder(new BevelBorder(1, null, null, null, null));
	    scrollPane.setHorizontalScrollBarPolicy(31);
	    this.frame.getContentPane().add(scrollPane);

	    this.messageOut = new JTextArea();
	    scrollPane.setViewportView(this.messageOut);
	    this.messageOut.setText(ResManager.getString("RobotTicket.textarea.messageOut"));
	    this.messageOut.setEditable(false);
	    this.messageOut.setLineWrap(true);
	  }

	  public static void main(String[] arg0)
	  {
	    EventQueue.invokeLater(new Runnable() {
	      public void run() {
	        try {
	          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	          MainWin window = new MainWin();
	          window.frame.setVisible(true);
	        } catch (Exception e) {
	          MainWin.logger.error("init main layout error : ", e);
	        }
	      }

	    });
	    try
	    {
	      File file = new File(path + "conf" + File.separator + "config.properties");

	      if (!file.exists()) {
	        return;
	      }
	      ResManager.initProperties(path + "conf" + File.separator + "config.properties");
	    }
	    catch (IOException e) {
	      logger.error("Read the external file failed : ", e);
	    }
	  }

	  public MainWin()
	  {
	    this.loginUrl = (path + "image" + File.separator + "passcode-login.jpg");
	    this.submitUrl = (path + "image" + File.separator + "passcode-submit.jpg");

	    initLayout();

	    initCookie();

	    initLoginImage();
	    try
	    {
	      ToolHelper.setUserInfo(path, "data" + File.separator + "UI.dat", new Object[] { this.username, this.password, this.linkman1_cardNo, this.linkman1_name, this.linkman1_mobile, this.linkman2_cardNo, this.linkman2_name, this.linkman2_mobile, this.linkman3_cardNo, this.linkman3_name, this.linkman3_mobile, this.linkman4_cardNo, this.linkman4_name, this.linkman4_mobile, this.linkman5_cardNo, this.linkman5_name, this.linkman5_mobile, this.formCode, this.toCode });
	    }
	    catch (Exception e) {
	      logger.error("初始化界面赋值失败！", e);
	    }
	  }

	  private void initCookie()
	  {
	    while (true)
	      try
	      {
	        this.client.getCookie("http://kyfw.12306.cn/otn/", HttpHeader.loginInitHearder(), null);
	        if ((!StringHelper.isEmptyString(this.client.BIGipServerotn)) && (!StringHelper.isEmptyString(this.client.JSESSIONID)))
	          break;
	      }
	      catch (Exception e) {
	        logger.error("get cookie error : ", e);
	        initCookie();
	      }
	  }

	  private void initLoginImage()
	  {
	    try
	    {
	      this.client.getPassCode("http://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand&0.5106279726205678", this.loginUrl, HttpHeader.getPassCode(true), null);
	      this.code.setIcon(ToolHelper.getImageIcon(this.loginUrl));
	    } catch (Exception e) {
	      logger.error("get passcode error : ", e);
	      initLoginImage();
	    }
	  }

	  public void showMsg(String msg)
	  {
	    JOptionPane.showMessageDialog(this.frame, msg);
	  }

	  public List<UserInfo> getUserInfo()
	  {
	    List list = new ArrayList();
	    if ((StringHelper.isEmptyString(this.linkman1_cardNo.getText().trim())) || (!StringHelper.isEmptyString(this.linkman1_name.getText().trim()))) {
	      if (!StringHelper.isEmptyString(this.linkman1_mobile.getText().trim())) {
	        UserInfo userInfo1 = new UserInfo(this.linkman1_cardNo.getText().trim(), this.linkman1_name.getText().trim(), this.linkman1_mobile.getText().trim());
	        list.add(userInfo1);
	      } else {
	        UserInfo userInfo1 = new UserInfo(this.linkman1_cardNo.getText().trim(), this.linkman1_name.getText().trim());
	        list.add(userInfo1);
	      }
	    }
	    if ((!StringHelper.isEmptyString(this.linkman2_cardNo.getText().trim())) || (!StringHelper.isEmptyString(this.linkman2_name.getText().trim()))) {
	      if (!StringHelper.isEmptyString(this.linkman2_mobile.getText().trim())) {
	        UserInfo userInfo2 = new UserInfo(this.linkman2_cardNo.getText().trim(), this.linkman2_name.getText().trim(), this.linkman2_mobile.getText().trim());
	        list.add(userInfo2);
	      } else {
	        UserInfo userInfo2 = new UserInfo(this.linkman2_cardNo.getText().trim(), this.linkman2_name.getText().trim());
	        list.add(userInfo2);
	      }
	    }
	    if ((!StringHelper.isEmptyString(this.linkman3_cardNo.getText().trim())) || (!StringHelper.isEmptyString(this.linkman3_name.getText().trim()))) {
	      if (!StringHelper.isEmptyString(this.linkman3_name.getText().trim())) {
	        UserInfo userInfo3 = new UserInfo(this.linkman3_cardNo.getText().trim(), this.linkman3_name.getText().trim(), this.linkman3_mobile.getText().trim());
	        list.add(userInfo3);
	      } else {
	        UserInfo userInfo3 = new UserInfo(this.linkman3_cardNo.getText().trim(), this.linkman3_name.getText().trim());
	        list.add(userInfo3);
	      }
	    }
	    if ((!StringHelper.isEmptyString(this.linkman4_cardNo.getText().trim())) || (!StringHelper.isEmptyString(this.linkman4_name.getText().trim()))) {
	      if (!StringHelper.isEmptyString(this.linkman4_name.getText().trim())) {
	        UserInfo userInfo4 = new UserInfo(this.linkman4_cardNo.getText().trim(), this.linkman4_name.getText().trim(), this.linkman4_mobile.getText().trim());
	        list.add(userInfo4);
	      } else {
	        UserInfo userInfo4 = new UserInfo(this.linkman4_cardNo.getText().trim(), this.linkman4_name.getText().trim());
	        list.add(userInfo4);
	      }
	    }
	    if ((!StringHelper.isEmptyString(this.linkman5_cardNo.getText().trim())) || (!StringHelper.isEmptyString(this.linkman5_name.getText().trim()))) {
	      if (!StringHelper.isEmptyString(this.linkman5_name.getText().trim())) {
	        UserInfo userInfo5 = new UserInfo(this.linkman5_cardNo.getText().trim(), this.linkman5_name.getText().trim(), this.linkman5_mobile.getText().trim());
	        list.add(userInfo5);
	      } else {
	        UserInfo userInfo5 = new UserInfo(this.linkman5_cardNo.getText().trim(), this.linkman5_name.getText().trim());
	        list.add(userInfo5);
	      }
	    }
	    return list;
	  }

	  private TicketSearch getOrderRequest()
	  {
	    this.req = new TicketSearch();
	    this.req.setFrom_station_name(this.formCode.getText().trim());
	    this.req.setTo_station_name(this.toCode.getText().trim());
	    this.req.setTrain_date(this.txtStartDate.getText().trim());
	    this.req.setTo_date(DateHelper.getCurDate());
	    return this.req;
	  }

	  static
	  {
	    logger.debug("mainWin path = " + path);
	  }

	  class StartButton
	    implements ActionListener
	  {
	    StartButton()
	    {
	    }

	    public void actionPerformed(ActionEvent e)
	    {
	      JButton btn = (JButton)e.getSource();
	      if (ResManager.getString("RobotTicket.btn.start").equals(btn.getText())) {
	        List list = MainWin.this.getUserInfo();

	        if (list.size() == 0) {
	          MainWin.this.showMsg("请至少输入1位联系人信息!");
	          return;
	        }

	        if (!MainWin.this.isLogin) {
	          MainWin.this.showMsg("请登录!");
	          return;
	        }

	        List msglist = ToolHelper.validateWidget(new Object[] { MainWin.this.txtStartDate, MainWin.this.formCode, MainWin.this.toCode });
	        if (msglist.size() > 0) {
	          String msg = "";
	          for (int i = 0; i < msglist.size(); i++) {
	            msg = new StringBuilder().append(msg).append(i == msglist.size() - 1 ? (String)msglist.get(i) : new StringBuilder().append((String)msglist.get(i)).append(",").toString()).toString();
	          }
	          MainWin.this.showMsg(new StringBuilder().append(msg).append("不能为空！").toString());
	          return;
	        }

	        MainWin.this.getOrderRequest();
	        if (MainWin.this.isRunThread) {
	          MainWin.this.showMsg("订票线程已启动!");
	          return;
	        }
	        MainWin.this.isStopRun = false;
	        MainWin.this.isRunThread = true;
	        MainWin.this.userInfoList = MainWin.this.getUserInfo();

	        new TicketThread(MainWin.this.req, MainWin.this.userInfoList, MainWin.this.mainWin).start();
	        MainWin.this.mainWin.startButton.setText(ResManager.getString("RobotTicket.btn.stop"));
	      } else if (ResManager.getString("RobotTicket.btn.stop").equals(btn.getText())) {
	        MainWin.this.isStopRun = true;
	        MainWin.this.isRunThread = false;
	        btn.setText(ResManager.getString("RobotTicket.btn.start"));
	      }
	    }
	  }

	  class LoginBtn
	    implements ActionListener
	  {
	    LoginBtn()
	    {
	    }

	    public void actionPerformed(ActionEvent e)
	    {
	      JButton btn = (JButton)e.getSource();
	      if (ResManager.getString("RobotTicket.btn.login").equals(btn.getText())) {
	        List list = ToolHelper.validateWidget(new Object[] { MainWin.this.username, MainWin.this.password, MainWin.this.authcode });
	        if (list.size() > 0) {
	          String msg = "";
	          for (int i = 0; i < list.size(); i++) {
	            msg = new StringBuilder().append(msg).append(i == list.size() - 1 ? (String)list.get(i) : new StringBuilder().append((String)list.get(i)).append(",").toString()).toString();
	          }
	          MainWin.this.showMsg(new StringBuilder().append(msg).append("不能为空！").toString());
	          return;
	        }
	        LoginUserInfo user = new LoginUserInfo(MainWin.this.username.getText(), MainWin.this.password.getText(), MainWin.this.authcode.getText());
	        LoginThread loginThread = new LoginThread(user, MainWin.this.mainWin);
	        loginThread.start();
	      }
	    }
	  }
}
