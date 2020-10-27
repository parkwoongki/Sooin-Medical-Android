package cse.mobile.sooinmedical.vo;

import java.io.Serializable;

public class BasketVo implements Serializable {
    private String item;
    private String manufacturer;
    private long count;

    public BasketVo() {
        this.item = null;
        this.manufacturer = null;
        this.count = 0;
    }

    public BasketVo(String item, String manufacturer, long count) {
        this.item = item;
        this.manufacturer = manufacturer;
        this.count = count;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
