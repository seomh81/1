package com.hdel.miri.concurrent.domain.dgk;

import com.hdel.miri.concurrent.domain.dgk.vo.CcLogVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.dgk.vo.RespVO;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.SelfInspectHis;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.CcSourceVO;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.GetSubscriberListVO;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.MsgTemplateVO;
import com.hdel.miri.concurrent.domain.dgk.vo.CcElevatorVO;
import com.hdel.miri.concurrent.global.config.database.annotation.DS1Annotation;
import org.apache.ibatis.annotations.Mapper;
import com.hdel.miri.concurrent.domain.scrm.SCRM;
import com.hdel.miri.concurrent.domain.user.User;
import com.hdel.miri.concurrent.domain.message.CcMessageVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;

@DS1Annotation
@Mapper
public interface CcRepository {
    void insertCcTargetElList(List<ReqVO.ElVO> target);

    //20230718 EL 번호 없은 계약
    void insertCcTargetNoElList(List<ReqVO.NoElVO> target);

    //20230810 SCRM/SRM - 지사/영업소 정보 가져오기
    void insertOrgMstInfoList(List<ReqVO.NoElVO> target);    

    // 20230617 gmw db_type 별 삭제
    void deleteCcTargetElList(String db_type);

    //20230718 del_yn = 'y' 삭제
    void deleteElevatorInfo(String elevator_no);

    // SCRM에서 가져온 EL정보 수집 대상 리스트
    List<String> getTargetElList(String db_type);

    //SCRM/SRM 가져온 EL정보 수집 대상 리스트-> db_type관계없이  
    List<ReqVO.ElVO> getTargetElListNew();

    //거래선 중복 El_no 수집 2023-10-04 
    List<ReqVO.ElInfoVO> getCheckElInfo();

    // CT_EL_INFO의 전체 호기 리스트 가져오기
    List<String> getAllElInfo();

    // 보험이력이 오늘자로 만료된 엘리베이터만 동기화 대상으로 한다.
    List<String> getInssuranceTargetElInfo();

    // 안전관리자가 오늘자로 만료된 엘리베이터만 동기화 대상으로 한다.
    List<String> getSafetyMgrElInfo();

    // CT_EL_INFO의 전체 호기 리스트 가져오기
    List<String> getElInfoBySrc(String db_type);

    List<CcElevatorVO.CcElevatorInfoForSAP> getAllElInfoForSAP(List<List<String>> target);

    // 정기검사 대상 가져오기 : 이번월에 검사실적이 없는 호기만
    List<String> getInspHistTargetList();

    // 실제 동기화 SQLs
    void insertElInfo(List<SCRM.VO> elList);

    void insertInssuranceInfo(List inssuranceList);

    void insertSafetyMgrInfo(List safetyMgrList);

    void insertSelfInspHisInfo(String yyyymm, SelfInspectHis.Item item);

    void insertInspHisInfo(List inspHisList);

    // logging을 위한 메소드
    int insertStartLog(CcLogVO.CcLogCreate info);

    //정기검사용
    int insertStartLogInspInfo(CcLogVO.CcLogCreate info);

    void updateEndLog(CcLogVO.CcLogUpdate info);

    void upsertUnbilledInfo(HashMap data);

    List<RespVO.ConcurrentLogVO> getConcurrentErrors(String type, String apiType);

    List<ReqVO.ElVO> getElevatorErrors();

    void deleteErrorLogs(List<RespVO.ConcurrentLogVO> data);

    void deleteErrorLogsNew(List<ReqVO.ElVO> data); 

    void clearUnbilledInfo(String type);

    void deleteExpiredElevatorInfo(String pType);

    void deleteAccountChange();

    //통합프로젝트/거래선 변경에 따른 Portfolio 업데이트 2023.10.30
    void updateIntgPrjNoTrlineCd();

    // 메세지 송신
    List<GetSubscriberListVO> GetSubscriberForAlarm(CcSourceVO req);

    MsgTemplateVO GetMsgTemplate(String alarmType);

    String GetAlarmYn();

    // 공지사항 알람 소스
    List<CcSourceVO> GetNoticeSourceList();

    // 계약만료 15일전 소스
    List<CcSourceVO> GetExpContractSourceList();

    // 지난주 자체점검 완료 리스트
    List<CcSourceVO> GetSelfInspCompleteSourceList();

    int insertAlarmLog(String _method, String _type, String _event, String _contents, String _receiverId,
            String _receiverPhoneNo);

    //메일 대상 추출 
    List<ReqVO.TargetMailVO> getSendMailAccount();      
    
    //메일 발송 로그 남기기
    int insertMailLog(ReqVO.mailLogVO mailVO);

    //메일 템플릿 가져오기 : 9997
    String GetMsgTemplateContents(String msgCode);

    // Manager 등록 2023-10-13 gmw
    List<ReqVO.UserVO> getUserExist(User.DefaultUserCreate request);
    
    int insertExistPortfolio(User.DefaultUserCreate request);

    // Manager 등록 2023-10-13 gmw
    int insertTypeManager(User.DefaultUserCreate request);

    int updateSendAcceptKey(User.UpdateAcceptKey request);

    int insertDefaultT(User.TermsDefaultCreate request);

    int insertDefaultS(User.SetupDefaultCreate request);

    int insertDefaultP(User.PortfolioDefaultCreate request);

    // int insertDefaultC(BigDecimal userPortfolioMappingId, User.DefaultUserCreate request);
    int insertDefaultC(BigDecimal userPortfolioMappingId,String  intgPrjTrlineCdCode);

    int insertDefaultA(User.AlarmDefaultCreate request);

    int insertUserRoleMapping2(User.DefaultUserCreate request);
    
    //유상 계약 번호로 거래선 가져오기
    List<User.ContractAPI> getContractInfo(User.DefaultUserCreate request);

    // 2023-10-17 add 위/경도 update
    void updateElInfo(List<SCRM.VO> elList);

    // 2023-10-17 add 위/경도 null인 승강기 조회 
    List<SCRM.VO> getUpdateElInfo();

}
    
