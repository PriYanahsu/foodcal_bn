package com.foodcal.foodcal_backend.security;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private jwtUtil jwtUtil;

    public JwtAuthFilter(jwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token != null && jwtUtil.validateToken(token)) {
            try {
                UserPrincipal userPrincipal = jwtUtil.getUserPrincipal(token);
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                    )
                );
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
