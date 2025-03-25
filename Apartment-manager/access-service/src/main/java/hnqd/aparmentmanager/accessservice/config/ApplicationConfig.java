package hnqd.aparmentmanager.accessservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hnqd.aparmentmanager.common.utils.UploadImage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper;
    }

    @Bean
    public UploadImage uploadImage() {
        return new UploadImage();
    }
}
