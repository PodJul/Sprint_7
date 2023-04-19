package pojo;

import io.qameta.allure.Step;

public class OrderTrack {
    private int track;
    public OrderTrack(int track){
        this.track=track;
    }
    public OrderTrack(){}
    @Step("Get order track")
    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }
}
