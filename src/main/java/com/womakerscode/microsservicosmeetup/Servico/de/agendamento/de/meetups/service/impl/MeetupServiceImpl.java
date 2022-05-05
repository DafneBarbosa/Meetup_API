package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.impl;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository.MeetupRepository;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.MeetupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MeetupServiceImpl implements MeetupService {

   private MeetupRepository repository;

   public MeetupServiceImpl(MeetupRepository repository) {
      this.repository = repository;
   }

   @Override
   public Meetup save(Meetup meetup) throws BusinessException {
      if (meetup.getId()!=null) {
         throw new BusinessException("Meetup already created");
      }
      return repository.save(meetup);
   }

   @Override
   public Optional<Meetup> getById(Integer id) {
      return repository.findById(id);
   }

   @Override
   public Meetup update(Meetup loan) {
      if (loan == null || loan.getId() == null) {
         throw new IllegalArgumentException("Registration id cannot be null");
      }
      if (repository.findById(loan.getId()).isEmpty()) {
         throw new IllegalArgumentException("Registration not found");
      }
      return repository.save(loan);
   }

   @Override
   public Page<Meetup> find(MeetupFilterDTO filterDTO, Pageable pageable) {
      if (filterDTO == null) {
         throw new IllegalArgumentException("Filter cannot be null");
      }
      return repository.findByRegistrationOnMeetup(filterDTO.getRegistration(), filterDTO.getEvent(), pageable);
   }

   @Override
   public Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable) {
      if (registration == null) {
         throw new IllegalArgumentException("Registration cannot be null");
      }
      return repository.findByRegistration(registration, pageable);
   }

   @Override
   public void delete(Meetup meetup) {
      if (meetup == null || meetup.getId() == null) {
         throw new IllegalArgumentException("Meetup id cannot be null");
      }
      if (repository.findById(meetup.getId()).isEmpty()) {
         throw new IllegalArgumentException("Meetup not found");
      }
      this.repository.delete(meetup);
   }

}
