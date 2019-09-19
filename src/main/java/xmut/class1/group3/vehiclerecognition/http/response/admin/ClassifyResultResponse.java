package xmut.class1.group3.vehiclerecognition.http.response.admin;

import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;

public class ClassifyResultResponse extends BaseResponse {
    private String accuracy;

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
