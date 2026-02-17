package com.example.amdaeus.mappers;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.BussinessTripRequests;
import org.springframework.stereotype.Service;

@Service
public class BTRMapper {

    public BussinessTripRequests toEntity(BussinessTripRequestsDTO dto) {
        BussinessTripRequests entity = new BussinessTripRequests();
        entity.setBTRid(Math.toIntExact(dto.btrId()));
        entity.setTitle(dto.title());
        entity.setTripReason(dto.tripReason());
        entity.setStartDate(dto.startDate());
        entity.setEndDate(dto.endDate());
        entity.setStartLocation(dto.startLocation());
        entity.setDestination(dto.destination());
        entity.setAnticipatedExpenseAmount(dto.anticipatedExpenseAmount());
        entity.setComments(dto.comments());

        return entity;
    }

    public BussinessTripRequestsDTO toDTO(BussinessTripRequests entity) {
        return new BussinessTripRequestsDTO(
                (long) entity.getBTRid(),
                entity.getTitle(),
                entity.getTripReason(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStartLocation(),
                entity.getDestination(),
                entity.getAnticipatedExpenseAmount(),
                entity.getComments(),
                entity.getUser().getUserName(),
                entity.getStatus()
        );
    }
}
