package com.mygym.app.controller;

import com.mygym.app.models.Grievance;
import com.mygym.app.models.GrievanceWrapper;
import com.mygym.app.service.GrievanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrievanceControllerTest {

    @Mock
    private GrievanceService service;

    @InjectMocks
    private GrievanceController controller;

    @Test
    @DisplayName("Should create grievance and return 200 OK")
    void testCreate_Success() {
        Grievance grievance = new Grievance(null, null, null, null, null, null, null, null, null, null);
        doNothing().when(service).createGrievance(any(Grievance.class));

        ResponseEntity<Object> response = controller.create(grievance);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).createGrievance(grievance);
    }

    @Test
    @DisplayName("Should fetch all grievances")
    void testGetAllGrievances_Success() {
        GrievanceWrapper wrapper = new GrievanceWrapper(List.of(new Grievance(null, null, null, null, null, null, null, null, null, null)));
        when(service.getAllGrievances()).thenReturn(wrapper);

        GrievanceWrapper result = controller.getAllGrievances();

        assertNotNull(result);
        verify(service, times(1)).allGrievances();
    }

    @Test
    @DisplayName("Should update grievance resolution status")
    void testUpdateGrienvance_Success() {
        Grievance grievance = new Grievance(null, null, null, null, null, null, null, null, null, null);
        when(service.updateGrienvance(any(Grievance.class))).thenReturn(true);

        ResponseEntity<Object> response = controller.updateGrienvance(grievance);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).updateGrienvance(grievance);
    }
}