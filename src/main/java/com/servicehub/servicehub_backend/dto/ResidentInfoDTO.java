package com.servicehub.servicehub_backend.dto;

import com.servicehub.servicehub_backend.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ResidentInfoDTO {
    private Long id;
    private String fullName;
    private String address;
    private Gender gender;
    private LocalDate dob;
}

