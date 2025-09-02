package com.gymbro.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gymbro.dto.AlunoDTO;
import com.gymbro.dto.PersonalDTO;
import com.gymbro.model.Aluno;
import com.gymbro.model.Personal;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        // Mapeia AlunoDTO.getSenha() → Aluno.setSenhaHash(...)
        mapper.typeMap(AlunoDTO.class, Aluno.class)
              .addMappings(m -> m.map(AlunoDTO::getSenha, Aluno::setSenhaHash));

        mapper.typeMap(PersonalDTO.class, Personal.class)
              .addMappings(m -> m.map(PersonalDTO::getSenha, Personal::setSenhaHash));

        return mapper;
    }
}
