package com.qilu.ec.main.sample;

public class DecorateHistory {
    private String time;
    private String img_base64;

    public DecorateHistory(String time, String img_base64) {
        this.time = time;
        this.img_base64 = img_base64;
    }

    public String getTime() {
        return time;
    }

    public String getImg_base64() {
        return img_base64;
    }

}
