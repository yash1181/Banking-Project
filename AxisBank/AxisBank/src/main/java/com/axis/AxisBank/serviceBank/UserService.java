package com.axis.AxisBank.serviceBank;

import com.axis.AxisBank.dto.*;

public interface UserService {

    ResponseWrapper createAccount(UserRequest userRequest);
    ResponseWrapper balanceEnquiry(EnquiryRequest request);
    ResponseWrapper nameEnquiry(EnquiryRequest request);
    ResponseWrapper creditAccount(CreditDebitRequest request);
    ResponseWrapper debitAccount(CreditDebitRequest request);
    ResponseWrapper transfer(TransferRequest request);

}
