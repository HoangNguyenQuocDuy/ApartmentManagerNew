package hnqd.aparmentmanager.gatewayservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

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

    public Set<String> getGetEndpoints() {
        return getEndpoints;
    }

    public Set<String> getPostEndpoints() {
        return postEndpoints;
    }
}
