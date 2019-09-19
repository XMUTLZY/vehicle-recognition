package xmut.class1.group3.vehiclerecognition.http.vo.image;

import xmut.class1.group3.vehiclerecognition.constants.ImageConstant;

import java.io.Serializable;
import java.util.Date;

public class Image implements Serializable {
    private Integer id;
    private Integer isSign = ImageConstant.IS_SIGN_FALSE;
    private String image;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
