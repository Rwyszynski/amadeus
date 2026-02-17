package com.example.amdaeus.controller;

import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.dto.SuccesfullyCreatedBTR;
import com.example.amdaeus.entity.BTRStatus;
import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.service.BussinessTripRequestService;
import com.example.amdaeus.service.JwtService;
import com.example.amdaeus.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BussinessTripRequestsControllerTest {

    @Mock
    private BussinessTripRequestService bussinessTripRequestService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BussinessTripRequestsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBTR_shouldReturnSuccess() throws Exception {
        // given
        String token = "validToken";
        String authHeader = "Bearer " + token;
        BussinessTripRequestsDTO dto = new BussinessTripRequestsDTO(
                1L, "Trip", "Reason", LocalDate.now(), LocalDate.now().plusDays(2),
                "Start", "Destination", BigDecimal.valueOf(100), null, "user1", BTRStatus.PENDING
        );
        User user = new User();
        user.setUserName("user1");
        user.setUserType(Collections.singleton(Role.STANDARD_ROLE));

        when(jwtService.extractUsername(token)).thenReturn("user1");
        when(userService.findByUserName("user1")).thenReturn(Optional.of(user));

        // when
        ResponseEntity<SuccesfullyCreatedBTR> response = controller.createBTR(authHeader, dto);

        // then
        verify(bussinessTripRequestService).createBTRWithValidation(dto, user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().message()).isEqualTo("BTR has been created successfully");
    }

    @Test
    void createBTR_shouldReturnUnauthorized_whenNoToken() {
        BussinessTripRequestsDTO dto = mock(BussinessTripRequestsDTO.class);
        ResponseEntity<SuccesfullyCreatedBTR> response = controller.createBTR(null, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().message()).isEqualTo("No token provided");
    }

    @Test
    void createBTR_shouldReturnUnauthorized_whenTokenExpired() {
        BussinessTripRequestsDTO dto = mock(BussinessTripRequestsDTO.class);
        String authHeader = "Bearer expiredToken";
        when(jwtService.extractUsername("expiredToken")).thenThrow(new ExpiredJwtException(null, null, "Expired"));

        ResponseEntity<SuccesfullyCreatedBTR> response = controller.createBTR(authHeader, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().message()).isEqualTo("Token wygasł");
    }

    @Test
    void createBTR_shouldReturnNotFound_whenUserNotFound() {
        BussinessTripRequestsDTO dto = mock(BussinessTripRequestsDTO.class);
        String authHeader = "Bearer token";
        when(jwtService.extractUsername("token")).thenReturn("unknownUser");
        when(userService.findByUserName("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<SuccesfullyCreatedBTR> response = controller.createBTR(authHeader, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).contains("User not found");
    }

    @Test
    void getMyBTRs_shouldReturnList() {
        String authHeader = "Bearer token";
        User user = new User();
        user.setUserName("user1");
        BussinessTripRequestsDTO dto = mock(BussinessTripRequestsDTO.class);
        List<BussinessTripRequestsDTO> list = List.of(dto);

        when(jwtService.extractUsername("token")).thenReturn("user1");
        when(userService.findByUserName("user1")).thenReturn(Optional.of(user));
        when(bussinessTripRequestService.getMyBTRs(Optional.of(user))).thenReturn(list);

        ResponseEntity<List<BussinessTripRequestsDTO>> response = controller.getMyBTRs(authHeader);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    void getPendingBTRs_shouldReturnDTOList() {
        BussinessTripRequestsDTO dto = mock(BussinessTripRequestsDTO.class);
        when(bussinessTripRequestService.getPendingBTRsForApproval()).thenReturn(Collections.emptyList());

        ResponseEntity<List<BussinessTripRequestsDTO>> response = controller.getPendingBTRs();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void approveBTR_shouldReturnOk() {
        ResponseEntity<String> response = controller.approveBTR(1L);

        verify(bussinessTripRequestService).approveBTR(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("BTR approved successfully");
    }

    @Test
    void rejectBTR_shouldReturnOk() {
        ResponseEntity<String> response = controller.rejectBTR(2L);

        verify(bussinessTripRequestService).rejectBTR(2L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("BTR rejected successfully");
    }

    @Test
    void deleteBTR_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.deleteBTR(3L);

        verify(bussinessTripRequestService).deleteBTR(3L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
