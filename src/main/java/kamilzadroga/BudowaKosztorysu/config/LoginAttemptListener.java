package kamilzadroga.BudowaKosztorysu.config;


import kamilzadroga.BudowaKosztorysu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoginAttemptListener {

    private final UserRepository userRepository;

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    @EventListener
    public void onFailure (AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();

        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

            if (user.getFailedLoginAttempts() >= MAX_ATTEMPTS) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            }

            userRepository.save(user);
        });
    }

    @EventListener
    public void onSuccess (AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();

        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            userRepository.save(user);
        });
    }
}
