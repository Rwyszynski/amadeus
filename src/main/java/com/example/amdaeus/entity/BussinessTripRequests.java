package com.example.amdaeus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BussinessTripRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int BTRid;

    private String title;
    private String tripReason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startLocation;
    private String destination;
    private BigDecimal anticipatedExpenseAmount;
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private BTRStatus status = BTRStatus.PENDING;
}
