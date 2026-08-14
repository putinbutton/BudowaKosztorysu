package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.ClientRequest;
import kamilzadroga.BudowaKosztorysu.dto.ClientResponse;
import kamilzadroga.BudowaKosztorysu.model.Client;

import java.util.List;

public interface ClientService {
    ClientResponse create(ClientRequest request);
    ClientResponse getById(Long id);
    List<ClientResponse> getAll();
    ClientResponse update(Long id, ClientRequest request);
    void delete(Long id);
}
