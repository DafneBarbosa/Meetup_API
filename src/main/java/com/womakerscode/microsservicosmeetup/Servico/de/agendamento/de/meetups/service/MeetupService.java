package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MeetupService {

   Meetup save(Meetup meetup) throws BusinessException;
   Optional<Meetup> getById(Integer id);
   Meetup update(Meetup loan);
   Page<Meetup> find(MeetupFilterDTO filterDTO, Pageable pageable);
   Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable);
   void delete(Meetup meetup);

}
