package com.example.amdaeus.controller;

import com.example.amdaeus.dto.ThresholdDTO;
import com.example.amdaeus.dto.ThresholdUpdateRequestDTO;
import com.example.amdaeus.mappers.ThresholdMapper;
import com.example.amdaeus.service.ThresholdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ThresholdControllerTest {

    @Mock
    private ThresholdService thresholdService;

    @Mock
    private ThresholdMapper thresholdMapper;

    @InjectMocks
    private ThresholdController thresholdController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getThreshold_shouldReturnThresholdDTO() {
        // given
        ThresholdDTO thresholdValue = new ThresholdDTO(BigDecimal.valueOf(500));
        ThresholdDTO thresholdDTO = new ThresholdDTO(BigDecimal.valueOf(500));

        // mockowanie serwisu
        when(thresholdService.getThresholdValue()).thenReturn(thresholdValue);

        // mockowanie mappera – ważne: przekazujemy dokładnie ten sam BigDecimal
        when(thresholdMapper.mapToThresholdDTO(thresholdValue)).thenReturn(thresholdDTO);

        // when
        ResponseEntity<ThresholdDTO> response = thresholdController.getThreshold();

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(thresholdDTO);
    }

    @Test
    void updateThreshold_shouldReturnUpdatedThresholdDTO() {
        // given
        BigDecimal newValue = BigDecimal.valueOf(1000);
        ThresholdUpdateRequestDTO requestDTO = new ThresholdUpdateRequestDTO(newValue);
        ThresholdDTO updatedDTO = new ThresholdDTO(newValue);

        // mockowanie serwisu
        when(thresholdService.updateThresholdValue(requestDTO)).thenReturn(updatedDTO);

        // when
        ResponseEntity<ThresholdDTO> response = thresholdController.updateThreshold(requestDTO);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(updatedDTO);
    }
}
