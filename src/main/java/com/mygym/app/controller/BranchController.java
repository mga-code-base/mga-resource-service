package com.mygym.app.controller;

import static com.mygym.app.util.ResourceConstants.BRANCH_REST_MAPPING;
import static com.mygym.app.util.ResourceConstants.ALL_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.ALL_BRANCH_NAME_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.GET_BY_ADMIN_ID_ENPOINT;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_ADD;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_UPDATE;
import static com.mygym.app.util.ResourceConstants.ID_ENDPOINT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.models.Branch;
import com.mygym.app.models.BranchNameWrapper;
import com.mygym.app.models.BranchWrapper;
import com.mygym.app.service.BranchService;

@RestController
@RequestMapping(BRANCH_REST_MAPPING)
public class BranchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchController.class);
    private final BranchService service;

    public BranchController(BranchService service) {
        this.service = service;
    }
    
    @PostMapping
    public String createBranch(@RequestBody Branch branch) {
        return service.createBranch(branch);
    }
    
    @GetMapping(ALL_ENDPOINT)
    public BranchWrapper getAllBranch() {
        LOGGER.info("🎯 [Branch Controller] Reached 'getAllBranch()' resource route execution phase successfully.");
        try {
            BranchWrapper result = service.getAllBranch();
            LOGGER.info("🎯 [Branch Controller] Service execution completed. Data wrap payload constructed successfully.");
            return result;
        } catch (Exception e) {
            LOGGER.error("💥 [Branch Controller] Exception thrown during data fetching routine loop!", e);
            throw e;
        }
    }
    
    // Path maps to: GET /api/branch/allbranchnames
    @GetMapping(ALL_BRANCH_NAME_ENDPOINT)
    public BranchNameWrapper getAllBrancheNames() {
        return service.getAllBranchNames();
    }
	
    // Path maps to: GET /api/branch/{id}
    @GetMapping(ID_ENDPOINT)
    public Branch getBranch(@PathVariable("id") String branchId) {
        return service.getBranch(branchId);
    }
    
    // Path maps to: GET /api/branch/getbyadminid/{adminid}
    @GetMapping(GET_BY_ADMIN_ID_ENPOINT)
    public Branch getBranchByAdminId(@PathVariable("adminid") String adminId) {
        return service.getBranchByAdminId(adminId);
    }
	
    // Path maps to: PUT /api/branch/{id}
    @PutMapping(ID_ENDPOINT)
    public Branch updateBranch(@PathVariable("id") String branchId, @RequestBody Branch branch) {
        return service.updateBranch(branchId, branch);
    }
	
    // Path maps to: PUT /api/branch/equipments/update/{id}
    @PutMapping(EQUIPMENTS_UPDATE)
    public ResponseEntity<Branch> updateEquipment(
            @PathVariable("id") String branchId, 
            @RequestParam("equipmentId") Integer equipmentId,
            @RequestParam("name") String name,
            @RequestParam("isInWorkingCondition") Boolean isInWorkingCondition) {
        return ResponseEntity.ok().body(
                service.updateEquipment(branchId, equipmentId, name, null, isInWorkingCondition)
        );
    }
	
    // Path maps to: PUT /api/branch/equipments/add/{id}
    @PutMapping(EQUIPMENTS_ADD)
    public ResponseEntity<Branch> addEquipmentToBranch(
            @PathVariable("id") String branchId, 
            @RequestParam("name") String name,
            @RequestParam("targetMuscle") String targetMuscle,
            @RequestParam("isInWorkingCondition") Boolean isInWorkingCondition) {
        return ResponseEntity.ok().body(
                service.addEquipmentToBranch(branchId, name, targetMuscle, isInWorkingCondition)
        );
    }
}
