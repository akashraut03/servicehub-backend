package com.servicehub.servicehub_backend.dto;


import com.servicehub.servicehub_backend.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderInfoDTO {

    private String fullName;
    private String address;
    private Gender gender;
    private LocalDate dob;
    private String aadhaarCardNumber;
    private String phoneNumber;

}
