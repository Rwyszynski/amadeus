package com.example.amdaeus.controller;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.BussinessTripRequests;
import com.example.amdaeus.service.BussinessTripRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
@RestController
@RequestMapping("/v1/btr")
@RequiredArgsConstructor
public class BussinessTripRequestsController {

    private final BussinessTripRequestService bussinessTripRequestService;

    // Utwórz nowy BTR
    @PostMapping
    public ResponseEntity<BussinessTripRequests> createBTR(
            @Valid @RequestBody BussinessTripRequestsDTO dto,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BussinessTripRequests btr = bussinessTripRequestService.createBTRWithValidation(dto, userDetails);
        return ResponseEntity.ok(btr);
    }

    // Pobierz wszystkie moje BTR
    @GetMapping("/me")
    public ResponseEntity<List<BussinessTripRequests>> getMyBTRs(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<BussinessTripRequests> btrs = bussinessTripRequestService.getMyBTRs(userDetails);
        return ResponseEntity.ok(btrs);
    }

    // Pobierz wszystkie BTR do zatwierdzenia (Approver)
    @GetMapping("/pending")
    public ResponseEntity<List<BussinessTripRequests>> getPendingBTRs() {
        List<BussinessTripRequests> btrs = bussinessTripRequestService.getPendingBTRsForApproval();
        return ResponseEntity.ok(btrs);
    }

    // Zatwierdź BTR
    @PostMapping("/{btrId}/approve")
    public ResponseEntity<BussinessTripRequests> approveBTR(@PathVariable Long btrId) {
        BussinessTripRequests btr = bussinessTripRequestService.approveBTR(btrId);
        return ResponseEntity.ok(btr);
    }

    // Odrzuć BTR
    @PostMapping("/{btrId}/reject")
    public ResponseEntity<BussinessTripRequests> rejectBTR(@PathVariable Long btrId) {
        BussinessTripRequests btr = bussinessTripRequestService.rejectBTR(btrId);
        return ResponseEntity.ok(btr);
    }

    // Usuń BTR
    @DeleteMapping("/{btrId}")
    public ResponseEntity<Void> deleteBTR(@PathVariable Long btrId) {
        bussinessTripRequestService.deleteBTR(btrId);
        return ResponseEntity.noContent().build();
    }
}