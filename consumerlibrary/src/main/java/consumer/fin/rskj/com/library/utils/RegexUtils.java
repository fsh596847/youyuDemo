package consumer.fin.rskj.com.library.utils;

import android.text.TextUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sks
 *         正则表达式的工具类
 */
public class RegexUtils {
  private final static String reg_idcard =
      "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

  private final static String phoneReg = "^1[3|4|5|7|8|][0-9]{9}$";
  private final static String BLANK = " ";

  /**
   * ^ 匹配一行的开头位置
   * (?![0-9]+$) 预测该位置后面不全是数字
   * (?![a-zA-Z]+$) 预测该位置后面不全是字母
   * [0-9A-Za-z] {8,16} 由8-16位数字或这字母组成
   * $ 匹配行结尾位置
   */
  private final static String PASSWORD_MATCHER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

  //密码合法性验证
  public static boolean passwordMatcher(String passWord) {

    if (passWord.length() < 6 || passWord.length() > 20) {
      return false;
    }

    return passWord.matches(PASSWORD_MATCHER);
  }

  /**
   * 是否匹配电话号码
   */
  public static boolean isMatchPhoneNum(String phoneNum) {
    if (TextUtils.isEmpty(phoneNum)) {
      return false;
    }
    return phoneNum.matches(phoneReg);
  }

  /**
   * 身份证合法性
   */
  public static boolean isIdCardNum(String idcard) {
    if (TextUtils.isEmpty(idcard)) {
      return false;
    }

    if (idcard.length() != 18) {
      return false;
    }

    return idcard.matches(reg_idcard);
    //Pattern p_idcard = Pattern.compile(reg_idcard);
    //Matcher m_idcard = p_idcard.matcher(idCards);
    //flg_idcard = m_idcard.matches();
  }

  /**
   * 将手机号格式化
   */
  public static String getRegPhoneNum(String phoneNum) {
    if (phoneNum == null) {
      return "";
    }
    StringBuilder builder = new StringBuilder(phoneNum);
    if (phoneNum.length() > 7) {
      builder.insert(3, BLANK);
      builder.insert(8, BLANK);
    } else if (phoneNum.length() > 4) {
      builder.insert(3, BLANK);
    }
    return builder.toString();
  }

  /**
   * 得到除首字符以为的字符全部替换成“*”的新字符串
   */
  public static String getRegName(String name) {
    String regName = "";
    if (TextUtils.isEmpty(name)) {
      return regName;
    }
    if (name.length() == 1) {
      return name;
    }
    String targetName = name.substring(1, name.length());//要替换的姓名
    StringBuilder sb = new StringBuilder();//"*"字符串
    for (int i = 0; i < targetName.length(); i++) {
      sb.append("*");
    }
    regName = name.replace(targetName, sb.toString());
    return regName;
  }

  /**
   * 金额格式化
   *
   * @return 格式后的金额
   */
  public static String insertComma(String number) {
    if (number == null || number.length() < 1) {
      return "";
    }
    String s = delComma(number);
    NumberFormat formater = null;
    double num = Double.parseDouble(s);
    if (s.contains(".")) {
      String[] arr = s.split(".");
      StringBuffer buff = new StringBuffer();
      buff.append("###,###.");
      if (arr.length > 1) {//有小数位
        for (int i = 0; i < arr[1].length(); i++) {
          buff.append("#");
        }
      } else {//默认添加一个小数位
        buff.append("#");
      }
      formater = new DecimalFormat(buff.toString());
    } else {
      formater = new DecimalFormat("###,###");
    }
    return formater.format(num);
  }

  /**
   * 金额去掉“,”
   *
   * @param s 金额
   * @return 去掉“,”后的金额
   */
  public static String delComma(String s) {
    String formatString = "";
    if (s != null && s.length() >= 1) {
      formatString = s.replaceAll(",", "");
    }

    return formatString;
  }

  public static boolean isValidEmail(String email) {//邮箱判断正则表达式
    String str =
        "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
    Pattern pattern = Pattern.compile(str);
    Matcher mc = pattern.matcher(email);
    return mc.matches();
  }

  public static String[] getImageTag(String content) {
    String img = "";
    Pattern p_image;
    Matcher m_image;
    String str = "";
    String[] images = null;
    String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
    p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
    m_image = p_image.matcher(content);
    while (m_image.find()) {
      img = m_image.group();

      Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)")
          .matcher(img);
      while (m.find()) {
        String tempSelected = m.group(1);
        if ("".equals(str)) {
          str = tempSelected;
        } else {
          String temp = tempSelected;
          str = str + "," + temp;
        }
      }
    }
    if (!"".equals(str)) {
      images = str.split(",");
    }

    return images;
  }
}
