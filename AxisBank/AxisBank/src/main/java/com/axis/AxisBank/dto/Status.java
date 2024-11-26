package com.axis.AxisBank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Status {
    private boolean s;
    private String m;
    private String c;
}
