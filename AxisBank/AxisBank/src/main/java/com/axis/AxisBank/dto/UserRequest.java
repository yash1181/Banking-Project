package com.axis.AxisBank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String r; // Contains the JSON-like string

    // Fields for the parsed data
    @NotBlank(message = "First name must not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name should contain letters only")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name should contain letters only")
    private String lastName;

    @NotNull(message = "Aadhaar number must not be null")
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be exactly 12 digits")
    private String addharNumber;

    @NotNull(message = "PAN number must not be null")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN number format")
    private String panNumber;

    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be either 'Male', 'Female', or 'Other'")
    private String gender;

    @NotBlank(message = "Address must not be blank")
    @Size(max = 255, message = "Address should not exceed 255 characters")
    private String address;

    @NotBlank(message = "State of Origin must not be blank")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "State of Origin should contain letters and spaces only")
    @Size(max = 50, message = "State of Origin should not exceed 50 characters")
    private String stateOfOrigin;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;



    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 4, message = "Password should be at least 4 characters")
    private String password;

    @Getter
    @Setter
    private MetaDataInfo md;

    // Method to parse the 'r' string and set fields
    public void parseR() {
        if (r != null && !r.isEmpty()) {
            try {
                // Remove the leading and trailing quote to parse it properly
                String jsonString = r.trim();
                jsonString = jsonString.substring(1, jsonString.length() - 1); // Remove leading and trailing quotes

                // Create an ObjectMapper to convert the string into a Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> fieldMap = objectMapper.readValue("{" + jsonString + "}", Map.class);

                // Populate the fields of the UserRequest object
                this.firstName = fieldMap.get("firstName");
                this.lastName = fieldMap.get("lastName");
                this.addharNumber = fieldMap.get("addharNumber");
                this.panNumber = fieldMap.get("panNumber");
                this.gender = fieldMap.get("gender");
                this.address = fieldMap.get("address");
                this.stateOfOrigin = fieldMap.get("stateOfOrigin");
                this.email = fieldMap.get("email");
                this.phoneNumber = fieldMap.get("phoneNumber");
                this.password = fieldMap.get("password");

            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception if the parsing fails
            }
        }
    }
}
