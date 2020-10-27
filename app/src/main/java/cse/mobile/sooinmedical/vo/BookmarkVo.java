package cse.mobile.sooinmedical.vo;

import java.io.Serializable;

public class BookmarkVo implements Serializable {
    private String item;
    private String manufacturer;

    public BookmarkVo() {
        this.item = null;
        this.manufacturer = null;
    }

    public BookmarkVo(String item, String manufacturer) {
        this.item = item;
        this.manufacturer = manufacturer;
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
}
