package cse.mobile.sooinmedical.vo;

import androidx.annotation.NonNull;

public class SuppliesVo {
    private String no;
    private String manufacturer;
    private String item;
    private String standard;

    public SuppliesVo() {
        this.no = "No";
        this.manufacturer = "제조사";
        this.item = "제품명";
        this.standard = "규격";
    }

    public SuppliesVo(String no, String manufacturer, String item, String standard) {
        this.no = no;
        this.manufacturer = manufacturer;
        this.item = item;
        this.standard = standard;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n제품명\n: " + this.getItem()
                + "\n\n제조사\n: " + this.getManufacturer()
                + "\n\n규격\n: " + this.getStandard() + "\n";
    }
}
