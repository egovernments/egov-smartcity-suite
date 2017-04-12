package org.egov.collection.integration.models;

public class RequestInfo {

    private String apiId;
    private String ver;
    private String ts;
    private String action;
    private String did;
    private String key;
    private String msgId;
    private String requesterId;
    private String authToken;
    private User userInfo;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(final String apiId) {
        this.apiId = apiId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(final String ver) {
        this.ver = ver;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(final String ts) {
        this.ts = ts;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getDid() {
        return did;
    }

    public void setDid(final String did) {
        this.did = did;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(final String msgId) {
        this.msgId = msgId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(final String requesterId) {
        this.requesterId = requesterId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
