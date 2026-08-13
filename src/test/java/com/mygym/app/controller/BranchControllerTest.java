package com.mygym.app.controller;

import com.mygym.app.models.Branch;
import com.mygym.app.models.BranchNameWrapper;
import com.mygym.app.models.BranchWrapper;
import com.mygym.app.service.BranchService;
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
class BranchControllerTest {

    @Mock
    private BranchService service;

    @InjectMocks
    private BranchController controller;

    @Test
    @DisplayName("Should create branch and return generated ID")
    void testCreateBranch_Success() {
        Branch branch = new Branch();
        when(service.createBranch(any(Branch.class))).thenReturn("B-101");

        String result = controller.createBranch(branch);

        assertEquals("B-101", result);
        verify(service, times(1)).createBranch(branch);
    }

    @Test
    @DisplayName("Should fetch all branches")
    void testGetAllBranch_Success() {
        BranchWrapper wrapper = new BranchWrapper(List.of(new Branch()));
        when(service.getAllBranch()).thenReturn(wrapper);

        BranchWrapper result = controller.getAllBranch();

        assertNotNull(result);
        verify(service, times(1)).getAllBranch();
    }

    @Test
    @DisplayName("Should fetch all branch names")
    void testGetAllBrancheNames_Success() {
        BranchNameWrapper wrapper = new BranchNameWrapper();
        when(service.getAllBranchNames()).thenReturn(wrapper);

        BranchNameWrapper result = controller.getAllBrancheNames();

        assertNotNull(result);
        verify(service, times(1)).getAllBranchNames();
    }

    @Test
    @DisplayName("Should fetch branch by ID")
    void testGetBranch_Success() {
        Branch branch = new Branch();
        branch.setBranchId("B-101");
        when(service.getBranch("B-101")).thenReturn(branch);

        Branch result = controller.getBranch("B-101");

        assertNotNull(result);
        assertEquals("B-101", result.getBranchId());
    }

    @Test
    @DisplayName("Should add equipment to branch and return 200 OK")
    void testAddEquipmentToBranch_Success() {
        Branch branch = new Branch();
        when(service.addEquipmentToBranch("B-101", "Treadmill", "Legs", true)).thenReturn(branch);

        ResponseEntity<Branch> response = controller.addEquipmentToBranch("B-101", "Treadmill", "Legs", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}