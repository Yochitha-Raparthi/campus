//package com.campuspulse.security;
//import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.IOException;import org.springframework.web.filter.OncePerRequestFilter;import org.springframework.stereotype.Component;import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.core.authority.SimpleGrantedAuthority;import com.campuspulse.repository.UserRepository;import java.util.List;
//@Component public class JwtFilter extends OncePerRequestFilter{
//private final JwtService jwt;private final UserRepository users;
//public JwtFilter(JwtService jwt,UserRepository users){this.jwt=jwt;this.users=users;}
//@Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String header=req.getHeader("Authorization");if(header!=null&&header.startsWith("Bearer ")){try{String email=jwt.subject(header.substring(7));users.findByEmailIgnoreCase(email).ifPresent(u->{var auth=new UsernamePasswordAuthenticationToken(u.email,null,List.of(new SimpleGrantedAuthority("ROLE_"+u.role.name())));SecurityContextHolder.getContext().setAuthentication(auth);});}catch(Exception ignored){SecurityContextHolder.clearContext();}}chain.doFilter(req,res);}
//}

package com.campuspulse.security;

import com.campuspulse.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserRepository users;

    public JwtFilter(JwtService jwt, UserRepository users) {
        this.jwt = jwt;
        this.users = users;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String email = jwt.subject(header.substring(7));
                users.findByEmailIgnoreCase(email).ifPresent(user -> {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user.email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.role.name())));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(req, res);
    }
}
