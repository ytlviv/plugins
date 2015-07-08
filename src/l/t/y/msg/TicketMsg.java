package l.t.y.msg;

import java.util.ArrayList;
import java.util.List;

import l.t.y.bean.TrainDataInfo;

public class TicketMsg extends BaseMsg
{
	  private List<TrainDataInfo> data = new ArrayList();

	  public List<TrainDataInfo> getData() {
	    return this.data;
	  }

	  public void setData(List<TrainDataInfo> data) {
	    this.data = data;
	  }
}
