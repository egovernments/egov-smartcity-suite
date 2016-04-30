package org.egov.api.oauth2.provider;

import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenEnhancer extends TokenEnhancerChain{

	@Autowired
	CityService cityService;
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        SecureUser su = (SecureUser) authentication.getUserAuthentication()
				.getPrincipal();              
        Map<String, Object> info = new LinkedHashMap<String, Object>();        
        info.put("Id", su.getUserId());
        info.put("name", su.getUser().getName());
        info.put("mobileNumber", su.getUser().getMobileNumber());
        info.put("emailId", su.getUser().getEmailId());
        info.put("userType", su.getUser().getType());
        info.put("cityLat", (cityService.cityDataForKey("citylat")==null?0:cityService.cityDataForKey("citylat")));
        info.put("cityLng", (cityService.cityDataForKey("citylng")==null?0:cityService.cityDataForKey("citylng")));
        token.setAdditionalInformation(info);
        return super.enhance(token, authentication);
    }
}