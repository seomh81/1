package com.hdel.miri.api.domain.auth;

import com.hdel.miri.api.domain.auth.valid.OnSignIn;
import com.hdel.miri.api.domain.auth.valid.OnRefreshToken;
import com.hdel.miri.api.domain.user.vo.enums.Role;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.domain.contract.Contract.ContractAPI;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AuthVO {


    /**
     * SignIn Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn extends AbstractRequest {
        @Schema(type = "String", example = "minhyuk.seo@hyundaielevator.com", description = "사용자 아이디")
        @NotNull(groups = {OnSignIn.class}, message = "사용자 아이디 입력이 필요합니다.")
        @NotEmpty(groups = {OnSignIn.class}, message = "사용자 아이디 입력이 필요합니다.")
        @NotBlank(groups = {OnSignIn.class}, message = "사용자 아이디 입력이 필요합니다.")
        private String userId;

        @Schema(type = "String", example = "111", description = "사용자 패스워드")
        @NotNull(groups = {OnSignIn.class}, message = "사용자 비밀번호 입력이 필요합니다.")
        @NotEmpty(groups = {OnSignIn.class}, message = "사용자 비밀번호 입력이 필요합니다.")
        @NotBlank(groups = {OnSignIn.class}, message = "사용자 비밀번호 입력이 필요합니다.")
        private String password;

        @Schema(type = "String", example = "lfjweofijewfwefkliowejfowe", description = "구글 파이어베이스 사용자 ID")
        @Hidden
        private String firebaseUserId;
    }

    /**
     * Refresh Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshToken extends AbstractRequest {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
        @NotNull(groups = {OnRefreshToken.class}, message = "사용자 아이디 입력이 필요합니다.")
        @NotEmpty(groups = {OnRefreshToken.class}, message = "사용자 아이디 입력이 필요합니다.")
        @NotBlank(groups = {OnRefreshToken.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnRefreshToken.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;
    }

    /**
     * Token Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token{
        @Schema(example = "test@test.co.kr")
        private String userId;
        @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdW5naG9vbi5zaGluQGJzZ2dsb2JhbC5jby5rciIsImlhdCI6MTY3ODk0MjQ5MiwiZXhwIjoxNjc4OTQ5NjkyfQ.ihKDLz9m_0UXZyW6sWg73CYSHb90w7ux4_OplIItKY12YZfvNHIghGNJ3cM3Y8zNZDsZtd5AstgEOCo7oMVtBw")
        private String accessToken;
        @Schema(example = "Not Provided")
        private String refreshToken;
        @Schema(example = "map_page")
        private String refRenderingType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class My{
        @Schema(example = "test@test.co.kr")
        private String userId;
        @Schema(example = "홍길동")
        private String userName;
        @Schema(example = "null")
        private String avatar;
        @Schema(example = "123-123")
        private String postnumber;
        @Schema(example = "서울시 강남구")
        private String address;
        @Schema(example = "서울시 강남구 홍길동 21")
        private String detailaddress;
        @Schema(example = "SYSTEM")
        private String roleType;
        @Schema(example = "01000000000")
        private String phonenumber;
        @Schema(example = "ko_kr")
        private String locale;
        @Schema(example = "light")
        private String theme;
        @Schema(example = "map_page")
        private String landingpageType;
        @Schema(example = "y")
        private String serviceUseAg;
        @Schema(example = "2023-03-08")
        private String serviceUseAgDt;
        @Schema(example = "y")
        private String plInfoUsingAg;
        @Schema(example = "2023-03-08")
        private String plInfoUsingAgDt;
        @Schema(example = "y")
        private String plInfoStoreTimeAg;
        @Schema(example = "2023-03-08")
        private String plInfoStoreTimeAgDt;
        @Schema(example = "y")
        private String adRecvAg;
        @Schema(example = "2023-03-08")
        private String adRecvAgDt;
        @Schema(example = "{}")
        private String widgets;
    }
    /**
     * Summary Auth Info.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private String userId;
        private String password;
        private String userName;
        private Role roleType;
    }
    /**
     * User Principal.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Principal implements UserDetails {
        private String userId;
        private String password;
        private String userName;
        private Collection<? extends GrantedAuthority> authorities;
        private Map<String, Object> attributes;
        public Principal(String userId, String userName, String password, List<GrantedAuthority> authorities) {
            this.userId = userId;
            this.userName = userName;
            this.password = password;
            this.authorities = authorities;
        }
        public static AuthVO.Principal create(Summary vo) {
            List<GrantedAuthority> authorities = Collections
                    .singletonList(new SimpleGrantedAuthority(vo.getRoleType().name()));
            return new AuthVO.Principal(
                    vo.getUserId(),
                    vo.getUserName(),
                    vo.getPassword(),
                    authorities);
        }
        public static AuthVO.Principal create(Summary user, Map<String, Object> attributes) {
            AuthVO.Principal userPrincipal = AuthVO.Principal.create(user);
            userPrincipal.setAttributes(attributes);
            return userPrincipal;
        }
        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }
        @Override
        public String getPassword() {
            return this.password;
        }
        @Override
        public String getUsername() {
            return this.userId;
        }
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserIdsVO extends AbstractRequest {
        @Schema(type = "List<checkValid>", description = "체크값 리스트")
        private List<checkValid> userIds;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class checkValid  {
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 아이디")
        private String userId;
        @Schema(type = "String", example = "Y", description = "유상계약번호")
        private String compsCntrNo;
        @Schema(type = "String", example = "Y", description = "유상계약번호")
        private String rowCnt;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidUserVO  {
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 아이디")
        private String userId;
        @Schema(type = "String", example = "Y", description = "이미 존재하면 N, 미존재시 Y")
        private String registYn;
        @Schema(type = "String", example = "1", description = "개별 구분용 넘버")
        private String rowCnt;
        @Schema(type = "String", example = "Y", description = "이미 존재하면 N, 미존재시 Y")
        private List<ContractAPI> contractList;
    }
}
