package com.axis.AxisBank.controller;

import com.axis.AxisBank.dto.*;
import com.axis.AxisBank.serviceBank.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management API's")
public class UserController {

    @Autowired
    private final UserService userService;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }
    @Operation(
            summary = "Create New Bank Account",
            description = "Creating a new User and assigning an account Id."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
//    @PostMapping very old
//    public BankResponse createAccount(@Valid @RequestBody UserRequest userRequest){
//        String r = (String) payload.get("r");
//        userRequest.parseR();
//        return userService.createAccount(userRequest);
//    }
   /* @PostMapping not so old
    public BankResponse createAccount(@Valid @RequestBody Map<String, Object> payload) throws JsonProcessingException {
        String r = (String) payload.get("r");
        UserRequest userRequest = objectMapper.readValue(r, UserRequest.class);
        return userService.createAccount(userRequest);
    }*/

    @PostMapping("/createAccount")
    public ResponseWrapper createAccount(@Valid @RequestBody Map<String, Object> payload) throws JsonProcessingException {
        // Extract the MetaDataInfo object from the payload
        MetaDataInfo md = objectMapper.convertValue(payload.get("md"), MetaDataInfo.class);
        // Extract the UserRequest object from the payload
        String r = (String) payload.get("r");
        UserRequest userRequest = objectMapper.readValue(r, UserRequest.class);
        // Set the extracted MetaDataInfo into UserRequest
        userRequest.setMd(md);

        // Call the service method to create the account
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Checking account balance."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 SUCCESS"
    )
    /*@GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }*/

    @GetMapping("balanceEnquiry")
    public ResponseWrapper balanceEnquiry(@RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        EnquiryRequest enquiryRequest = objectMapper.readValue(r, EnquiryRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        enquiryRequest.setMetaData(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + enquiryRequest.getMetaData());

        return userService.balanceEnquiry(enquiryRequest);
    }




    @GetMapping("/nameEnquiry")
    public ResponseWrapper nameEnquiry(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        EnquiryRequest enquiryRequest = objectMapper.readValue(r, EnquiryRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        enquiryRequest.setMetaData(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + enquiryRequest.getMetaData());

        return userService.nameEnquiry(enquiryRequest);
    }


    /*@PostMapping("credit")
    public BankResponse creditAmount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }*/
   /* @PostMapping("/credit")
    public BankResponse credit(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        CreditDebitRequest creditRequest = objectMapper.readValue(r, CreditDebitRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        creditRequest.setMetadata(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + creditRequest.getMetadata());

        return userService.creditAccount(creditRequest);
    }*/

    @PostMapping("/credit")
    public ResponseWrapper creditAccount(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        CreditDebitRequest creditRequest = objectMapper.readValue(r, CreditDebitRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        creditRequest.setMetadata(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + creditRequest.getMetadata());

        return userService.creditAccount(creditRequest);
    }


    /*@PostMapping("debit")
    public BankResponse debitAmount( @RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }*/

    @PostMapping("/debit")
    public ResponseWrapper debitAccount(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        CreditDebitRequest debitRequest = objectMapper.readValue(r, CreditDebitRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        debitRequest.setMetadata(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + debitRequest.getMetadata());

        return userService.debitAccount(debitRequest);
    }

    /*@PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }*/

    /*@PostMapping("/transfer")
    public BankResponse transfer(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        TransferRequest transferRequest = objectMapper.readValue(r, TransferRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        transferRequest.setMetaData(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + transferRequest.getMetaData());

        return userService.transfer(transferRequest);
    }*/

    @PostMapping("/transfer")
    public ResponseWrapper transferAccount(@Valid @RequestBody Map<String, Object> payload) throws Exception {
        String r = (String) payload.get("r");
        ObjectMapper objectMapper = new ObjectMapper();
        TransferRequest transferRequest = objectMapper.readValue(r, TransferRequest.class);

        // Map the metadata separately
        Map<String, Object> md = (Map<String, Object>) payload.get("md");
        MetaDataInfo metadata = objectMapper.convertValue(md, MetaDataInfo.class);
        transferRequest.setMetaData(metadata);

        // Log the received payload and metadata
        System.out.println("Payload: " + payload);
        System.out.println("Metadata: " + transferRequest.getMetaData());

        return userService.transfer(transferRequest);
    }

}