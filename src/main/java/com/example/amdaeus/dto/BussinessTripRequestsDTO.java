package com.example.amdaeus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BussinessTripRequestsDTO(String title, String tripReason, LocalDate startDate,
                                       LocalDate endDate, String startLocation, String destination,
                                       BigDecimal anticipatedExpenseAmount, @JsonProperty(required = false) String comments) {
}
