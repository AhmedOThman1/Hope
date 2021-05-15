package com.hopeTheRobot.pojo;

public class HighTempItem {

    private Long dateMillis;
    private Double temp;
    private String image;

    public HighTempItem() {
    }

    public Long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(Long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
