/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.user.ekyc.util;

public class UserEKYCConstants {

    private UserEKYCConstants(){
    }

    public enum ErrorMessages {

        CONN_CREATE_DB_ERROR("EKYC-0001", "Database error occurred while connectiong to ekyc database"),
        CLAIMS_MAPPING_MISSING_ERROR("EKYC-0002", "EKYC claims mapping is missing");

        private final String code;
        private final String description;

        ErrorMessages(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + " - " + description;
        }

    }

    public static class SQLQueries {

        public static final String ADD_USER_EKYC_VC = "INSERT INTO IDN_USER_EKYC_VC " +
                                                                  "(SESSION_ID, USER_ID, TENANT_ID, STATUS, VC)" +
                                                                  " VALUES (?, ?, ?, ?, ?)";
        public static final String GET_USER_EKYC_VCS = "SELECT SESSION_ID,STATUS, VC FROM IDN_USER_EKYC_VC " +
                " WHERE USER_ID = ? AND TENANT_ID = ?";

        public static final String GET_USER_EKYC_VC = "SELECT  STATUS, VC FROM IDN_USER_EKYC_VC " +
                " WHERE SESSION_ID = ? AND USER_ID = ? AND TENANT_ID = ?";

        public static final String DELETE_USER_EKYC_VC = "DELETE FROM IDN_USER_EKYC_VC " +
                " WHERE SESSION_ID = ? AND USER_ID = ? AND TENANT_ID = ?";

        public static final String UPDATE_USER_EKYC_VC = "UPDATE IDN_USER_EKYC_VC " +
                " SET VC = ?, STATUS = ? WHERE SESSION_ID = ? AND USER_ID = ? AND TENANT_ID = ?";
    }

    public static class EKYCProccessStatuses {
        public static final String PENDING = "PENDING";
        public static final String FINISHED = "FINISHED";
    }

}
