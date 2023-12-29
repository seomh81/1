package com.hdel.miri.api.domain.user;


import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface UserRepository {
    List<User.UserSummary> selectList(User.UserSearch request);
    List<User.ViewRenderingByUser> selectCurrentUserPage(User.UserRenderingRequest request);
    List<User.mailReSendByUser> selectAcceptKeyForReSend(User.UserRecovery request);
    String selectUserByPhoneAndUsername(User.FindAccount request);
    String selectUserByPhoneAndUserId(User.FindPassword request);

    List<User.nonContractData> selectNonContractList();

    User.userAcceptMailInfo selectInfoForAcceptUser(User.UserAccountStatus request);

    List<String> selectSystemUser();

    int selectCountByUserId(User.UserAlready request);
    int selectCountByUserIdExt(User.UserAlreadyExt request);
    /**
     * Join By UserType
     * */
    int insertTypeUser(User.DefaultOtherUserCreate request);
    int insertTypeManager(User.DefaultUserCreate request);
    int insertTypeMajor(User.DefaultOtherUserCreate request);
    int insertTypeEngineer(User.DefaultOtherUserCreate request);
    int insertTypeAccount(User.DefaultOtherUserCreate request);
    int insertTypeSystemAdmin(User.DefaultOtherUserCreate request);
    int insertTypeAuto(User.DefaultOtherUserCreate request);

    int insertUserRoleMapping(User.DefaultOtherUserCreate request);
    int insertUserRoleMapping2(User.DefaultUserCreate request);
    

    int update(User.UserUpdate request);
    int updateAdminPage(User.UserUpdateAdminPage request);
    int updateAccountStatus(User.UserAccountStatus request);
    int updateInnerAccountStatus(User.UserAccountStatus request);
    int updatePassword(User.ChangePassword request);
    int updatePasswordForAnonymous(User.FindPassword request);
    int updateSendAcceptKey(User.UpdateAcceptKey request);
    int updateRecvAcceptKey(User.UpdateAcceptKey request);
    int remove(User.UserRemove request);
    int recovery(User.UserRecovery request);

    int insertPortfolioNonContract(User.setNonContractUser request);
}
