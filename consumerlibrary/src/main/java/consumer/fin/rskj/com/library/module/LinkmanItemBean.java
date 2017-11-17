package consumer.fin.rskj.com.library.module;

/**
 * 联系人实体
 */

public class LinkmanItemBean
{
    private String name;//联系人姓名
    private String telephone;//联系人电话
    private String company;//公司
    private String remarks;//备注

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "LinkmanItemBean{" +
                "name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", company='" + company + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
