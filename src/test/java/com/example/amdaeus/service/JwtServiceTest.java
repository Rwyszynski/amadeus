import com.example.amdaeus.entity.Role;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.service.JwtService;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_shouldContainUsernameAndRoles() {
        // given
        User user = new User();
        user.setEmailAddress("test@example.com");
        user.setUserType(Set.of(Role.ADMIN_ROLE)); // tutaj ADMIN_ROLE z enumu

        // when
        String token = jwtService.generateToken(user);
        Set<String> roles = jwtService.extractRoles(token);

        // then
        assertThat(jwtService.extractUsername(token)).isEqualTo("test@example.com");
        assertThat(roles).containsExactlyInAnyOrder("ADMIN_ROLE"); // <-- dokładnie tak, jak w enumie
    }

    @Test
    void generateToken_withStandardRole() {
        // given
        User user = new User();
        user.setEmailAddress("standard@example.com");
        user.setUserType(Set.of(Role.STANDARD_ROLE));

        // when
        String token = jwtService.generateToken(user);
        Set<String> roles = jwtService.extractRoles(token);

        // then
        assertThat(jwtService.extractUsername(token)).isEqualTo("standard@example.com");
        assertThat(roles).containsExactlyInAnyOrder("STANDARD_ROLE");
    }
}
