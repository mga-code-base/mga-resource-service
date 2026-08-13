package com.mygym.app.util;

public class ResourceConstants {
	
	public final static String CONTENT_TYPE = "Content-Type";
	
	public final static String APP_JSON = "application/json";
	
	public final static String ALL_ENDPOINT = "/all";
	
	public final static String ID_ENDPOINT = "/{id}";
	
	public final static String PREVIOUS_ENDOINT = "/previous";
	
	public final static String PROGRESS_ENDPOINT = "/progress";
	
	public static final String ALL_ACTIVE_ENDPOINT = "/allactive";
	
	public static final String ALL_BRANCH_NAME_ENDPOINT = "/allbranchnames";
	
	public final static String BRANCH_CRUD_BEAN = "branchCrudClient";

	public final static String DEV_BRANCH_BASE_URL = "http://mga-resource-crud-service/crud/branches";
	
	public final static String PRD_BRANCH_BASE_URL = "http://mga-resource-crud-service.default.svc.cluster.local:80/crud/branches";
	
	public final static String BRANCH_REST_MAPPING = "/api/branch";
	
	public final static String GET_BY_ADMIN_ID_ENPOINT = "/getbyadminid/{adminid}";
	
	public final static String GRIEVANCE_CRUD_BEAN = "grievanceCrudClient";
	
	public final static String DEV_GRIEVANCE_BASE_URL = "http://mga-resource-crud-service/crud/grievances";
	
	public final static String PRD_GRIEVANCE_BASE_URL = "http://mga-resource-crud-service.default.svc.cluster.local:80/crud/grievances";
	
	public final static String GRIEVANCE_ID_ENPOINT = "/{id}";
	
	public final static String GRIEVANCE_REST_MAPPING = "/api/grievances";
	
	public final static String GRIEVANCE_ACTIVE_IN_BRANCH_ID = "/allactive/{id}";
	
	public final static String GRIEVANCE_ACTIVE_FOR_MEMBER = "/allactive/member/{id}";
	
	public final static String GRIEVANCE_INACTIVE_FOR_MEMBER = "/allinactive/member/{id}";
	
	public final static String NOTIFICATIONS_CRUD_BEAN = "notificationsCrudClient";
	
	public final static String DEV_NOTIFICATIONS_BASE_URL = "http://mga-resource-crud-service/crud/notifications";
	
	public final static String PRD_NOTIFICATIONS_BASE_URL = "http://mga-resource-crud-service.default.svc.cluster.local:80/crud/notifications";
	
	public static final String NOTIFICATION_CRUD_REST_BASE = "/crud/notifications";
	
	public final static String NOTIFICATION_REST_MAPPING = "/api/notifications";
	
	public final static String NOTIFICATIONS_ID_ENPOINT = "/{id}";
	
	public static final String EQUIPMENTS_UPDATE = "/equipments/update/{id}";
	
	public static final String EQUIPMENTS_ADD = "/equipments/add/{id}";
	
}
