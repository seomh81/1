package com.hdel.miri.api.domain.privilege.policy;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@DS2Annotation
@Mapper
public interface PolicyRepository {

    /**
     * View - Functions API.
     * */
    List<Policy.FunctionsByView> selectFunctionsByView();
    int insertFunctionsByView(Policy.FunctionsMappedByView request);
    int deleteFunctionsByView(Policy.FunctionsMappedByView request);
    /**
     * View - VueFile API.
     * */
    int updateLinkVueFile(Policy.VueFileLink request);
    int updateUnlinkVueFile(Policy.VueFileLink request);
    /**
     * Menu - Views API.
     * */
    List<Policy.ViewsByMenu> selectViewsByMenu();
    int insertViewsByMenu(Policy.ViewsMappedByMenu request);
    int deleteViewsByMenu(Policy.ViewsMappedByMenu request);
    /**
     * Role - Users API.
     * */
    List<Policy.UsersByRole> selectUsersByRole();
    int insertUsersByRole(Policy.UsersMappedByRole request);
    int deleteUsersByRole(Policy.UsersMappedByRole request);
    /**
     * Role - Menus API.
     * */
    List<HashMap<String,Object>> selectMenusByRole();
    int insertMenusByRole(Policy.MenusMappedByRole request);
    int deleteMenusByRole(Policy.MenusMappedByRole request);
}
