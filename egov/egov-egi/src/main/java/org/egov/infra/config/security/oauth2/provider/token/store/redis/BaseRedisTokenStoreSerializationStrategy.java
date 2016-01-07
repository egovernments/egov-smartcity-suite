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

package org.egov.infra.config.security.oauth2.provider.token.store.redis;

/**
 * This from spring oauth2 master branch, has to be replaced with original when released.
 **/
public abstract class BaseRedisTokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {

        private static final byte[] EMPTY_ARRAY = new byte[0];

        private static boolean isEmpty(byte[] bytes) {
                return bytes == null || bytes.length == 0;
        }

        @Override
        public <T> T deserialize(byte[] bytes, Class<T> clazz) {
                if (isEmpty(bytes)) {
                        return null;
                }
                return deserializeInternal(bytes, clazz);
        }

        protected abstract <T> T deserializeInternal(byte[] bytes, Class<T> clazz);

        @Override
        public String deserializeString(byte[] bytes) {
                if (isEmpty(bytes)) {
                        return null;
                }
                return deserializeStringInternal(bytes);
        }

        protected abstract String deserializeStringInternal(byte[] bytes);

        @Override
        public byte[] serialize(Object object) {
                if (object == null) {
                        return EMPTY_ARRAY;
                }
                return serializeInternal(object);
        }

        protected abstract byte[] serializeInternal(Object object);

        @Override
        public byte[] serialize(String data) {
                if (data == null) {
                        return EMPTY_ARRAY;
                }
                return serializeInternal(data);
        }

        protected abstract byte[] serializeInternal(String data);

}