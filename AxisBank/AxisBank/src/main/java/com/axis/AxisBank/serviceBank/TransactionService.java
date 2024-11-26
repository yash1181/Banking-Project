package com.axis.AxisBank.serviceBank;

import com.axis.AxisBank.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
