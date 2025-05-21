package space.thinhtran.warehouse.service;

public interface IPermissionService {
    Boolean hasPermission(String roleName, String httpMethod, String requestUri);
}
