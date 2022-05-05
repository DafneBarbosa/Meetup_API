package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto.MeetupDTO;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto.MeetupFilterDTO;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.resource.MeetupController;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.exception.BusinessException;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Meetup;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.model.entity.Registration;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.MeetupService;
import com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.service.RegistrationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

   static final String MEETUP_API = "/api/meetups";

   @Autowired
   MockMvc mockMvc;

   @MockBean
   private RegistrationService registrationService;

   @MockBean
   private MeetupService meetupService;

   @Test
   @DisplayName("Should register a meetup")
   public void createTest() throws Exception, BusinessException {
      MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakers").build();
      Registration registration = Registration.builder().id(11).registration("123").build();
      Meetup meetup = Meetup.builder().id(11).event("Womakers").registration(registration).meetupDate("10/10/2021").build();

      String json = new ObjectMapper().writeValueAsString(dto);

      BDDMockito.given(registrationService.getRegistrationByRegistrationAttribute("123")).willReturn(Optional.of(registration));
      BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json);

      mockMvc.perform(request)
              .andExpect(status().isCreated())
              .andExpect(content().string("11"));
   }


   @Test
   @DisplayName("Should return error when trying to register a meetup with non existent registration")
   public void createTest2() throws Exception {
      MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakers").build();
      String json = new ObjectMapper().writeValueAsString(dto);

      BDDMockito.given(registrationService.getRegistrationByRegistrationAttribute("123")).willReturn(Optional.empty());

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json);

      mockMvc.perform(request)
              .andExpect(status().isBadRequest());
   }

   @Test
   @DisplayName("Should return error when trying to register a meetup that already exist")
   public void  createTest3() throws Exception, BusinessException {
      MeetupDTO dto = MeetupDTO.builder().registrationAttribute("123").event("Womakers").build();
      String json = new ObjectMapper().writeValueAsString(dto);

      Registration registration = Registration.builder().id(21).name("Dafne Barbosa").registration("29").build();
      BDDMockito.given(registrationService.getRegistrationByRegistrationAttribute("29")).willReturn(Optional.of(registration));

      BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willThrow(new BusinessException("Meetup already created"));

      MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json);

      mockMvc.perform(requestBuilder)
              .andExpect(status().isBadRequest());
   }

   @Test
   @DisplayName("Should find a meetup by regitration atribute")
   public void  findByAttributeTest() throws Exception, BusinessException {
      MeetupFilterDTO dto = MeetupFilterDTO.builder().registration("123").event("Womakers").build();
      MeetupDTO meetupDto = MeetupDTO.builder().registrationAttribute("123").event("Womakers").build();
      Registration registration = Registration.builder().id(11).registration("123").build();
      Meetup meetup = Meetup.builder().id(11).event("Womakers").registration(registration).meetupDate("10/10/2021").build();

      String json = new ObjectMapper().writeValueAsString(dto);

      BDDMockito.given(meetupService.find(Mockito.any(MeetupFilterDTO.class), Mockito.any(Pageable.class)) )
              .willReturn(new PageImpl<Meetup>(Arrays.asList(meetup), PageRequest.of(0,100), 1));

      String queryString = String.format("?id=%s&page=0&size=100",meetup.getId());

      MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
              .get(MEETUP_API.concat(queryString))
              .accept(MediaType.APPLICATION_JSON);

      mockMvc .perform(requestBuilder)
              .andExpect(status().isOk())
              .andExpect(jsonPath("content", Matchers.hasSize(1)))
              .andExpect(jsonPath("totalElements"). value(1))
              .andExpect(jsonPath("pageable.pageSize"). value(100))
              .andExpect(jsonPath("pageable.pageNumber"). value(0));
   }


}
