package com.womakerscode.microsservicosmeetup.Servico.de.agendamento.de.meetups;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServicoDeAgendamentoDeMeetupsApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServicoDeAgendamentoDeMeetupsApplication.class, args);
	}

}
