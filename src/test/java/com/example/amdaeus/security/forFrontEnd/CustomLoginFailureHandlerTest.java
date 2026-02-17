package com.example.amdaeus.security.forFrontEnd;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CustomLoginFailureHandlerTest {

    private CustomLoginFailureHandler failureHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException exception;

    @BeforeEach
    void setUp() {
        failureHandler = new CustomLoginFailureHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        exception = mock(AuthenticationException.class);
    }

    @Test
    void onAuthenticationFailure_shouldRedirectToLoginWithError() throws IOException, ServletException {
        // given
        when(request.getParameter("username")).thenReturn("testUser");

        // when
        failureHandler.onAuthenticationFailure(request, response, exception);

        // then
        verify(request, times(1)).getParameter("username"); // sprawdzamy, że pobrano username
        verify(response, times(1)).sendRedirect("/login?error"); // sprawdzamy redirect
    }

}
