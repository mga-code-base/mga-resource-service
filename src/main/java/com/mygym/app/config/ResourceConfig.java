package com.mygym.app.config;

import static com.mygym.app.util.ResourceConstants.BRANCH_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.BRANCH_CRUD_PATH;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_CRUD_PATH;
import static com.mygym.app.util.ResourceConstants.NOTIFICATIONS_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.NOTIFICATIONS_CRUD_PATH;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.mygym.app.client.DownstreamClients;

@Configuration
public class ResourceConfig {

    private static final String RESOURCE_CRUD = "resource-crud";

    private final DownstreamClients clients;
    private final String resourceCrudUrl;

    public ResourceConfig(DownstreamClients clients, @Value("${mga.clients.resource-crud-url}") String resourceCrudUrl) {
        this.clients = clients;
        this.resourceCrudUrl = resourceCrudUrl;
    }

    @Bean(name = BRANCH_CRUD_BEAN)
    public RestClient branchCrudClient() {
        return clients.create(RESOURCE_CRUD, resourceCrudUrl + BRANCH_CRUD_PATH);
    }

    @Bean(name = GRIEVANCE_CRUD_BEAN)
    public RestClient grievanceCrudClient() {
        return clients.create(RESOURCE_CRUD, resourceCrudUrl + GRIEVANCE_CRUD_PATH);
    }

    @Bean(name = NOTIFICATIONS_CRUD_BEAN)
    public RestClient notificationsCrudClient() {
        return clients.create(RESOURCE_CRUD, resourceCrudUrl + NOTIFICATIONS_CRUD_PATH);
    }
}
