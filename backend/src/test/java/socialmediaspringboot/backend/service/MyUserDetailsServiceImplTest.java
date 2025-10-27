package socialmediaspringboot.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.UserRepository;
import socialmediaspringboot.backend.service.User.MyUserDetailsService;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role role = new Role();
        role.setRoleName("USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        mockUser = new User();
        mockUser.setEmail("123@gmail.com");
        mockUser.setPassword("password123");
        mockUser.setRoles(roles);
    }
    @Test
    void loadUserByEmail_ShouldReturnUserDetails_WhenUserExists() {
       when(userRepository.findByEmail("123@gmail.com"))
               .thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("123@gmail.com");

        assertNotNull(userDetails);
        assertEquals("123@gmail.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByEmail("123@gmail.com");
    }
    @Test
    void loadUserByEmail_ShouldThrowException_WhenEmailNotFound() {
        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown@example.com")
        );

        verify(userRepository, times(1)).findByEmail("unknown@example.com");
    }
}
