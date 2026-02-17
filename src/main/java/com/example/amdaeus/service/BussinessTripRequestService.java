package com.example.amdaeus.service;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.*;
import com.example.amdaeus.entity.errors.BussinessTripRequestNotFoundException;
import com.example.amdaeus.entity.errors.ThresholdNotFoundExeption;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.repository.BTRRepository;
import com.example.amdaeus.repository.ThresholdRepository;
import com.example.amdaeus.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BussinessTripRequestService {

    private final BTRRepository btrRepository;
    private final UserRepository userRepository;
    private final BTRMapper btrMapper;
    private final ThresholdRepository thresholdRepository;
    private final JwtService jwtService;

    @Transactional
    public BussinessTripRequests createBTRWithValidation(@Valid BussinessTripRequestsDTO dto, User user) {

        if (dto.anticipatedExpenseAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Anticipated expense amount cannot be negative");
        }
        if (dto.endDate().isBefore(dto.startDate())) {
            throw new IllegalArgumentException("Bad date range: endDate must be after startDate");
        }

        BussinessTripRequests btr = btrMapper.toEntity(dto);
        btr.setUser(user);

        BigDecimal thresholdValue = thresholdRepository.findById(1L)
                .orElseThrow(() -> new ThresholdNotFoundExeption("Threshold not found"))
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

    @Transactional
    public BussinessTripRequests approveBTR(Long btrId) {
        BussinessTripRequests btr = btrRepository.findById(btrId)
                .orElseThrow(() -> new BussinessTripRequestNotFoundException("BTR not found with id: " + btrId));
        btr.setStatus(BTRStatus.APPROVED);
        return btrRepository.save(btr);
    }

    @Transactional
    public BussinessTripRequests rejectBTR(Long btrId) {
        BussinessTripRequests btr = btrRepository.findById(btrId)
                .orElseThrow(() -> new BussinessTripRequestNotFoundException("BTR not found with id: " + btrId));
        btr.setStatus(BTRStatus.REJECTED);
        return btrRepository.save(btr);
    }

    @Transactional
    public void deleteBTR(Long btrId) {
        if (!btrRepository.existsById(btrId)) {
            throw new BussinessTripRequestNotFoundException("BTR not found with id: " + btrId);
        }
        btrRepository.deleteById(btrId);
    }

    public List<BussinessTripRequests> getAllBtrsForAdmin() {
        return (List<BussinessTripRequests>) btrRepository.findAll();
    }

    public Optional<BussinessTripRequests> getBtrDetails(Long id) {
        return Optional.ofNullable(btrRepository.findById(id)
                .orElseThrow(() -> new BussinessTripRequestNotFoundException("BTR not found with id: " + id)));
    }

    public List<BussinessTripRequestsDTO> getMyBTRs(Optional<User> user) {
        return btrRepository.findByUserName(String.valueOf(user)).stream()
                .map(btrMapper::toDTO)
                .toList();
    }

    public List<BussinessTripRequests> getPendingBTRsForApproval() {
        return btrRepository.findByStatus(BTRStatus.PENDING);
    }


}
