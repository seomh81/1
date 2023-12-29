package com.hdel.miri.concurrent.domain.auth;

import com.hdel.miri.concurrent.domain.auth.valid.OnRefreshToken;
import com.hdel.miri.concurrent.domain.user.vo.enums.Role;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import com.hdel.miri.concurrent.domain.auth.valid.OnSignIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    @EqualsAndHashCode(callSuper=false)
    public static final class SignIn extends AbstractRequest {
        @Schema(type = "String", example = "test@aa.bb", description = "사용자 아이디")
        @NotBlank(groups = {OnSignIn.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnSignIn.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "String", example = "1234", description = "사용자 패스워드")
        @NotBlank(groups = {OnSignIn.class}, message = "사용자 비밀번호 입력이 필요합니다.")
        private String password;
    }

    /**
     * Refresh Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class RefreshToken extends AbstractRequest {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
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
    @EqualsAndHashCode(callSuper=false)
    public static final class Token{
        private String userId;
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class My{
        private String userId;
        private String userName;
        private String avatar;
        private String phonenumber;
        private String locale;
        private String theme;
        private String landingpageType;
    }
    /**
     * Summary Auth Info.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class Summary {
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
    @EqualsAndHashCode(callSuper=false)
    public static final class Principal implements UserDetails {
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
}
