package xmut.class1.group3.vehiclerecognition.http.response.admin;

import xmut.class1.group3.vehiclerecognition.http.response.BaseResponse;
import java.util.Map;

public class TestResultResponse extends BaseResponse {
    private Map<String, String> resultMap;

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }
}
