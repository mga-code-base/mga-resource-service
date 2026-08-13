package com.mygym.app.service;

import com.mygym.app.models.Grievance;
import com.mygym.app.models.GrievanceWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrievanceServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private GrievanceService grievanceService;

    @Test
    @DisplayName("Should fetch all grievances downstream")
    void testGetAllGrievances_Success() {
        GrievanceWrapper wrapper = new GrievanceWrapper(List.of(new Grievance()));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/allactive"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(GrievanceWrapper.class)).thenReturn(wrapper);

        GrievanceWrapper result = grievanceService.getAllGrievances();

        assertNotNull(result);
    }
}