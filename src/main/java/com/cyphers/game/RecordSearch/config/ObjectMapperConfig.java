package com.cyphers.game.RecordSearch.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Configuration
public class ObjectMapperConfig {
	@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // JavaTimeModule을 등록하여 LocalDateTime 직렬화/역직렬화를 위한 설정 추가
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        objectMapper.registerModule(javaTimeModule);

        // DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE 옵션을 설정하여 시간대 조정 비활성화
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}
