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

import java.util.List;
import java.util.Map;

/**
 * DTO for EKYC configuration
 */
public class EKYCConfigurationDTO {
    private String url;
    private String apiKey;
    private String apiSecret;
    private String callbackUrl;
    private List<String> services;
    private Map<String, String> claimsMapping;
    private boolean skipTlsCheck;

    public EKYCConfigurationDTO() {
    }

    public EKYCConfigurationDTO(String url, String apiKey, String apiSecret, String callbackUrl, List<String> services,
                                Map<String, String> claimsMapping, boolean skipTlsCheck) {
        this.url = url;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callbackUrl = callbackUrl;
        this.services = services;
        this.claimsMapping = claimsMapping;
        this.skipTlsCheck = skipTlsCheck;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public Map<String, String> getClaimsMapping() {
        return claimsMapping;
    }

    public void setClaimsMapping(Map<String, String> claimsMapping) {
        this.claimsMapping = claimsMapping;
    }

    public boolean isSkipTlsCheck() {
        return skipTlsCheck;
    }

    public void setSkipTlsCheck(boolean skipTlsCheck) {
        this.skipTlsCheck = skipTlsCheck;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EKYCConfigurationDTO{");
        sb.append("url='").append(url).append('\'');
        sb.append(", apiKey='").append(apiKey).append('\'');
        sb.append(", apiSecret='").append(apiSecret).append('\'');
        sb.append(", callbackUrl='").append(callbackUrl).append('\'');
        sb.append(", services=").append(services);
        sb.append(", claimsMapping=").append(claimsMapping);
        sb.append(", skipTlsCheck=").append(skipTlsCheck);
        sb.append('}');
        return sb.toString();
    }
}

