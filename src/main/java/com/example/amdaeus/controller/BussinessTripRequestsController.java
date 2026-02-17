package com.example.amdaeus.controller;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.dto.SuccesfullyCreatedBTR;
import com.example.amdaeus.entity.*;
import com.example.amdaeus.entity.errors.BussinessTripRequestNotFoundException;
import com.example.amdaeus.entity.errors.UserNotFoundExeption;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.service.BussinessTripRequestService;
import com.example.amdaeus.service.JwtService;
import com.example.amdaeus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/btr")
@RequiredArgsConstructor
public class BussinessTripRequestsController {

    private final BussinessTripRequestService bussinessTripRequestService;
    private final JwtService jwtService;
    private final UserService userService;
    private final BTRMapper btrMapper;

    @PostMapping
    public ResponseEntity<SuccesfullyCreatedBTR> createBTR(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid BussinessTripRequestsDTO dto) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SuccesfullyCreatedBTR("No token provided"));
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            User user = userService.findByUserName(username)
                    .orElseThrow(() -> new UserNotFoundExeption("User not found with username: " + username));
            bussinessTripRequestService.createBTRWithValidation(dto, user);
            return ResponseEntity.ok(new SuccesfullyCreatedBTR("BTR has been created successfully"));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SuccesfullyCreatedBTR("Token wygasł"));
        } catch (UserNotFoundExeption e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SuccesfullyCreatedBTR(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SuccesfullyCreatedBTR("Błąd: " + e.getMessage()));
        }
    }

    @GetMapping("/my-btrs")
    public ResponseEntity<List<BussinessTripRequestsDTO>> getMyBTRs(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        User user = userService.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundExeption("User not found"));

        List<BussinessTripRequestsDTO> myBtrs = bussinessTripRequestService.getMyBTRs(Optional.ofNullable(user));
        return ResponseEntity.ok(myBtrs);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BussinessTripRequestsDTO>> getPendingBTRs() {
        List<BussinessTripRequests> btrs = bussinessTripRequestService.getPendingBTRsForApproval();
        List<BussinessTripRequestsDTO> dtos = btrs.stream()
                .map(btr -> btrMapper.toDTO(btr))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/btr/approve/{id}")
    public ResponseEntity<String> approveBTR(@PathVariable("id") Long id) {
        bussinessTripRequestService.approveBTR(id);
        return ResponseEntity.ok("BTR approved successfully");
    }

    @GetMapping("/btr/reject/{id}")
    public ResponseEntity<String> rejectBTR(@PathVariable("id") Long id) {
        bussinessTripRequestService.rejectBTR(id);
        return ResponseEntity.ok("BTR rejected successfully");
    }

    @DeleteMapping("/{btrId}")
    public ResponseEntity<Void> deleteBTR(@PathVariable Long btrId) {
        bussinessTripRequestService.deleteBTR(btrId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BussinessTripRequestsDTO>> getAllBtrsForAdmin() {
        List<BussinessTripRequests> btrs = bussinessTripRequestService.getAllBtrsForAdmin();
        List<BussinessTripRequestsDTO> dtos = btrs.stream()
                .map(btrMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BussinessTripRequestsDTO> getBtrDetails(@PathVariable Long id) {
        BussinessTripRequests btr = bussinessTripRequestService.getBtrDetails(id)
                .orElseThrow(() -> new BussinessTripRequestNotFoundException("BTR not found with id: " + id));
        return ResponseEntity.ok(btrMapper.toDTO(btr));
    }
}