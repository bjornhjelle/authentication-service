package no.bjornhjelle.authentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.bjornhjelle.authentication.components.JwtManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import static no.bjornhjelle.authentication.config.Constants.AUTHORITY_PREFIX;
import static no.bjornhjelle.authentication.config.Constants.ROLE_CLAIM;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private SecurityProblemSupport problemSupport;
    private ObjectMapper objectMapper;
    private JwtManager jwtManager;
    private UserDetailsService userService;

    public SecurityConfig(@Qualifier("UserDetailsService") UserDetailsService userService,
                          ObjectMapper objectMapper,
                          JwtManager jwtManager,
                          SecurityProblemSupport problemSupport) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.jwtManager = jwtManager;
        this.problemSupport = problemSupport;
    }


    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui",
            "/swagger-ui/",
            "/v3/api-docs",
            "/webjars/**",
            // actuator endpoints
            "/actuator/health",
            "/actuator/info",
            "/users/*",
            "/users",
            "/favicon.ico",
            "/" // just to avoid WARNING log when azure always-on calls /
    };

    private static final String[] POST_WHITELIST = {
            Constants.SIGNUP_URL,
            Constants.TOKEN_URL
    };
    private static final String[] DELETE_WHITELIST = {
            Constants.TOKEN_URL
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests(authz -> authz
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers(HttpMethod.POST, POST_WHITELIST).permitAll()
                        .mvcMatchers(HttpMethod.DELETE, DELETE_WHITELIST).permitAll()
                        //.mvcMatchers(HttpMethod.POST, ... ).hasAuthority("SCOPE_read")
                        //.anyRequest().permitAll();
                        .anyRequest().authenticated())

                /*
                .addFilter(new LoginFilter(super.authenticationManager(), jwtManager, objectMapper))
                .addFilter(new JwtAuthenticationFilter(super.authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                */

                /* .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                   jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())))*/
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .httpBasic();


        http
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport);
    }

    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorityConverter = new JwtGrantedAuthoritiesConverter();
        authorityConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
        authorityConverter.setAuthoritiesClaimName(ROLE_CLAIM);
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorityConverter);
        return converter;
    }

    /*
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2/**");
    }
     */


}
