package com.hdel.miri.concurrent.domain.message;

import com.hdel.miri.concurrent.domain.dgk.CcAsyncService;
import com.hdel.miri.concurrent.domain.dgk.CcRepository;
import com.hdel.miri.concurrent.domain.dgk.CcService;
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

import org.apache.commons.collections.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;
import com.hdel.miri.concurrent.domain.srm.SRMRepository;

@Tag(name = "공공데이타 포탈 API 연계", description = "공공데이타 포탈 연계 API")
@RestController
@RequestMapping("/v2/message/send")
@RequiredArgsConstructor
public class CcMessageController {

        private final CcMessageService  ccMessageService;
        private final CcService         ccService;
        private final CcRepository      ccRepository;
        private final CcAsyncService    ccAsyncService;
        private final SCRMRepository    scrmRepository;
        private final SRMRepository     srmRepository;

        @Operation(summary = "고장 - 발생/접수 메세지 전송", description = "고장 - 발생/접수 메세지 전송 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/fault-report")
        public ResponseEntity SendFaultReport(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendFaultReport(req);
        }

        @Operation(summary = "고장 - 배치완료 메세지 전송", description = "고장 - 배치완료 메세지 전송 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/fault-service-ready")
        public ResponseEntity SendFaultServiceReady(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendFaultServiceReady(req);
        }

        @Operation(summary = "고장 - 처리완료 메세지 전송", description = "고장 - 처리완료 메세지 전송 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/fault-complete")
        public ResponseEntity SendFaultComplete(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendFaultComplete(req);
        }

        @Operation(summary = "계약 - 미수금 알림", description = "계약 - 미수금 알림 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/misu")
        public ResponseEntity SendUnBilledInfo(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendUnBilledInfo(req);
        }

        @Operation(summary = "계약 - 계약만료 15일전", description = "계약 - 계약만료 15일전 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/lost-contract")
        public ResponseEntity SendContractExpirationInfo(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendContractExpirationInfo(req);
        }

        @Operation(summary = "공지사항 - 공지사항 전송", description = "공지사항 - 공지사항 전송 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/notice")
        public ResponseEntity SendNotice(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendNotice(req);
        }

        @Operation(summary = "정기점검 - 점검완료", description = "정기점검 - 점검완료 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
                @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
        })
        @PostMapping(value = "/selfinsp-complete")
        public ResponseEntity SendSelfInspComplete(
                @Parameter(description = "Schemas - CcMessageVO.CcSourceVO")
                @RequestBody
                CcMessageVO.CcSourceVO req
        ) {
                return ccMessageService.SendSelfInspComplete(req);
        }
}
