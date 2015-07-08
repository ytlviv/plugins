package l.t.y.msg;

public class BaseMsg {
	  private String validateMessagesShowId;
	  private boolean status;
	  private int httpstatus;
	  private String[] messages;
	  private Object validateMessages;

	  public String getValidateMessagesShowId()
	  {
	    return this.validateMessagesShowId;
	  }

	  public void setValidateMessagesShowId(String validateMessagesShowId) {
	    this.validateMessagesShowId = validateMessagesShowId;
	  }

	  public boolean getStatus() {
	    return this.status;
	  }

	  public void setStatus(boolean status) {
	    this.status = status;
	  }

	  public int getHttpstatus() {
	    return this.httpstatus;
	  }

	  public void setHttpstatus(int httpstatus) {
	    this.httpstatus = httpstatus;
	  }

	  public String[] getMessages() {
	    return this.messages;
	  }

	  public void setMessages(String[] messages) {
	    this.messages = messages;
	  }

	  public Object getValidateMessages() {
	    return this.validateMessages;
	  }

	  public void setValidateMessages(Object validateMessages) {
	    this.validateMessages = validateMessages;
	  }
}
