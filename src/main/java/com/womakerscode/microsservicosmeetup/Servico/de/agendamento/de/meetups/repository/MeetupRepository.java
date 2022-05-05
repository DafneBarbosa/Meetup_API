package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

   //Search meetups by registration or event
   @Query( value = " select l from Meetup as l join l.registration as b where b.registration = :registration or l.event =:event ")
   Page<Meetup> findByRegistrationOnMeetup(
           @Param("registration") String registration,
           @Param("event") String event,
           Pageable pageable
   );

   //Search meetups by registration
   Page<Meetup> findByRegistration(Registration registration, Pageable pageable);

}
