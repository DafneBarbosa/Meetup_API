package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisteredMeetupDTO {

   private Boolean registered;

}
