package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.RegisterRequest;
import kamilzadroga.BudowaKosztorysu.dto.UserResponse;
import kamilzadroga.BudowaKosztorysu.model.User;

public interface UserService {

    UserResponse register(RegisterRequest request);
}
