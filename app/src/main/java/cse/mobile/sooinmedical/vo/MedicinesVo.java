package cse.mobile.sooinmedical.vo;

import androidx.annotation.NonNull;

public class MedicinesVo {
    private String no;
    private String item;
    private String standard;
    private String manufacturer;
    private String product_classification; // 너무 길어서 카멜 기법은.. 패스
    private String shape_classification;
    private String medicinal_ingredients_classification_code;
    private String medicinal_ingredients_classification;
    private String medicinal_ingredients_code;
    private String medicinal_ingredients;
    private String prescription_classification;

    public MedicinesVo(String no, String item, String standard, String manufacturer, String product_classification, String shape_classification, String medicinal_ingredients_classification_code, String medicinal_ingredients_classification, String medicinal_ingredients_code, String medicinal_ingredients, String prescription_classification) {
        this.no = no;
        this.item = item;
        this.standard = standard;
        this.manufacturer = manufacturer;
        this.product_classification = product_classification;
        this.shape_classification = shape_classification;
        this.medicinal_ingredients_classification_code = medicinal_ingredients_classification_code;
        this.medicinal_ingredients_classification = medicinal_ingredients_classification;
        this.medicinal_ingredients_code = medicinal_ingredients_code;
        this.medicinal_ingredients = medicinal_ingredients;
        this.prescription_classification = prescription_classification;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProduct_classification() {
        return product_classification;
    }

    public void setProduct_classification(String product_classification) {
        this.product_classification = product_classification;
    }

    public String getShape_classification() {
        return shape_classification;
    }

    public void setShape_classification(String shape_classification) {
        this.shape_classification = shape_classification;
    }

    public String getMedicinal_ingredients_classification_code() {
        return medicinal_ingredients_classification_code;
    }

    public void setMedicinal_ingredients_classification_code(String medicinal_ingredients_classification_code) {
        this.medicinal_ingredients_classification_code = medicinal_ingredients_classification_code;
    }

    public String getMedicinal_ingredients_classification() {
        return medicinal_ingredients_classification;
    }

    public void setMedicinal_ingredients_classification(String medicinal_ingredients_classification) {
        this.medicinal_ingredients_classification = medicinal_ingredients_classification;
    }

    public String getMedicinal_ingredients_code() {
        return medicinal_ingredients_code;
    }

    public void setMedicinal_ingredients_code(String medicinal_ingredients_code) {
        this.medicinal_ingredients_code = medicinal_ingredients_code;
    }

    public String getMedicinal_ingredients() {
        return medicinal_ingredients;
    }

    public void setMedicinal_ingredients(String medicinal_ingredients) {
        this.medicinal_ingredients = medicinal_ingredients;
    }

    public String getPrescription_classification() {
        return prescription_classification;
    }

    public void setPrescription_classification(String prescription_classification) {
        this.prescription_classification = prescription_classification;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n의약품명 :\n" + this.getItem()
                + "\n\n규격 :\n" + this.getStandard()
                + "\n\n제조사 :\n" + this.getManufacturer()
                + "\n\n제품구분 :\n" + this.getProduct_classification()
                + "\n\n제형구분 :\n" + this.getShape_classification()
                + "\n\n성분분류코드 :\n" + this.getMedicinal_ingredients_classification_code()
                + "\n\n성분분류 :\n" + this.getMedicinal_ingredients_classification()
                + "\n\n성분코드 :\n" + this.getMedicinal_ingredients_code()
                + "\n\n성분 :\n" + this.getMedicinal_ingredients()
                + "\n\n특수제품 :\n" + this.getPrescription_classification() + "\n";
    }
}
