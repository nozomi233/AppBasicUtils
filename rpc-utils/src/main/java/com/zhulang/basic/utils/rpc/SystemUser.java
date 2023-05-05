package com.zhulang.basic.utils.rpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
@Data
public class SystemUser implements Serializable {

    /**
     * 运营网关用户参数
     */
    @JsonProperty("_user")
    protected BaseSystemUser user;

    protected String userAgent;


    public String getRealName() {
        if (user != null) {
            return user.getRealName();
        }
        return null;
    }

    public String getGuid() {
        if (user != null) {
            return user.getGuid();
        }
        return null;
    }

    public String getUserName() {
        if (user != null) {
            return user.getUserName();
        }
        return null;
    }

    public String getEmail() {
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    @Data
    @EqualsAndHashCode
    public static class BaseSystemUser implements Serializable {
        private String guid;

        private String userName;

        private String realName;

        private String email;
    }


}
