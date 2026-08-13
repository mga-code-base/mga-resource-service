package com.mygym.app.service;

import static com.mygym.app.util.ResourceConstants.ALL_BRANCH_NAME_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.ALL_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.BRANCH_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_ADD;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_UPDATE;
import static com.mygym.app.util.ResourceConstants.GET_BY_ADMIN_ID_ENPOINT;
import static com.mygym.app.util.ResourceConstants.ID_ENDPOINT;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Branch;
import com.mygym.app.models.BranchNameWrapper;
import com.mygym.app.models.BranchWrapper;
import com.mygym.app.models.Equipment;

/**
 * Service orchestrating branch configurations, equipment updates, and admin assignments across CRUD storage.
 */
@Service
public class BranchService {

    private RestClient restClient;

    public BranchService(@Qualifier(BRANCH_CRUD_BEAN) RestClient restClient) {
        this.restClient = restClient;
    }

    public String createBranch(Branch branch) {
        List<String> branchAdminsList = Arrays.asList(branch.getBranchId());
        branch.setAdminIds(branchAdminsList);
        return restClient.post()
                .body(branch)
                .retrieve()
                .body(String.class);
    }

    public BranchWrapper getAllBranch() {
        BranchWrapper wrapper = restClient.get()
                .uri(ALL_ENDPOINT)
                .retrieve()
                .body(BranchWrapper.class);
        List<Branch> allBranches = wrapper.getAllBranches();
        allBranches = allBranches.stream().filter(branch -> Objects.nonNull(
                branch.getIsActive()) && branch.getIsActive())
                .collect(Collectors.toList());
        return new BranchWrapper(allBranches);
    }

    public BranchNameWrapper getAllBranchNames() {
        return restClient.get()
                .uri(ALL_BRANCH_NAME_ENDPOINT)
                .retrieve()
                .body(BranchNameWrapper.class);
    }

    public Branch getBranchByAdminId(String adminId) {
        return restClient.get()
                .uri(GET_BY_ADMIN_ID_ENPOINT, adminId)
                .retrieve()
                .body(Branch.class);
    }

    public Branch getBranch(String branchId) {
        return restClient.get()
                .uri(ID_ENDPOINT, branchId)
                .retrieve()
                .body(Branch.class);
    }

    public Branch updateBranch(String branchId, Branch branch) {
        return restClient.put()
                .uri(ID_ENDPOINT, branchId)
                .body(branch)
                .retrieve()
                .body(Branch.class);
    }

    public Branch updateEquipment(String branchId, Integer equipmentId, String name, String targetMuscle, Boolean isInWorkingCondition) {
        return restClient.put()
                .uri(EQUIPMENTS_UPDATE, branchId)
                .body(new Equipment(equipmentId, name, targetMuscle, isInWorkingCondition, null))
                .retrieve()
                .body(Branch.class);
    }

    public Branch addEquipmentToBranch(String branchId, String name, String targetMuscle, Boolean isInWorkingCondition) {
        return restClient.put()
                .uri(EQUIPMENTS_ADD, branchId)
                .body(new Equipment(null, name, targetMuscle, isInWorkingCondition, null))
                .retrieve()
                .body(Branch.class);
    }
}