package com.mygym.app.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

class ServiceTokenProviderTest {

    private final JwtEncoder encoder = mock(JwtEncoder.class);
    private final AtomicReference<Instant> now = new AtomicReference<>(Instant.parse("2026-11-11T10:00:00Z"));
    private final Clock clock = new Clock() {
        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now.get();
        }
    };

    @Test
    @DisplayName("Should mint a SERVICE token once and reuse it until close to expiry")
    void testTokenIsCachedAndRefreshed() {
        when(encoder.encode(any())).thenReturn(jwt("first"), jwt("second"));
        ServiceTokenProvider provider = new ServiceTokenProvider(encoder, "mga-people-service", clock);

        assertEquals("first", provider.token());
        now.set(now.get().plus(Duration.ofMinutes(3)));
        assertEquals("first", provider.token());
        now.set(now.get().plus(Duration.ofMinutes(2)));
        assertEquals("second", provider.token());

        ArgumentCaptor<JwtEncoderParameters> parameters = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(encoder, times(2)).encode(parameters.capture());
        assertEquals("SERVICE", parameters.getValue().getClaims().getClaim("role"));
        assertEquals("mga-people-service", parameters.getValue().getClaims().getSubject());
    }

    private static Jwt jwt(String value) {
        return Jwt.withTokenValue(value).header("alg", "HS256").claim("sub", "mga-people-service").build();
    }
}
