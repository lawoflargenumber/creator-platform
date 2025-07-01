package creatorplatform.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.JwtException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, javax.servlet.ServletException {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (jwtUtils.validateJwtToken(token)) {
                    String email = jwtUtils.getUserNameFromJwtToken(token);
                    var userDetails = userDetailsService.loadUserByUsername(email);
                    var auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException ignored) {}
        }
        chain.doFilter(req, res);
    }
}
