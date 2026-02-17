package com.example.amdaeus.service;

import com.example.amdaeus.dto.ThresholdDTO;
import com.example.amdaeus.dto.ThresholdUpdateRequestDTO;
import com.example.amdaeus.entity.Threshold;
import com.example.amdaeus.entity.errors.ThresholdNotFoundExeption;
import com.example.amdaeus.repository.ThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ThresholdService {

    private final ThresholdRepository repository;

    public ThresholdDTO updateThresholdValue(ThresholdUpdateRequestDTO request) {
        Threshold threshold = repository.findById(1L)
                .orElse(new Threshold(request.amount()));

        threshold.setAmount(request.amount());
        threshold.setUpdateDate(LocalDate.now());
        repository.save(threshold);
        return new ThresholdDTO(threshold.getAmount());
    }

    public ThresholdDTO getThresholdValue() {
        Threshold threshold = repository.findById(1L)
                .orElseThrow(() -> new ThresholdNotFoundExeption("Threshold not found"));
        return new ThresholdDTO(threshold.getAmount());
    }
}

