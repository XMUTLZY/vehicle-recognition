package xmut.class1.group3.vehiclerecognition.http.response;

import java.util.List;

public class BaseResponse<T> {

    public static final Integer SUCCESS_CODE = 0;
    public static final Integer FAILED_CODE = 500;
    public static final Integer NOT_FOUND_CODE = 404;
    public static final String MSG_SUCCESS = "请求成功";
    public static final String MSG_FAILED = "请求失败";

    public BaseResponse() {
        this.code = SUCCESS_CODE;
        this.msg = MSG_SUCCESS;
    }

    private Integer code;

    private String msg;

    private Integer count;

    private List<T> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
