package com.mygym.app.service;

import static com.mygym.app.util.ResourceConstants.ALL_BRANCH_NAME_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.ALL_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.BRANCH_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_ADD;
import static com.mygym.app.util.ResourceConstants.EQUIPMENTS_UPDATE;
import static com.mygym.app.util.ResourceConstants.GET_BY_ADMIN_ID_ENPOINT;
import static com.mygym.app.util.ResourceConstants.ID_ENDPOINT;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Branch;
import com.mygym.app.models.BranchNameWrapper;
import com.mygym.app.models.BranchWrapper;
import com.mygym.app.models.Equipment;
import com.mygym.app.web.DownstreamUnavailableException;

@Service
public class BranchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchService.class);

    private final RestClient restClient;
    private final AtomicReference<BranchNameWrapper> lastKnownBranchNames = new AtomicReference<>();

    public BranchService(@Qualifier(BRANCH_CRUD_BEAN) RestClient restClient) {
        this.restClient = restClient;
    }

    public String createBranch(Branch branch) {
        return restClient.post()
                .body(branch.withAdminIds(List.of(branch.branchId())))
                .retrieve()
                .body(String.class);
    }

    public BranchWrapper getAllBranch() {
        BranchWrapper wrapper = restClient.get()
                .uri(ALL_ENDPOINT)
                .retrieve()
                .body(BranchWrapper.class);
        List<Branch> allBranches = wrapper.allBranches();
        allBranches = allBranches.stream().filter(branch -> Objects.nonNull(
                branch.isActive()) && branch.isActive())
                .collect(Collectors.toList());
        return new BranchWrapper(allBranches);
    }

    public BranchNameWrapper getAllBranchNames() {
        try {
            BranchNameWrapper names = restClient.get()
                    .uri(ALL_BRANCH_NAME_ENDPOINT)
                    .retrieve()
                    .body(BranchNameWrapper.class);
            if (names != null) {
                lastKnownBranchNames.set(names);
            }
            return names;
        } catch (DownstreamUnavailableException e) {
            BranchNameWrapper cached = lastKnownBranchNames.get();
            if (cached == null) {
                throw e;
            }
            LOGGER.warn("Serving last known branch names while {} is unavailable", e.getDownstream());
            return cached;
        }
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