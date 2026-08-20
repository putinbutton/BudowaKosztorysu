package kamilzadroga.BudowaKosztorysu.config;

import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class LoginAttemptListenerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginAttemptListener loginAttemptListener;

    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUsername("kamil");
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
    }

    @Test
    void oneFailedAttempt_incrementsCounterWithoutLockingAccount () {
        when(userRepository.findByUsername("kamil")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("kamil", "zlehaslo");
        AuthenticationFailureBadCredentialsEvent event =
                new AuthenticationFailureBadCredentialsEvent(auth, new BadCredentialsException("Złe hasło"));

        loginAttemptListener.onFailure(event);

        assertEquals(1, user.getFailedLoginAttempts());
        assertNull(user.getLockedUntil());
        verify(userRepository).save(user);
    }
    @Test
    void fifthFailedAttempt_locksAccount() {
        user.setFailedLoginAttempts(4); // fourth attempt already failed earlier
        when(userRepository.findByUsername("kamil")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("kamil", "wrongPassword");
        AuthenticationFailureBadCredentialsEvent event =
                new AuthenticationFailureBadCredentialsEvent(auth, new BadCredentialsException("Bad credentials"));

        loginAttemptListener.onFailure(event);

        assertEquals(5, user.getFailedLoginAttempts());
        assertNotNull(user.getLockedUntil());
        assertTrue(user.getLockedUntil().isAfter(LocalDateTime.now()));
        verify(userRepository).save(user);
    }

    @Test
    void successfulLogin_resetsCounterAndLock() {
        user.setFailedLoginAttempts(3);
        user.setLockedUntil(LocalDateTime.now().plusMinutes(10));
        when(userRepository.findByUsername("kamil")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("kamil", "correctPassword");
        AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(auth);

        loginAttemptListener.onSuccess(event);

        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockedUntil());
        verify(userRepository).save(user);
    }

    @Test
    void unknownUsername_doesNotThrowAndDoesNotSave() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken("unknown", "anyPassword");
        AuthenticationFailureBadCredentialsEvent event =
                new AuthenticationFailureBadCredentialsEvent(auth, new BadCredentialsException("Bad credentials"));

        assertDoesNotThrow(() -> loginAttemptListener.onFailure(event));
        verify(userRepository, never()).save(any());
    }

}
