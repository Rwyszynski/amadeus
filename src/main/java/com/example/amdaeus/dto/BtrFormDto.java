package com.example.amdaeus.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BtrFormDto {

    private String title;
    private String tripReason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startLocation;
    private String destination;
    private BigDecimal anticipatedExpenseAmount;
    private String comments;

    // gettery i settery
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTripReason() { return tripReason; }
    public void setTripReason(String tripReason) { this.tripReason = tripReason; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public BigDecimal getAnticipatedExpenseAmount() { return anticipatedExpenseAmount; }
    public void setAnticipatedExpenseAmount(BigDecimal anticipatedExpenseAmount) {
        this.anticipatedExpenseAmount = anticipatedExpenseAmount;
    }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}