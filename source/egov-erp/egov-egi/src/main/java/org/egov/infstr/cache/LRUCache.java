/*
 * @(#)LRUCache.java 3.0, 16 Jun, 2013 4:05:29 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An simple LRU (Least Recently Used) cache for caching objects. <br>
 * <b>Note:</b> This is NOT a distributed cache.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 1L;
	private final int maxSize;

	/**
	 * Creates a new LRU cache
	 * @param minSize Minimum size of the cache
	 * @param maxSize Maximum size of the cache
	 */
	public LRUCache(final int minSize, final int maxSize) {
		super(minSize, 1.1f, true);
		this.maxSize = maxSize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return size() > this.maxSize;
	}
}
