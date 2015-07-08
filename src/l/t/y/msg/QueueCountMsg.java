package l.t.y.msg;

public class QueueCountMsg extends BaseMsg
{
	  private QueueCount data;

	  public QueueCount getData()
	  {
	    return this.data;
	  }

	  public void setData(QueueCount data) {
	    this.data = data;
	  }
	}