package l.t.y.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import l.t.y.bean.TrainInfo;
import l.t.y.bean.UserInfo;
import l.t.y.helper.ResManager;
import l.t.y.helper.StringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoGetTrainInfo {
	private static final Logger logger = LoggerFactory.getLogger(AutoGetTrainInfo.class);

	  private List<TrainInfo> trainQueryInfoList = new ArrayList();
	  private List<UserInfo> userInfoList;
	  private MainWin mainWin;
	  private String[] specificTrainKeys;
	  private String[] specificTrainSeat;
	  public static Map seatMap = null;

	  private Map<String, TrainInfo> specificTrains = new LinkedHashMap();

	  private Map<String, TrainInfo> specificSeatTrains = new LinkedHashMap();

	  public AutoGetTrainInfo(List<TrainInfo> trainQueryInfoList, MainWin mainWin, List<UserInfo> userInfoList)
	  {
	    this.trainQueryInfoList.addAll(trainQueryInfoList);
	    this.mainWin = mainWin;
	    this.userInfoList = userInfoList;

	    this.specificTrainKeys = getKeys();

	    this.specificTrainSeat = getSeatKeys();
	    trainQueryInfoClass();
	  }

	  public void trainQueryInfoClass()
	  {
	    for (int j = 0; (j < this.specificTrainKeys.length) && 
	      (!StringHelper.isEmptyString(this.specificTrainKeys[j])); j++)
	    {
	      for (int i = this.trainQueryInfoList.size() - 1; i >= 0; i--) {
	        if (this.specificTrainKeys[j].equalsIgnoreCase(((TrainInfo)this.trainQueryInfoList.get(i)).getStation_train_code()))
	        {
	          this.specificTrains.put(((TrainInfo)this.trainQueryInfoList.get(i)).getStation_train_code(), this.trainQueryInfoList.get(i));
	          this.trainQueryInfoList.remove(i);
	        }
	      }
	    }

	    for (int i = this.trainQueryInfoList.size() - 1; i >= 0; i--)
	    {
	      if (StringHelper.isEqualString(((TrainInfo)this.trainQueryInfoList.get(i)).getCanWebBuy(), "Y")) {
	        this.specificSeatTrains.put(((TrainInfo)this.trainQueryInfoList.get(i)).getStation_train_code(), this.trainQueryInfoList.get(i));
	        this.trainQueryInfoList.remove(i);
	      }

	    }

	    String specificTrain = "";
	    for (Map.Entry key : this.specificTrains.entrySet()) {
	      specificTrain = specificTrain + ((TrainInfo)key.getValue()).getStation_train_code() + ",";
	    }

	    String specificSeatTrain = "";
	    for (Map.Entry key : this.specificSeatTrains.entrySet()) {
	      specificSeatTrain = specificSeatTrain + ((TrainInfo)key.getValue()).getStation_train_code() + ",";
	    }

	    if (!StringHelper.isEmptyString(specificTrain)) {
	      this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "您指定的列车为:" + specificTrain.substring(0, specificTrain.length() - 1) + "\n");
	      logger.debug("指定列车信息:" + specificTrain.substring(0, specificTrain.length() - 1));
	    }

	    if (!StringHelper.isEmptyString(specificSeatTrain)) {
	      this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "指定之外的列车为:" + specificSeatTrain.substring(0, specificSeatTrain.length() - 1) + "\n");
	      logger.debug("指定之外列车信息:" + specificSeatTrain.substring(0, specificSeatTrain.length() - 1));
	    }
	  }

	  public TrainInfo getSeattrainQueryInfo()
	  {
	    boolean isAssign = false;
	    TrainInfo returninfo = null;

	    if ((this.specificTrainKeys.length > 0) && (!StringHelper.isEmptyString(this.specificTrainKeys[0]))) {
	      for (int i = 0; i < this.specificTrainKeys.length; i++) {
	        TrainInfo info = (TrainInfo)this.specificTrains.get(this.specificTrainKeys[i]);
	        if (info != null) {
	          returninfo = getSeattrainQueryInfo(info);
	        }
	        if (returninfo != null) {
	          return returninfo;
	        }
	      }

	    }

	    if (!isAssign) {
	      for (Map.Entry map : this.specificSeatTrains.entrySet()) {
	        TrainInfo info = getSeattrainQueryInfo((TrainInfo)map.getValue());
	        if (info != null) {
	          returninfo = info;
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "最终车次为:" + info.getStation_train_code() + "\n");
	          return returninfo;
	        }
	      }
	    }
	    return null;
	  }

	  public TrainInfo getSeattrainQueryInfo(TrainInfo info)
	  {
	    String seat = "";

	    if ((this.specificTrainSeat.length > 0) && (!StringHelper.isEmptyString(this.specificTrainSeat[0]))) {
	      for (int i = 0; i < this.specificTrainSeat.length; i++) {
	        seat = this.specificTrainSeat[i];
	        try
	        {
	          String seatCount = getSeatCount(seat, info);
	          if (!checkSeatIsTicket(seatCount))
	          {
	            if (Integer.parseInt(seatCount) >= this.userInfoList.size()) {
	              setUserSest(this.userInfoList, this.specificTrainSeat[i]);
	              this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "您指定的座位席别为:" + seatMap.get(seat) + "\n");
	              return info;
	            }
	          }
	        } catch (NumberFormatException ex) { setUserSest(this.userInfoList, this.specificTrainSeat[i]);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "您指定的座位席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }

	      }

	    }

	    if ((this.mainWin.boxkTwoSeat.isSelected()) && 
	      (!"--".equals(info.getZe_num())) && (!"无".equals(info.getZe_num()))) {
	      try {
	        if (Integer.parseInt(info.getZe_num()) >= this.userInfoList.size()) {
	          seat = Constants.TWO_SEAT;
	          setUserSest(this.userInfoList, Constants.TWO_SEAT);
	          logger.debug("动车优先车次为:" + info.getStation_train_code());
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.TWO_SEAT;
	        setUserSest(this.userInfoList, Constants.TWO_SEAT);
	        logger.debug("动车优先车次为:" + info.getStation_train_code());
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }

	    }

	    if ((this.mainWin.hardSleePer.isSelected()) && 
	      (!"--".equals(info.getYw_num())) && (!"无".equals(info.getYw_num()))) {
	      try {
	        if (Integer.parseInt(info.getYw_num()) >= this.userInfoList.size()) {
	          seat = Constants.HARD_SLEEPER;
	          setUserSest(this.userInfoList, Constants.HARD_SLEEPER);
	          logger.debug("卧铺优先车次为:" + info.getStation_train_code());
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.HARD_SLEEPER;
	        setUserSest(this.userInfoList, Constants.HARD_SLEEPER);
	        logger.debug("卧铺优先车次为:" + info.getStation_train_code());
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }

	    }

	    if ((!"--".equals(info.getZe_num())) && (!"无".equals(info.getZe_num()))) {
	      try {
	        if (Integer.parseInt(info.getZe_num()) >= this.userInfoList.size()) {
	          seat = Constants.TWO_SEAT;
	          setUserSest(this.userInfoList, Constants.TWO_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.TWO_SEAT;
	        setUserSest(this.userInfoList, Constants.TWO_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getZy_num())) && (!"无".equals(info.getZy_num()))) {
	      try {
	        if (Integer.parseInt(info.getZy_num()) >= this.userInfoList.size()) {
	          seat = Constants.ONE_SEAT;
	          setUserSest(this.userInfoList, Constants.ONE_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.ONE_SEAT;
	        setUserSest(this.userInfoList, Constants.ONE_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getYw_num())) && (!"无".equals(info.getYw_num()))) {
	      try {
	        if (Integer.parseInt(info.getYw_num()) >= this.userInfoList.size()) {
	          seat = Constants.HARD_SLEEPER;
	          setUserSest(this.userInfoList, Constants.HARD_SLEEPER);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.HARD_SLEEPER;
	        setUserSest(this.userInfoList, Constants.HARD_SLEEPER);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getRw_num())) && (!"无".equals(info.getRw_num()))) {
	      try {
	        if (Integer.parseInt(info.getRw_num()) >= this.userInfoList.size()) {
	          seat = Constants.SOFT_SLEEPER;
	          setUserSest(this.userInfoList, Constants.SOFT_SLEEPER);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.SOFT_SLEEPER;
	        setUserSest(this.userInfoList, Constants.SOFT_SLEEPER);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getRz_num())) && (!"无".equals(info.getRz_num()))) {
	      try {
	        if (Integer.parseInt(info.getRz_num()) >= this.userInfoList.size()) {
	          seat = Constants.SOFT_SEAT;
	          setUserSest(this.userInfoList, Constants.SOFT_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.SOFT_SEAT;
	        setUserSest(this.userInfoList, Constants.SOFT_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getYz_num())) && (!"无".equals(info.getYz_num()))) {
	      try {
	        if (Integer.parseInt(info.getYz_num()) >= this.userInfoList.size()) {
	          seat = Constants.HARD_SEAT;
	          setUserSest(this.userInfoList, Constants.HARD_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.HARD_SEAT;
	        setUserSest(this.userInfoList, Constants.HARD_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getGr_num())) && (!"无".equals(info.getGr_num()))) {
	      try {
	        if (Integer.parseInt(info.getGr_num()) >= this.userInfoList.size()) {
	          seat = Constants.VAG_SLEEPER;
	          setUserSest(this.userInfoList, Constants.VAG_SLEEPER);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.VAG_SLEEPER;
	        setUserSest(this.userInfoList, Constants.VAG_SLEEPER);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getTz_num())) && (!"无".equals(info.getTz_num()))) {
	      try {
	        if (Integer.parseInt(info.getTz_num()) >= this.userInfoList.size()) {
	          seat = Constants.BEST_SEAT;
	          setUserSest(this.userInfoList, Constants.BEST_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.BEST_SEAT;
	        setUserSest(this.userInfoList, Constants.BEST_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }

	    if ((!"--".equals(info.getSwz_num())) && (!"无".equals(info.getSwz_num()))) {
	      try {
	        if (Integer.parseInt(info.getSwz_num()) >= this.userInfoList.size()) {
	          seat = Constants.BUSS_SEAT;
	          setUserSest(this.userInfoList, Constants.BUSS_SEAT);
	          this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	          return info;
	        }
	      } catch (NumberFormatException ex) {
	        seat = Constants.BUSS_SEAT;
	        setUserSest(this.userInfoList, Constants.BUSS_SEAT);
	        this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "自动选择的席别为:" + seatMap.get(seat) + "\n");
	        return info;
	      }
	    }
	    return null;
	  }

	  private String[] getKeys()
	  {
	    return ResManager.getByKey("traincode").split(",");
	  }

	  private String[] getSeatKeys()
	  {
	    return ResManager.getByKey("settype").split(",");
	  }

	  private void setUserSest(List<UserInfo> userInfoList, String seat)
	  {
	    for (UserInfo info : userInfoList)
	      info.setSeatType(seat);
	  }

	  private boolean checkSeatIsTicket(String seat)
	  {
	    return (StringHelper.isEqualString("--", seat)) || (StringHelper.isEqualString("无", seat));
	  }

	  private String getSeatCount(String seat, TrainInfo info)
	  {
	    if (StringHelper.isEqualString("9", seat))
	      return info.getSwz_num();
	    if (StringHelper.isEqualString("P", seat))
	      return info.getTz_num();
	    if (StringHelper.isEqualString("M", seat))
	      return info.getZy_num();
	    if (StringHelper.isEqualString("O", seat))
	      return info.getZe_num();
	    if (StringHelper.isEqualString("6", seat))
	      return info.getGr_num();
	    if (StringHelper.isEqualString("4", seat))
	      return info.getRw_num();
	    if (StringHelper.isEqualString("3", seat))
	      return info.getYw_num();
	    if (StringHelper.isEqualString("2", seat)) {
	      return info.getRz_num();
	    }
	    return info.getYz_num();
	  }

	  static
	  {
	    seatMap = new HashMap();
	    seatMap.put("9", "商务座");
	    seatMap.put("P", "特等座");
	    seatMap.put("M", "一等座");
	    seatMap.put("O", "二等座");
	    seatMap.put("6", "高级软卧");
	    seatMap.put("4", "软卧");
	    seatMap.put("3", "硬卧");
	    seatMap.put("2", "软座");
	    seatMap.put("1", "硬座");
	  }
}
