package com.example.backend.Security;

import com.example.backend.Model.Utilisateur;
import com.example.backend.Repository.UtilisateurRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;

    public JwtFilter(JwtUtil jwtUtil, UtilisateurRepository utilisateurRepository) {
        this.jwtUtil = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (!jwtUtil.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalide ou expiré ❌");
                return;
            }

            String email = jwtUtil.extractEmail(token);

            // ← بدلنا findByEmail بـ findByEmailWithCollections
            Utilisateur user = utilisateurRepository.findByEmailWithCollections(email);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Utilisateur introuvable ❌");
                return;
            }

            if (!user.getUtilisateurActive()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Compte non activé ❌");
                return;
            }

            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getProfil().getStructure().name()),
                    new SimpleGrantedAuthority("PROFIL_" + user.getProfil().getCodeProfil())
            );

            var auth = new UsernamePasswordAuthenticationToken(
                    user, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}