package com.example.amdaeus.service;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.BTRStatus;
import com.example.amdaeus.entity.BussinessTripRequests;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.repository.BTRRepository;
import com.example.amdaeus.repository.ThresholdRepository;
import com.example.amdaeus.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BussinessTripRequestService {

    private final BTRRepository btrRepository;
    private final UserRepository userRepository;
    private final BTRMapper btrMapper;
    private final ThresholdRepository thresholdRepository;

    @Transactional
    public BussinessTripRequests createBTRWithValidation(@Valid BussinessTripRequestsDTO dto, UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.anticipatedExpenseAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Kwota nie może być ujemna");
        }
        if (dto.endDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException("Data zakończenia nie może być wcześniejsza niż data rozpoczęcia");
        }

        BussinessTripRequests btr = btrMapper.toEntity(dto);
        btr.setUser(user);

        BigDecimal thresholdValue = thresholdRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Threshold not found"))
                .getAmount();

        if (user.getUserType().contains(Role.C_LEVEL_ROLE)) {
            btr.setStatus(BTRStatus.APPROVED);
        } else if (dto.anticipatedExpenseAmount().compareTo(thresholdValue) <= 0) {
            btr.setStatus(BTRStatus.APPROVED);
        } else {
            btr.setStatus(BTRStatus.PENDING);
        }
        return btrRepository.save(btr);
    }

    public List<BussinessTripRequests> getMyBTRs(UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return btrRepository.findByUserName(String.valueOf(user));
    }

    public List<BussinessTripRequests> getPendingBTRsForApproval() {
        return btrRepository.findByStatus(BTRStatus.PENDING);
    }

    @Transactional
    public BussinessTripRequests approveBTR(Long btrId) {
        BussinessTripRequests btr = btrRepository.findById(btrId)
                .orElseThrow(() -> new NoSuchElementException("BTR not found"));
        btr.setStatus(BTRStatus.APPROVED);
        return btrRepository.save(btr);
    }

    @Transactional
    public BussinessTripRequests rejectBTR(Long btrId) {
        BussinessTripRequests btr = btrRepository.findById(btrId)
                .orElseThrow(() -> new NoSuchElementException("BTR not found"));
        btr.setStatus(BTRStatus.REJECTED);
        return btrRepository.save(btr);
    }

    @Transactional
    public void deleteBTR(Long btrId) {
        btrRepository.deleteById(btrId);
    }
}
