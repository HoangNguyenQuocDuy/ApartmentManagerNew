package hnqd.aparmentmanager.gatewayservice.config;

import hnqd.aparmentmanager.common.Enum.HttpMethod;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Component
public class AuthenticationFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final PublicApiConfig publicApi;

    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider, PublicApiConfig publicApi) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.publicApi = publicApi;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isPublicApi(exchange.getRequest().getURI().getPath(), exchange.getRequest().getMethod().name())) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest());
        if (token != null) {
            if (!jwtTokenProvider.validateToken(token)) {
                return Mono.error(
                        new hnqd.aparmentmanager.common.exceptions.CommonException.TokenExpired("Token expired")
                );
            }

            String userId = jwtTokenProvider.getUserIdFromToken(token);

            exchange = exchange.mutate().request(
                    exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("Authorization", "Bearer " + token)
                            .build()
                    ).build();
        } else {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token"));
        }
        return chain.filter(exchange);
    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    private boolean isPublicApi(String path, String method) {
        Set<String> endpoints = new HashSet<>();
        if (method.toUpperCase().equals(HttpMethod.GET.name())) {
            endpoints.addAll(publicApi.getGetEndpoints());
        } else if (method.toUpperCase().equals(HttpMethod.POST.getName())) {
            endpoints.addAll(publicApi.getPostEndpoints());
        }

        for (String api : endpoints) {
            if (api.endsWith("**") && path.startsWith(api)) {
                return true;
            }
            if (path.equals(api)) {
                return true;
            }
        }
        return false;
    }
}
