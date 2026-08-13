package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.RegisterRequest;
import kamilzadroga.BudowaKosztorysu.dto.UserResponse;
import kamilzadroga.BudowaKosztorysu.exception.UserAlreadyExistsException;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Użytkownik " + request.username() + " istnieje.");
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .companyName(request.companyName())
                .address(request.address())
                .phone(request.phone())
                .email(request.email())
                .nip(request.nip())
                .build();

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getCompanyName());
    }
}
