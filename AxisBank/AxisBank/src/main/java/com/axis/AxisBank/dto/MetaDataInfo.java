package com.axis.AxisBank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataInfo {

    @JsonProperty("apiversion")
    private String apiversion;
    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("channelType")
    private String channelType;
    @JsonProperty("timeStamp")
    private String timeStamp;
    @JsonProperty("hCheckValue")
    private String hCheckValue;
    @JsonProperty("requestUUID")
    private String requestUUID;
    @JsonProperty("serRequestId")
    private String serRequestId;
    @JsonProperty("journeyId")
    private String journeyId;

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String gethCheckValue() {
        return hCheckValue;
    }

    public void sethCheckValue(String hCheckValue) {
        this.hCheckValue = hCheckValue;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    public String getSerRequestId() {
        return serRequestId;
    }

    public void setSerRequestId(String serRequestId) {
        this.serRequestId = serRequestId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }




}
