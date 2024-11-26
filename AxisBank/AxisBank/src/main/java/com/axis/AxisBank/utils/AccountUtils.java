package com.axis.AxisBank.utils;

import java.time.Year;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.floor;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This User Already Has An Account Created.";

    public static final String ACCOUNT_CREATION_SUCCESS =  "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account Has Been Created.";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with provided Account Number does not exists.";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found.";

    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Amount Credited Successfully";

    public static final String ACCOUNT_DEBITED_SUCCESS = "006";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Amount Debited Successfully";

    public static final String INSUFFICIENT_BALANCE_CODE = "007";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "You don't have sufficient balance to debit amount.";

    public static final String SUCCESSFULLY_AMOUNT_TRANSFERRED_CODE = "008";
    public static final String SUCCESSFULLY_AMOUNT_TRANSFERRED_MESSAGE = "The amount has been successfully transferred.";


    public static String generateAccountNumber() {
        // Static set to keep track of generated account numbers
        Set<String> generatedAccountNumbers = new HashSet<>();
        Random random = new Random();

        // Account number should be of 12 digits
        // 4 digits should be current year + random 8 digits
        Year currentYear = Year.now();
        String year = String.valueOf(currentYear.getValue());

        String accountNumber;
        do {
            long min = 10000000L;
            long max = 99999999L;

            // Generate random number between min and max
            long randNumber = min + (long) (random.nextDouble() * (max - min + 1));

            // Convert the random number to a string
            String randomNumber = String.valueOf(randNumber);

            // Concatenate year and random number to form the account number
            accountNumber = year + randomNumber;
        } while (generatedAccountNumbers.contains(accountNumber));

        // Add the newly generated account number to the set
        generatedAccountNumbers.add(accountNumber);

        return accountNumber;
    }


}
