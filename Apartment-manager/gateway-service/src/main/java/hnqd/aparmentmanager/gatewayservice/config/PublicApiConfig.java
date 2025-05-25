package hnqd.aparmentmanager.gatewayservice.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Getter
@Configuration
public class PublicApiConfig {
    @Value("${publicApi.GET}")
    private Set<String> getEndpoints;

    @Value("${publicApi.POST}")
    private Set<String> postEndpoints;

    @PostConstruct
    public void init() {
        System.out.println("GET Endpoints: " + getEndpoints);
        System.out.println("POST Endpoints: " + postEndpoints);  // Debugging
    }

}
