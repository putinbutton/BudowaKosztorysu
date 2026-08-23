package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.ClientRequest;
import kamilzadroga.BudowaKosztorysu.dto.ClientResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private User owner;
    private User otherOwner;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setUsername("kamil");

        otherOwner = new User();
        otherOwner.setUsername("jan");
    }

    @Test
    void create_shouldSaveClientWithCurrentUserAsOwner() {

        when(currentUserService.getCurrentUser()).thenReturn(owner);
        when(clientRepository.save(any(Client.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        ClientRequest request = new ClientRequest("Jan Kowalski", "123123123", "jan@example.com");

        ClientResponse response = clientService.create(request);

        assertEquals("Jan Kowalski", response.name());
        assertEquals("123123123", response.phoneNumber());
        assertEquals("jan@example.com", response.email());

        verify(clientRepository).save(any(Client.class));

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());
        Client savedClient = clientCaptor.getValue();

        assertEquals(owner, savedClient.getOwner());
    }

    @Test
    void getById_shouldShowClientIdWithCurrentUserAsOwner () {


        Client client = new Client();
        client.setId(1L);
        client.setName("Jan Kowalski");
        client.setOwner(owner);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(currentUserService.getCurrentUser()).thenReturn(owner);

        ClientResponse response = clientService.getById(1L);

        assertEquals(1L, response.id());
        assertEquals("Jan Kowalski", response.name());
    }

    @Test
    void getById_checkIsolationDataByOtherOwner () {
        Client client = new Client();
        client.setId(1L);
        client.setName("Jan Kowalski");
        client.setOwner(otherOwner);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(currentUserService.getCurrentUser()).thenReturn(owner);

        assertThrows(BudowaKosztorysuNotFoundException.class, () -> clientService.getById(1L));
    }

    @Test
    void getAll_shouldShowSizeOfClientsWithCurrentUserOwner () {

        Client client1 = new Client();
        client1.setOwner(owner);
        client1.setName("Jan");
        client1.setPhoneNumber("123");
        client1.setEmail("jan@example.com");

        Client client2 = new Client();
        client2.setOwner(owner);
        client2.setName("Tomek");
        client2.setPhoneNumber("321");
        client2.setEmail("tomek@example.com");

        List<Client> clients = List.of(client1, client2);
        when(clientRepository.findByOwner(owner)).thenReturn(clients);
        when(currentUserService.getCurrentUser()).thenReturn(owner);

        List<ClientResponse> result = clientService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete_shouldNotDeleteClientIdWithOtherOwner () {

        Client client = new Client();
        client.setId(1L);
        client.setName("Damian");
        client.setPhoneNumber("123");
        client.setEmail("damian@example.com");
        client.setOwner(otherOwner);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(currentUserService.getCurrentUser()).thenReturn(owner);

        assertThrows(BudowaKosztorysuNotFoundException.class, () -> clientService.delete(1L));

        verify(clientRepository, never()).deleteById(anyLong());
    }
}
