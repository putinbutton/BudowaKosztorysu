# Estimly — kosztorysy budowlane online

**Estimly** to aplikacja webowa (SaaS) do tworzenia i zarządzania kosztorysami budowlanymi. Pozwala firmom budowlanym prowadzić bazę klientów, projekty i kosztorysy z pozycjami materiałowo-robociznowymi, generować profesjonalne kosztorysy w PDF (po polsku i niemiecku) oraz zarządzać wszystkim z poziomu przeglądarki.

🔗 **Działająca wersja:** [estimly.pl](https://www.estimly.pl)

---

## Funkcje

- **Wielu użytkowników (multi-tenant)** — każda zarejestrowana firma widzi wyłącznie swoje dane; pełna izolacja klientów, projektów i kosztorysów między kontami
- **Zarządzanie klientami i projektami** — CRUD z poziomu przeglądarki (lista, dodawanie, edycja, usuwanie)
- **Kosztorysy z pozycjami** — materiał/robocizna, jednostki miary (metr, m², m³, worek, godzina), automatyczne liczenie wartości pozycji i sumy kosztorysu
- **Eksport do PDF** — profesjonalnie sformatowany dokument z danymi wykonawcy, klienta i tabelą pozycji
- **Tłumaczenie kosztorysu na niemiecki** — integracja z Google Cloud Translation API, jedno zbiorcze zapytanie na cały dokument
- **Bezpieczne logowanie** — rejestracja, hasła hashowane (BCrypt), sesje przez Spring Security
- **Migracje bazy danych** — pełna historia zmian schematu przez Flyway, gotowe do bezpiecznych wdrożeń produkcyjnych

---

## Stack technologiczny

| Warstwa | Technologia |
|---|---|
| Backend | Java, Spring Boot, Spring Data JPA, Spring Security |
| Baza danych | PostgreSQL (produkcja), H2 (development), Flyway (migracje) |
| Frontend | Thymeleaf, HTML/CSS |
| PDF | OpenPDF |
| Tłumaczenia | Google Cloud Translation API |
| Hosting | Railway (aplikacja + PostgreSQL w tej samej sieci prywatnej) |
| Inne | Lombok, Maven, Docker (lokalny development) |

---

## Architektura

Projekt podzielony jest na jasne warstwy:

```
model/       — encje JPA (Client, Project, Estimate, EstimateItem, User)
dto/         — DTO do REST API (*Request/*Response) i formularzy webowych (*Form)
repository/  — interfejsy Spring Data JPA
service/     — logika biznesowa
controller/  — kontrolery REST (JSON)
web/         — kontrolery Thymeleaf (HTML)
config/      — konfiguracja Spring Security
exception/   — własne wyjątki + globalna obsługa błędów
```

Każda encja domenowa ma **dwie** ścieżki dostępu — REST API (`/api/...`, zwraca JSON) oraz interfejs webowy (zwykłe adresy, renderowany HTML) — obie korzystające z tej samej warstwy serwisów.

Wszystkie encje dziedziczą po `BaseEntity` (`@MappedSuperclass`), która dostarcza `id`, `uuid` oraz `@Version` (optymistyczne blokowanie), z bezpiecznym `equals`/`hashCode` opartym na `UUID` zamiast na `id` bazodanowym — odporne na typowe pułapki z leniwie ładowanymi (`LAZY`) relacjami Hibernate.

Dane są w pełni odizolowane między firmami: każdy `Client` ma przypisanego właściciela (`User`), a `Project` → `Estimate` → `EstimateItem` dziedziczą tę izolację przez łańcuch relacji.

---

## Uruchomienie lokalne

### Wymagania
- Java 21
- Maven
- Docker (do lokalnego PostgreSQL)

### Development z H2 (najszybciej)

```bash
./mvnw spring-boot:run
```

Aplikacja wystartuje na `http://localhost:8080` z bazą H2 w pamięci — dane znikają po restarcie.

### Development z PostgreSQL (lokalny Docker)

```bash
docker run --name kosztorysy-postgres \
  -e POSTGRES_DB=kosztorysy \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 -d postgres:16

./mvnw spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

### Zmienne środowiskowe (tłumaczenia)

Do funkcji tłumaczenia PDF wymagany jest klucz Google Cloud Translation API:

```
GOOGLE_TRANSLATE_API_KEY=twój_klucz
```

---

## Migracje bazy danych

Struktura bazy zarządzana jest przez **Flyway** (`src/main/resources/db/migration/`). Każda migracja aplikowana jest dokładnie raz; zmiany schematu wprowadza się zawsze przez nową migrację, nigdy przez edycję istniejącej.

---

## Autor

**Kamil Zadroga**

Projekt stworzony jako aplikacja portfolio w ramach nauki na stanowisko Junior Java Developer — od modelu domenowego, przez REST API i frontend Thymeleaf, po pełne wdrożenie produkcyjne z bazą danych, autoryzacją wieloużytkownikową i integracjami zewnętrznymi (Google Cloud Translation API).