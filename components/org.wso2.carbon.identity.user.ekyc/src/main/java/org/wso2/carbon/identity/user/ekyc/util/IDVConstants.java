/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

/**
 * Constants for IDV
 */
public class IDVConstants {

    public static final String EKYC_RESOURCE_TYPE = "EKYC_CONFIGURATION";
    public static final String RESOURCE_NAME = "ekyc";

    public static final String CONFIG_URL = "url";
    public static final String CONFIG_API_KEY = "apiKey";
    public static final String CONFIG_API_SECRET = "apiSecret";
    public static final String CONFIG_CALLBACK_URL = "callbackUrl";
    public static final String CONFIG_SERVICES = "services";
    public static final String CONFIG_CLAIMS_MAPPING = "claimsMapping";
    public static final String CONFIG_SKIP_TLS_CHECK = "skipTlsCheck";

    /**
     * IDV error messages
     */
    public enum ErrorMessages {
        IDV_CONNECTION_ERROR("EKYC-1001", "Error while connecting to idv"),
        IDV_CONNECTION_RESPONSE_CODE_NOT_OK("EKYC-1002", "IDV response was not ok"),
        IDV_HTTP_CLIENT_INITIALIZATION_ERROR("EKYC-1003", "Error when initializing Http Client");

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

    /**
     * Url paths for IDV hub
     */
    public static class UrlPaths {
        public static final String GET_SESSION_PATH = "/api/v1/session";
        public static final String POST_VC_PATH = "/api/v1/idp/vc";
        public static final String POST_CLAIM_VERIFY_PATH = "/api/v1/idp/verify-claim";
    }

}
