package l.t.y.bean;

public class LoginUserInfo {
	private String username;
	  private String password;
	  private String randcode;

	  public LoginUserInfo()
	  {
	  }

	  public LoginUserInfo(String username, String password, String randcode)
	  {
	    this.username = username;
	    this.password = password;
	    this.randcode = randcode;
	  }

	  public String getUsername() {
	    return this.username;
	  }

	  public void setUsername(String username) {
	    this.username = username;
	  }

	  public String getPassword() {
	    return this.password;
	  }

	  public void setPassword(String password) {
	    this.password = password;
	  }

	  public String getRandcode() {
	    return this.randcode;
	  }

	  public void setRandcode(String randcode) {
	    this.randcode = randcode;
	  }
}
