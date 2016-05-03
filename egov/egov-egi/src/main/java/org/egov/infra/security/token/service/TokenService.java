/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infra.security.token.service;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.token.entity.Token;
import org.egov.infra.security.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TokenService {
    private static final long DEFAULT_ALLOWED_TOKEN_AGE_SECS = 5 * 60;// Default
                                                                      // TTL for
                                                                      // Token
                                                                      // is 5
                                                                      // mins.
    private static final Logger LOGGER = Logger.getLogger(TokenService.class);
    private final TokenRepository tokenRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    public TokenService(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token findByTokenNumber(final String tokenNumber) {
        return tokenRepository.findByTokenNumber(tokenNumber);
    }

    public Token findByTokenNumberandService(final String tokenNumber, final String service) {
        return tokenRepository.findByTokenNumberAndService(tokenNumber, service);
    }

    /**
     * Generates a UUID token with 5 mins default TTL (Time To Live) and saves
     * to database.
     */
    @Transactional
    public Token generate() {
        final Token token = generate(DEFAULT_ALLOWED_TOKEN_AGE_SECS, null, null);
        return token;
    }

    /**
     * Generates a UUID token with given TTL (Time To Live) and saves to
     * database.
     */
    @Transactional
    public Token generate(final long ttlSec) {
        final Token token = createToken(ttlSec, null, null);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Generated token: " + token);
        return token;
    }

    @Transactional
    public Token generate(final long ttlSec, final String service) {
        return generate(ttlSec, null, service);
    }

    /**
     * Generates a UUID token with given token identity.
     */
    @Transactional
    public Token generate(final String tokenIdentity, final String service) {
        return generate(DEFAULT_ALLOWED_TOKEN_AGE_SECS, tokenIdentity, service);
    }

    /**
     * Generates a UUID token with given token identity and a time to to live.
     */
    @Transactional
    public Token generate(final long ttlSec, final String tokenIdentity, final String service) {
        return createToken(ttlSec, tokenIdentity, service);
    }

    /**
     * Create Token in database with given TTL in secs.
     */
    @Transactional
    private Token createToken(final long ttlSecs, final String tokenIdentity, final String service) {
        final String uUID = UUID.randomUUID().toString();
        final Token token = new Token();
        token.setTokenNumber(uUID);
        token.setTtlSecs(ttlSecs);
        token.setCreatedDate(new Date());
        token.setService(service);
        token.setTokenIdentity(tokenIdentity);
        return tokenRepository.save(token);
    }

    /**
     * Checks whether a token can be redeemed and then redeems it i.e. removes
     * it from the database. If the token does not exist or has expired, it
     * throws an exception.
     */
    @Transactional
    public void redeem(final String tokenNumber, final String service) {
        final Token token = tokenRepository.findByTokenNumberAndService(tokenNumber, service);
        if (token == null)
            throw new ApplicationRuntimeException("Token " + tokenNumber + " does not exist!");
        redeem(token);
    }

    /**
     * Checks whether a token can be redeemed and then redeems it i.e. removes
     * it from the database. If the token does not exist or has expired, it
     * throws an exception.
     */
    @Transactional
    public void redeem(final Token token) {
        checkIsRedeemable(token);
        tokenRepository.delete(token);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Redeemed token: " + token.getTokenNumber());
    }

    public Token checkIsRedeemable(final Token token) {
        final Date tokenDate = token.getCreatedDate();
        final long tokenTTL = token.getTtlSecs() * 1000;// for Token object ttl
                                                        // is in secs.
        final long now = new Date().getTime();
        if (now - tokenDate.getTime() > tokenTTL)
            throw new ApplicationRuntimeException("Token " + token.getTokenNumber() + " has expired!");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("checkIsRedeemable() for token " + token.getTokenNumber() + " passed, token created time was "
                    + tokenDate);
        return token;
    }

    /**
     * Checks whether a token can be redeemed but does not actually redeem it.
     * If the token does not exist or has expired, it throws an exception.
     */
    public Token checkIsRedeemable(final String tokenNumber, final String service) {
        final Token token = findByTokenNumberandService(tokenNumber, service);
        if (token == null)
            throw new ApplicationRuntimeException("Token " + tokenNumber + " for service " + service + " does not exist!");
        return checkIsRedeemable(token);
    }
}
