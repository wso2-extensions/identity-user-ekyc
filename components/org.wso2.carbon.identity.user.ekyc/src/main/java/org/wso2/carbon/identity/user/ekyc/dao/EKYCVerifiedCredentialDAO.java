/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.user.ekyc.dao;

import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.user.ekyc.dto.EKYCVerifiedCredentialDTO;
import org.wso2.carbon.identity.user.ekyc.exception.UserEKYCException;
import org.wso2.carbon.identity.user.ekyc.util.UserEKYCConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EKYCVerifiedCredentialDAO {


    public static EKYCVerifiedCredentialDAO getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void createUserEKYC(String sessionId, String userId,int tenantId, String status, String vc) throws UserEKYCException {
        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UserEKYCConstants
                     .SQLQueries.ADD_USER_EKYC_VC)){

            preparedStatement.setString(1, sessionId);
            preparedStatement.setString(2, userId);
            preparedStatement.setInt(3, tenantId);
            preparedStatement.setString(4, status);
            preparedStatement.setString(5, vc);
            preparedStatement.executeUpdate();

            if (!dbConnection.getAutoCommit()) {
                dbConnection.commit();
            }
        } catch (SQLException e) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages
                    .CONN_CREATE_DB_ERROR.getDescription(), e);
        }
    }

    public List<EKYCVerifiedCredentialDTO> getUserEKYCVCs(String userId, int tenantId) throws UserEKYCException {
        List<EKYCVerifiedCredentialDTO> ekycVcs = new ArrayList<>();
        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UserEKYCConstants
                     .SQLQueries.GET_USER_EKYC_VCS)){

            preparedStatement.setString(1, userId);
            preparedStatement.setInt(2, tenantId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String sessionId = resultSet.getString(1);
                    String status = resultSet.getString(2);
                    String vc = resultSet.getString(3);

                    EKYCVerifiedCredentialDTO ekycVerifiedVCredentialDTO = new EKYCVerifiedCredentialDTO(
                            sessionId,
                            userId,
                            tenantId,
                            status,
                            vc
                    );
                    ekycVcs.add(ekycVerifiedVCredentialDTO);
                }
            }
        } catch (SQLException e) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages
                    .CONN_CREATE_DB_ERROR.getDescription(), e);
        }
        return ekycVcs;
    }

    public EKYCVerifiedCredentialDTO getUserEKYCVC(String sessionId, String userId, int tenantId) throws UserEKYCException {
        EKYCVerifiedCredentialDTO ekycVerifiedVCredentialDTO = null;
        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UserEKYCConstants
                     .SQLQueries.GET_USER_EKYC_VC)){

            preparedStatement.setString(1, sessionId);
            preparedStatement.setString(2, userId);
            preparedStatement.setInt(3, tenantId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.first()){
                    String status = resultSet.getString(1);
                    String vc = resultSet.getString(2);

                    ekycVerifiedVCredentialDTO = new EKYCVerifiedCredentialDTO(
                            sessionId,
                            userId,
                            tenantId,
                            status,
                            vc
                    );
                }
            }
        } catch (SQLException e) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages
                    .CONN_CREATE_DB_ERROR.getDescription(), e);
        }
        return ekycVerifiedVCredentialDTO;
    }

    public void deleteUserEKYCVC(String sessionId, String userId, int tenantId) throws UserEKYCException {
        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UserEKYCConstants
                     .SQLQueries.DELETE_USER_EKYC_VC)){

            preparedStatement.setString(1, sessionId);
            preparedStatement.setString(2, userId);
            preparedStatement.setInt(3, tenantId);

            preparedStatement.execute();
            if (!dbConnection.getAutoCommit()) {
                dbConnection.commit();
            }
        } catch (SQLException e) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages
                    .CONN_CREATE_DB_ERROR.getDescription(), e);
        }
    }

    public void updateVerifiedCredential(String sessionId, String userId, int tenantId, String vc, String status) throws UserEKYCException {

        try (Connection dbConnection = IdentityDatabaseUtil.getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(UserEKYCConstants
                     .SQLQueries.UPDATE_USER_EKYC_VC)) {

            preparedStatement.setString(1, vc);
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, sessionId);
            preparedStatement.setString(4, userId);
            preparedStatement.setInt(5, tenantId);
            preparedStatement.executeUpdate();

            if (!dbConnection.getAutoCommit()) {
                dbConnection.commit();
            }
        } catch (SQLException e) {
            throw new UserEKYCException(UserEKYCConstants.ErrorMessages
                    .CONN_CREATE_DB_ERROR.getDescription(), e);
        }
    }


    private static class LazyHolder {
        private static final EKYCVerifiedCredentialDAO INSTANCE = new EKYCVerifiedCredentialDAO();
    }

}