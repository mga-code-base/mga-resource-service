package com.mygym.app.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.HttpServerErrorException;

import com.mygym.app.web.DownstreamUnavailableException;

class ResilienceInterceptorTest {

    private final CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
    private final ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
    private ResilienceInterceptor interceptor;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        when(circuitBreaker.run(any(), any())).thenAnswer(invocation -> {
            Supplier<Object> call = invocation.getArgument(0);
            Function<Throwable, Object> fallback = invocation.getArgument(1);
            try {
                return call.get();
            } catch (RuntimeException e) {
                return fallback.apply(e);
            }
        });
        interceptor = new ResilienceInterceptor("people-crud", circuitBreaker, 3, Duration.ZERO);
    }

    @Test
    @DisplayName("Should retry an idempotent GET after a 503 and return the successful response")
    void testGetRetriedOnServerError() throws IOException {
        MockClientHttpRequest request = request(HttpMethod.GET);
        MockClientHttpResponse ok = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        when(execution.execute(any(), any()))
                .thenReturn(new MockClientHttpResponse(new byte[0], HttpStatus.SERVICE_UNAVAILABLE))
                .thenReturn(ok);

        ClientHttpResponse response = interceptor.intercept(request, new byte[0], execution);

        assertSame(ok, response);
        verify(execution, times(2)).execute(any(), any());
    }

    @Test
    @DisplayName("Should never retry a POST and surface the downstream 5xx")
    void testPostNotRetried() throws IOException {
        when(execution.execute(any(), any()))
                .thenReturn(new MockClientHttpResponse(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR));

        HttpServerErrorException error = assertThrows(HttpServerErrorException.class,
                () -> interceptor.intercept(request(HttpMethod.POST), new byte[0], execution));

        assertEquals(500, error.getStatusCode().value());
        verify(execution, times(1)).execute(any(), any());
    }

    @Test
    @DisplayName("Should report the downstream as unavailable after repeated I/O failures")
    void testIoFailuresBecomeUnavailable() throws IOException {
        when(execution.execute(any(), any())).thenThrow(new IOException("connection refused"));

        DownstreamUnavailableException error = assertThrows(DownstreamUnavailableException.class,
                () -> interceptor.intercept(request(HttpMethod.GET), new byte[0], execution));

        assertEquals("people-crud", error.getDownstream());
        verify(execution, times(3)).execute(any(), any());
    }

    @Test
    @DisplayName("Should pass client errors straight through without counting them as failures")
    void testClientErrorPassesThrough() throws IOException {
        MockClientHttpResponse notFound = new MockClientHttpResponse(new byte[0], HttpStatus.NOT_FOUND);
        when(execution.execute(any(), any())).thenReturn(notFound);

        assertSame(notFound, interceptor.intercept(request(HttpMethod.GET), new byte[0], execution));
        verify(execution, times(1)).execute(any(), any());
    }

    private MockClientHttpRequest request(HttpMethod method) {
        return new MockClientHttpRequest(method, URI.create("http://people-crud/crud/admins/7"));
    }
}
