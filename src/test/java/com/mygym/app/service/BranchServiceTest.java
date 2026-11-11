package com.mygym.app.service;

import com.mygym.app.models.Branch;
import com.mygym.app.models.BranchWrapper;
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
class BranchServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private BranchService branchService;

    @Test
    @DisplayName("Should create branch via RestClient post request")
    void testCreateBranch_Success() {
        Branch branch = new Branch(null, "B-101", null, null, null, null, null, null, null);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(eq(branch.withAdminIds(List.of("B-101"))))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("B-101");

        String result = branchService.createBranch(branch);

        assertEquals("B-101", result);
    }

    @Test
    @DisplayName("Should fetch and filter active branches")
    void testGetAllBranch_Success() {
        Branch branch1 = new Branch(null, null, null, null, null, null, null, null, true);

        BranchWrapper wrapper = new BranchWrapper(List.of(branch1));

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(eq("/all"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(BranchWrapper.class)).thenReturn(wrapper);

        BranchWrapper result = branchService.getAllBranch();

        assertNotNull(result);
        assertEquals(1, result.allBranches().size());
    }
}