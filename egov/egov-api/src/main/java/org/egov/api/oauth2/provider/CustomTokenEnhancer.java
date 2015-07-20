package org.egov.api.oauth2.provider;

import java.util.LinkedHashMap;
import java.util.Map;

import org.egov.infra.config.security.authentication.SecureUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;

public class CustomTokenEnhancer extends TokenEnhancerChain{

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        SecureUser su = (SecureUser) authentication.getUserAuthentication()
				.getPrincipal();              
        Map<String, Object> info = new LinkedHashMap<String, Object>();
        info.put("userId", su.getUserId());
        info.put("userName", su.getUsername());
        token.setAdditionalInformation(info);
        return super.enhance(token, authentication);
    }
}