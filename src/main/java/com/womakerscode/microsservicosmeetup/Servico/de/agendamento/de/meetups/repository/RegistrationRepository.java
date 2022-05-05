package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    //verify if a Registration already exists
    boolean existsByRegistration(String registration);

    //search Registration by registration
    Optional<Registration> findByRegistration(String registrationAtrb);

}
