package com.mygym.app.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class DownstreamAuthInterceptorTest {

    private final ServiceTokenProvider tokens = mock(ServiceTokenProvider.class);
    private final ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
    private final DownstreamAuthInterceptor interceptor = new DownstreamAuthInterceptor(tokens);

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("Should relay the caller's bearer token and idempotency key")
    void testRelaysUserToken() throws IOException {
        MockHttpServletRequest incoming = new MockHttpServletRequest();
        incoming.addHeader(HttpHeaders.AUTHORIZATION, "Bearer user-token");
        incoming.addHeader(DownstreamAuthInterceptor.IDEMPOTENCY_KEY, "key-1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(incoming));
        MockClientHttpRequest outgoing = outgoing();
        when(execution.execute(any(), any())).thenReturn(new MockClientHttpResponse(new byte[0], HttpStatus.OK));

        interceptor.intercept(outgoing, new byte[0], execution);

        assertEquals("Bearer user-token", outgoing.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        assertEquals("key-1", outgoing.getHeaders().getFirst(DownstreamAuthInterceptor.IDEMPOTENCY_KEY));
    }

    @Test
    @DisplayName("Should fall back to a service token for anonymous flows such as login")
    void testUsesServiceTokenWithoutCaller() throws IOException {
        when(tokens.token()).thenReturn("service-token");
        MockClientHttpRequest outgoing = outgoing();
        when(execution.execute(any(), any())).thenReturn(new MockClientHttpResponse(new byte[0], HttpStatus.OK));

        interceptor.intercept(outgoing, new byte[0], execution);

        assertEquals("Bearer service-token", outgoing.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        assertNull(outgoing.getHeaders().getFirst(DownstreamAuthInterceptor.IDEMPOTENCY_KEY));
    }

    private MockClientHttpRequest outgoing() {
        return new MockClientHttpRequest(HttpMethod.POST, URI.create("http://people-crud/crud/admins/authenticate"));
    }
}
