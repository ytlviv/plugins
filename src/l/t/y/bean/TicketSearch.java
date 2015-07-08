package l.t.y.bean;

public class TicketSearch {
	 private String train_date;
	  private String from_station;
	  private String to_station;
	  private String from_station_name;
	  private String to_station_name;
	  private String to_date;
	  private String w_flag = "dc";

	  private String purpose_codes = "ADULT";

	  public String getTrain_date() {
	    return this.train_date;
	  }

	  public void setTrain_date(String train_date) {
	    this.train_date = train_date;
	  }

	  public String getFrom_station() {
	    return this.from_station;
	  }

	  public void setFrom_station(String from_station) {
	    this.from_station = from_station;
	  }

	  public String getTo_station() {
	    return this.to_station;
	  }

	  public void setTo_station(String to_station) {
	    this.to_station = to_station;
	  }

	  public String getFrom_station_name() {
	    return this.from_station_name;
	  }

	  public void setFrom_station_name(String from_station_name) {
	    this.from_station_name = from_station_name;
	  }

	  public String getTo_station_name() {
	    return this.to_station_name;
	  }

	  public void setTo_station_name(String to_station_name) {
	    this.to_station_name = to_station_name;
	  }

	  public String getTo_date() {
	    return this.to_date;
	  }

	  public void setTo_date(String to_date) {
	    this.to_date = to_date;
	  }

	  public String getW_flag() {
	    return this.w_flag;
	  }

	  public void setW_flag(String w_flag) {
	    this.w_flag = w_flag;
	  }

	  public String getPurpose_codes() {
	    return this.purpose_codes;
	  }

	  public void setPurpose_codes(String purpose_codes) {
	    this.purpose_codes = purpose_codes;
	  }
}
