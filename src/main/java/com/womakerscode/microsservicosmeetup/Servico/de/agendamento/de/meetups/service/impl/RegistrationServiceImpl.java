package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.impl;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository.RegistrationRepository;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.RegistrationService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository repository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    //Save new Registration -> do not save if: registration already exists
    @Override
    public Registration save(Registration registration) throws BusinessException {
        if (repository.existsByRegistration(registration.getRegistration())) {
            throw new BusinessException("Registration already created");
        }
        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationById(Integer id) {
        return this.repository.findById(id);
    }

    //Delete Registration -> do not delete if: registration or registration id provided are null; registration does not exist.
    @Override
    public void delete(Registration registration) {
        if (registration == null || registration.getId() == null) {
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        if (!repository.existsByRegistration(registration.getRegistration())) {
            throw new IllegalArgumentException("Registration not found");
        }
        this.repository.delete(registration);
    }

    //Update Registration -> do not update if: registration or registration id provided are null; registration does not exist.
    @Override
    public Registration update(Registration registration) {
        if (registration == null || registration.getId() == null) {
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        if (!repository.existsByRegistration(registration.getRegistration())) {
            throw new IllegalArgumentException("Registration not found");
        }
        return this.repository.save(registration);
    }

    //Find all Registrations
    @Override
    public Page<Registration> find(Registration filter, Pageable pageRequest) {
        Example<Registration> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

    //Get Registration by atribute registration -> do not delete if: registrationAttribute provided is null;
    @Override
    public Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute) {
        if (registrationAttribute == null) {
            throw new IllegalArgumentException("Registration cannot be null");
        }
        return repository.findByRegistration(registrationAttribute);
    }

}
