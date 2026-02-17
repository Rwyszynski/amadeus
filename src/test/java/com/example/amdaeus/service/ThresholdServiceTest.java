package com.example.amdaeus.service;

import com.example.amdaeus.dto.ThresholdDTO;
import com.example.amdaeus.dto.ThresholdUpdateRequestDTO;
import com.example.amdaeus.entity.Threshold;
import com.example.amdaeus.entity.errors.ThresholdNotFoundExeption;
import com.example.amdaeus.repository.ThresholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ThresholdServiceTest {

    private ThresholdRepository repository;
    private ThresholdService service;

    @BeforeEach
    void setUp() {
        repository = mock(ThresholdRepository.class);
        service = new ThresholdService(repository);
    }

    @Test
    void updateThresholdValue_existingThreshold_shouldUpdate() {
        Threshold existing = new Threshold(BigDecimal.valueOf(100));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        ThresholdUpdateRequestDTO request = new ThresholdUpdateRequestDTO(BigDecimal.valueOf(200));
        ThresholdDTO result = service.updateThresholdValue(request);

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        verify(repository).save(any(Threshold.class));
    }


    @Test
    void updateThresholdValue_noExistingThreshold_shouldCreate() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        ThresholdUpdateRequestDTO request = new ThresholdUpdateRequestDTO(BigDecimal.valueOf(150));

        ThresholdDTO result = service.updateThresholdValue(request);

        ArgumentCaptor<Threshold> captor = ArgumentCaptor.forClass(Threshold.class);
        verify(repository).save(captor.capture());

        Threshold saved = captor.getValue();
        assertThat(saved.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(saved.getUpdateDate()).isEqualTo(LocalDate.now());
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void getThresholdValue_existingThreshold_shouldReturnDTO() {
        Threshold threshold = new Threshold(BigDecimal.valueOf(120));
        when(repository.findById(1L)).thenReturn(Optional.of(threshold));

        ThresholdDTO result = service.getThresholdValue();

        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(120));
    }

    @Test
    void getThresholdValue_noThreshold_shouldThrowException() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when / then
        assertThrows(ThresholdNotFoundExeption.class, () -> service.getThresholdValue());
    }
}
