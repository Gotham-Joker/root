package com.ht.api.caller.config;

public class RetrofitProperties {
    private String baseUrl;
    /**
     * 信任所有证书
     */
    private Boolean trustAllCert = false;
    private Integer timeout = 10;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getTrustAllCert() {
        return trustAllCert;
    }

    public void setTrustAllCert(Boolean trustAllCert) {
        this.trustAllCert = trustAllCert;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
