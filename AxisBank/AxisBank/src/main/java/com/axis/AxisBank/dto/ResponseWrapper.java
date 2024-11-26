package com.axis.AxisBank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseWrapper {
    private MetaDataInfo md;
    private String r; // This will contain the serialized BankResponse
    private Status s;
}