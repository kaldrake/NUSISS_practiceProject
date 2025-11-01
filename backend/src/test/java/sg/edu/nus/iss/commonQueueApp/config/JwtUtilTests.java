/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sg.edu.nus.iss.commonQueueApp.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
/**
 *
 * @author junwe
 */
public class JwtUtilTests {
    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("user@example.com");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void getSubject_ShouldReturnOriginalSubject() {
        String subject = "user@example.com";
        String token = jwtUtil.generateToken(subject);

        String extracted = jwtUtil.getSubject(token);

        assertThat(extracted).isEqualTo(subject);
    }

    @Test
    void getSubject_ShouldThrowException_ForInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThatThrownBy(() -> jwtUtil.getSubject(invalidToken))
                .isInstanceOf(io.jsonwebtoken.JwtException.class);
    }

    @Test
    void token_ShouldExpire() throws InterruptedException {
        // Create a short-lived JWT util for test
        JwtUtil shortLivedJwt = new JwtUtil(1000L);

        String token = shortLivedJwt.generateToken("user");
        Thread.sleep(1500); // wait for 1.5 sec

        assertThatThrownBy(() -> shortLivedJwt.getSubject(token))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }
}
