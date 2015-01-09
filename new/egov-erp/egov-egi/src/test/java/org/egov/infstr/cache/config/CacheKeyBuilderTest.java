package org.egov.infstr.cache.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.junit.Test;
import org.springmodules.cache.key.CacheKeyGenerator;

public class CacheKeyBuilderTest {

	@Test
	public void testGenerateKey() throws Exception {
		EGOVThreadLocals.setDomainName("domName");
		final Long arg1 = 2L;// (long) Math.pow(2.0, 32.0); typical case which
								// will make test fail
		final Long arg2 = 1L;
		final Method methodToCache = CacheKeyBuilderTest.class
				.getDeclaredMethod("someMethodWeWantCached", Object.class);
		final CacheKeyGenerator g = new CacheKeyBuilder();

		Serializable key1 = g.generateKey(fakeMethodInvocation(methodToCache,
				arg1));
		final Serializable key2 = g.generateKey(fakeMethodInvocation(
				methodToCache, arg2));
		assertFalse(key1.equals(key2));
		key1 = g.generateKey(fakeMethodInvocation(methodToCache, arg2));
		assertTrue(key1.equals(key2));
	}

	private MethodInvocation fakeMethodInvocation(final Method methodToCache,
			final Object arg) {
		return new MethodInvocation() {
			@Override
			public Method getMethod() {
				return methodToCache;
			}

			@Override
			public Object[] getArguments() {
				return new Object[] { arg };
			}

			@Override
			public Object getThis() {
				return CacheKeyBuilderTest.this;
			}

			@Override
			public AccessibleObject getStaticPart() {
				return null;
			}

			@Override
			public Object proceed() throws Throwable {
				return null;
			}
		};
	}

	public Object someMethodWeWantCached(final Object o) {
		return o;
	}

}
