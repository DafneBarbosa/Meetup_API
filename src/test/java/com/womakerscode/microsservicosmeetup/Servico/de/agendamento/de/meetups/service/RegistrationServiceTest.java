package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository.RegistrationRepository;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    RegistrationService registrationService;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach
    public void setUp() {
        this.registrationService = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a registration")
    public void saveTest() throws BusinessException {
        Registration registration = createValidRegistration();
        Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidRegistration());

        Registration savedRegistration = registrationService.save(registration);

        assertThat(savedRegistration.getId()).isEqualTo(21);
        assertThat(savedRegistration.getName()).isEqualTo("Dafne Barbosa");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo("23/11/2018");
        assertThat(savedRegistration.getRegistration()).isEqualTo("29");
    }

    @Test
    @DisplayName("Should throw exception when trying to save a registration that already exists")
    public void shouldNotSaveTest() {
        Registration registration = createValidRegistration();
        Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable( () -> registrationService.save(registration));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Should get a registration by id")
    public void getByIdTest() {
        Registration registration = createValidRegistration();
        Mockito.when(repository.findById(registration.getId())).thenReturn(Optional.of(registration));

        Optional<Registration> foundRegistration = registrationService.getRegistrationById(registration.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(registration.getId());
        assertThat(foundRegistration.get().getName()).isEqualTo(registration.getName());
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(foundRegistration.get().getRegistration()).isEqualTo(registration.getRegistration());
    }

    @Test
    @DisplayName("Should return empty when a registration does not exists")
    public void notFoundByIdTest() {
        Integer id = 1;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Registration> registration  = registrationService.getRegistrationById(id);

        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete a registration")
    public void deleteTest() {
        Registration registration = createValidRegistration();
        Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

        assertDoesNotThrow(() -> registrationService.delete(registration));

        Mockito.verify(repository, Mockito.times(1)).delete(registration);
    }

    @Test
    @DisplayName("Should update a registration")
    public void updateTest() {
        Registration registration = createValidRegistration();

        Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);
        Mockito.when(repository.save(registration)).thenReturn(registration);
        Registration registrationUpdated = registrationService.update(registration);

        assertThat(registrationUpdated.getId()).isEqualTo(registration.getId());
        assertThat(registrationUpdated.getName()).isEqualTo(registration.getName());
        assertThat(registrationUpdated.getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(registrationUpdated.getRegistration()).isEqualTo(registration.getRegistration());
    }

    @Test
    @DisplayName("Should find all registrations")
    public void findAllTest() {
        Registration registration = createValidRegistration();
        PageRequest pageRequest = PageRequest.of(0,10);

        List<Registration> listRegistrations = Arrays.asList(registration);
        Page<Registration> page = new PageImpl<>(Arrays.asList(registration), PageRequest.of(0,10), 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.find(registration, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistrations);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get an registration by registration attribute")
    public void getByRegistrationAtributeTest() {
        Registration registration = createValidRegistration();
        String registrationAttribute = "29";

        Mockito.when(repository.findByRegistration(registrationAttribute)).thenReturn(Optional.of(registration));
        Optional<Registration> registrationFound  = registrationService.getRegistrationByRegistrationAttribute(registrationAttribute);

        assertThat(registrationFound.isPresent()).isTrue();
        assertThat(registrationFound.get().getId()).isEqualTo(21);
        assertThat(registrationFound.get().getRegistration()).isEqualTo(registrationAttribute);
        Mockito.verify(repository, Mockito.times(1)).findByRegistration(registrationAttribute);
    }


    private Registration createValidRegistration() {
        return Registration.builder()
                .id(21)
                .name("Dafne Barbosa")
                .dateOfRegistration("23/11/2018")
                .registration("29")
                .build();
    }

}
