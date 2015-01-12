package org.egov.web.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.egov.infstr.services.Page;
import org.junit.Test;
import org.mockito.Mockito;

public class EgovPaginatedListTest {

	@Test
	public final void testEgovPaginatedListPage() {
		final Page page = Mockito.mock(Page.class);
		final EgovPaginatedList pageList = new EgovPaginatedList(page, 1);
		assertNotNull(pageList);
	}

	@Test
	public final void testEgovPaginatedListIntInt() {
		EgovPaginatedList pageList = new EgovPaginatedList(1, 1);
		assertNotNull(pageList);
		pageList = new EgovPaginatedList(-1, 1);
		assertNotNull(pageList);
	}

	@Test
	public final void testEgovPaginatedListPageStringSortOrderEnum() {
		final Page page = Mockito.mock(Page.class);
		final EgovPaginatedList pageList = new EgovPaginatedList(page, null,
				null);
		assertNotNull(pageList);
	}

	@Test
	public final void testEgovPaginatedListIntIntStringSortOrderEnum() {
		Mockito.mock(Page.class);
		EgovPaginatedList pageList = new EgovPaginatedList(-1, 1, null, null);
		assertNotNull(pageList);
		pageList = new EgovPaginatedList(1, 1, null, null);
		assertNotNull(pageList);
	}

	private EgovPaginatedList createpageList() {
		final Page page = Mockito.mock(Page.class);
		return new EgovPaginatedList(page, 1);
	}

	@Test
	public final void testGetFullListSize() {
		createpageList().getFullListSize();
		assertTrue(true);
	}

	@Test
	public final void testSetFullListSize() {
		createpageList().setFullListSize(1);
		assertTrue(true);
	}

	@Test
	public final void testGetList() {
		createpageList().getList();
		assertTrue(true);
	}

	@Test
	public final void testSetList() {
		createpageList().setList(Collections.emptyList());
		assertTrue(true);
	}

	@Test
	public final void testGetObjectsPerPage() {
		createpageList().getObjectsPerPage();
		assertTrue(true);
	}

	@Test
	public final void testSetObjectsPerPage() {
		createpageList().setObjectsPerPage(1);
		assertTrue(true);
	}

	@Test
	public final void testGetPageNumber() {
		createpageList().getPageNumber();
		assertTrue(true);
	}

	@Test
	public final void testSetPageNumber() {
		createpageList().setPageNumber(-1);
		assertTrue(true);
		createpageList().setPageNumber(1);
		assertTrue(true);
	}

	@Test
	public final void testGetSearchId() {
		createpageList().getSearchId();
		assertTrue(true);
	}

	@Test
	public final void testGetSortCriterion() {
		createpageList().getSortCriterion();
		assertTrue(true);
	}

	@Test
	public final void testSetSortCriterion() {
		createpageList().setSortCriterion(null);
		assertTrue(true);
	}

	@Test
	public final void testGetSortDirection() {
		createpageList().getSortDirection();
		assertTrue(true);
	}

	@Test
	public final void testSetSortDirection() {
		createpageList().setSortDirection(null);
		assertTrue(true);
	}

}
