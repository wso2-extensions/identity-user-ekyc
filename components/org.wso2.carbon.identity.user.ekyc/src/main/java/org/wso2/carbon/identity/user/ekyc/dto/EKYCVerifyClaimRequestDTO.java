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
 * DTO for requesting verification of a claim
 */
public class EKYCVerifyClaimRequestDTO {
    private String sessionId;
    private String claim;
    private String claimValue;

    public EKYCVerifyClaimRequestDTO(String sessionId, String claim, String claimValue) {
        this.sessionId = sessionId;
        this.claim = claim;
        this.claimValue = claimValue;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getClaimValue() {
        return claimValue;
    }

    public void setClaimValue(String claimValue) {
        this.claimValue = claimValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EKYCVerifyClaimRequestDTO{");
        sb.append("sessionId='").append(sessionId).append('\'');
        sb.append(", claim='").append(claim).append('\'');
        sb.append(", claimValue='").append(claimValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
