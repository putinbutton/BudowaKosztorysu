package kamilzadroga.BudowaKosztorysu.service;


import kamilzadroga.BudowaKosztorysu.dto.ClientRequest;
import kamilzadroga.BudowaKosztorysu.dto.ClientResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    private final CurrentUserService currentUserService;

    @Override
    public ClientResponse create(ClientRequest request) {
        Client client = new Client();
        client.setName(request.name());
        client.setPhoneNumber(request.phoneNumber());
        client.setEmail(request.email());
        client.setOwner(currentUserService.getCurrentUser());

        Client saved = clientRepository.save(client);

        return new ClientResponse(saved.getId(), saved.getName(), saved.getPhoneNumber(), saved.getEmail());
    }

    @Override
    public ClientResponse getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));
        if(!client.getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        return new ClientResponse(client.getId(), client.getName(), client.getPhoneNumber(), client.getEmail());
    }

    @Override
    public List<ClientResponse> getAll() {
        User currentClient = currentUserService.getCurrentUser();
       return clientRepository.findByOwner(currentClient).stream()
               .map(client -> new ClientResponse(
                       client.getId(),
                       client.getName(),
                       client.getPhoneNumber(),
                       client.getEmail()))
               .toList();
    }

    @Override
    public ClientResponse update (Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));

        if(!client.getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }

        client.setName(request.name());
        client.setPhoneNumber(request.phoneNumber());
        client.setEmail(request.email());

        Client updated = clientRepository.save(client);
        return new ClientResponse(
                updated.getId(),
                updated.getName(),
                updated.getPhoneNumber(),
                updated.getEmail());

    }

    @Override
    public void delete(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));
        if(!client.getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }

        clientRepository.deleteById(id);
    }
}
