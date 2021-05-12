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

package org.wso2.carbon.identity.user.ekyc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.user.ekyc.dao.EKYCVerifiedCredentialDAO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCConfigurationDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCSessionDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDTO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDataDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;
import org.wso2.carbon.identity.user.ekyc.exception.UserEKYCException;
import org.wso2.carbon.identity.user.ekyc.idv.IDVService;
import org.wso2.carbon.identity.user.ekyc.internal.EKYCServiceDataHolder;
import org.wso2.carbon.identity.user.ekyc.util.UserEKYCConstants;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.user.ekyc.util.EKYCConfigurationMapper.buildEKYCConfigurationFromResource;
import static org.wso2.carbon.identity.user.ekyc.util.IDVConstants.EKYC_RESOURCE_TYPE;
import static org.wso2.carbon.identity.user.ekyc.util.IDVConstants.RESOURCE_NAME;

public class UserEKYCConnectorImpl implements UserEKYCConnector {

    private static final Log log = LogFactory.getLog(UserEKYCConnectorImpl.class);

    private UserEKYCConnectorImpl() {
    }

    private static class LazyHolder {
        private static UserEKYCConnectorImpl INSTANCE = null;
    }

    public static UserEKYCConnectorImpl getInstance() {
        if (LazyHolder.INSTANCE == null) {
            LazyHolder.INSTANCE = new UserEKYCConnectorImpl();
        }
        return LazyHolder.INSTANCE;
    }

    @Override
    public EKYCSessionDTO generateNewSession(String userId, int tenantId, String service, List<String> claims) throws UserEKYCException, IDVException, ConfigurationManagementException {
        EKYCSessionDTO ekycSessionDTO = getIdvService().generateNewSession(service, claims);
        EKYCVerifiedCredentialDAO.getInstance().createUserEKYC(
                ekycSessionDTO.getSessionId(),
                userId,
                tenantId,
                UserEKYCConstants.EKYCProccessStatuses.PENDING,
                null);
        log.debug("Generated session: " + ekycSessionDTO.toString());
        return ekycSessionDTO;
    }

    @Override
    public List<EKYCVerifiedCredentialDTO> getVerifiedCredentials(String userId, int tenantId) throws UserEKYCException {
        List<EKYCVerifiedCredentialDTO> ekycVerifiedCredentialDTOs = EKYCVerifiedCredentialDAO
                .getInstance().getUserEKYCVCs(userId, tenantId);
        log.debug("User VCs " + Arrays.toString(ekycVerifiedCredentialDTOs.toArray()));
        return ekycVerifiedCredentialDTOs;
    }

    @Override
    public void deleteVerifiedCredential(String sessionId, String userId, int tenantId) throws UserEKYCException {
        EKYCVerifiedCredentialDAO
                .getInstance().deleteUserEKYCVC(sessionId, userId, tenantId);
        log.debug("Deleted VC: " + sessionId);
    }

    @Override
    public EKYCVerifiedCredentialDTO getPendingVerifiedCredential(String sessionId, String userId, int tenantId) throws UserEKYCException, IDVException, ConfigurationManagementException {
        EKYCVerifiedCredentialDTO ekycVerifiedCredentialDTO = EKYCVerifiedCredentialDAO
                .getInstance().getUserEKYCVC(sessionId, userId, tenantId);
        if (ekycVerifiedCredentialDTO != null && isVerifiedCredentialPending(ekycVerifiedCredentialDTO)) {
            JsonObject vc = getIdvService().getSessionVc(userId, sessionId);
            EKYCVerifiedCredentialDAO.getInstance()
                    .updateVerifiedCredential(sessionId, userId, tenantId,
                            vc.toString(), UserEKYCConstants.EKYCProccessStatuses.FINISHED);
            return EKYCVerifiedCredentialDAO.getInstance().getUserEKYCVC(sessionId, userId, tenantId);
        }
        return null;
    }

    @Override
    public void updateUserClaimsFromVerifiedCredential(String sessionId, String userId, String userName, int tenantId) throws UserEKYCException, ConfigurationManagementException, UserStoreException {
        EKYCVerifiedCredentialDTO ekycVerifiedCredentialDTO = EKYCVerifiedCredentialDAO
                .getInstance().getUserEKYCVC(sessionId, userId, tenantId);
        if (isUpdatePossible(ekycVerifiedCredentialDTO, tenantId)) {
            EKYCVerifiedCredentialDataDTO ekycVerifiedCredentialDataDTO = getEkycVerifiedCredentialDataDTO(ekycVerifiedCredentialDTO);
            Map<String, String> claimsMapping = getClaimsMapping();
            Map<String, String> mappedClaims = ekycVerifiedCredentialDataDTO.getClaims().entrySet().stream()
                    .filter((entry) -> claimsMapping.keySet().contains(entry.getKey()))
                    .map((entry) -> getMappedClaim(entry, claimsMapping))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            log.debug("Updating user claims " + StringUtils.join(mappedClaims));
            getUserStoreManager(tenantId).setUserClaimValues(userName, mappedClaims, null);
        }
    }

    private IDVService getIdvService() {
        return EKYCServiceDataHolder.getInstance().getIdvService();
    }

    private EKYCVerifiedCredentialDataDTO getEkycVerifiedCredentialDataDTO(EKYCVerifiedCredentialDTO ekycVerifiedCredentialDTO) {
        return new Gson().fromJson(ekycVerifiedCredentialDTO
                .getVerifiedCredential(), EKYCVerifiedCredentialDataDTO.class);
    }

    private Map<String, String> getClaimsMapping() throws ConfigurationManagementException, UserEKYCException {
        Map<String, String> claimsMapping = getEKYCConfiguration().getClaimsMapping();
        if (claimsMapping == null) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages.CLAIMS_MAPPING_MISSING_ERROR);
        }
        return claimsMapping;
    }

    private Map.Entry<String, String> getMappedClaim(Map.Entry<String, Object> claim, Map<String, String> claimsMapping) {
        return new AbstractMap.SimpleEntry<String, String>(
                claimsMapping.get(claim.getKey()),
                claim.getValue().toString());
    }

    private UserStoreManager getUserStoreManager(int tenantId) throws UserStoreException {
        return EKYCServiceDataHolder.getInstance().getRealmService().getTenantUserRealm(tenantId).getUserStoreManager();
    }


    private boolean isUpdatePossible(EKYCVerifiedCredentialDTO ekycVerifiedCredentialDTO, int tenantId) throws UserStoreException {
        return !getUserStoreManager(tenantId).isReadOnly() &&
                ekycVerifiedCredentialDTO != null &&
                ekycVerifiedCredentialDTO.getStatus().equals(UserEKYCConstants.EKYCProccessStatuses.FINISHED);
    }

    private boolean isVerifiedCredentialPending(EKYCVerifiedCredentialDTO ekycVerifiedCredentialDTO) {
        return ekycVerifiedCredentialDTO.getStatus().equals(UserEKYCConstants.EKYCProccessStatuses.PENDING);
    }

    private EKYCConfigurationDTO getEKYCConfiguration() throws ConfigurationManagementException {
        Resource resource = EKYCServiceDataHolder.getInstance()
                .getConfigurationManager()
                .getResource(EKYC_RESOURCE_TYPE, RESOURCE_NAME);
        return buildEKYCConfigurationFromResource(resource);
    }


}
