package org.wso2.carbon.identity.user.ekyc;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.mockito.testng.MockitoSettings;
import org.testng.Assert;
import org.testng.annotations.*;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.user.ekyc.dao.EKYCVerifiedCredentialDAO;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDTO;
import org.wso2.carbon.identity.user.ekyc.exception.IDVException;
import org.wso2.carbon.identity.user.ekyc.exception.UserEKYCException;
import org.mockito.testng.MockitoTestNGListener;
import org.wso2.carbon.identity.user.ekyc.idv.IDVService;
import org.wso2.carbon.identity.user.ekyc.internal.EKYCServiceDataHolder;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
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
    private  String TEST_VC_DATA;
    private  EKYCVerifiedCredentialDTO TEST_VC;

    private EKYCVerifiedCredentialDAO ekycVerifiedCredentialDAO;
    private EKYCServiceDataHolder ekycServiceDataHolder;
    private RealmService realmService;
    private UserRealm userRealm;
    private IDVService idvService;
    private UserStoreManager userStoreManager;
    private ConfigurationManager configurationManager;
    private ArgumentCaptor<Map<String,String>> claimsCaptor;
    private ArgumentCaptor<String> vcCaptor;

    @BeforeClass
    public void before()  throws org.wso2.carbon.user.api.UserStoreException, ConfigurationManagementException, IOException{
        TEST_VC_DATA = getTestVc();
        TEST_VC = new EKYCVerifiedCredentialDTO(
                TEST_SESSION_ID,
                TEST_USER_ID,
                TEST_TENANT_ID,
                TEST_VC_STATUS,
                TEST_VC_DATA);

        ekycVerifiedCredentialDAO = mock(EKYCVerifiedCredentialDAO.class);
        ekycServiceDataHolder = mock(EKYCServiceDataHolder.class);
        realmService = mock(RealmService.class);
        userRealm = mock(UserRealm.class);
        idvService = mock(IDVService.class);
        userStoreManager = mock(UserStoreManager.class);
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
    public void getPendingVerifiedCredential() throws UserEKYCException, IDVException, ConfigurationManagementException, UserStoreException, IOException {
        //BEFORE
        TEST_VC.setStatus(PENDING);
        when(ekycVerifiedCredentialDAO.getUserEKYCVC(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID)).thenReturn(TEST_VC);
        when(idvService.getSessionVc(TEST_USER_ID,TEST_SESSION_ID)).thenReturn(new JsonParser().parse(TEST_VC_DATA).getAsJsonObject());
        //TEST
        UserEKYCConnectorImpl.getInstance().getPendingVerifiedCredential(TEST_SESSION_ID,TEST_USER_ID,TEST_TENANT_ID);
        //ASSERT
        verify(ekycVerifiedCredentialDAO).updateVerifiedCredential(
                matches(TEST_SESSION_ID),
                matches(TEST_USER_ID),
                eq(TEST_TENANT_ID),
                vcCaptor.capture(),
                matches(FINISHED));
        String vc = vcCaptor.getValue();
        Assert.assertEquals(vc,getTestVc());
    }

    @Test
    public void testUpdateUserClaimsFromVerifiedCredential() throws UserEKYCException, org.wso2.carbon.user.api.UserStoreException, ConfigurationManagementException {
        //BEFORE
        TEST_VC.setStatus(FINISHED);
        when(ekycVerifiedCredentialDAO.getUserEKYCVC(TEST_SESSION_ID, TEST_USER_ID, TEST_TENANT_ID)).thenReturn(TEST_VC);
        //TEST
        UserEKYCConnectorImpl.getInstance().updateUserClaimsFromVerifiedCredential(TEST_SESSION_ID,TEST_USER_ID,TEST_USERNAME,TEST_TENANT_ID);
        //ASSERT
        verify(userStoreManager).setUserClaimValues(matches(TEST_USERNAME),claimsCaptor.capture(),any());
        Map<String,String> claimsToModify = claimsCaptor.getValue();
        Assert.assertEquals(claimsToModify.size(),2);
        Assert.assertEquals(claimsToModify.get("http://wso2.org/claims/lastname"),"Doe");
        Assert.assertEquals(claimsToModify.get("http://wso2.org/claims/firstname"),"John");
    }

    private Resource getEkycResource() throws IOException {
        Properties ekycProperty = new Properties();
        ekycProperty.load(getClass().getClassLoader().getResourceAsStream("ekyc.properties"));
        Resource ekycResource = new Resource(EKYC_RESOURCE_TYPE, RESOURCE_NAME);

        List<Attribute> attributeList = ekycProperty.entrySet().stream()
                .map((entry)-> new Attribute(entry.getKey().toString(),entry.getValue().toString()))
                .collect(Collectors.toList());
        ekycResource.setAttributes(attributeList);

        return ekycResource;
    }

    private String getTestVc() throws IOException {
        return new JsonParser().parse(Resources.toString(getClass().getClassLoader().getResource("vc.json"), StandardCharsets.UTF_8)).toString();
    }
}