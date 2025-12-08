// // package com.example.chat.config;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;

// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;

// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// // @Configuration
// // @EnableWebSecurity
// // public class SecurityConfig {

// //     private final JwtAuthFilter jwtAuthFilter;

// //     public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
// //         this.jwtAuthFilter = jwtAuthFilter;
// //     }

// //     @Bean
// //     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

// //         http.csrf(csrf -> csrf.disable())

// //                 .authorizeHttpRequests(auth -> auth
// //                         .requestMatchers(
// //                                 "/api/auth/**",
// //                                 "/ws/**",
// //                                 "/",
// //                                 "/index.html",
// //                                 "/h2-console/**"
// //                         ).permitAll()
// //                         .anyRequest().authenticated()
// //                 )

// //                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

// //         return http.build();
// //     }

// //     @Bean
// //     public PasswordEncoder passwordEncoder() {
// //         return new BCryptPasswordEncoder();
// //     }
// // }
// package com.example.chat.config;

// import com.example.chat.config.JwtAuthFilter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;

//     public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
//         this.jwtAuthFilter = jwtAuthFilter;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//         http
//             .csrf(csrf -> csrf.disable())                          // H2 console requires CSRF disabled
//             .headers(headers -> headers.frameOptions(frame -> frame.disable()))
//   // Allow iframe for H2 console

//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers(
//                         "/api/auth/**",
//                         "/ws/**",
//                         "/h2-console/**",
//                         "/",
//                         "/index.html"
//                 ).permitAll()
//                 .anyRequest().authenticated()
//             )

//             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }
// }
package com.example.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Ignore H2 Console completely
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .headers(headers ->
                headers.frameOptions(frame -> frame.disable())
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",
                    "/ws/**",
                    "/h2-console/**",   // <-- REQUIRED FIX
                    "/",
                    "/index.html"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
