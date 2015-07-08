package l.t.y.thread;

import java.util.HashMap;
import java.util.Map;

import l.t.y.bean.LoginUserInfo;
import l.t.y.helper.HttpHeader;
import l.t.y.helper.JSONHelper;
import l.t.y.helper.StringHelper;
import l.t.y.main.MainWin;
import l.t.y.msg.CheckCodeMsg;
import l.t.y.msg.CheckUserMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(LoginThread.class);
	  private MainWin mainWin;
	  private LoginUserInfo user;
	  private JSONHelper jsonHelper = JSONHelper.buildNormalIgnoreBinder();

	  private CheckCodeMsg codeMsg = null;

	  private CheckUserMsg userMsg = null;

	  private boolean isLogin = false;

	  public LoginThread()
	  {
	  }

	  public LoginThread(LoginUserInfo user, MainWin mainWin) {
	    this.mainWin = mainWin;
	    this.user = user;
	  }

	  public void run()
	  {
	    while (!this.isLogin)
	    {
	      Map params = new HashMap();
	      params.put("rand", "sjrand");
	      params.put("randCode", this.user.getRandcode());
	      try {
	        String msg = this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn", params, HttpHeader.postCheckCode(true), null, false);
	        this.codeMsg = ((CheckCodeMsg)this.jsonHelper.parse(msg, CheckCodeMsg.class));
	      } catch (Exception e) {
	        logger.error("check code error : ", e);
	      }
	      
//	      continue;

	      if ((this.codeMsg.getHttpstatus() != 200) || (this.codeMsg.getStatus() != true) || (!StringHelper.isEqualString(this.codeMsg.getData(), "Y"))) {
	        this.mainWin.showMsg("网络不通或验证码错误,请重试!");
	        break;
	      }

	      params = null;
	      try {
	        params = new HashMap();
	        params.put("loginUserDTO.user_name", this.user.getUsername());
	        params.put("randCode", this.user.getRandcode());
	        params.put("userDTO.password", this.user.getPassword());
	        String msg = this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/login/loginAysnSuggest", params, HttpHeader.postCheckCode(true), null, false);
	        this.userMsg = ((CheckUserMsg)this.jsonHelper.parse(msg, CheckUserMsg.class));
	      } catch (Exception e) {
	        logger.error("check user error : ", e);
	      }
	      
//	      continue;

	      if (this.userMsg.getData() == null) {
	        this.mainWin.showMsg(this.userMsg.getMessages()[0]);
	      }

	      params = null;
	      try {
	        params = new HashMap();
	        params.put("_json_att", "");
	        this.mainWin.client.postRequest("http://kyfw.12306.cn/otn/login/userLogin", params, HttpHeader.login(), null, false);
	      } catch (Exception e) {
	        logger.error("login user error : ", e);
	      }continue;

//	      this.isLogin = true;
//	      this.mainWin.isLogin = true;
//	      this.mainWin.messageOut.setText(this.mainWin.messageOut.getText() + "登录成功!\n");
//	      this.mainWin.showMsg("登录成功!");
	    }
	  }
}
