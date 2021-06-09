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
 * DTO for response of claim verification
 */
public class EKYCVerifyClaimResponseDTO {
    private String vcValue;
    private String claimValue;
    private Double similarity;

    public EKYCVerifyClaimResponseDTO(String vcValue, String claimValue, Double similarity) {
        this.vcValue = vcValue;
        this.claimValue = claimValue;
        this.similarity = similarity;
    }

    public String getVcValue() {
        return vcValue;
    }

    public void setVcValue(String vcValue) {
        this.vcValue = vcValue;
    }

    public String getClaimValue() {
        return claimValue;
    }

    public void setClaimValue(String claimValue) {
        this.claimValue = claimValue;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EKYCVerifyClaimResponseDTO{");
        sb.append("vcValue='").append(vcValue).append('\'');
        sb.append(", claimValue='").append(claimValue).append('\'');
        sb.append(", similarity=").append(similarity);
        sb.append('}');
        return sb.toString();
    }
}

