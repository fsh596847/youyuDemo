package consumer.fin.rskj.com.library.module;

/**
 * Created by HP on 2017/10/26.
 */

public class LoanPurpose {

    private int editLevel;
    private int editType;
    private String enumCode;
    private String enumDesc;
    private String enumName;
    private String enumValue;
    private int enumOrder;
    private String enumId;
    private String enumPid;


    public int getEditLevel() {
        return editLevel;
    }

    public void setEditLevel(int editLevel) {
        this.editLevel = editLevel;
    }

    @Override
    public String toString() {
        return "LoanPurpose{" +
                "editLevel=" + editLevel +
                ", editType=" + editType +
                ", enumCode='" + enumCode + '\'' +
                ", enumDesc='" + enumDesc + '\'' +
                ", enumName='" + enumName + '\'' +
                ", enumValue='" + enumValue + '\'' +
                ", enumOrder=" + enumOrder +
                ", enumId='" + enumId + '\'' +
                ", enumPid='" + enumPid + '\'' +
                '}';
    }

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public String getEnumCode() {
        return enumCode;
    }

    public void setEnumCode(String enumCode) {
        this.enumCode = enumCode;
    }

    public String getEnumDesc() {
        return enumDesc;
    }

    public void setEnumDesc(String enumDesc) {
        this.enumDesc = enumDesc;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public int getEnumOrder() {
        return enumOrder;
    }

    public void setEnumOrder(int enumOrder) {
        this.enumOrder = enumOrder;
    }

    public String getEnumId() {
        return enumId;
    }

    public void setEnumId(String enumId) {
        this.enumId = enumId;
    }

    public String getEnumPid() {
        return enumPid;
    }

    public void setEnumPid(String enumPid) {
        this.enumPid = enumPid;
    }
}
