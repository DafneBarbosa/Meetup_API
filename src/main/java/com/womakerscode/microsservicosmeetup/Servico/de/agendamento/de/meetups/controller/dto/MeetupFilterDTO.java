package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupFilterDTO {

   private String registration;
   private  String event;

}
