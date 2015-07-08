package l.t.y.msg;

public class QueryOrderWaitMsg extends BaseMsg
{
	  private QueryOrderWait data;

	  public QueryOrderWait getData()
	  {
	    return this.data;
	  }

	  public void setData(QueryOrderWait data) {
	    this.data = data;
	  }
}
