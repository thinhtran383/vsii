package space.thinhtran.warehouse.common;

public class Constant {
    public static class ErrorCode {
        public static final String PRODUCT_NOT_FOUND = "product.not.found";

        public static final String ORDER_NOT_FOUND = "order.not.found";
        public static final String ORDER_DETAIL_NOT_FOUND = "order.detail.not.found";


        public static final String USER_NOT_FOUND = "user.not.found";
        public static final String USER_ALREADY_EXISTS = "user.already.exists";
        public static final String USER_BAD_CREDENTIALS = "user.bad.credentials";

        public static final String AUTH_JWT_MISSING = "auth.token";
        public static final String AUTH_NO_PERMISSION = "auth.no.permission";
        public static final String AUTH_PERMISSION_ROLE_NOT_FOUND = "auth.permission.not.found.role";
    }
}
