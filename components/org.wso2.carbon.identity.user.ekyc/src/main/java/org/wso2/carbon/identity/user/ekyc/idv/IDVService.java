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

package org.wso2.carbon.identity.user.ekyc.idv;

import com.google.gson.JsonObject;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCSessionDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifyClaimResponseDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;

import java.util.List;

/**
 * Interface to connect to IDV hub
 */
public interface IDVService {
    /**
     * Methods triggers new EKYC session in IDV hub
     *
     * @param service Name of the service that should be used in IDV hub
     * @param claims  List of claim names that IDV should gather
     * @return
     * @throws IDVException
     * @throws ConfigurationManagementException
     */
    EKYCSessionDTO generateNewSession(String service, List<String> claims)
            throws IDVException, ConfigurationManagementException;

    /**
     * Method fetches Verified Credential from IDV Hub
     *
     * @param userId
     * @param sessionId
     * @return Verified Credential
     * @throws IDVException
     * @throws ConfigurationManagementException
     */
    JsonObject getSessionVerifiedCredential(String userId, String sessionId) throws IDVException,
            ConfigurationManagementException;

    /**
     * Method requests the verification of single claim in Verified Credential in IDV hub
     *
     * @param sessionId
     * @param claim
     * @param value
     * @return <code>EKYCVerifyClaimResponseDTO</code> which contains similarity value
     * @throws IDVException
     * @throws ConfigurationManagementException
     */
    EKYCVerifyClaimResponseDTO getVerifyClaim(String sessionId, String claim, String value)
            throws IDVException, ConfigurationManagementException;
}
