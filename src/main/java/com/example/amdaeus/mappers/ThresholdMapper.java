package com.example.amdaeus.mappers;

import com.example.amdaeus.dto.ThresholdDTO;
import org.springframework.stereotype.Service;

@Service
public class ThresholdMapper {

    public ThresholdDTO mapToThresholdDTO(ThresholdDTO threshold) {
        return new ThresholdDTO(
                threshold.amount()
        );
    }
}
