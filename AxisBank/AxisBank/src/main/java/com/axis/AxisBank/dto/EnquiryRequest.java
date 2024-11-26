package com.axis.AxisBank.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnquiryRequest {

    private String accountNumber;

    public MetaDataInfo getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataInfo metaData) {
        this.metaData = metaData;
    }

    private MetaDataInfo metaData;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
