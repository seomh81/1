package com.hdel.miri.api.domain.user;

import com.hdel.miri.api.domain.alarm.AlarmSetup;
import com.hdel.miri.api.domain.alarm.AlarmSetupRepository;
import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.contract.ContractRepository;
import com.hdel.miri.api.domain.contract.Contract.ContractSearchEmployee;
import com.hdel.miri.api.domain.mail.MailService;
import com.hdel.miri.api.domain.mail.MailVO;
import com.hdel.miri.api.domain.masterdata.MasterData;
import com.hdel.miri.api.domain.masterdata.MasterDataRepository;
import com.hdel.miri.api.domain.masterdata.MasterDataService;
import com.hdel.miri.api.domain.mms.mmsService;
import com.hdel.miri.api.domain.mms.mmsVO;
import com.hdel.miri.api.domain.portfolio.Portfolio;
import com.hdel.miri.api.domain.portfolio.PortfolioRepository;
import com.hdel.miri.api.domain.scrm.SCRMService;
import com.hdel.miri.api.domain.setup.Setup;
import com.hdel.miri.api.domain.setup.SetupRepository;
import com.hdel.miri.api.domain.srm.SRMService;
import com.hdel.miri.api.domain.terms.Terms;
import com.hdel.miri.api.domain.terms.TermsRepository;
import com.hdel.miri.api.global.config.OpenURIConfig;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import com.ibm.db2.cmx.runtime.internal.repository.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import org.apache.commons.collections.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ServletContext context;
    private final OpenURIConfig openURIConfig;
    private final MailService mailService;
    private final mmsService mmsService;
    private final Message message;
    private final PasswordEncoder passwordEncoder;
    private final ResponseTemplate responseTemplate;
    private final SRMService srmService;
    private final SCRMService scrmService;
    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final SetupRepository setupRepository;
    private final PortfolioRepository portfolioRepository;
    private final ContractRepository contractRepository;
    private final MasterDataRepository masterDataRepository;
    private final AlarmSetupRepository alarmSetupRepository;
    private final CCRepository  ccRepository;

    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true; 
        }
        return err;
    }

    /**
     * Already
     * */
    public ResponseEntity isAlready(User.UserAlready request){
        try {

            if(!isValidEmail(request.getUserId())) {
                return responseTemplate.withInValidEmailForamt(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data(Boolean.FALSE)
                    .because("이메일 형식을 확인하십시오.")
                    .build());
            }

            if(0 < userRepository.selectCountByUserId(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .because(message.becauseAlreadyRegisted(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.FALSE)
                    .because(message.becauseAvailableAccount(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }
    /**
     * Manage - Already
     * */
    public ResponseEntity isManageAlready(User.MultipleUserAlreadyExt request){
        try {
            List<User.UserAlreadyExt> list = request.getList();
            list.forEach(userAlreadyExt -> {
                userAlreadyExt.setResult(0 < userRepository.selectCountByUserIdExt(userAlreadyExt));
            });
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(list)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    /**
     * Find Account.
     * */
    public ResponseEntity findAccount(User.FindAccount request){
        try {
            final String userId = userRepository.selectUserByPhoneAndUsername(request);
            String tempEmailAddr = "";
            String msgEmailAddr = "";
            if(userId != null && !userId.equals("")){
                ArrayList<String> toAddr = new ArrayList<>(Arrays.asList(userId.split(",")));
                mailService.send(MailVO.Request.builder()
                        .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                        .toAddress(toAddr)
                        .subject("[현대엘리베이터] 아이디 찾기 메일 입니다.")
                        .content("귀하의 사용자 아이디는 " + userId + "입니다.")
                        .build());

                for(int i = 0; i < toAddr.size(); i++)
                {
                    tempEmailAddr = toAddr.get(i);
                    msgEmailAddr += (i != 0 ? "," : "") + "*" + tempEmailAddr.substring(tempEmailAddr.indexOf("@"));
                }

                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSendMail(request.getUserLocale()).replace("{0}", msgEmailAddr)).build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("아이디를 찾지 못했습니다.").build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }


    }

    /**
     * Find Password
     * */
    public ResponseEntity findPassword(User.FindPassword request) {
        try {
            String userId = userRepository.selectUserByPhoneAndUserId(User.FindPassword.builder()
                    .userId(request.getUserId())
                    .phonenumber(request.getPhonenumber())
                    .build());
            // String originPasswrod = Base64.encode(UUID.randomUUID().toString().getBytes());
            String originPasswrod = GetRandomPassword();
    
            final String tempPassword = passwordEncoder.encode(originPasswrod);
            if(0 < userRepository.updatePasswordForAnonymous(User.FindPassword.builder()
                    .newPassword(tempPassword)
                    .userId(request.getUserId())
                    .phonenumber(request.getPhonenumber())
                    .build())){

                mailService.send(MailVO.Request.builder()
                        .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                        .toAddress(Arrays.asList(userId))
                        .subject("[현대엘리베이터] 패스워드 초기화 메일")
                        .content("귀하의 패스워드가 " + originPasswrod + "로 변경되었습니다")
                        .build());

                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue())
                        .data(message.withSendMail(request.getUserLocale()).replace("{0}", "*"+userId.substring(userId.indexOf("@")))).because("가입하신 이메일을 확인해 주세요!").build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).data("계정을 찾을 수 없습니다.").because(message.becauseNotUpdate(request.getUserLocale())).build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    /**
     * Change Password
     * */
    public ResponseEntity changePassword(User.ChangePassword request){
        request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        try {
            if(0 < userRepository.updatePassword(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotUpdate(request.getUserLocale())).build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    /**
     * Reset Password
     * */
    public ResponseEntity resetPassword(User.RestPassword request) {
        try {
            // String originPasswrod = Base64.encode(UUID.randomUUID().toString().getBytes());
            String originPasswrod = GetRandomPassword();
            

            final String tempPassword = passwordEncoder.encode(originPasswrod);
            if(0 < userRepository.updatePassword(User.ChangePassword.builder()
                    .userId(request.getUserId())
                    .newPassword(tempPassword)
                    .build())){

                mailService.send(MailVO.Request.builder()
                        .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                        .toAddress(Arrays.asList(request.getUserId()))
                        .subject("[현대엘리베이터] 패스워드 초기화 메일")
                        .content("귀하의 패스워드가 " + originPasswrod + " 로 변경되었습니다")
                        .build());

                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue())
                        .data(message.withSendMail(request.getUserLocale()).replace("{0}", "*"+request.getUserId().substring(request.getUserId().indexOf("@")))).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotUpdate(request.getUserLocale())).build(),request.getUserLocale());
        } catch ( Exception e) {
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }


    /**
     * User List
     * */
    public ResponseEntity getUserList(User.UserSearch request){
        try {
            return responseTemplate.withSuccess(
                    ResponseBody.builder().result(
                                    ResultCode.SUCCESS.getValue())
                            .data(userRepository.selectList(request))
                            .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

     /**
     * User List
     * */
    public ResponseEntity getNonContractUserList(User.UserSearch request){
        try {
            return responseTemplate.withSuccess(
                    ResponseBody.builder().result(
                                    ResultCode.SUCCESS.getValue())
                            .data(userRepository.selectNonContractList())
                            .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    /**
     * 사용자 정보 수정
     * */
    @Transactional("db2TxManager")
    public ResponseEntity update(User.UserUpdate request){
        try {
            if(0 < userRepository.update(request)){
                termsRepository.updateAdRecvAg(Terms.TermsUpdate.builder()
                                .userId(request.getCurrentUser())
                                .plInfoStoreTimeAg(request.getPlInfoStoreTimeAg())
                                .adRecvAg(request.getAdRecvAg())
                        .build());
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    /**
     * 사용자 정보 수정
     * */
    @Transactional("db2TxManager")
    public ResponseEntity updateAdminPage(User.UserUpdateAdminPage request){
        try {
            if(0 < userRepository.updateAdminPage(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    @Transactional("db2TxManager")
    public ResponseEntity updateAccountStatus(User.UserAccountStatus request){
        try {
            if(0 < userRepository.updateAccountStatus(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }
    @Transactional("db2TxManager")
    public ResponseEntity updateAccountStatus(User.UserAccountStatusRequest request){
        try {
            request.getTargetList().forEach(userAccountStatus -> {
                userRepository.updateAccountStatus(userAccountStatus);
            });
            return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
        }catch (Exception e){

            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    @Transactional("db2TxManager")
    public ResponseEntity updateAccept(String key,String user){
        try {
            if(0 < userRepository.updateRecvAcceptKey(User.UpdateAcceptKey.builder()
                            .userId(user)
                            .recvAcceptKey(key)
                    .build())){
                userRepository.updateInnerAccountStatus(User.UserAccountStatus.builder()
                                .userId(user)
                                .accountStatus("0010")
                        .build());
                
                User.userAcceptMailInfo acceptInfo = userRepository.selectInfoForAcceptUser(User.UserAccountStatus.builder().userId(user).build());

                if(acceptInfo.getCreationFg().equals("N"))
                {
                    String _msgTemplate = "";
                    List<String> mailToAddress;
                    if(acceptInfo.getSalesUserId() == null || acceptInfo.getSalesUserId() == "")
                    {
                        _msgTemplate = ccRepository.GetMsgTemplateContents("9995");

                        _msgTemplate = _msgTemplate.replace("#{고객명}", acceptInfo.getUserName());
                        _msgTemplate = _msgTemplate.replace("#{고객ID}", user);
                        _msgTemplate = _msgTemplate.replace("#{유상계약번호}", acceptInfo.getCompsCntrNo() != null ? ","+acceptInfo.getCompsCntrNo() : "");

                        mailToAddress = userRepository.selectSystemUser();
                    }
                    else
                    {
                        _msgTemplate = ccRepository.GetMsgTemplateContents("9996");

                        _msgTemplate = _msgTemplate.replace("#{사원명}", acceptInfo.getSalesUserName());
                        _msgTemplate = _msgTemplate.replace("#{고객명}", acceptInfo.getUserName());

                        mailToAddress = Arrays.asList(acceptInfo.getSalesUserId());
                    }

                    mailService.send(MailVO.Request.builder()
                            .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                            .toAddress(mailToAddress)
                            .subject("[현대엘리베이터] 고객 승인 안내 메일")
                            .content(_msgTemplate)
                            .build(),   true);
                }
                
                return ResponseEntity.ok(message.withVerifiedEmailSuccess("ko_kr"));
            }else return ResponseEntity.ok(message.withVerifiedEmailFailure("ko_kr"));
        }catch (Exception e){
            throw new RuntimeException(message.withVerifiedEmailFailure("ko_kr"));
        }
    }

    @Transactional(value = "db2TxManager")
    public ResponseEntity remove(User.UserRemove request) {
        try {
            if(0 < userRepository.remove(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }
    @Transactional(value = "db2TxManager")
    public ResponseEntity recovery(User.UserRecovery request) {
        try {
            if(0 < userRepository.recovery(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }
    @Transactional(value = "db2TxManager")
    public ResponseEntity mailResend(User.UserRecovery request) {
        try {
            List<User.mailReSendByUser> reSendUserList = userRepository.selectAcceptKeyForReSend(request);

            String origenPassWord = null;
            String tempPassWord   = null;
            String _msgTemplate   = null;

            for(int i = 0; i < reSendUserList.size(); i++)
            {
                origenPassWord = GetRandomPassword();
                tempPassWord   = passwordEncoder.encode(origenPassWord);

                if(0 < userRepository.updatePassword(User.ChangePassword.builder()
                            .userId(reSendUserList.get(i).getUserId())
                            .newPassword(tempPassWord).build()))
                {   //가입 안내 메일 전송
                    _msgTemplate = ccRepository.GetMsgTemplateContents("9999");
                    _msgTemplate = _msgTemplate.replace("#{고객명}", reSendUserList.get(i).getUserName());
                    _msgTemplate = _msgTemplate.replace("#{비밀번호}", "귀하의 임시 비밀번호는 " + origenPassWord + " 입니다.");
                    _msgTemplate = _msgTemplate.replace("#{링크}", openURIConfig.BASE_URI.concat(context.getContextPath()).concat("/v2/user/confirm-accept?key=").concat(reSendUserList.get(i).getSendAcceptKey()).concat("&user=").concat(reSendUserList.get(i).getUserId()));

                    mailService.send(MailVO.Request.builder()
                            .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                            .toAddress(Arrays.asList(reSendUserList.get(i).getUserId()))
                            .subject("[현대엘리베이터] 가입 안내 메일")
                            .content(_msgTemplate)
                            .build(), true);
    
                    //가입 안내 문자 전송
                    _msgTemplate = ccRepository.GetMsgTemplateContents("9998");
                    _msgTemplate = _msgTemplate.replace("#{고객명}", reSendUserList.get(i).getUserName());
                    _msgTemplate = _msgTemplate.replace("#{이메일}", reSendUserList.get(i).getUserId());

                    mmsService.sendMMS(mmsVO.request.builder()
                                        .receiveNo(reSendUserList.get(i).getPhoneNumber())
                                        .title("[현대엘리베이터] 고객포탈 가입 안내")
                                        .contents(_msgTemplate).build());
                }
            }
                
            return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.withFailureSendMail(request.getUserLocale()));
        }
    }

    /**
     * 랜덤 패스워드 생성
     */
    private String GetRandomPassword() {
        // String charStr = RandomStringUtils.randomAlphanumeric(7);
        // String specialStr = RandomStringUtils.randomAscii(1);

        // return charStr+specialStr;
            // alphaNum에 특수문자를 추가하여 커스텀이 가능하다.
        String alphaStr = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz023456789@#$%^&*()";
        String specialStr = "@#$%^&*()";
        String numStr = "023456789";
        int alphaLength = alphaStr.length();
        int specialLength = specialStr.length();
        int numLength = numStr.length();

        Random random = new Random();

        StringBuffer code = new StringBuffer();
        for (int i = 0; i < 7; i++) {
            code.append(alphaStr.charAt(random.nextInt(alphaLength)));
        }

        code.append(specialStr.charAt(random.nextInt(specialLength)));
        code.append(numStr.charAt(random.nextInt(numLength)));

        return code.toString();

    }


    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinSystemAdmin(User.DefaultOtherUserCreate request){
        // request.setOriginPassword(Base64.encode(UUID.randomUUID().toString().getBytes()));
        request.setOriginPassword(GetRandomPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        request.setRoleType("SYSTEM");

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            /** 사용자 정보 생성*/
            if(0 < userRepository.insertTypeSystemAdmin(request)){
                otherJoin(request);
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }
    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinUser(User.DefaultOtherUserCreate request){
        // request.setOriginPassword(Base64.encode(UUID.randomUUID().toString().getBytes()));
        request.setOriginPassword(GetRandomPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        request.setRoleType("USER");

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            /** 사용자 정보 생성*/
            if(0 < userRepository.insertTypeUser(request)){
                otherJoin(request);
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinManager(User.DefaultUserCreate request){
        request.setOriginPassword(request.getPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        request.setRoleType("MANAGER");

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            /** 사용자 정보 생성*/
            if(0 < userRepository.insertTypeManager(request)){
                join(request);
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinMajor(User.DefaultOtherUserCreate request){
        request.setOriginPassword(GetRandomPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        if(request.getNote().equals("manager"))
        {
            request.setRoleType("MANAGER");
        }
        else
        {
            request.setRoleType("MAJOR");
        }

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            /** 사용자 정보 생성*/
            if(0 < userRepository.insertTypeMajor(request)){
                otherJoin(request);
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinEngineer(User.DefaultOtherUserCreate request){
        // request.setOriginPassword(Base64.encode(UUID.randomUUID().toString().getBytes()));
        request.setOriginPassword(GetRandomPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        request.setRoleType("ENGINEER");

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            /** 사용자 정보 생성*/
            if(0 < userRepository.insertTypeEngineer(request)){
                otherJoin(request);
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinAccount(User.DefaultOtherUserCreate request){
        // request.setOriginPassword(Base64.encode(UUID.randomUUID().toString().getBytes()));
        request.setOriginPassword(GetRandomPassword());
        request.setPassword(passwordEncoder.encode(request.getOriginPassword()));
        request.setRoleType("ACCOUNT");

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", ""));

        try {
            if(0 < userRepository.insertTypeAccount(request)){
                otherJoin(request);
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale())).build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));

        }
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinMany(User.DefaultOtherUserCreateList request){
        int successCnt = 0;
        int failCnt    = 0;

        User.returnResultMany resultData = new User.returnResultMany();
        
        for(User.DefaultOtherUserCreate insertUser : request.getCreateUserList())
        {
            insertUser.setOriginPassword(GetRandomPassword());
            insertUser.setPassword(passwordEncoder.encode(insertUser.getOriginPassword()));
            if(insertUser.getNote().equals("manager"))
            {
                insertUser.setRoleType("MANAGER");
            }
            else
            {
                insertUser.setRoleType("MAJOR");
            }

            insertUser.setPhonenumber(insertUser.getPhonenumber().replaceAll("-", ""));

            try{
                if(0 < userRepository.insertTypeMajor(insertUser))
                {
                    otherJoin(insertUser);
                    successCnt++;
                }
                else
                {
                    failCnt++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                failCnt++;
            }
        }

        resultData.setSuccessCnt(Integer.toString(successCnt));
        resultData.setFailCnt(Integer.toString(failCnt));

        return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultData).build(),request.getUserLocale());
    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity joinAutoManager(User.DefaultOtherUserCreateAuto request){
        User.returnResultAuto resultData = new User.returnResultAuto();

        if(request.getUserId().equals("") || request.getUserId() == "")
        {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("필수값 : id").build(),request.getUserLocale());
        }

        if(!isValidEmail(request.getUserId())) 
        {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("이메일 형식").build(),request.getUserLocale());
        }

        if(userRepository.selectCountByUserId(User.UserAlready.builder().userId(request.getUserId()).build()) > 0)
        {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("id 중복").build(),request.getUserLocale());
        }

        request.setPhonenumber(request.getPhonenumber().replaceAll("-", "").trim());

        if(request.getPhonenumber().equals("") || request.getPhonenumber() == "")
        {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("필수값 : 연락처").build(),request.getUserLocale());
        }

        if(request.getPhonenumber().length() != 11 || !request.getPhonenumber().substring(0, 3).equals("010"))
        {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("연락처 유효성").build(),request.getUserLocale());
        }
        User.DefaultOtherUserCreate insertData = new User.DefaultOtherUserCreate();
        insertData.setUserId(request.getUserId());
        insertData.setUserName(request.getUserName().trim());
        insertData.setPhonenumber(request.getPhonenumber());

        insertData.setOriginPassword(GetRandomPassword());
        insertData.setPassword(passwordEncoder.encode(insertData.getOriginPassword()));
        insertData.setRoleType("MANAGER");
        insertData.setNote("auto");
        try{
            if(0 < userRepository.insertTypeAuto(insertData))
            {
                otherJoin(insertData);
                
               return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data("등록 완료").build(),request.getUserLocale());
            }
            else
            {
                return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("등록 오류").build(),request.getUserLocale());
            }
        } catch (Exception e) {
           return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data("등록 오류").build(),request.getUserLocale());
        }
    }

    public void join(User.DefaultUserCreate request) throws Exception{
        String passRandom = request.getNote();
        /** 사용자 이메일 인증 키 발송 */
        String sendAcceptKey = Base64.encode(UUID.randomUUID().toString().getBytes()).concat(Base64.encode(UUID.randomUUID().toString().getBytes()));
        userRepository.updateSendAcceptKey(User.UpdateAcceptKey.builder()
                        .userId(request.getUserId())
                        .sendAcceptKey(sendAcceptKey)
                .build());

        /** 서비스 약관 동의 생성 */
        termsRepository.insertDefault(Terms.TermsDefaultCreate.builder()
                .userId(request.getUserId())
                .serviceUseAg(request.getTermsServiceUseAg())
                .plInfoUsingAg(request.getTermsPlInfoUsingAg())
                .plInfoStoreTimeAg(request.getTermsPlInfoStoreTimeAg())
                .adRecvAg(request.getTermsAdRecvAg()).build());
        /** 기본 사용자 설정 생성 */
        setupRepository.insertDefault(Setup.SetupDefaultCreate.builder().userId(request.getUserId()).build());

        /** 기본 포트폴리오 생성 */
        Portfolio.PortfolioDefaultCreate portfolio = Portfolio.PortfolioDefaultCreate.builder()
                                                        .userId(request.getUserId())
                                                        .defaultYn("y")
                                                        .portfolioName("lobby").build();
        portfolioRepository.insertDefault(portfolio);

        /** 포트폴리오 X 계약 정보 생성 */
        List<Contract.ContractAPI> contractList = request.getLobby();
        int rst = contractRepository.insertDefault(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), contractList);

        /** 알람 기본 정보 생성 */
        // List<MasterData.AlarmCode> alarmCodes = masterDataRepository.selectAlarmSetup();
        alarmSetupRepository.insertDefault(AlarmSetup.AlarmDefaultCreate.builder()
                .userId(request.getUserId())
                // .codes(alarmCodes)
                .build());

        // USER-ROLE Mapping 등록
        userRepository.insertUserRoleMapping2(request);

        // 메일 Format을 가져온다 .
        String _msgTemplate = ccRepository.GetMsgTemplateContents("9999");
        _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());

        if(passRandom.equals("Random")){ //!"MANAGER".equals(request.getRoleType())) {
            _msgTemplate = _msgTemplate.replace("#{비밀번호}", "귀하의 임시 비밀번호는 " + request.getOriginPassword() + " 입니다.");
        } else {
            _msgTemplate = _msgTemplate.replace("#{비밀번호}", "");
        }

        _msgTemplate = _msgTemplate.replace("#{링크}", openURIConfig.BASE_URI.concat(context.getContextPath()).concat("/v2/user/confirm-accept?key=").concat(sendAcceptKey).concat("&user=").concat(request.getUserId()));

        mailService.send(MailVO.Request.builder()
                .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                .toAddress(Arrays.asList(request.getUserId()))
                .subject("[현대엘리베이터] 가입 안내 메일")
                .content(_msgTemplate)
                .build(), true);
        //가입 안내 문자 전송
        _msgTemplate = ccRepository.GetMsgTemplateContents("9998");
        _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());
        _msgTemplate = _msgTemplate.replace("#{이메일}", request.getUserId());

        mmsService.sendMMS(mmsVO.request.builder()
                            .receiveNo(request.getPhonenumber())
                            .title("[현대엘리베이터] 고객포탈 가입 안내")
                            .contents(_msgTemplate).build());
    }

    public void otherJoin(User.DefaultOtherUserCreate request) throws Exception{
        /** 사용자 이메일 인증 키 발송 */
        String sendAcceptKey = Base64.encode(UUID.randomUUID().toString().getBytes()).concat(Base64.encode(UUID.randomUUID().toString().getBytes()));
        userRepository.updateSendAcceptKey(User.UpdateAcceptKey.builder()
                .userId(request.getUserId())
                .sendAcceptKey(sendAcceptKey)
                .build());

        /** 서비스 약관 동의 생성 */
        termsRepository.insertDefault(Terms.TermsDefaultCreate.builder()
                .userId(request.getUserId())
                .serviceUseAg(request.getTermsServiceUseAg())
                .plInfoUsingAg(request.getTermsPlInfoUsingAg())
                .plInfoStoreTimeAg(request.getTermsPlInfoStoreTimeAg())
                .adRecvAg(request.getTermsAdRecvAg()).build());
        /** 기본 사용자 설정 생성 */
        setupRepository.insertDefault(Setup.SetupDefaultCreate.builder().userId(request.getUserId()).build());
        /** 기본 포트폴리오 생성 */
        String strYn = "y";
        //등록 사용자가 영업사원일때 lobby 포트폴리오가 디폴트가 되지 않도록
        if(request.getRoleType().equals("ACCOUNT") || request.getRoleType().equals("ENGINEER"))
        {
            strYn = "n";
        }
        
        Portfolio.PortfolioDefaultCreate portfolio = Portfolio.PortfolioDefaultCreate.builder().userId(request.getUserId()).defaultYn(strYn).portfolioName("lobby").build();
        portfolioRepository.insertDefault(portfolio);

        List<Contract.ContractAPI> contractList = new ArrayList();
        // 대량 등록시에는 iUserId 항목에 null이 아니게 된다. iUserId가 null아 아니면 해당 email주소로 lobby를 가져와서 rquest에 복사해서 넣는다.
            int rst = 0;

            switch(request.getRoleType()) {
                case "USER":
                    if(request.getIUserId() != null && request.getIUserId() != "") {
                        rst = contractRepository.insertUserFromManager(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), request);     
                    } else {
                        contractList = request.getLobby();
                        rst = contractRepository.insertDefault(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), contractList);
                    }
                    break;
                case "ACCOUNT":
                    if(request.getEmpId() != null && request.getEmpId() != "") {
                        rst =contractRepository.insertAccountFromSystem(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), request);
                    } else {
                        contractList = request.getLobby();
                        rst = contractRepository.insertDefault(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), contractList);
                    }
                    
                    //영업사원이 포트폴리오 lobby 사용을 막기위해 대체 포트폴리오 default의 생성
                    portfolio = Portfolio.PortfolioDefaultCreate.builder().userId(request.getUserId()).defaultYn("y").portfolioName("default").build();
                    portfolioRepository.insertDefault(portfolio);
                    //대체 포트폴리오 default에 계약 한건 맵핑
                    contractRepository.insertAccountFromLobby(portfolio.getUserPortfolioMappingId(), portfolio.getUserId(), request);
                    break;
                case "ENGINEER":
                    if(request.getEmpId() != null && request.getEmpId() != "") {
                        Contract.ContractSearchEmployee reqVO = new Contract.ContractSearchEmployee();
                        reqVO.setRoleType("ENGINEER");
                        reqVO.setEmpId(request.getEmpId());

                        List<Contract.ContractEmployeeAPI> _lobby = ListUtils.union(srmService.employeeContractSearch(reqVO),scrmService.employeeContractSearch(reqVO));
                        if(_lobby != null && !_lobby.isEmpty()) {
                            rst = contractRepository.insertEnginerrFromSystem(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), _lobby);    
                        }
                    } else {
                        contractList = request.getLobby();
                        rst = contractRepository.insertDefault(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), contractList);
                    }
                    
                    //영업사원과 동일
                    portfolio = Portfolio.PortfolioDefaultCreate.builder().userId(request.getUserId()).defaultYn("y").portfolioName("default").build();
                    portfolioRepository.insertDefault(portfolio);
                    //대체 포트폴리오 default에 계약 한건 맵핑
                    contractRepository.insertAccountFromLobby(portfolio.getUserPortfolioMappingId(), portfolio.getUserId(), request);
                    break;
                default:
                    if(!request.getNote().equals("auto"))
                    {
                        contractList = request.getLobby();
                        rst = contractRepository.insertDefault(portfolio.getUserPortfolioMappingId(), request.getCurrentUser(), contractList);
                    }
            }
        /** 알람 기본 정보 생성 */
        // List<MasterData.AlarmCode> alarmCodes = masterDataRepository.selectAlarmSetup();
        alarmSetupRepository.insertDefault(AlarmSetup.AlarmDefaultCreate.builder()
                .userId(request.getUserId())
                // .codes(alarmCodes)
                .build());

        // USER-ROLE Mapping 등록
        userRepository.insertUserRoleMapping(request);

        // 메일 Format을 가져온다
        
        System.out.println(openURIConfig.BASE_URI.concat(context.getContextPath()));
        String _msgTemplate = ccRepository.GetMsgTemplateContents("9999");
        _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());
        
        _msgTemplate = _msgTemplate.replace("#{비밀번호}", "귀하의 임시 비밀번호는 " + request.getOriginPassword() + " 입니다.");
        
        _msgTemplate = _msgTemplate.replace("#{링크}", openURIConfig.BASE_URI.concat(context.getContextPath()).concat("/v2/user/confirm-accept?key=").concat(sendAcceptKey).concat("&user=").concat(request.getUserId()));

        mailService.send(MailVO.Request.builder()
                .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                .toAddress(Arrays.asList(request.getUserId()))
                .subject("[현대엘리베이터] 가입 안내 메일")
                .content(_msgTemplate)
                .build(), true);

        //가입 안내 문자 전송
        _msgTemplate = ccRepository.GetMsgTemplateContents("9998");
        _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());
        _msgTemplate = _msgTemplate.replace("#{이메일}", request.getUserId());

        mmsService.sendMMS(mmsVO.request.builder()
                            .receiveNo(request.getPhonenumber())
                            .title("[현대엘리베이터] 고객포탈 가입 안내")
                            .contents(_msgTemplate).build());
    }

    /**
     * Current User View Rendering Info
     * */
    public ResponseEntity currentViewRendering(User.UserRenderingRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(userRepository.selectCurrentUserPage(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    @Transactional(value = "db2TxManager", propagation = Propagation.NESTED)
    public ResponseEntity mappingMany(User.setNonContractUserList request){
        int successCnt = 0;
        int failCnt    = 0;

        User.returnResultMany resultData = new User.returnResultMany();
        
        for(User.setNonContractUser setUser : request.getSetUserList())
        {
            try{
                userRepository.insertPortfolioNonContract(setUser);
                successCnt++;
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                failCnt++;
            }
        }

        resultData.setSuccessCnt(Integer.toString(successCnt));
        resultData.setFailCnt(Integer.toString(failCnt));

        return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultData).build(),request.getUserLocale());
    }

}
