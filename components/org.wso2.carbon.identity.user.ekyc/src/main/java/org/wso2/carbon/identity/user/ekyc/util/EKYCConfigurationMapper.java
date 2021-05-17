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

package org.wso2.carbon.identity.user.ekyc.util;

import com.google.gson.Gson;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCConfigurationDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.user.ekyc.util.IDVConstants.*;

public class EKYCConfigurationMapper {

    public static EKYCConfigurationDTO buildEKYCConfigurationFromResource(Resource resource) {
        EKYCConfigurationDTO ekycConfiguration = new EKYCConfigurationDTO();
        Map<String, String> attributesMap =
                resource.getAttributes().stream()
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case CONFIG_URL:
                    ekycConfiguration.setUrl(attribute.getValue());
                    break;
                case CONFIG_API_KEY:
                    ekycConfiguration.setApiKey(attribute.getValue());
                    break;
                case CONFIG_API_SECRET:
                    ekycConfiguration.setApiSecret(attribute.getValue());
                    break;
                case CONFIG_SERVICES:
                    ekycConfiguration.setServices(Arrays.asList(attribute.getValue().split(",")));
                    break;
                case CONFIG_CALLBACK_URL:
                    ekycConfiguration.setCallbackUrl(attribute.getValue());
                    break;
                case CONFIG_CLAIMS_MAPPING:
                    ekycConfiguration.setClaimsMapping(new Gson().fromJson(attribute.getValue(),Map.class));
                    break;
                case CONFIG_SKIP_TLS_CHECK:
                    ekycConfiguration.setSkipTlsCheck(Boolean.valueOf(attribute.getValue()));
                    break;
            }
        });
        return ekycConfiguration;
    }

    public static Resource buildResourceEKYCConfigurationDTO(EKYCConfigurationDTO ekycConfiguration) {
        Resource resource = new Resource();
        resource.setResourceName(RESOURCE_NAME);
        Map<String, String> ekycConfigurationAttributes = new HashMap<>();
        ekycConfigurationAttributes.put(CONFIG_URL, ekycConfiguration.getUrl());
        ekycConfigurationAttributes.put(CONFIG_API_KEY, ekycConfiguration.getApiKey());
        ekycConfigurationAttributes.put(CONFIG_API_SECRET, ekycConfiguration.getApiSecret());
        ekycConfigurationAttributes.put(CONFIG_CALLBACK_URL, ekycConfiguration.getCallbackUrl());
        ekycConfigurationAttributes.put(CONFIG_SERVICES, String.join(",",ekycConfiguration.getServices()));
        ekycConfigurationAttributes.put(CONFIG_CLAIMS_MAPPING, new Gson().toJson(ekycConfiguration.getClaimsMapping()));
        ekycConfigurationAttributes.put(CONFIG_SKIP_TLS_CHECK, Boolean.toString(ekycConfiguration.isSkipTlsCheck()));

        List<Attribute> resourceAttributes =
                ekycConfigurationAttributes.entrySet().stream()
                        .filter(attribute -> attribute.getValue() != null && !"null".equals(attribute.getValue()))
                        .map(attribute -> new Attribute(attribute.getKey(), attribute.getValue()))
                        .collect(Collectors.toList());
        resource.setAttributes(resourceAttributes);
        return resource;
    }
}
