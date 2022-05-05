package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

   @Autowired
   TestEntityManager entityManager;

   @Autowired
   MeetupRepository repository;

   @Test
   @DisplayName("Should get a meetup by id")
   public void findByIdTest() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);
      entityManager.persist(meetup);

      Optional<Meetup> foundMeetup = repository.findById(meetup.getId());

      assertThat(foundMeetup.isPresent()).isTrue();
   }

   @Test
   @DisplayName("Should save a meetup")
   public void saveTest() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);

      Meetup savedMeetup = repository.save(meetup);

      assertThat(savedMeetup.getId()).isNotNull();
   }

   @Test
   @DisplayName("Should delete meetup")
   public void deleteTest() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);
      entityManager.persist(meetup);

      Meetup foundMeetup = entityManager.find(Meetup.class,meetup.getId());
      repository.delete(foundMeetup);
      Meetup deletedMeetup = entityManager.find(Meetup.class,meetup.getId());

      assertThat(deletedMeetup).isNull();
   }

   @Test
   @DisplayName("Should find a meetup given a title")
   public void findByTitle() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);
      entityManager.persist(meetup);
      PageRequest pageRequest = PageRequest.of(0,10);

      List<Meetup> listMeetups = Arrays.asList(meetup);
      Page<Meetup> foundMeetup = repository.findByRegistrationOnMeetup("", meetup.getEvent(), pageRequest);

      assertThat(foundMeetup.getTotalElements()).isEqualTo(1);
      assertThat(foundMeetup.getContent()).isEqualTo(listMeetups);
      assertThat(foundMeetup.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(foundMeetup.getPageable().getPageSize()).isEqualTo(10);
   }

   @Test
   @DisplayName("Should find a meetup given a regitration atribute")
   public void findByRegistrationAtribute() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);
      entityManager.persist(meetup);
      PageRequest pageRequest = PageRequest.of(0,10);

      List<Meetup> listMeetups = Arrays.asList(meetup);
      Page<Meetup> foundMeetup = repository.findByRegistrationOnMeetup(meetup.getRegistration().getRegistration(), "", pageRequest);

      assertThat(foundMeetup.getTotalElements()).isEqualTo(1);
      assertThat(foundMeetup.getContent()).isEqualTo(listMeetups);
      assertThat(foundMeetup.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(foundMeetup.getPageable().getPageSize()).isEqualTo(10);
   }

   @Test
   @DisplayName("Should find a meetup given a regitration")
   public void findByRegistration() {
      Registration registration = createNewRegistration();
      entityManager.persist(registration);
      Meetup meetup = createNewMeetup(registration);
      entityManager.persist(meetup);
      PageRequest pageRequest = PageRequest.of(0,10);

      List<Meetup> listMeetups = Arrays.asList(meetup);
      Page<Meetup> foundMeetup = repository.findByRegistration(registration, pageRequest);

      assertThat(foundMeetup.getTotalElements()).isEqualTo(1);
      assertThat(foundMeetup.getContent()).isEqualTo(listMeetups);
      assertThat(foundMeetup.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(foundMeetup.getPageable().getPageSize()).isEqualTo(10);
   }

   public static Registration createNewRegistration() {
      return Registration.builder()
              .name("Dafne Barbosa")
              .dateOfRegistration("21/09/2020")
              .registration("56288").build();
   }

   public static Meetup createNewMeetup(Registration registration) {
      return Meetup.builder()
              .event("Presentation")
              .registration(registration)
              .meetupDate("21/09/2022")
              .registered(true).build();
   }
}
