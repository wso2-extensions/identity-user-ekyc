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
package org.wso2.carbon.identity.user.ekyc.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.wso2.carbon.identity.configuration.mgt.core.ConfigurationManager;
import org.wso2.carbon.identity.user.ekyc.UserEKYCConnector;
import org.wso2.carbon.identity.user.ekyc.UserEKYCConnectorImpl;
import org.wso2.carbon.user.core.service.RealmService;


@Component(
        name = "org.wso2.carbon.identity.user.ekyc.internal.EKYCServiceComponent",
        immediate = true)
public class EKYCServiceComponent {

    private static final Log log = LogFactory.getLog(EKYCServiceComponent.class);

    private EKYCServiceDataHolder dataHolder = EKYCServiceDataHolder.getInstance();

    @Activate
    protected void activate(BundleContext context) {
        try {

            ServiceRegistration userEKYCConnectorSR = context
                    .registerService(UserEKYCConnector.class.getName(), UserEKYCConnectorImpl
                            .getInstance(), null);
            if (userEKYCConnectorSR != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Identity user account ekyc service component activated successfully.");
                }
            } else {
                log.error("Identity user account ekyc service component activation failed.");
            }

        } catch (Exception e) {
            log.error("Failed to activate identity ekyc connector service component ", e);
        }
    }

    @Reference(service = RealmService.class)
    protected void setRealmService(RealmService realmService) {
        log.debug("Setting the Realm Service");
        dataHolder.setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        log.debug("Unsetting the Realm Service");
        dataHolder.setRealmService(null);
    }

    @Reference(service = ConfigurationManager.class)
    protected void setConfigurationManager(ConfigurationManager configurationManager) {
        log.debug("Setting the ConfigurationManager");
        dataHolder.setConfigurationManager(configurationManager);
    }

    protected void unsetConfigurationManager(ConfigurationManager configurationManager) {
        log.debug("Unsetting the ConfigurationManager");
        dataHolder.setConfigurationManager(null);
    }


    @Deactivate
    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("Identity account ekyc connector service component is deactivated ");
        }
    }


}

