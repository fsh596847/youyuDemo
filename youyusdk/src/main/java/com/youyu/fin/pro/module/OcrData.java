package com.youyu.fin.pro.module;

/**
 * Created by HP on 2017/6/29.
 */

public class OcrData {

    private String idCardFront;
    private String idCardFrontSuffix;
    private String idCardBack;
    private String idCardBackSuffix;
    private String cmd;


    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardFrontSuffix() {
        return idCardFrontSuffix;
    }

    public void setIdCardFrontSuffix(String idCardFrontSuffix) {
        this.idCardFrontSuffix = idCardFrontSuffix;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardBackSuffix() {
        return idCardBackSuffix;
    }

    public void setIdCardBackSuffix(String idCardBackSuffix) {
        this.idCardBackSuffix = idCardBackSuffix;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
