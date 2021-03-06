/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.user.ekyc;

import com.google.common.io.Resources;
import com.google.gson.JsonParser;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;
import org.mockito.testng.MockitoSettings;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.user.ekyc.dao.EKYCVerifiedCredentialDAO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;
import org.wso2.carbon.identity.user.ekyc.exception.UserEKYCException;
import org.wso2.carbon.identity.user.ekyc.idv.IDVService;
import org.wso2.carbon.identity.user.ekyc.internal.EKYCServiceDataHolder;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.wso2.carbon.identity.user.ekyc.util.IDVConstants.EKYC_RESOURCE_TYPE;
import static org.wso2.carbon.identity.user.ekyc.util.IDVConstants.RESOURCE_NAME;
import static org.wso2.carbon.identity.user.ekyc.util.UserEKYCConstants.EKYCProccessStatuses.FINISHED;
import static org.wso2.carbon.identity.user.ekyc.util.UserEKYCConstants.EKYCProccessStatuses.PENDING;

@Listeners(MockitoTestNGListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserEKYCConnectorImplTest {

    private static final String TEST_SESSION_ID = "test-session-id";
    private static final String TEST_USER_ID = "test-user-id";
    private static final String TEST_USERNAME = "test-username";
    private static final int TEST_TENANT_ID = 1234;
    private static final String TEST_VC_STATUS = FINISHED;
    private String testVcData;
    private EKYCVerifiedCredentialDTO testVc;

    private EKYCVerifiedCredentialDAO ekycVerifiedCredentialDAO;
    private EKYCServiceDataHolder ekycServiceDataHolder;
    private RealmService realmService;
    private UserRealm userRealm;
    private IDVService idvService;
    private AbstractUserStoreManager userStoreManager;
    private ConfigurationManager configurationManager;
    private ArgumentCaptor<Map<String, String>> claimsCaptor;
    private ArgumentCaptor<String> vcCaptor;

    @BeforeClass
    public void before() throws org.wso2.carbon.user.api.UserStoreException, ConfigurationManagementException,
            IOException, IDVException {
        testVcData = getTestVc();
        testVc = new EKYCVerifiedCredentialDTO(
                TEST_SESSION_ID,
                TEST_USER_ID,
                TEST_TENANT_ID,
                TEST_VC_STATUS,
                testVcData);

        ekycVerifiedCredentialDAO = mock(EKYCVerifiedCredentialDAO.class);
        ekycServiceDataHolder = mock(EKYCServiceDataHolder.class);
        realmService = mock(RealmService.class);
        userRealm = mock(UserRealm.class);
        idvService = mock(IDVService.class);
        userStoreManager = mock(AbstractUserStoreManager.class);
        configurationManager = mock(ConfigurationManager.class);
        claimsCaptor = ArgumentCaptor.forClass(Map.class);
        vcCaptor = ArgumentCaptor.forClass(String.class);

        when(userStoreManager.isReadOnly()).thenReturn(false);
        when(userRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(realmService.getTenantUserRealm(TEST_TENANT_ID)).thenReturn(userRealm);
        when(ekycServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(configurationManager.getResource(EKYC_RESOURCE_TYPE, RESOURCE_NAME)).thenReturn(getEkycResource());
        when(ekycServiceDataHolder.getConfigurationManager()).thenReturn(configurationManager);
        when(ekycServiceDataHolder.getIdvService()).thenReturn(idvService);

        MockedStatic<EKYCServiceDataHolder> serviceMock = Mockito.mockStatic(EKYCServiceDataHolder.class);
        serviceMock.when(() -> EKYCServiceDataHolder.getInstance()).thenReturn(ekycServiceDataHolder);

        MockedStatic<EKYCVerifiedCredentialDAO> daoMock = Mockito.mockStatic(EKYCVerifiedCredentialDAO.class);
        daoMock.when(() -> EKYCVerifiedCredentialDAO.getInstance()).thenReturn(ekycVerifiedCredentialDAO);
    }

    @Test
    public void getPendingVerifiedCredential() throws UserEKYCException, IDVException,
            ConfigurationManagementException, UserStoreException, IOException {
        //BEFORE
        testVc.setStatus(PENDING);
        when(ekycVerifiedCredentialDAO.getUserEKYCVC(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID))
                .thenReturn(testVc);
        when(idvService.getSessionVerifiedCredential(TEST_USER_ID, TEST_SESSION_ID))
                .thenReturn(new JsonParser().parse(testVcData).getAsJsonObject());
        //TEST
        UserEKYCConnectorImpl.getInstance().getPendingVerifiedCredential(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID);
        //ASSERT
        verify(ekycVerifiedCredentialDAO).updateVerifiedCredential(
                matches(TEST_SESSION_ID),
                matches(TEST_USER_ID),
                eq(TEST_TENANT_ID),
                vcCaptor.capture(),
                matches(FINISHED));
        String vc = vcCaptor.getValue();
        Assert.assertEquals(vc, getTestVc());
    }

    @Test
    public void testUpdateUserClaimsFromVerifiedCredential() throws UserEKYCException, org.wso2.carbon.user.api
            .UserStoreException, ConfigurationManagementException {
        //BEFORE
        testVc.setStatus(FINISHED);
        when(ekycVerifiedCredentialDAO.getUserEKYCVC(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID))
                .thenReturn(testVc);
        //TEST
        UserEKYCConnectorImpl.getInstance()
                .updateUserClaimsFromVerifiedCredential(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID);
        //ASSERT
        verify(userStoreManager).setUserClaimValuesWithID(matches(TEST_USER_ID), claimsCaptor.capture(), any());
        Map<String, String> claimsToModify = claimsCaptor.getValue();
        Assert.assertEquals(claimsToModify.size(), 2);
        Assert.assertEquals(claimsToModify.get("http://wso2.org/claims/lastname"), "Doe");
        Assert.assertEquals(claimsToModify.get("http://wso2.org/claims/firstname"), "John");
    }

    private Resource getEkycResource() throws IOException {
        Properties ekycProperty = new Properties();
        ekycProperty.load(getClass().getClassLoader().getResourceAsStream("ekyc.properties"));
        Resource ekycResource = new Resource(EKYC_RESOURCE_TYPE, RESOURCE_NAME);

        List<Attribute> attributeList = ekycProperty.entrySet().stream()
                .map((entry) -> new Attribute(entry.getKey().toString(), entry.getValue().toString()))
                .collect(Collectors.toList());
        ekycResource.setAttributes(attributeList);

        return ekycResource;
    }

    private String getTestVc() throws IOException {
        return new JsonParser()
                .parse(Resources.toString(getClass().getClassLoader().getResource("vc.json"), StandardCharsets.UTF_8))
                .toString();
    }
}

