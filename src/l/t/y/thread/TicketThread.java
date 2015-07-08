package l.t.y.thread;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import l.t.y.bean.TicketSearch;
import l.t.y.bean.TrainDataInfo;
import l.t.y.bean.TrainInfo;
import l.t.y.bean.UserInfo;
import l.t.y.helper.HttpHeader;
import l.t.y.helper.JSONHelper;
import l.t.y.helper.ResManager;
import l.t.y.helper.StringHelper;
import l.t.y.helper.TicketHelper;
import l.t.y.helper.ToolHelper;
import l.t.y.main.AutoGetTrainInfo;
import l.t.y.main.MainWin;
import l.t.y.msg.AutoSubmitMsg;
import l.t.y.msg.CheckCodeMsg;
import l.t.y.msg.CheckInfoMsg;
import l.t.y.msg.QueryOrderWaitMsg;
import l.t.y.msg.QueueCountMsg;
import l.t.y.msg.TicketMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketThread extends Thread{
	 private static final Logger logger = LoggerFactory.getLogger(TicketThread.class);
	  private MainWin mainWin;
	  private TicketSearch ticksearch;
	  private List<TrainInfo> trainInfoList = null;
	  private static boolean isSuccess;
	  private JSONHelper jsonHelper = JSONHelper.buildNormalIgnoreBinder();

	  private static Map<String, String> cookies = new LinkedHashMap();

	  private AutoSubmitMsg autoSubmitMsg = null;

	  private CheckInfoMsg checkInfoMsg = null;

	  private QueueCountMsg queueCountMsg = null;

	  private CheckCodeMsg codeMsg = null;

	  private QueryOrderWaitMsg queryOrderWaitMsg = null;

	  private TrainInfo trainInfo = null;
	  private List<UserInfo> userInfoList;

	  public TicketThread()
	  {
	  }

	  public TicketThread(TicketSearch ticksearch, List<UserInfo> userInfoList, MainWin mainWin)
	  {
	    this.ticksearch = ticksearch;
	    this.mainWin = mainWin;
	    this.userInfoList = userInfoList;
	    getCookie();
	  }

	  public void run()
	  {
	    int search = 0;
	    isSuccess = false;
	    while (!isSuccess) {
	      if (this.mainWin.isStopRun) {
	        this.mainWin.showMsg("停止线程成功!");
	        break;
	      }

	      Map params = new LinkedHashMap();
	      params.put("leftTicketDTO.train_date", this.ticksearch.getTrain_date());
	      params.put("leftTicketDTO.from_station", this.ticksearch.getFrom_station());
	      params.put("leftTicketDTO.to_station", this.ticksearch.getTo_station());
	      params.put("purpose_codes", "ADULT");
	      try {
	        if (search != 0) {
	          sleep(3000L);
	        }
	        search++;
	        String datas = this.mainWin.client.getRequest("http://kyfw.12306.cn/otn/leftTicket/query", params, HttpHeader.tiketSearch(), cookies, false);
	        TicketMsg ticketMsg = (TicketMsg)this.jsonHelper.parse(datas, TicketMsg.class);
	        this.trainInfoList = getAllTrainInfo(ticketMsg);

	        this.trainInfoList = screenTrain(this.trainInfoList);
	      } catch (Exception e) {
	        logger.error("query ticket error : ", e);
	      }
	      
//	      continue;

	      while (!this.mainWin.isStopRun)
	      {
	        this.trainInfo = new AutoGetTrainInfo(this.trainInfoList, this.mainWin, this.userInfoList).getSeattrainQueryInfo();
	        if (this.trainInfo == null) {
	          break;
	        }
	        if (this.trainInfoList.size() == 0)
	        {
	          break;
	        }
	        params = null;
	        try {
	          params = new LinkedHashMap();
	          params.put("bed_level_order_num", "000000000000000000000000000000");
	          params.put("cancel_flag", "2");
	          params.put("oldPassengerStr", TicketHelper.getOldPassengerStr(this.userInfoList));
	          params.put("passengerTicketStr", TicketHelper.getPassengerTicketStr(this.userInfoList));
	          params.put("purpose_codes", "ADULT");
	          params.put("query_from_station_name", this.ticksearch.getFrom_station_name());
	          params.put("query_to_station_name", this.ticksearch.getTo_station_name());
	          params.put("secretStr", URLDecoder.decode(this.trainInfo.getSecretStr(), "UTF-8"));
	          params.put("tour_flag", "dc");
	          params.put("train_date", this.ticksearch.getTrain_date());
	          String msg = this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/confirmPassenger/autoSubmitOrderRequest", params, HttpHeader.submitOrder(), cookies, false);
	          this.autoSubmitMsg = ((AutoSubmitMsg)this.jsonHelper.parse(msg, AutoSubmitMsg.class));
	        } catch (Exception e) {
	          logger.error("submit order error : ", e);
	        }
	        if ((this.autoSubmitMsg == null) || (this.autoSubmitMsg.getHttpstatus() != 200) || (this.autoSubmitMsg.getStatus() != true)) {
	          deleteTrain(this.trainInfo.getStation_train_code());
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "+车次" + this.trainInfo.getStation_train_code() + "提交订单信息失败!\n");
	        }
	        else
	        {
	          try
	          {
	            this.queueCountMsg = queueCount();
	          } catch (Exception e) {
	            logger.error("queueCount error : ", e);
	          }

	          if ((this.queueCountMsg == null) || (this.queueCountMsg.getData().getOp_2() == true)) {
	            this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "车次:" + this.trainInfo.getStation_train_code() + "排队人数超过余票数!\n");
	          }
	          else
	          {
	            String ticketInfo = "";
	            try {
	              ticketInfo = ToolHelper.getSeatJsValue(this.queueCountMsg.getData().getTicket(), ((UserInfo)this.userInfoList.get(0)).getSeatType());
	            } catch (Exception e) {
	              logger.error("script run error : ", e);
	            }
	            this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "当前排队人数:{" + this.queueCountMsg.getData().getCountT() + "},当前座位席别为:{" + AutoGetTrainInfo.seatMap.get(((UserInfo)this.userInfoList.get(0)).getSeatType()) + "},剩余车票为:{" + ticketInfo + "}!\n");

	            final JDialog randcodeDialog = new JDialog(this.mainWin, "输入验证码", true);
	            randcodeDialog.setSize(200, 150);
	            randcodeDialog.setLocationRelativeTo(this.mainWin);
	            randcodeDialog.setResizable(false);
	            JLabel l_randcode = new JLabel("请输入验证码:", 0);

	            final JTextField t_randcode = new JTextField(10);
	            final JButton btn_randcode = new JButton("");

	            refreshCode(btn_randcode);
	            btn_randcode.addActionListener(new ActionListener()
	            {
	              public void actionPerformed(ActionEvent e)
	              {
	                TicketThread.this.refreshCode(btn_randcode);
	              }
	            });
	            JPanel p_randcode = new JPanel();
	            p_randcode.setLayout(new FlowLayout());
	            p_randcode.add(btn_randcode);
	            p_randcode.add(t_randcode);

	            JButton btn_confirm = new JButton("提交");
	            JPanel p_confirm = new JPanel();

	            randcodeDialog.getRootPane().setDefaultButton(btn_confirm);

	            btn_confirm.addActionListener(new ActionListener() {
	              public void actionPerformed(ActionEvent ev) {
	                String randcode = t_randcode.getText();
	                if ((randcode == null) || (randcode.trim().length() != 4)) {
	                  t_randcode.setText("");
	                  t_randcode.grabFocus();
	                }
	                else {
	                  Map params = new HashMap();
	                  params.put("rand", "sjrand");
	                  params.put("randCode", randcode);
	                  params.put("_json_att", "");
	                  try {
	                    String msg = TicketThread.this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn", params, HttpHeader.postCheckCode(false), null, false);
	                    TicketThread.this.codeMsg = ((CheckCodeMsg)TicketThread.this.jsonHelper.parse(msg, CheckCodeMsg.class));
	                  } catch (Exception e) {
	                    TicketThread.logger.error("check code error : ", e);
	                  }
	                  if ((TicketThread.this.codeMsg.getHttpstatus() != 200) || (TicketThread.this.codeMsg.getStatus() != true) || (!StringHelper.isEqualString(TicketThread.this.codeMsg.getData(), "Y"))) {
	                    TicketThread.this.mainWin.messageOut.setText(TicketThread.this.mainWin.messageOut.getText() + "验证码错误!\n");
	                    TicketThread.this.refreshCode(btn_randcode);
	                  }

	                  int tickSum = 0;
	                  while (true) {
	                    try
	                    {
	                      TicketThread.this.checkInfoMsg = TicketThread.this.confirmSingle(t_randcode.getText());
	                      if ((TicketThread.this.checkInfoMsg.getHttpstatus() == 200) && (TicketThread.this.checkInfoMsg.getStatus()) && (TicketThread.this.checkInfoMsg.getData().getSubmitStatus())) {
	                        TicketThread.this.mainWin.messageOut.setText(TicketThread.this.mainWin.messageOut.getText() + "订票好像成功!\n");
//	                        TicketThread.access$502(true);
	                        TicketThread.this.mainWin.isStopRun = true;
	                        TicketThread.this.mainWin.isRunThread = false;
	                        TicketThread.this.mainWin.showMsg("订票好像成功!");
	                      }
	                    }catch (Exception e) {
	                      TicketThread.logger.error("confirmSingle error : ", e);
	                      tickSum++;
	                      if (tickSum > 2) {
	                        TicketThread.this.deleteTrain(TicketThread.this.trainInfo.getStation_train_code());
	                      }
	                      break;
	                    }

	                  }

	                  TicketThread.this.mainWin.startButton.setText(ResManager.getString("RobotTicket.btn.start"));
	                  randcodeDialog.setVisible(false);
	                  randcodeDialog.dispose();

	                  int sum = 0;
	                  while (sum < 3)
	                    try {
	                      TicketThread.this.queryOrderWaitMsg = TicketThread.this.queryWait();
	                      TicketThread.this.mainWin.messageOut.setText(TicketThread.this.mainWin.messageOut.getText() + "当前等待人数:{" + TicketThread.this.queryOrderWaitMsg.getData().getWaitCount() + "}人,等待时间为：{" + TicketThread.this.queryOrderWaitMsg.getData().getWaitTime() + "}秒!\n");

	                      if ((!StringHelper.isEmptyString(TicketThread.this.queryOrderWaitMsg.getData().getOrderId())) && (!StringHelper.isEqualString(TicketThread.this.queryOrderWaitMsg.getData().getOrderId(), "null"))) {
	                        break;
	                      }
	                      Thread.sleep(2000L);
	                      sum++;
	                    } catch (Exception e) {
	                      TicketThread.logger.error("queryWait error : ", e);
	                    }
	                }
	              }
	            });
	            p_confirm.add(btn_confirm);
	            Container container = randcodeDialog.getContentPane();
	            container.setLayout(new GridLayout(3, 1));
	            container.add(l_randcode);
	            container.add(p_randcode);
	            container.add(p_confirm);
	            randcodeDialog.setVisible(true);
	          }
	        }
	      }
	    }
	  }

	  private void getCookie()
	  {
	    String formCode = TicketHelper.getCityCode(TicketHelper.convertStation(this.ticksearch.getFrom_station_name()));
	    this.ticksearch.setFrom_station(formCode);
	    cookies.put("_jc_save_fromStation", ToolHelper.getUnicode(this.ticksearch.getFrom_station_name(), formCode));
	    String toCode = TicketHelper.getCityCode(TicketHelper.convertStation(this.ticksearch.getTo_station_name()));
	    this.ticksearch.setTo_station(toCode);
	    cookies.put("_jc_save_toStation", ToolHelper.getUnicode(this.ticksearch.getTo_station_name(), formCode));
	    cookies.put("_jc_save_fromDate", this.ticksearch.getTrain_date());
	    cookies.put("_jc_save_toDate", "2014-02-24");
	    cookies.put("_jc_save_wfdc_flag", "dc");
	  }

	  private List<TrainInfo> getAllTrainInfo(TicketMsg ticketMsg)
	  {
	    List trainInfo = new ArrayList();
	    for (TrainDataInfo train : ticketMsg.getData()) {
	      TrainInfo tempTrain = train.getQueryLeftNewDTO();

	      tempTrain.setSecretStr(train.getSecretStr());
	      trainInfo.add(tempTrain);
	    }
	    return trainInfo;
	  }

	  public QueueCountMsg queueCount()
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    Map params = new LinkedHashMap();
	    params.put("_json_att", "");
	    params.put("fromStationTelecode", this.trainInfo.getFrom_station_telecode());
	    params.put("leftTicket", this.trainInfo.getYp_info());
	    params.put("purpose_codes", "ADULT");
	    params.put("seatType", ((UserInfo)this.userInfoList.get(0)).getSeatType());
	    params.put("stationTrainCode", this.trainInfo.getStation_train_code());
	    params.put("toStationTelecode", this.trainInfo.getTo_station_telecode());
	    params.put("train_date", new Date().toString().replace("CST", "UTC+0800"));
	    params.put("train_no", this.trainInfo.getTrain_no());
	    String datas = this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/confirmPassenger/getQueueCountAsync", params, HttpHeader.checkOrder(), cookies, false);
	    return (QueueCountMsg)this.jsonHelper.parse(datas, QueueCountMsg.class);
	  }

	  public CheckInfoMsg confirmSingle(String randCode)
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    Map params = new LinkedHashMap();
	    params.put("passengerTicketStr", TicketHelper.getPassengerTicketStr(this.userInfoList));
	    params.put("oldPassengerStr", TicketHelper.getOldPassengerStr(this.userInfoList));
	    params.put("randCode", randCode);
	    params.put("purpose_codes", "ADULT");
	    params.put("key_check_isChange", this.autoSubmitMsg.getDataStrs()[1]);
	    params.put("leftTicketStr", this.trainInfo.getYp_info());

	    params.put("train_location", this.trainInfo.getLocation_code());
	    params.put("_json_att", "");
	    String datas = this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueueAsys", params, HttpHeader.checkOrder(), cookies, false);
	    return (CheckInfoMsg)this.jsonHelper.parse(datas, CheckInfoMsg.class);
	  }

	  public QueryOrderWaitMsg queryWait()
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    Map params = new LinkedHashMap();
	    params.put("random", String.valueOf(System.currentTimeMillis()));
	    params.put("tourFlag", "dc");
	    params.put("_json_att", "");
	    String datas = this.mainWin.client.getRequest("http://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime", params, HttpHeader.checkOrder(), cookies, false);
	    return (QueryOrderWaitMsg)this.jsonHelper.parse(datas, QueryOrderWaitMsg.class);
	  }

	  public List<TrainInfo> screenTrain(List<TrainInfo> trainInfos)
	  {
	    List trainInfo = new ArrayList();
	    for (TrainInfo o : this.trainInfoList) {
	      if (StringHelper.isEqualString(o.getCanWebBuy(), "Y")) {
	        trainInfo.add(o);
	      }
	    }
	    return trainInfo;
	  }

	  public void deleteTrain(String trainNo)
	  {
	    for (int i = 0; i < this.trainInfoList.size(); i++)
	      if (StringHelper.isEqualString(((TrainInfo)this.trainInfoList.get(i)).getStation_train_code(), trainNo)) {
	        this.trainInfoList.remove(i);
	        break;
	      }
	  }

	  public void refreshCode(JButton btn_randcode)
	  {
	    try
	    {
	      String path = this.mainWin.submitUrl;
	      this.mainWin.client.getPassCode("http://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand&0.5106279726205678", path, HttpHeader.getPassCode(false), null);
	      btn_randcode.setIcon(ToolHelper.getImageIcon(path));
	    } catch (Exception ex) {
	      logger.error("getPassCode error : ", ex);
	      refreshCode(btn_randcode);
	    }
	  }
}
