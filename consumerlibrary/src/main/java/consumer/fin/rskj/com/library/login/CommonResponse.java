package consumer.fin.rskj.com.library.login;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回数据
 */
public class CommonResponse {
  //响应代码 fail或者success
  private String code;
  //响应信息
  private String message;
  //响应数据
  private Map<String, Object> data;

  public CommonResponse() {
    this("success", "");
  }

  public CommonResponse(String code, String message) {
    this.code = code;
    this.message = message;
    this.data = new HashMap<String, Object>();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setAttribute(String key, Object value) {
    this.data.put(key, value);
  }

  public Object getAttribute(String key) {
    return this.data.get(key);
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
