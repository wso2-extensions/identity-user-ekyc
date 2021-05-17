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

import java.util.Map;

public class EKYCVerifiedCredentialDataDTO {

    private Map<String,Object> verification;
    private Map<String,Object> claims;

    public EKYCVerifiedCredentialDataDTO() {
    }

    public EKYCVerifiedCredentialDataDTO(Map<String, Object> verification, Map<String, Object> claims) {
        this.verification = verification;
        this.claims = claims;
    }

    public Map<String, Object> getVerification() {
        return verification;
    }

    public void setVerification(Map<String, Object> verification) {
        this.verification = verification;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }
}
