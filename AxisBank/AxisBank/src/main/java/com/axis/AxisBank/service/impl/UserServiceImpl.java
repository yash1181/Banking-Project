package com.axis.AxisBank.service.impl;

import com.axis.AxisBank.dto.*;
import com.axis.AxisBank.entity.User;
import com.axis.AxisBank.entity.MetaData;
import com.axis.AxisBank.repository.MetaDataRepository;
import com.axis.AxisBank.repository.UserRepository;
import com.axis.AxisBank.serviceBank.EmailService;
import com.axis.AxisBank.serviceBank.TransactionService;
import com.axis.AxisBank.serviceBank.UserService;
import com.axis.AxisBank.utils.AccountUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Meta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    MetaDataRepository metaDataRepository;

    @Autowired
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, MetaDataRepository metaDataRepository) {
        this.userRepository = userRepository;
        this.metaDataRepository = metaDataRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

   /* @Override
    public BankResponse createAccount(UserRequest userRequest) {
        *//* Creating an Account - saving a new user in database  *//*
        //Check if user already has an account

        MetaDataInfo md = userRequest.getMd();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();

            metaDataRepository.save(metadataEntity);
        }

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }


        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .addharNumber(userRequest.getAddharNumber())
                .panNumber(userRequest.getPanNumber())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);
        //Send Email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created")
                .messageBody("Congratulations on opening an account with us! We are happy to have you onboard!\n Your Account has been successfully created.\n" +
                        " Your Account details : \n" +
                        "Account Name : "+ savedUser.getLastName() +" " +savedUser.getFirstName()+"\nAccount number : "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+ savedUser.getLastName())
                        .build())
                .build();

    }*/

    @Override
    public ResponseWrapper createAccount(UserRequest userRequest) {
        // Extract metadata from the request
        MetaDataInfo md = userRequest.getMd();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();
            metaDataRepository.save(metadataEntity);
        }

        // Check if user already exists by email
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return buildResponseWrapper(
                    md,
                    AccountUtils.ACCOUNT_EXISTS_CODE,
                    AccountUtils.ACCOUNT_EXISTS_MESSAGE,
                    null,
                    false
            );
        }

        // Create a new user entity
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .addharNumber(userRequest.getAddharNumber())
                .panNumber(userRequest.getPanNumber())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(encryptedPassword)
                .phoneNumber(userRequest.getPhoneNumber())
                .status("ACTIVE")
                .build();

        // Save the new user in the database
        User savedUser = userRepository.save(newUser);

        // Send email notification
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created")
                .messageBody("Congratulations on opening an account with us! We are happy to have you onboard!\n Your Account has been successfully created.\n" +
                        " Your Account details : \n" +
                        "Account Name : " + savedUser.getLastName() + " " + savedUser.getFirstName() + "\nAccount number : " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        // Build and return the response wrapper
        return buildResponseWrapper(
                md,
                AccountUtils.ACCOUNT_CREATION_SUCCESS,
                AccountUtils.ACCOUNT_CREATION_MESSAGE,
                AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build(),
                true
        );
    }

    private ResponseWrapper buildResponseWrapper(MetaDataInfo md, String responseCode, String responseMessage, AccountInfo accountInfo, boolean status) {
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .accountInfo(accountInfo)
                .build();

        return ResponseWrapper.builder()
                .md(md)
                .r(serializeResponse(bankResponse))
                .s(Status.builder()
                        .s(status)
                        .m(responseMessage)
                        .c(responseCode)
                        .build())
                .build();
    }

    private String serializeResponse(BankResponse bankResponse) {
        try {
            return objectMapper.writeValueAsString(bankResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /*@Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        //Check id the provided account number exists in db
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                        .build())
                .build();
    }*/


    @Override
    public ResponseWrapper balanceEnquiry(EnquiryRequest request) {
        MetaDataInfo md = request.getMetaData();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No MetaData Found in the request");
        }

        // Check if the provided account number exists in the database
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return buildResponseWrapper(md, AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null, false);
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();

        // Build and return the response wrapper
        return buildResponseWrapper(md, AccountUtils.ACCOUNT_EXISTS_CODE, AccountUtils.ACCOUNT_EXISTS_MESSAGE, bankResponse.getAccountInfo(), true);
    }



    @Override
    public ResponseWrapper nameEnquiry(EnquiryRequest request) {
        MetaDataInfo md = request.getMetaData();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No MetaData Found in the request");
        }

        // Check if account exists
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return buildResponseWrapper(md, AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null, false);
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        AccountInfo accountInfo = AccountInfo.builder()
                .accountNumber(request.getAccountNumber())
                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                .build();

        // Build and return responseWrapper
        return buildResponseWrapper(md, AccountUtils.ACCOUNT_EXISTS_CODE, AccountUtils.ACCOUNT_EXISTS_MESSAGE, accountInfo, true);
    }





    /*@Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // Save Metadata
        MetaDataInfo md = request.getMetadata();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();

            // Logging to confirm the values before saving
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No metadata found in the request");
        }

        // Checking if the account exists
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal currentBalance = new BigDecimal(userToCredit.getAccountBalance().toString());
        BigDecimal creditAmount = new BigDecimal(request.getAmount().toString());

        userToCredit.setAccountBalance(currentBalance.add(creditAmount));
        userRepository.save(userToCredit);

        // Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();

        // Create email details
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("Amount Credited")
                .messageBody("Your account has been successfully credited.\n" +
                        "Account Name: " + userToCredit.getFirstName() + " " + userToCredit.getLastName() + "\n" +
                        "Account Number: " + userToCredit.getAccountNumber() + "\n" +
                        "Credited Amount: " + creditAmount.toString() + "\n" +
                        "New Balance: " + userToCredit.getAccountBalance())
                .build();

        // Send email alert
        emailService.sendEmailAlert(emailDetails);

        return response;
    }*/

    @Override
    public ResponseWrapper creditAccount(CreditDebitRequest request) {
        // Save Metadata
        MetaDataInfo md = request.getMetadata();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();

            // Logging to confirm the values before saving
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No metadata found in the request");
        }

        // Checking if the account exists
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return buildResponseWrapper(md, AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null, false);
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal currentBalance = new BigDecimal(userToCredit.getAccountBalance().toString());
        BigDecimal creditAmount = new BigDecimal(request.getAmount().toString());

        userToCredit.setAccountBalance(currentBalance.add(creditAmount));
        userRepository.save(userToCredit);

        // Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        // Create BankResponse
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();

        // Create email details
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToCredit.getEmail())
                .subject("Amount Credited")
                .messageBody("Your account has been successfully credited.\n" +
                        "Account Name: " + userToCredit.getFirstName() + " " + userToCredit.getLastName() + "\n" +
                        "Account Number: " + userToCredit.getAccountNumber() + "\n" +
                        "Credited Amount: " + creditAmount.toString() + "\n" +
                        "New Balance: " + userToCredit.getAccountBalance())
                .build();

        // Send email alert
        emailService.sendEmailAlert(emailDetails);

        // Build and return ResponseWrapper
        return buildResponseWrapper(md, AccountUtils.ACCOUNT_CREDITED_SUCCESS, AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE, bankResponse.getAccountInfo(), true);
    }

    /*@Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        System.out.println("Debiting");
        MetaDataInfo md = request.getMetadata();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();

            // Logging to confirm the values before saving
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No metadata found in the request");
        }

        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal availableBalance = new BigDecimal(userToDebit.getAccountBalance().toString());
        BigDecimal debitAmount = new BigDecimal(request.getAmount().toString());

        if (availableBalance.compareTo(debitAmount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(availableBalance.subtract(debitAmount));
            userRepository.save(userToDebit);

            //Save Transaction
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDto);

            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();

            // Create email details
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("Account Debited")
                    .messageBody("Your account has been successfully debited.\n" +
                            "Account Name: " + userToDebit.getFirstName() + " " + userToDebit.getLastName() + "\n" +
                            "Account Number: " + userToDebit.getAccountNumber() + "\n" +
                            "Debited Amount: " + debitAmount.toString() + "\n" +
                            "Remaining Balance: " + userToDebit.getAccountBalance())
                    .build();

            // Send email alert
            emailService.sendEmailAlert(emailDetails);

            return response;
        }
    }*/

    @Override
    public ResponseWrapper debitAccount(CreditDebitRequest request){
        MetaDataInfo md = request.getMetadata();
        if(md != null){
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();
            System.out.println("Saving metadata : "+metadataEntity);
            metaDataRepository.save(metadataEntity);
        }else{
            System.out.println("No MetaData Found in the request");
        }

        // check if account exists
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return buildResponseWrapper(md,AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null, false);
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal currentBalance = new BigDecimal(userToDebit.getAccountBalance().toString());
        BigDecimal debitAmount = new BigDecimal(request.getAmount().toString());

        userToDebit.setAccountBalance(currentBalance.subtract(debitAmount));
        userRepository.save(userToDebit);

        //Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        //create bankResponse
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();

        //create email details
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userToDebit.getEmail())
                .subject("Amount Debited")
                .messageBody("Your account has been successfully credited.\n" +
                        "Account Name: " + userToDebit.getFirstName() + " " + userToDebit.getLastName() + "\n" +
                        "Account Number: " + userToDebit.getAccountNumber() + "\n" +
                        "Credited Amount: " + debitAmount.toString() + "\n" +
                        "New Balance: " + userToDebit.getAccountBalance())
                .build();

        // send email alert
        emailService.sendEmailAlert(emailDetails);

        //build and return responseWrapper
        return buildResponseWrapper(md, AccountUtils.ACCOUNT_DEBITED_SUCCESS, AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE, bankResponse.getAccountInfo(), true);
    }


    /*@Override
    public BankResponse transfer(TransferRequest request) {
        //get the account number to be debited check the user is available or not
        //check if debiting amount not more than the balance
        //debit the amount
        //get the account to credit

        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0 ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("Hi "+sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName()+
                        "\nYour account is debited with the amount of "+request.getAmount()+".\n Your current balance is "
                        +sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit Alert")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Hi "+destinationAccountUser.getFirstName()+" "+destinationAccountUser.getLastName()+
                        "\nYour account is credited with the amount of "+request.getAmount()+"\nFrom "+sourceAccountUser.getFirstName()+" "+sourceAccountUser
                        .getLastName()+" with account number"+sourceAccountUser.getAccountNumber()+".\n Your current balance is "
                        +destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        //Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_CODE)
                .responseMessage(AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_MESSAGE)
                .accountInfo(null)
                .build();


    }*/


    @Override
    public ResponseWrapper transfer(TransferRequest request) {
        // Extract and save metadata if available
        MetaDataInfo md = request.getMetaData();
        if (md != null) {
            MetaData metadataEntity = MetaData.builder()
                    .apiVersion(md.getApiversion())
                    .clientID(md.getClientID())
                    .channelType(md.getChannelType())
                    .timeStamp(md.getTimeStamp())
                    .hCheckValue(md.gethCheckValue())
                    .requestUUID(md.getRequestUUID())
                    .serRequestId(md.getSerRequestId())
                    .journeyId(md.getJourneyId())
                    .build();
            System.out.println("Saving metadata: " + metadataEntity);
            metaDataRepository.save(metadataEntity);
        } else {
            System.out.println("No MetaData Found in the request");
        }

        // Check if the destination account exists
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return buildResponseWrapper(md, AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null, false);
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        // Check if the source account has sufficient balance
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
            return buildResponseWrapper(md, AccountUtils.INSUFFICIENT_BALANCE_CODE, AccountUtils.INSUFFICIENT_BALANCE_MESSAGE, null, false);
        }

        // Debit the source account
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        // Send debit alert email
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("Hi " + sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() +
                        "\nYour account is debited with the amount of " + request.getAmount() + ".\nYour current balance is "
                        + sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        // Credit the destination account
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        // Send credit alert email
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit Alert")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Hi " + destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName() +
                        "\nYour account is credited with the amount of " + request.getAmount() + "\nFrom " + sourceAccountUser.getFirstName() + " " + sourceAccountUser
                        .getLastName() + " with account number " + sourceAccountUser.getAccountNumber() + ".\nYour current balance is "
                        + destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        // Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        // Create and return response wrapper
        BankResponse bankResponse = BankResponse.builder()
                .responseCode(AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_CODE)
                .responseMessage(AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_MESSAGE)
                .accountInfo(null)
                .build();

        return buildResponseWrapper(md, AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_CODE, AccountUtils.SUCCESSFULLY_AMOUNT_TRANSFERRED_MESSAGE, bankResponse.getAccountInfo(), true);
    }




}
