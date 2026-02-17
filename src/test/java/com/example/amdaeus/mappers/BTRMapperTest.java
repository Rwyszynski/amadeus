package com.example.amdaeus.mappers;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.entity.BTRStatus;
import com.example.amdaeus.entity.BussinessTripRequests;
import com.example.amdaeus.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BTRMapperTest {

    private final BTRMapper mapper = new BTRMapper();

    @Test
    @DisplayName("should map DTO to Entity correctly")
    void shouldMapDtoToEntity() {
        // given
        BussinessTripRequestsDTO dto = new BussinessTripRequestsDTO(
                1L,
                "Trip to Berlin",
                "Conference",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 5),
                "Warsaw",
                "Berlin",
                new BigDecimal(1000.0),
                "No comments",
                "johndoe",
                BTRStatus.PENDING
        );

        // when
        BussinessTripRequests entity = mapper.toEntity(dto);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getBTRid()).isEqualTo(dto.btrId().intValue());
        assertThat(entity.getTitle()).isEqualTo(dto.title());
        assertThat(entity.getTripReason()).isEqualTo(dto.tripReason());
        assertThat(entity.getStartDate()).isEqualTo(dto.startDate());
        assertThat(entity.getEndDate()).isEqualTo(dto.endDate());
        assertThat(entity.getStartLocation()).isEqualTo(dto.startLocation());
        assertThat(entity.getDestination()).isEqualTo(dto.destination());
        assertThat(entity.getAnticipatedExpenseAmount()).isEqualTo(dto.anticipatedExpenseAmount());
        assertThat(entity.getComments()).isEqualTo(dto.comments());
    }

    @Test
    @DisplayName("should map Entity to DTO correctly")
    void shouldMapEntityToDto() {
        // given
        User user = new User();
        user.setUserName("johndoe");

        BussinessTripRequests entity = new BussinessTripRequests();
        entity.setBTRid(1);
        entity.setTitle("Trip to Berlin");
        entity.setTripReason("Conference");
        entity.setStartDate(LocalDate.of(2026, 3, 1));
        entity.setEndDate(LocalDate.of(2026, 3, 5));
        entity.setStartLocation("Warsaw");
        entity.setDestination("Berlin");
        entity.setAnticipatedExpenseAmount(new BigDecimal(1000.0));
        entity.setComments("No comments");
        entity.setUser(user);
        entity.setStatus(BTRStatus.PENDING);

        // when
        BussinessTripRequestsDTO dto = mapper.toDTO(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.btrId()).isEqualTo(entity.getBTRid());
        assertThat(dto.title()).isEqualTo(entity.getTitle());
        assertThat(dto.tripReason()).isEqualTo(entity.getTripReason());
        assertThat(dto.startDate()).isEqualTo(entity.getStartDate());
        assertThat(dto.endDate()).isEqualTo(entity.getEndDate());
        assertThat(dto.startLocation()).isEqualTo(entity.getStartLocation());
        assertThat(dto.destination()).isEqualTo(entity.getDestination());
        assertThat(dto.anticipatedExpenseAmount()).isEqualTo(entity.getAnticipatedExpenseAmount());
        assertThat(dto.comments()).isEqualTo(entity.getComments());
        assertThat(dto.userName()).isEqualTo(user.getUserName());
        assertThat(dto.status()).isEqualTo(entity.getStatus());
    }
}
