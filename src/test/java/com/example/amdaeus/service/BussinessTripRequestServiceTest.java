package com.example.amdaeus.service;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.*;
import com.example.amdaeus.entity.errors.ThresholdNotFoundExeption;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.repository.BTRRepository;
import com.example.amdaeus.repository.ThresholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BussinessTripRequestServiceTest {

    @Mock
    private BTRRepository btrRepository;

    @Mock
    private ThresholdRepository thresholdRepository;

    @Mock
    private BTRMapper btrMapper;

    @InjectMocks
    private BussinessTripRequestService bussinessTripRequestService;

    private User user;
    private BussinessTripRequestsDTO dto;
    private Threshold threshold;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserType(Collections.singleton(Role.STANDARD_ROLE)); // nie C-level

        dto = new BussinessTripRequestsDTO(
                1L,                          // btrId
                "Conference Trip",           // title
                "Attending tech conference", // tripReason
                LocalDate.now(),             // startDate
                LocalDate.now().plusDays(3), // endDate
                "Warsaw",                    // startLocation
                "Berlin",                    // destination
                BigDecimal.valueOf(120),     // anticipatedExpenseAmount
                null,                        // comments
                "johndoe",                   // userName
                BTRStatus.PENDING            // status
        );

        threshold = new Threshold();
        threshold.setId(1L);
        threshold.setAmount(BigDecimal.valueOf(200)); // threshold value
    }

    @Test
    void createBTRWithValidation_shouldApprove_whenBelowThreshold() {
        // mockowanie repozytoriów i mappera
        when(thresholdRepository.findById(1L)).thenReturn(Optional.of(threshold));

        BussinessTripRequests entity = new BussinessTripRequests();
        when(btrMapper.toEntity(dto)).thenReturn(entity);
        when(btrRepository.save(any(BussinessTripRequests.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // wywołanie testowanej metody
        BussinessTripRequests result = bussinessTripRequestService.createBTRWithValidation(dto, user);

        // sprawdzenie, czy status został ustawiony na APPROVED
        assertThat(result.getStatus()).isEqualTo(BTRStatus.APPROVED);

        // weryfikacja, że save zostało wywołane
        verify(btrRepository, times(1)).save(entity);
    }

    @Test
    void createBTRWithValidation_shouldSetPending_whenAboveThreshold() {
        user.setUserType(Collections.singleton(Role.STANDARD_ROLE));
        dto = new BussinessTripRequestsDTO(
                2L,
                "Expensive Trip",
                "VIP Meeting",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Warsaw",
                "London",
                BigDecimal.valueOf(300), // powyżej threshold
                null,
                "janedoe",
                BTRStatus.PENDING
        );

        when(thresholdRepository.findById(1L)).thenReturn(Optional.of(threshold));
        BussinessTripRequests entity = new BussinessTripRequests();
        when(btrMapper.toEntity(dto)).thenReturn(entity);
        when(btrRepository.save(any(BussinessTripRequests.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BussinessTripRequests result = bussinessTripRequestService.createBTRWithValidation(dto, user);

        assertThat(result.getStatus()).isEqualTo(BTRStatus.PENDING);
        verify(btrRepository, times(1)).save(entity);
    }

    @Test
    void createBTRWithValidation_shouldThrowException_whenThresholdMissing() {
        // Mock mapper, żeby nie zwracał null
        BussinessTripRequests entity = new BussinessTripRequests();
        when(btrMapper.toEntity(dto)).thenReturn(entity);

        // Threshold zwraca empty
        when(thresholdRepository.findById(1L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                ThresholdNotFoundExeption.class,
                () -> bussinessTripRequestService.createBTRWithValidation(dto, user)
        );
    }

}
