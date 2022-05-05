package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RegistrationService {

    Registration save(Registration any) throws BusinessException;
    Optional<Registration> getRegistrationById(Integer id);
    void delete(Registration registration);
    Registration update(Registration registration);
    Page<Registration> find(Registration filter, Pageable pageRequest);
    Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute);

}
