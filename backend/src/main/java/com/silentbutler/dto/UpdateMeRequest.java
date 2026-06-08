package com.silentbutler.dto;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class UpdateMeRequest {
 
    @NotBlank(message = "Username must not be blank")
    private String username;
 
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;
 
    // no role field — users cannot change their own role
}
 