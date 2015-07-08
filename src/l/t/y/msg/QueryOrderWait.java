package l.t.y.msg;

public class QueryOrderWait {
	private boolean queryOrderWaitTimeStatus;
	  private int count;
	  private int waitTime;
	  private String requestId;
	  private String waitCount;
	  private String tourFlag;
	  private String orderId;

	  public boolean isQueryOrderWaitTimeStatus()
	  {
	    return this.queryOrderWaitTimeStatus;
	  }

	  public void setQueryOrderWaitTimeStatus(boolean queryOrderWaitTimeStatus) {
	    this.queryOrderWaitTimeStatus = queryOrderWaitTimeStatus;
	  }

	  public int getCount() {
	    return this.count;
	  }

	  public void setCount(int count) {
	    this.count = count;
	  }

	  public int getWaitTime() {
	    return this.waitTime;
	  }

	  public void setWaitTime(int waitTime) {
	    this.waitTime = waitTime;
	  }

	  public String getRequestId() {
	    return this.requestId;
	  }

	  public void setRequestId(String requestId) {
	    this.requestId = requestId;
	  }

	  public String getWaitCount() {
	    return this.waitCount;
	  }

	  public void setWaitCount(String waitCount) {
	    this.waitCount = waitCount;
	  }

	  public String getTourFlag() {
	    return this.tourFlag;
	  }

	  public void setTourFlag(String tourFlag) {
	    this.tourFlag = tourFlag;
	  }

	  public String getOrderId() {
	    return this.orderId;
	  }

	  public void setOrderId(String orderId) {
	    this.orderId = orderId;
	  }
}
