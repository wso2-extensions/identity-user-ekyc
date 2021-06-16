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

/**
 * EKYC connector.
 */
public interface UserEKYCConnector {

    /**
     * Generate new EKYC session.
     *
     * @param userId userId
     * @param tenantId
     * @param service  Name of the service that shoudl be used in IDV, optional
     * @param claims   List of claims that should be returned in Verified Credential
     * @return <code>EKYCSessionDTO</code> with new session and redirect Url to initiate EKC process
     * @throws UserEKYCException Exception caused by EKYC processing
     * @throws IDVException Exception caused by IDV hub communication
     * @throws ConfigurationManagementException Exception caused by EKYC configuration
     */
    EKYCSessionDTO generateNewSession(String userId, int tenantId, String service, List<String> claims) throws
            UserEKYCException, IDVException, ConfigurationManagementException;

    /**
     * Get all verified credentials of user.
     *
     * @param userId user Id
     * @param tenantId tenant Id
     * @return <code>List&lt;EKYCVerifiedCredentialDTO&gt;</code>List of Verified Credentials
     * @throws UserEKYCException Exception caused by EKYC processing
     */
    List<EKYCVerifiedCredentialDTO> getVerifiedCredentials(String userId, int tenantId) throws UserEKYCException;

    /**
     * Delete Verified Credential.
     *
     * @param sessionId session Id
     * @param userId user Id
     * @param tenantId tenant Id
     * @throws UserEKYCException Exception caused by EKYC processing
     */
    void deleteVerifiedCredential(String sessionId, String userId, int tenantId) throws UserEKYCException;

    /**
     * Fetch update of Verified Credential from IDV hub and save it in db.
     *
     * @param sessionId session Id
     * @param userId user Id
     * @param tenantId tenant Id
     * @return <code>EKYCVerifiedCredentialDTO</code>Verified Credential
     * @throws UserEKYCException Exception caused by EKYC processing
     * @throws IDVException Exception caused by IDV hub communication
     * @throws ConfigurationManagementException Exception caused by EKYC configuration
     */
    EKYCVerifiedCredentialDTO getPendingVerifiedCredential(String sessionId, String userId, int tenantId) throws
            UserEKYCException, IDVException, ConfigurationManagementException;

    /**
     * Update user claims from Verified Claims based on IDV configuration mapping.
     *
     * @param sessionId session Id
     * @param userId user Id
     * @param tenantId tenant Id
     * @throws UserEKYCException Exception caused by EKYC processing
     * @throws UserStoreException Exception caused by operations on User Store
     * @throws ConfigurationManagementException Exception caused by EKYC configuration
     */
    void updateUserClaimsFromVerifiedCredential(String sessionId, String userId, int tenantId)
            throws UserEKYCException, UserStoreException, ConfigurationManagementException;
}

