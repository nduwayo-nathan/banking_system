package com.bank_MS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "First name is required")
    @Size(max=30, min = 3 , message = "First name must be at between 3 and 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max=30, min = 3 , message = "Last name must be at between 3 and 30 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must be at most 50 characters")
    private String email;
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid mobile number")
    private String mobile;

    @NotBlank(message = "Date of birth is required")
    @Size(max = 10, message = "Date of birth must be at most 10 characters (YYYY-MM-DD)")
    private String dob;

    private String account;
    private BigDecimal balance;
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    private String password;
}
