package l.t.y.msg;

public class CheckInfoMsg  extends BaseMsg{
	  private CheckInfo data;

	  public CheckInfo getData()
	  {
	    return this.data;
	  }

	  public void setData(CheckInfo data) {
	    this.data = data;
	  }
}	
