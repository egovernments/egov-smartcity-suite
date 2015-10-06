package org.egov.infra.config.security.oauth2.provider.token.store.redis;

/**
 * This from spring oauth2 master branch, has to be replaced with original when released.
 **/
public interface RedisTokenStoreSerializationStrategy {

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    String deserializeString(byte[] bytes);

    byte[] serialize(Object object);

    byte[] serialize(String data);

}
