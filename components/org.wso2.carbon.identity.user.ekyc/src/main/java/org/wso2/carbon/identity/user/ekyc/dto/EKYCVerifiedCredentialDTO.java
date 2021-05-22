/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.user.ekyc.dto;

/**
 * DTO for Verified Credential
 */
public class EKYCVerifiedCredentialDTO {

    private String sessionId;
    private String userId;
    private int tenantId;
    private String status;
    private String verifiedCredential;

    public EKYCVerifiedCredentialDTO() {
    }

    public EKYCVerifiedCredentialDTO(String sessionId, String userId, String status) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.status = status;
    }

    public EKYCVerifiedCredentialDTO(String sessionId, String userId, String status, String verifiedCredential) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.status = status;
        this.verifiedCredential = verifiedCredential;
    }

    public EKYCVerifiedCredentialDTO(String sessionId, String userId, int tenantId, String status,
                                     String verifiedCredential) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.tenantId = tenantId;
        this.status = status;
        this.verifiedCredential = verifiedCredential;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifiedCredential() {

        return verifiedCredential;
    }

    public void setVerifiedCredential(String verifiedCredential) {

        this.verifiedCredential = verifiedCredential;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EKYCVerifiedCredentialDTO{");
        sb.append("sessionId='").append(sessionId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", tenantId=").append(tenantId);
        sb.append(", status='").append(status).append('\'');
        sb.append(", verifiedCredential='").append(verifiedCredential).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
