package com.worktree.hrms.constants;

import java.util.Collections;
import java.util.Map;

public class CommonConstants {

    private CommonConstants() {
    }

    public static final String STATUS = "status";
    public static final String STATUS_CODE = "statusCode";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String ACCESS_DENIED = "You don't have access to this services, please contact administrator";

    public static final String INTERNAL_SERVER_ERROR = "An internal error occurred. We apologize for the inconvenience. If the problem continues, please contact support.";

    public static final Map<String, Object> SUCCESS_RESPONSE = Collections.unmodifiableMap(Map.of(
            STATUS, SUCCESS,
            STATUS_CODE, 200
    ));

    public class ServerConfig {

        private ServerConfig() {
        }

        public static final String AI_CONFIG = "AI_CONFIG";
        public static final String EMAIL_CONFIG = "EMAIL_CONFIG";

        public static final String MOBILE_CONFIG = "MOBILE_CONFIG";

        public static final String STORAGE_CONFIG = "STORAGE_CONFIG";

    }

    public class Features {
        private Features() {
        }

        public static final String EMAIL_SETTINGS = "Email Settings";
        public static final String MOBILE_SETTINGS = "Mobile Settings";
        public static final String AI_SETTINGS = "AI Settings";
        public static final String COUPON_SETTINGS = "Coupon Settings";
        public static final String PAYSLIP_SETTINGS = "Payslip Settings";
        public static final String KEYSTORE_SETTINGS = "Keystore Settings";

        public static final String STORAGE_SETTINGS = "Storage Settings";
    }

}


