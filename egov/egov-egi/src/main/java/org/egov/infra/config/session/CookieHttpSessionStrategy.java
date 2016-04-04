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
package org.egov.infra.config.session;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.CookieSerializer.CookieValue;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionManager;
import org.springframework.session.web.http.MultiHttpSessionStrategy;
import org.springframework.util.Assert;

/**
 * This is a copy of {@link org.springframework.session.web.http.CookieHttpSessionStrategy} from spring security This is a patch
 * since {@link org.springframework.session.web.http.CookieHttpSessionStrategy} Cookie Path setting is handled improperly by
 * spring-session. This class should be removed once spring-session released with proper patch, hopefully in version 1.1
 */
public final class CookieHttpSessionStrategy implements MultiHttpSessionStrategy, HttpSessionManager {
    private static final String SESSION_IDS_WRITTEN_ATTR = CookieHttpSessionStrategy.class.getName()
            .concat(".SESSIONS_WRITTEN_ATTR");

    static final String DEFAULT_ALIAS = "0";

    static final String DEFAULT_SESSION_ALIAS_PARAM_NAME = "_s";

    private final Pattern ALIAS_PATTERN = Pattern.compile("^[\\w-]{1,50}$");

    private String sessionParam = DEFAULT_SESSION_ALIAS_PARAM_NAME;

    private CookieSerializer cookieSerializer = new DefaultCookieSerializer();

    @Override
    public String getRequestedSessionId(final HttpServletRequest request) {
        final Map<String, String> sessionIds = getSessionIds(request);
        final String sessionAlias = getCurrentSessionAlias(request);
        return sessionIds.get(sessionAlias);
    }

    @Override
    public String getCurrentSessionAlias(final HttpServletRequest request) {
        if (sessionParam == null)
            return DEFAULT_ALIAS;
        final String u = request.getParameter(sessionParam);
        if (u == null)
            return DEFAULT_ALIAS;
        if (!ALIAS_PATTERN.matcher(u).matches())
            return DEFAULT_ALIAS;
        return u;
    }

    @Override
    public String getNewSessionAlias(final HttpServletRequest request) {
        final Set<String> sessionAliases = getSessionIds(request).keySet();
        if (sessionAliases.isEmpty())
            return DEFAULT_ALIAS;
        long lastAlias = Long.decode(DEFAULT_ALIAS);
        for (final String alias : sessionAliases) {
            final long selectedAlias = safeParse(alias);
            if (selectedAlias > lastAlias)
                lastAlias = selectedAlias;
        }
        return Long.toHexString(lastAlias + 1);
    }

    private long safeParse(final String hex) {
        try {
            return Long.decode("0x" + hex);
        } catch (final NumberFormatException notNumber) {
            return 0;
        }
    }

    @Override
    public void onNewSession(final Session session, final HttpServletRequest request, final HttpServletResponse response) {
        final Set<String> sessionIdsWritten = getSessionIdsWritten(request);
        if (sessionIdsWritten.contains(session.getId()))
            return;
        sessionIdsWritten.add(session.getId());

        final Map<String, String> sessionIds = getSessionIds(request);
        final String sessionAlias = getCurrentSessionAlias(request);
        sessionIds.put(sessionAlias, session.getId());

        final String cookieValue = createSessionCookieValue(sessionIds);
        cookieSerializer.writeCookieValue(new CookieValue(request, response, cookieValue));
    }

    @SuppressWarnings("unchecked")
    private Set<String> getSessionIdsWritten(final HttpServletRequest request) {
        Set<String> sessionsWritten = (Set<String>) request.getAttribute(SESSION_IDS_WRITTEN_ATTR);
        if (sessionsWritten == null) {
            sessionsWritten = new HashSet<String>();
            request.setAttribute(SESSION_IDS_WRITTEN_ATTR, sessionsWritten);
        }
        return sessionsWritten;
    }

    private String createSessionCookieValue(final Map<String, String> sessionIds) {
        if (sessionIds.isEmpty())
            return "";
        if (sessionIds.size() == 1 && sessionIds.keySet().contains(DEFAULT_ALIAS))
            return sessionIds.values().iterator().next();

        final StringBuffer buffer = new StringBuffer();
        for (final Map.Entry<String, String> entry : sessionIds.entrySet()) {
            final String alias = entry.getKey();
            final String id = entry.getValue();

            buffer.append(alias);
            buffer.append(" ");
            buffer.append(id);
            buffer.append(" ");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    @Override
    public void onInvalidateSession(final HttpServletRequest request, final HttpServletResponse response) {
        final Map<String, String> sessionIds = getSessionIds(request);
        final String requestedAlias = getCurrentSessionAlias(request);
        sessionIds.remove(requestedAlias);

        final String cookieValue = createSessionCookieValue(sessionIds);
        cookieSerializer.writeCookieValue(new CookieValue(request, response, cookieValue));
    }

    /**
     * Sets the name of the HTTP parameter that is used to specify the session alias. If the value is null, then only a single
     * session is supported per browser.
     *
     * @param sessionAliasParamName the name of the HTTP parameter used to specify the session alias. If null, then ony a single
     * session is supported per browser.
     */
    public void setSessionAliasParamName(final String sessionAliasParamName) {
        sessionParam = sessionAliasParamName;
    }

    /**
     * Sets the {@link CookieSerializer} to be used.
     *
     * @param cookieSerializer the cookieSerializer to set. Cannot be null.
     */
    public void setCookieSerializer(final CookieSerializer cookieSerializer) {
        Assert.notNull(cookieSerializer, "cookieSerializer cannot be null");
        this.cookieSerializer = cookieSerializer;
    }

    /**
     * Sets the name of the cookie to be used
     * @param cookieName the name of the cookie to be used
     * @deprecated use {@link #setCookieSerializer(CookieSerializer)}
     */
    @Deprecated
    public void setCookieName(final String cookieName) {
        final DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(cookieName);
        cookieSerializer = serializer;
    }

    @Override
    public Map<String, String> getSessionIds(final HttpServletRequest request) {
        final List<String> cookieValues = cookieSerializer.readCookieValues(request);
        final String sessionCookieValue = StringUtils.defaultIfBlank(cookieValues.isEmpty() ? "" : cookieValues.iterator().next(),
                "");
        final Map<String, String> result = new LinkedHashMap<String, String>();
        final StringTokenizer tokens = new StringTokenizer(sessionCookieValue, " ");
        if (tokens.countTokens() == 1) {
            result.put(DEFAULT_ALIAS, tokens.nextToken());
            return result;
        }
        while (tokens.hasMoreTokens()) {
            final String alias = tokens.nextToken();
            if (!tokens.hasMoreTokens())
                break;
            final String id = tokens.nextToken();
            result.put(alias, id);
        }
        return result;
    }

    @Override
    public HttpServletRequest wrapRequest(final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute(HttpSessionManager.class.getName(), this);
        return request;
    }

    @Override
    public HttpServletResponse wrapResponse(final HttpServletRequest request, final HttpServletResponse response) {
        return new MultiSessionHttpServletResponse(response, request);
    }

    class MultiSessionHttpServletResponse extends HttpServletResponseWrapper {
        private final HttpServletRequest request;

        public MultiSessionHttpServletResponse(final HttpServletResponse response, final HttpServletRequest request) {
            super(response);
            this.request = request;
        }

        @Override
        public String encodeRedirectURL(String url) {
            url = super.encodeRedirectURL(url);
            return CookieHttpSessionStrategy.this.encodeURL(url, getCurrentSessionAlias(request));
        }

        @Override
        public String encodeURL(String url) {
            url = super.encodeURL(url);

            final String alias = getCurrentSessionAlias(request);
            return CookieHttpSessionStrategy.this.encodeURL(url, alias);
        }
    }

    @Override
    public String encodeURL(final String url, final String sessionAlias) {
        final String encodedSessionAlias = urlEncode(sessionAlias);
        final int queryStart = url.indexOf("?");
        final boolean isDefaultAlias = DEFAULT_ALIAS.equals(encodedSessionAlias);
        if (queryStart < 0)
            return isDefaultAlias ? url : url + "?" + sessionParam + "=" + encodedSessionAlias;
        final String path = url.substring(0, queryStart);
        String query = url.substring(queryStart + 1, url.length());
        final String replacement = isDefaultAlias ? "" : "$1" + encodedSessionAlias;
        query = query.replaceFirst("((^|&)" + sessionParam + "=)([^&]+)?", replacement);
        if (!isDefaultAlias && url.endsWith(query)) {
            // no existing alias
            if (!(query.endsWith("&") || query.length() == 0))
                query += "&";
            query += sessionParam + "=" + encodedSessionAlias;
        }

        return path + "?" + query;
    }

    private String urlEncode(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}