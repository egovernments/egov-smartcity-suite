/**
 * 
 */
package org.egov.infstr.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for LRU cache
 */
public class LRUCacheTest {
	private LRUCache<String, String> cache;

	@Before
	public void setUp() {
		this.cache = new LRUCache<String, String>(1, 2);
		this.cache.put("key1", "value1");
		this.cache.put("key2", "value2");
	}

	@Test
	public void testGet() {
		assertEquals("value1", this.cache.get("key1"));
		assertEquals("value2", this.cache.get("key2"));
		assertNull(this.cache.get("key3"));
	}

	@Test
	public void testCapacity() {
		// get key2 to make it more recently used
		this.cache.get("key2");
		// now add new key. this should evict key1
		// as it is the least recently used
		this.cache.put("key3", "value3");
		assertNull(this.cache.get("key1"));
		assertEquals("value3", this.cache.get("key3"));
	}
}
