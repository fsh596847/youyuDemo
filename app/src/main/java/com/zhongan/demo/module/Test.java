package com.zhongan.demo.module;

import java.util.List;

/**
 * Created by HIPAA on 2017/11/8.
 */

public class Test {

  /**
   * code : success
   * message :
   * data : {"gdtj":[{"advertTitle":"erjihuanle","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/n3slmomds0.png","advertUrl":"http://hl.vcash.cn/resource/register/index.html?id=11351&code=RS0001","productId":null},{"advertTitle":"jielehua","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/xl0p3mhp10.png","advertUrl":"https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=&WT.mc_id=N57FHXM2073F662300HZ&dsid=MYY000001","productId":null},{"advertTitle":"lirenhui","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/jsb760ef0l.png","advertUrl":"http://wechat.lrh.vcredit.com/transferRegister?account=38404&qcrcode=https%3A%2F%2Fmp.weixin.qq.com%2Fcgi-bin%2Fshowqrcode%3Fticket%3DgQFj8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyWlA1ZnA0Q21iYy0xMDAwMDAwN0sAAgQM_QpZAwQAAAAA","productId":null}],"bktj":[{"advertTitle":"zhaoshangyinhang","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170920/19kjuv97wy.png","advertUrl":"https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=&WT.mc_id=N57FHXM2073F662300HZ&dsid=MYY000001","productId":null},{"advertTitle":"jiaotongyinhang","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170920/pgyzyj486n.png","advertUrl":"https://creditcardapp.bankcomm.com/applynew/front/apply/mgm/account/wechatEntry.html?recomId=15371153&saleCode=017dd0100&entryRecomId=&trackCode=A082515495803&from=groupmessage","productId":null}],"rmcp":[{"advertTitle":"mashanghuan","advertPic":"http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/u37cridpgr.png","advertUrl":"https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=&WT.mc_id=N57FHXM2073F662300HZ&dsid=MYY000001","productId":null}]}
   */

  private String code;
  private String message;
  private DataBean data;


  public static class DataBean {
    private List<GdtjBean> gdtj;
    private List<BktjBean> bktj;
    private List<RmcpBean> rmcp;



    public static class GdtjBean {
      /**
       * advertTitle : erjihuanle
       * advertPic : http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/n3slmomds0.png
       * advertUrl : http://hl.vcash.cn/resource/register/index.html?id=11351&code=RS0001
       * productId : null
       */

      private String advertTitle;
      private String advertPic;
      private String advertUrl;
      private Object productId;


    }

    public static class BktjBean {
      /**
       * advertTitle : zhaoshangyinhang
       * advertPic : http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170920/19kjuv97wy.png
       * advertUrl : https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=&WT.mc_id=N57FHXM2073F662300HZ&dsid=MYY000001
       * productId : null
       */

      private String advertTitle;
      private String advertPic;
      private String advertUrl;
      private Object productId;


    }

    public static class RmcpBean {
      /**
       * advertTitle : mashanghuan
       * advertPic : http://test.xmqq99.com/website/res/webSite/positionMapping/img/20170919/u37cridpgr.png
       * advertUrl : https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=&WT.mc_id=N57FHXM2073F662300HZ&dsid=MYY000001
       * productId : null
       */

      private String advertTitle;
      private String advertPic;
      private String advertUrl;
      private Object productId;


    }
  }
}
