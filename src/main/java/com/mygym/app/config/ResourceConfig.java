package com.mygym.app.config;

import static com.mygym.app.util.ResourceConstants.APP_JSON;
import static com.mygym.app.util.ResourceConstants.BRANCH_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.CONTENT_TYPE;
import static com.mygym.app.util.ResourceConstants.DEV_BRANCH_BASE_URL;
import static com.mygym.app.util.ResourceConstants.DEV_GRIEVANCE_BASE_URL;
import static com.mygym.app.util.ResourceConstants.DEV_NOTIFICATIONS_BASE_URL;
import static com.mygym.app.util.ResourceConstants.GRIEVANCE_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.NOTIFICATIONS_CRUD_BEAN;
import static com.mygym.app.util.ResourceConstants.PRD_BRANCH_BASE_URL;
import static com.mygym.app.util.ResourceConstants.PRD_GRIEVANCE_BASE_URL;
import static com.mygym.app.util.ResourceConstants.PRD_NOTIFICATIONS_BASE_URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ResourceConfig {

	 @Value("${app.environment}")
	 private String env;
    
    @Bean(name = BRANCH_CRUD_BEAN)
    public RestClient restAdminClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_BRANCH_BASE_URL : PRD_BRANCH_BASE_URL;
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = GRIEVANCE_CRUD_BEAN)
    public RestClient restTrainerClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_GRIEVANCE_BASE_URL : PRD_GRIEVANCE_BASE_URL;
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
    
    @Bean(name = NOTIFICATIONS_CRUD_BEAN)
    public RestClient restCrudClient() {
    	String baseUrl = env.contentEquals("DEV") ? DEV_NOTIFICATIONS_BASE_URL : PRD_NOTIFICATIONS_BASE_URL;
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT_TYPE, APP_JSON)
                .build();
    }
}
