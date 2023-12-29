package com.hdel.miri.api.domain.cc;

import com.hdel.miri.api.domain.cc.CC.AlarmHisVO;
import com.hdel.miri.api.domain.cc.CC.CCServiceVO;
import com.hdel.miri.api.domain.cc.CC.MsgTemplateVO;
import com.hdel.miri.api.domain.cc.CC.PrjNoVO;
import com.hdel.miri.api.domain.cc.CC.ServiceReqInfoVO;
import com.hdel.miri.api.domain.mail.MailVO;
import com.hdel.miri.api.domain.miri.MIRI;
import com.hdel.miri.api.domain.hrts.HRTS;
import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;
import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.contract.Contract.ContractJoin2;
import com.hdel.miri.api.domain.contract.Contract.ContractJoinDistinct;

import java.math.BigDecimal;
import java.util.List;

@DS2Annotation
@Mapper
public interface CCRepository {

    List<CC.SelfInspectVO> selectSelfInsepect(CC.InspectSearch request);
    List<CC.SelfInspectHeadVO> selectSelfInsepectManage(CC.InspectSearchByPortfolio request);
    List<CC.SelfInspectHistoryVO> selectSelfInsepectHistory(CC.SelfInspectVO request);
    // List<CC.SelfInspectHistoryVO> selectSelfInsepectHeadHistory(CC.InspectSearchBySelfInspectHead request);
    List<CC.InspectVO> selectInsepect(CC.InspectSearch request);
    List<CC.InspectHeadVO> selectInsepectManage(CC.InspectSearchByPortfolio request);

    List<CC.ELInfoJoinToHCCC> selectCurrentELInfoJoinToHCCC(CC.CCCurrentRequest request);
    List<CC.ELInfoJoinToHCCC> selectPortfolioELInfoJoinToHCCC(CC.CCCurrentPortfolioRequest request);

   
    Long selectElevatorTotalCount(CC.CCCurrentPortfolioRequest request);
    Long selectElevatorSuccessCount(CC.CCCurrentPortfolioRequest request);
    Long selectElevatorFailCount(CC.CCCurrentPortfolioRequest request);

    Long selectCompTotalCount(CC.CCCurrentPortfolioRequest request);
    Long selectMisuTotalCount(CC.CCCurrentPortfolioRequest request);

    // Calendar VO
    List<String> getPrjInfoWithPortfolioId(CC.CCCurrentCalenderRequest request);
    List<CC.CalenderVO> selectSelfInsepectCurrentCalender(CC.CCCurrentCalenderRequest request);
    List<CC.CalenderVO> selectInsepectCurrentCalender(CC.CCCurrentCalenderRequest request);
    List<CC.CCElevatorInfoVO> selectElevatorList(CC.CCCurrentElevatorSearchByPortfolio request);
    CC.CCElevatorDetailInfoVO selectElevator(CC.CCCurrentElevatorRequest request);
    List<CC.CCBillHistoryVO> selectBillHistory(CC.CCCurrentBillHistoryRequest request);
    List<CC.CCUnitsTreeVO> selectUnitTreeByPortfolio(CC.CCCurrentPortfolioRequest request);
    List<String> selectProjnosByPortfolio(String userPortfolioMappingId);
    List<CC.CCProjNoExtTRLineVO> selectProjnosByPortfolioExtTRLine(String userPortfolioMappingId);
    List<CC.IntegratedInspectVO> selectProjnosByPortfoliAIMasterJoin(HRTS.HRTSMasterSearchWeb userPortfolioMappingId);
    Long selectSelfInspTotalWithMonth(CC.InspectSearchByPortfolioExtMonth request);
    Long selectSelfInspSuccessWithMonth(CC.InspectSearchByPortfolioExtMonth request);
    List<CC.IntegratedInspectVO> selectIntegratedInspList(CC.CCCurrentPortfolioRequestExtSearch request);
    CC.IntegratedMasterInfoVO selectIntegratedMasterInfo(CC.ElvSearch request);
    CC.CCCurrentElevatorChangeDateResponse selectElevatorInfoWithChangeDate(CC.CCCurrentElevatorChangeDateRequest request);

    List<String> getPrjInfoWithPortfolioId(CC.CCServiceRequest request);
    List<CCServiceVO> getServices(CC.CCServiceRequest request);
 
    List<AlarmHisVO> getAlarmHis(CC.AlarmSearchREQ request);
    List<MsgTemplateVO> getMsgTemplate(CC.MsgTemplateSearchREQ request);
    int updateMsgTemplate(CC.MsgTemplateUpdateREQ request);

    int getCompanionDaysDistinct(Contract.ContractSearchCurrentUser request);

    // 정기점검 특이사항
    List<String> getFailRemakrOr();
    List<String> getFailRemakrNot();

    List<CC.BuldNmVO> getBuldNms(List<String> prjhnos);

    int updateWidgets(CC.DashboardWidgetREQ request);

    ServiceReqInfoVO selectServiceReqInfo(CC.CCServiceREQ request);
    void insertServiceRequest(CC.ServiceReqInfoVO request);

    List<Contract.ContractEmployeeAPI> employeeContractSearch(Contract.ContractSearchEmployee request);

    String GetMsgTemplateContents(String msgCode);

    List<PrjNoVO> selectProjNosFromMappingId(HRTS.HRTSDistanceAvgSearch request);
    // 메일 전송 로그
    int insertMailLog(CC.mailLogVO mailVO);
    //KPI용 Login Data 조회
    List<MIRI.MIRIPortalLoginData> getMIRIPortalData(MIRI.MIRIPortalLoginSearch request);
}