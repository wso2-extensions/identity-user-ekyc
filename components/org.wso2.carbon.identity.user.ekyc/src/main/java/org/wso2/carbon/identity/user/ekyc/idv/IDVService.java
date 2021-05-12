/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.user.ekyc.idv;

import com.google.gson.JsonObject;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCSessionDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifyClaimResponseDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;

import java.util.List;

public interface IDVService {
    EKYCSessionDTO generateNewSession(String service, List<String> claims) throws IDVException, ConfigurationManagementException;
    JsonObject getSessionVc(String userId, String sessionId) throws IDVException, ConfigurationManagementException;
    EKYCVerifyClaimResponseDTO getVerifyClaim(String sessionId, String claim, String value) throws IDVException, ConfigurationManagementException;
}
