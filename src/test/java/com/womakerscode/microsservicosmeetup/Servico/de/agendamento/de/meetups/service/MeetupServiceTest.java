package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service;

import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.repository.MeetupRepository;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.impl.MeetupServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class MeetupServiceTest {

   MeetupService meetupService;

   @MockBean
   MeetupRepository repository;

   @BeforeEach
   public void setUp() {
      this.meetupService = new MeetupServiceImpl(repository);
   }

   @Test
   @DisplayName("Should save a meetup")
   public void saveTest() throws BusinessException {
      Registration registration = createNewRegistration();
      Meetup meetup = createNewMeetup(registration);
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      Mockito.when(repository.save(meetup)).thenReturn(meetupWithId);

      Meetup savedMeetup = meetupService.save(meetup);

      assertThat(savedMeetup.getId()).isEqualTo(16);
      assertThat(savedMeetup.getEvent()).isEqualTo("Presentation");
      assertThat(savedMeetup.getRegistration().getName()).isEqualTo("Dafne Barbosa");
      assertThat(savedMeetup.getRegistration().getDateOfRegistration()).isEqualTo("23/11/2018");
      assertThat(savedMeetup.getRegistration().getRegistration()).isEqualTo("29");
      assertThat(savedMeetup.getMeetupDate()).isEqualTo("21/09/2022");
      assertThat(savedMeetup.getRegistered()).isEqualTo(true);
   }

   @Test
   @DisplayName("Should throw exception when trying to save a meetup that already exists")
   public void shouldNotSaveTest() throws BusinessException {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);

      Throwable exception = Assertions.catchThrowable( () -> meetupService.save(meetupWithId));
      assertThat(exception)
              .isInstanceOf(BusinessException.class)
              .hasMessage("Meetup already created");

      Mockito.verify(repository, Mockito.never()).save(meetupWithId);
   }

   @Test
   @DisplayName("Should get a meetup by id")
   public void getByIdTest() {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      Mockito.when(repository.findById(meetupWithId.getId())).thenReturn(Optional.of(meetupWithId));

      Optional<Meetup> foundMeetup= meetupService.getById(meetupWithId.getId());

      assertThat(foundMeetup.isPresent()).isTrue();
      assertThat(foundMeetup.get().getId()).isEqualTo(meetupWithId.getId());
      assertThat(foundMeetup.get().getEvent()).isEqualTo(meetupWithId.getEvent());
      assertThat(foundMeetup.get().getRegistration().getName()).isEqualTo(registration.getName());
      assertThat(foundMeetup.get().getRegistration().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
      assertThat(foundMeetup.get().getRegistration().getRegistration()).isEqualTo(registration.getRegistration());
   }

   @Test
   @DisplayName("Should return empty when a registration does not exists")
   public void notFoundByIdTest() {
      Integer id = 1;
      Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

      Optional<Meetup> meetup  = meetupService.getById(id);

      assertThat(meetup.isPresent()).isFalse();
   }

   @Test
   @DisplayName("Should delete a meetup")
   public void deleteTest() {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      Mockito.when(repository.findById(meetupWithId.getId())).thenReturn(Optional.of(meetupWithId));

      assertDoesNotThrow(() -> meetupService.delete(meetupWithId));

      Mockito.verify(repository, Mockito.times(1)).delete(meetupWithId);
   }

   @Test
   @DisplayName("Should update a meetup")
   public void updateTest() {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      Mockito.when(repository.findById(meetupWithId.getId())).thenReturn(Optional.of(meetupWithId));

      Mockito.when(repository.save(meetupWithId)).thenReturn(meetupWithId);
      Meetup meetupUpdated = meetupService.update(meetupWithId);

      assertThat(meetupUpdated.getId()).isEqualTo(meetupWithId.getId());
      assertThat(meetupUpdated.getEvent()).isEqualTo(meetupWithId.getEvent());
      assertThat(meetupUpdated.getRegistration().getName()).isEqualTo(registration.getName());
      assertThat(meetupUpdated.getRegistration().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
      assertThat(meetupUpdated.getRegistration().getRegistration()).isEqualTo(registration.getRegistration());
   }

   @Test
   @DisplayName("Should find meetup by attributes")
   public void findByAttributesTest() {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      MeetupFilterDTO filterDTO = new MeetupFilterDTO(registration.getRegistration(),meetupWithId.getEvent());
      PageRequest pageRequest = PageRequest.of(0,10);

      List<Meetup> listMeetups = Arrays.asList(meetupWithId);
      Page<Meetup> page = new PageImpl<>(Arrays.asList(meetupWithId), PageRequest.of(0,10), 1);

      Mockito.when(repository.findByRegistrationOnMeetup(Mockito.any(String.class),Mockito.any(String.class), Mockito.any(PageRequest.class))).thenReturn(page);
      Page<Meetup> result = meetupService.find(filterDTO, pageRequest);

      assertThat(result.getTotalElements()).isEqualTo(1);
      assertThat(result.getContent()).isEqualTo(listMeetups);
      assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(result.getPageable().getPageSize()).isEqualTo(10);
   }

   @Test
   @DisplayName("Should get an meetup by registration")
   public void getByRegistrationTest() {
      Registration registration = createNewRegistration();
      Meetup meetupWithId = createNewMeetup(registration);
      meetupWithId.setId(16);
      PageRequest pageRequest = PageRequest.of(0,10);

      List<Meetup> listMeetups = Arrays.asList(meetupWithId);
      Page<Meetup> page = new PageImpl<>(Arrays.asList(meetupWithId), PageRequest.of(0,10), 1);

      Mockito.when(repository.findByRegistration(Mockito.any(Registration.class), Mockito.any(PageRequest.class))).thenReturn(page);
      Page<Meetup> result = meetupService.getRegistrationsByMeetup(registration, pageRequest);

      assertThat(result.getTotalElements()).isEqualTo(1);
      assertThat(result.getContent()).isEqualTo(listMeetups);
      assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
      assertThat(result.getPageable().getPageSize()).isEqualTo(10);
   }


   private Registration createNewRegistration() {
      return Registration.builder()
              .id(21)
              .name("Dafne Barbosa")
              .dateOfRegistration("23/11/2018")
              .registration("29")
              .build();
   }

   public static Meetup createNewMeetup(Registration registration) {
      return Meetup.builder()
              .event("Presentation")
              .registration(registration)
              .meetupDate("21/09/2022")
              .registered(true).build();
   }
}
