package com.regent.rpush.client.sample;

import com.beust.jcommander.Parameter;

/**
 * 配置类
 *
 * @author 钟宝林
 * @since 2021/7/17/017 16:09
 **/
public final class Config {

    @Parameter(names = "-s", description = "服务地址")
    private String servicePath;
    @Parameter(names = "-ci", description = "clientId")
    private String clientId;
    @Parameter(names = "-cs", description = "clientSecret")
    private String clientSecret;
    @Parameter(names = "-l", description = "登录名")
    private String loginName;
    @Parameter(names = "-p", description = "登录密码")
    private String password;

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
