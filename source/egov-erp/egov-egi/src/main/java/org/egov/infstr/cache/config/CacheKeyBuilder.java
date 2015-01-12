/*
 * @(#)CacheKeyBuilder.java 3.0, 16 Jun, 2013 4:08:52 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.cache.config;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.springmodules.cache.key.CacheKeyGenerator;
import org.springmodules.cache.key.HashCodeCacheKey;
import org.springmodules.cache.key.HashCodeCalculator;
import org.springmodules.cache.util.Reflections;

public class CacheKeyBuilder implements CacheKeyGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable generateKey(final MethodInvocation invocation) {

		final int classHash = invocation.getThis().getClass().getName().hashCode();
		final int methodHash = invocation.getMethod().getName().hashCode();
		final int domainHash = EGOVThreadLocals.getDomainName().hashCode();
		final HashCodeCalculator hashCodeCalculator = new HashCodeCalculator();
		hashCodeCalculator.append(domainHash);
		hashCodeCalculator.append(classHash);
		hashCodeCalculator.append(methodHash);
		final Object[] methodArguments = invocation.getArguments();
		if (methodArguments != null) {
			final int methodArgumentCount = methodArguments.length;
			for (int i = 0; i < methodArgumentCount; i++) {
				final Object methodArgument = methodArguments[i];
				int hash = 0;
				if (methodArgument instanceof Enum) {
					hash = methodArgument.toString().hashCode();
				} else {
					hash = Reflections.reflectionHashCode(methodArgument);
				}

				hashCodeCalculator.append(hash);
			}
		}

		final long checkSum = hashCodeCalculator.getCheckSum();
		final int hashCode = hashCodeCalculator.getHashCode();
		return new HashCodeCacheKey(checkSum, hashCode);

	}
}