/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.security.token.service;

import org.egov.infra.security.token.entity.Token;
import org.egov.infra.security.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class TokenService {

    // Default TTL for Token is 5 mins.
    private static final long DEFAULT_ALLOWED_TOKEN_AGE_SECS = 5 * 60L;
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token getToken(String tokenNumber, String tokenIdentity, String service) {
        return tokenRepository.findByTokenNumberAndTokenIdentityAndService(tokenNumber, tokenIdentity, service);
    }

    @Transactional
    public Token generate(String tokenNumber, String tokenIdentity, String service) {
        return createToken(DEFAULT_ALLOWED_TOKEN_AGE_SECS, tokenNumber, tokenIdentity, service);
    }

    @Transactional
    public Token createToken(long ttlSecs, String tokenNumber, String tokenIdentity, String service) {
        Token token = new Token();
        token.setTokenNumber(tokenNumber);
        token.setTtlSecs(ttlSecs);
        token.setCreatedDate(new Date());
        token.setService(service);
        token.setTokenIdentity(tokenIdentity);
        return tokenRepository.save(token);
    }

    @Transactional
    public boolean redeemToken(String tokenNumber, String tokenIdentity, String service) {
        Token token = getToken(tokenNumber, tokenIdentity, service);
        if (token != null)
            tokenRepository.delete(token);
        return token != null && token.isRedeemable();
    }
}
