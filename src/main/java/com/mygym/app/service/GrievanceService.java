package com.mygym.app.service;

import static com.mygym.app.util.ResourceConstants.ALL_ACTIVE_ENDPOINT;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_ACTIVE_FOR_MEMBER;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_ID_ENPOINT;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_INACTIVE_FOR_MEMBER;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mygym.app.models.Grievance;
import com.mygym.app.models.GrievanceWrapper;

/**
 * Service managing member feedback, branch issue escalation, and resolution workflows.
 */
@Service
public class GrievanceService {

    private RestClient restClient;

    public GrievanceService(@Qualifier(GRIEVANCE_CRUD_BEAN) RestClient restClient) {
        this.restClient = restClient;
    }

    public void createGrievance(Grievance grievance) {
        restClient.post()
                .body(grievance)
                .retrieve()
                .body(String.class);
    }

    public Grievance getGrievance(Long grievanceId) {
        return restClient.get()
                .uri(GRIEVANCE_ID_ENPOINT, grievanceId)
                .retrieve()
                .body(Grievance.class);
    }

    public Integer updateGrievance(Integer grievanceId, Grievance grievance) {
        return restClient.put()
                .uri(GRIEVANCE_ID_ENPOINT, grievanceId)
                .body(grievance)
                .retrieve()
                .body(Integer.class);
    }

    public GrievanceWrapper getAllGrievances() {
        return restClient.get()
                .uri(ALL_ACTIVE_ENDPOINT)
                .retrieve()
                .body(GrievanceWrapper.class);
    }

    public GrievanceWrapper getAllActiveGrievancesInBranch(String branchId) {
        return restClient.get()
                .uri(ALL_ACTIVE_ENDPOINT, branchId)
                .retrieve()
                .body(GrievanceWrapper.class);
    }

    public GrievanceWrapper getAllActiveGrievancesForMember(String memberId) {
        return restClient.get()
                .uri(GRIEVANCE_ACTIVE_FOR_MEMBER, memberId)
                .retrieve()
                .body(GrievanceWrapper.class);
    }

    public GrievanceWrapper getAllInactiveGrievancesForMember(String memberId) {
        return restClient.get()
                .uri(GRIEVANCE_INACTIVE_FOR_MEMBER, memberId)
                .retrieve()
                .body(GrievanceWrapper.class);
    }

    public Boolean updateGrienvance(Grievance grievance) {
        return restClient.put()
                .body(grievance)
                .retrieve()
                .body(Boolean.class);
    }
}