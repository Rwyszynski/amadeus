package com.example.amdaeus.controller;

import com.example.amdaeus.dto.ThresholdDTO;
import com.example.amdaeus.dto.ThresholdUpdateRequestDTO;
import com.example.amdaeus.mappers.ThresholdMapper;
import com.example.amdaeus.service.ThresholdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/threshold")
@RestController
public class ThresholdController {

    private final ThresholdService thresholdService;
    private final ThresholdMapper thresholdMapper;

    public ThresholdController(ThresholdService thresholdService, ThresholdMapper thresholdMapper) {
        this.thresholdService = thresholdService;
        this.thresholdMapper = thresholdMapper;
    }

    @GetMapping
    public ResponseEntity<ThresholdDTO> getThreshold() {
        return ResponseEntity.ok(thresholdMapper.mapToThresholdDTO(thresholdService.getThresholdValue()));
    }

    @PutMapping
    public ResponseEntity<ThresholdDTO> updateThreshold(@RequestBody ThresholdUpdateRequestDTO request) {
        ThresholdDTO updated = thresholdService.updateThresholdValue(request);
        return ResponseEntity.ok(updated);
    }
}
