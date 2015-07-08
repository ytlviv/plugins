package l.t.y.msg;

public class AutoSubmitMsg extends BaseMsg{
	  private AutoSubmit data;

	  public AutoSubmit getData()
	  {
	    return this.data;
	  }

	  public void setData(AutoSubmit data) {
	    this.data = data;
	  }

	  public String[] getDataStrs() {
	    return this.data.getResult().split("#");
	  }
}
