package creatorplatform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    // 인증이 필요없는 경로들
    private static final List<String> OPEN_API_ENDPOINTS = Arrays.asList(
        "/auth/register",
        "/auth/login",
        "/auth/refresh",
        "/actuator"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // 인증이 필요없는 경로는 그냥 통과
        if (isOpenApiPath(path)) {
            return chain.filter(exchange);
        }
        
        // JWT 토큰 추출
        String token = extractToken(exchange.getRequest());
        
        if (token != null && jwtUtils.validateJwtToken(token)) {
            // JWT 토큰이 유효한 경우 - 사용자 ID를 헤더에 추가하여 하위 서비스에 전달
            String userId = jwtUtils.getUserNameFromJwtToken(token);
            
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("userId", userId)
                .build();
                
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } else {
            // JWT 토큰이 없거나 유효하지 않은 경우 - 401 Unauthorized 반환
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
    
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    private boolean isOpenApiPath(String path) {
        return OPEN_API_ENDPOINTS.stream().anyMatch(endpoint -> path.startsWith(endpoint));
    }
    
    @Override
    public int getOrder() {
        return -1; // 높은 우선순위로 설정 (다른 필터들보다 먼저 실행)
    }
} 