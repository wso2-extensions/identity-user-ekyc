/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.user.ekyc.dto;

/**
 * DTO for requesting a Verified Credential
 */
public class EKYCVCRequestDTO {
    private String sub;
    private String sessionId;

    public EKYCVCRequestDTO(String sub, String sessionId) {
        this.sub = sub;
        this.sessionId = sessionId;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EKYCVCRequestDTO{");
        sb.append("sub='").append(sub).append('\'');
        sb.append(", sessionId='").append(sessionId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
