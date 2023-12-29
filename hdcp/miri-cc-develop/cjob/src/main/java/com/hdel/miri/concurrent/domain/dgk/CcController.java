package com.hdel.miri.concurrent.domain.dgk;

import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.util.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;
import com.hdel.miri.concurrent.domain.srm.SRMRepository;
import com.hdel.miri.concurrent.domain.user.User;

@Tag(name = "공공데이타 포탈 API 연계", description = "공공데이타 포탈 연계 API")
@RestController
@RequestMapping("/v2/cc/")
@RequiredArgsConstructor
public class CcController {

        private final CcService ccService;
        private final CcRepository ccRepository;
        private final CcAsyncService ccAsyncService;
        private final SCRMRepository scrmRepository;
        private final SRMRepository srmRepository;

        private String apType = System.getProperty("aptype");

        @Operation(summary = "승강기 타겟 Temp 등록", description = "**SCRM/SRM**에서 타겟을 가져와서 등록 API, <br> 데이타타입 : ReqVO.ElevatorTargetVO <br> lastUpdateDt에 ALL or all을 넣고 elList를 넣지 않으면 전체를 동기화대상으로 가져옴")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/get-cc-target-el-list") 
        public void upsertCcTargetElList(
                        @Parameter(description = "Schemas - get-cc-target-el-list") @RequestBody ReqVO.ElevatorTargetSyncVO request) {
                List<ReqVO.ElVO> _scrmList = null;
                List<ReqVO.ElVO> _srmList = null;
                List<ReqVO.NoElVO> _scrmListNo = null;
                List<ReqVO.NoElVO> _srmListNo = null; 
                List<ReqVO.NoElVO> _scrmOrgInfo = null;
                List<ReqVO.NoElVO> _srmOrgInfo = null;
                //SCRM 계약 대상 추출               
                if (request.getScrmList() == null || request.getScrmList().size() < 1) {
                        if (request.getLastUpdateDt() == null || request.getLastUpdateDt() == "") {
                                Calendar calendar = new GregorianCalendar();
                                SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

                                String chkDate = SDF.format(calendar.getTime());
                                System.out.println("Today : " + chkDate);

                                calendar.add(Calendar.DATE, -1);
                                chkDate = SDF.format(calendar.getTime());
                                System.out.println("Yesterday : " + chkDate);
                                request.setLastUpdateDt(chkDate);
                        } else if ("ALL".equals(request.getLastUpdateDt()) || "all".equals(request.getLastUpdateDt())) {
                                request.setLastUpdateDt("");
                        }

                        _scrmList = scrmRepository.getCcTargetElList(request);
                        // List<ReqVO.ElVO> _unionList =
                        // ListUtils.union(scrmRepository.getCcTargetElList(request),
                        // srmRepository.getCcTargetElList(request));
                        ccAsyncService.insertCcTargetElList(_scrmList, "SCRM");
                        
                        //승강기 번호 없는 계약건-1
                        _scrmListNo = scrmRepository.getCcTargetNoElList(request);
                         ccAsyncService.insertCcTargetNoElList(_scrmListNo, "SCRM");

                         //지사코드 가져오기
                         _scrmOrgInfo = scrmRepository.getOrgInfoList();
                         ccAsyncService.insertOrgMstInfoList(_scrmOrgInfo);

                } else {
                        ccAsyncService.insertCcTargetElList(MakeSyncVO(request.getScrmList(), "SCRM"), "SCRM");
                }

                //SRM 계약 대상 추출               
                if (request.getSrmList() == null || request.getSrmList().size() < 1) {
                        if (request.getLastUpdateDt() == null || request.getLastUpdateDt() == "") {
                                Calendar calendar = new GregorianCalendar();
                                SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

                                String chkDate = SDF.format(calendar.getTime());
                                System.out.println("Today : " + chkDate);

                                calendar.add(Calendar.DATE, -1);
                                chkDate = SDF.format(calendar.getTime());
                                System.out.println("Yesterday : " + chkDate);
                                request.setLastUpdateDt(chkDate);
                        } else if ("ALL".equals(request.getLastUpdateDt()) || "all".equals(request.getLastUpdateDt())) {
                                request.setLastUpdateDt("");
                        }

                        _srmList = srmRepository.getCcTargetElList(request);
                        // List<ReqVO.ElVO> _unionList = ListUtils.union(scrmRepository.getCcTargetElList(request),
                        //                                               srmRepository.getCcTargetElList(request));
                        ccAsyncService.insertCcTargetElList(_srmList, "SRM");

                        //승강기 번호 없는 계약건 SRM
                        _srmListNo = srmRepository.getCcTargetNoElList(request);
                         ccAsyncService.insertCcTargetNoElList(_srmListNo, "SRM");

                         //영업소 정보 가져오기
                         _srmOrgInfo = srmRepository.getOrgInfoList();
                         ccAsyncService.insertOrgMstInfoList(_srmOrgInfo);

                } else {
                        ccAsyncService.insertCcTargetElList(MakeSyncVO(request.getSrmList(), "SRM"), "SRM");
                }

                //승강기 위치 정보 (위도, 경도) Update 2023-10-17
                syncUpdateElInfo();      

                //통합프로젝트번호/거래선 변경에 따는 포트폴리오 데이터 Update 2023-10-30
                syncPortfolioUpdate();      
        }

        private List<ReqVO.ElVO> MakeSyncVO(List<String> elNos, String dbType) {
                List<ReqVO.ElVO> _rtnVal = new ArrayList<>();
                for (String _elNo : elNos) {
                        _rtnVal.add(ReqVO.ElVO.builder().db_type(dbType).del_yn("n").elevator_no(_elNo).build());
                }
                return _rtnVal;
        }

        @Operation(summary = "건물별 승강기 등록", description = "건물별 승강기 등록 API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-building-el-info")
        public void syncBuildingElInfo() {
                if(ccService.syncBuildingElInfoNew() == 1){
                    //승강기 정보변경에 따른 보정
                    ccService.syncScrmSrmElInfo();
                }
        }
        //2023-10-04 add
        @Operation(summary = "승강기, 거래선 중복에 따른 보정_SCRM_SRM ", description = "승강기/거래선 변경 보정 (SCRM/SRM) API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-change-el-info")
        public void syncChangeElInfo() {
                //승강기 정보변경에 따른 보정(SCRM, SRM)
                ccService.syncScrmSrmElInfo();
        }

        //2023-10-17 add
        @Operation(summary = "승강기 위치 정보 업데이트", description = "승강기 위치 정보 등록 API-누락분 ")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-update-el-info")
        public void syncUpdateElInfo() {
                if (!"ap1".equals(apType)){
                        ccService.syncUpdateElInfo();
                }
        }

        //2023-10-30 add
        @Operation(summary = "통합프로젝트/거래선 변경에 따른 Portfolio 업데이트", description = "통합프로젝트/거래선 변경에 따른 portfolio 업데이트")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-update-portfolio-info")
        public void syncPortfolioUpdate() {
                if (!"ap1".equals(apType)){
                        // 계약 영업사원 변경 정리
                        ccRepository.updateIntgPrjNoTrlineCd();
                }
        }
        
        @Operation(summary = "정기검사 이력 등록", description = "정기검사 이력 등록 API, ReqVO.SyncElevatorTargetVO")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-insphis-info")
        public void syncInspHisInfo(
                        @Parameter(description = "Schemas - sync-insphis-info") @RequestBody ReqVO.SyncInspElevatorTargetVO request) {
                ccService.syncInspHisInfo(request);
        }

        @Operation(summary = "정기검사 이력 등록- 과거이력", description = "정기검사 이력 등록 API, ReqVO.SyncElevatorTargetVO")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-insphis-info-last")
        public void syncInspHisInfoLast(
                        @Parameter(description = "Schemas - sync-insphis-info") @RequestBody ReqVO.SyncInspElevatorTargetVO request) {
                ccService.syncInspHisInfoLast(request);
        }

        @Operation(summary = "승강기 보험정보 등록", description = "승강기 보험정보 등록 API, ReqVO.SyncElevatorTargetVO")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-inssurance-info")
        public void syncInssuranceInfo(
                        @Parameter(description = "Schemas - sync-inssurance-info") @RequestBody ReqVO.SyncElevatorTargetVO request) {
                ccService.syncInssuranceInfo(request);
        }

        @Operation(summary = "승강기 안전관리자 등록", description = "승강기 안전관리자 등록 API, ReqVO.SyncElevatorTargetVO")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-safetymgr-info")
        public void syncSafetyMgrInfo(
                        @Parameter(description = "Schemas - sync-safetymgr-info") @RequestBody ReqVO.SyncElevatorTargetVO request) {
                ccService.syncSafetyMgrInfo(request);
        }

        @Operation(summary = "자체점검 이력 등록", description = "자체점검 이력 등록 API, ReqVO.SyncElevatorTargetVO2")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-selfinsphis-info")
        public void syncSelfInspHisInfo(
                        @Parameter(description = "Schemas - sync-selfinsphis-info") @RequestBody ReqVO.SyncElevatorTargetVO2 request) {
                ccService.syncSelfInspHisInfo(request);
        }

        @Operation(summary = "미수금 이력 등록", description = "미수금 이력 등록 API, ReqVO.SyncElevatorTargetVO")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-unbilled-info")
        public void syncUnbilledInfo(
                        @Parameter(description = "Schemas - sync-unbilled-info") @RequestBody ReqVO.UnBilledElevatorTargetVO request) {
                ccService.syncUnbilledInfo(request);
        }

        @Operation(summary = "계약 영업사원 변경 관리", description = "계약 영업사원 변경 관리 API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/sync-account-change-info")
        public void syncAccountChangeInfo() {
                ccService.syncAccountChangeInfo();
        }

        @Operation(summary = "정주기 API에러 재처리", description = "정주기 API에러 재처리 API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/fix-api-errors")
        @Scheduled(cron = "0 0 7 * * *")
        public void fixApiErrors() {
                if (!"ap1".equals(apType)){
                        //API 에러 재처리
                        ccService.fixApiErrors();

                        //승강기 정보변경에 따른 보정 
                        ccService.syncScrmSrmElInfo();

                        // 계약 영업사원 변경 정리
                        ccRepository.deleteAccountChange();
                }
        }

        @Operation(summary = "test", description = "test API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/test")
        public void Test() {
                String test = "http://openapi.elevator.go.kr/openapi/service/ElevatorInspectsafeService/getInspectsafeList?ServiceKey=GiAj%2F6Rr7rffxurP6kidOWfVafSTq5eGCvd2aIjN%2B9u%2F%2FcCeHMtS89DbjtrUMIgLZHUGbaVVKa%2FA%2BA8BfJm9yg%3D%3D&page_no=1&numOfRows=100&elevator_no=2345678&appr_sdt=20221201&appr_edt=20221231";
                String[] _ltest = test.split("&");

                System.out.println("결과...");
        }

        // SCRM/SRM 사용자 등록 2023-10-13 start
        @Operation(summary = "SCRM/SRM 건물관리자 자동 등록", description = "SCRM/SRM 건물관리자 자동 등록 API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/auto-register-manager")
        public void autoRegManager(){
                // SCRM/SRM 건물관리인 miri portal 등록 
                if (!"ap1".equals(apType)) {                
                       ccService.registerManager();
                }
        }

        @Operation(summary = "건물관리자 가입 통보 메일 발송", description = "영업사원에게 메일 전송 API")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                        @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/send-mail-account")
        public void sendMailAccount() {
                if (!"ap1".equals(apType)) {                
                        ccService.sendMailAccount();
                }
        }
        /**
         * SCRM/SRM의 어제 수정된 데이타를 동기화 대상으로 가져오기
         * 매일 0시, 대략 1일 300건정도 되는것으로 보임
         */
        @Scheduled(cron = "0 0 0 * * *")
        public void upsertCcTargetElList() {
                if (!"ap1".equals(apType)) {
                        // 어제수정된 대상을 가져와서 등록
                        ReqVO.ElevatorTargetSyncVO defaultVO = ReqVO.ElevatorTargetSyncVO.builder()
                                        .scrmList(new ArrayList()).srmList(new ArrayList()).lastUpdateDt(null).build();
                        upsertCcTargetElList(defaultVO);
                }
        }

        /**
         * 건물별 승강기 동기화
         * 매일 1시
         */
        @Scheduled(cron = "0 0 1 * * *")
        public void cronSyncBuildingElInfo() {
                if (!"ap1".equals(apType)) {
                        syncBuildingElInfo();
                }
        }

        /**
         * SCRM / SRM 건물관리인 회원 자동 등록 2023-10-13 add
         * 매일 9시
         */
        @Scheduled(cron = "0 0 12 * * 1-5")
        public void cronSyncRegManager() {

                if (!"ap1".equals(apType)) {
                       autoRegManager();
                }
        }
        /**
         * 미수금 가져오기
         * 매일 5시 --> 4:30 변경
         */
        @Scheduled(cron = "0 30 4 * * *")
        public void syncUnbilledInfo() {
                ReqVO.UnBilledElevatorTargetVO defaultVO = ReqVO.UnBilledElevatorTargetVO.builder()
                                .scrmList(new ArrayList()).srmList(new ArrayList()).build();
                syncUnbilledInfo(defaultVO);
        }

        /**
         * 정기검사 동기화  DEV에서만 수행함.
         * 토요일 2시
         */
        //@Scheduled(cron = "0 0 2 * * 6")
        public void syncInspHisInfo() {
                if (!"ap1".equals(apType)) {
                        ReqVO.SyncInspElevatorTargetVO defaultVO = ReqVO.SyncInspElevatorTargetVO.builder()
                                        .elList(new ArrayList()).syncYear(null).build();
                        syncInspHisInfo(defaultVO);
                }
        }

        /**
         * 정기점검 동기화
         * 목요일 4시
         */
        @Scheduled(cron = "0 0 4 * * 4")
        public void syncSelfInspHisInfo() {
                if (!"ap1".equals(apType)) {
                        ReqVO.SyncElevatorTargetVO2 defaultVO = ReqVO.SyncElevatorTargetVO2.builder()
                                        .elList(new ArrayList()).build();
                        syncSelfInspHisInfo(defaultVO);
                }
        }

        /**
         * 보험정보 동기화
         * 수요일 4시
         */
        @Scheduled(cron = "0 0 4 * * 3")
        public void syncInssuranceInfo() {
                if (!"ap1".equals(apType)) {
                        ReqVO.SyncElevatorTargetVO defaultVO = ReqVO.SyncElevatorTargetVO.builder()
                                        .elList(new ArrayList()).build();
                        syncInssuranceInfo(defaultVO);
                }
        }

        /**
         * 안전관리자 동기화
         * 화요일 4시
         */
        @Scheduled(cron = "0 0 4 * * 2")
        public void syncSafetyMgrInfo() {
                if (!"ap1".equals(apType)) {
                        ReqVO.SyncElevatorTargetVO defaultVO = ReqVO.SyncElevatorTargetVO.builder()
                                        .elList(new ArrayList()).build();
                        syncSafetyMgrInfo(defaultVO);
                }
        }

        /**
         * 영업사원에게 지난주 등록된 건물관리자 확인 요청 메일 발송
         * 월요일  오전 8시
         */
        @Scheduled(cron = "0 0 8 * * 1")
        public void syncSendMailAccount() {
                if (!"ap1".equals(apType)) {
                        ccService.sendMailAccount();
                }
        }

        /**
         * 모회사/자회사 el_no 변경 시, 기존 el_no 의 거래선이 변경된 경우 동기화 처리 한다
         * 평일(월~금) 오전 12:00
         */
        // @Scheduled(cron = "0 10 7 * * *")
        // public void syncElNoScrmSrm() {
        //         if (!"ap1".equals(apType)) {
        //                 syncChangeElInfo(); 
        //         }
        // }
        //
        //2023-10-19 add
        //@Operation(summary = "호출 테스트 ", description = "호출 test")
        //@ApiResponses({
        //                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
        //                @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
        //                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        //})
        //@PostMapping(value = "/join-manager")
        //public void joinManager() {
        //       ccService.joinManager();
        //}
}
