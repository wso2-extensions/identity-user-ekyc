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

package org.wso2.carbon.identity.user.ekyc;

import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCSessionDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;
import org.wso2.carbon.identity.user.ekyc.exception.UserEKYCException;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.List;

public interface UserEKYCConnector {

    /**
     * Generate new session for eKYC process
     *
     * @throws UserEKYCException
     */
    EKYCSessionDTO generateNewSession(String userId, int tenantId, String service, List<String> claims) throws
            UserEKYCException, IDVException, ConfigurationManagementException;

    List<EKYCVerifiedCredentialDTO> getVerifiedCredentials(String userId, int tenantId) throws UserEKYCException;

    void deleteVerifiedCredential(String sessionId, String userId, int tenantId) throws UserEKYCException;

    EKYCVerifiedCredentialDTO getPendingVerifiedCredential(String sessionId, String userId, int tenantId) throws
            UserEKYCException, IDVException, ConfigurationManagementException;

    void updateUserClaimsFromVerifiedCredential(String sessionId, String userId, String userName, int tenantId)
            throws UserEKYCException, UserStoreException, ConfigurationManagementException;
}
