package edu.byu.cs.tweeter.model.net.response;

public class CountResponse {
    private int count;

    public CountResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }
}
