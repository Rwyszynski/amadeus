package com.example.amdaeus.service;

import com.example.amdaeus.dto.ThresholdDTO;
import com.example.amdaeus.mappers.ThresholdMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ThresholdMapperTest {

    private final ThresholdMapper mapper = new ThresholdMapper();

    @Test
    @DisplayName("should map ThresholdDTO correctly")
    void shouldMapThresholdDTOCorrectly() {
        // given
        ThresholdDTO original = new ThresholdDTO(new BigDecimal("100.00"));

        // when
        ThresholdDTO result = mapper.mapToThresholdDTO(original);

        // then
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualTo(original.amount());
        assertThat(result).isNotSameAs(original); // upewniamy się, że to nowy obiekt
    }
}
