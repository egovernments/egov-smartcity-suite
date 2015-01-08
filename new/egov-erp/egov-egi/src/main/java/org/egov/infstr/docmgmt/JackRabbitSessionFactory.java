/*
 * @(#)JackRabbitSessionFactory.java 3.0, 17 Jun, 2013 11:57:45 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.docmgmt;

import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Workspace;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import org.springmodules.jcr.JcrSessionFactory;

/**
 * Jackrabbit specific implementation for {@link org.springmodules.jcr.JcrSessionFactory} In future this can be replaced with latest {@link org.springmodules.jcr.jackrabbit.JackrabbitSessionFactory} This is same as{@link org.springmodules.jcr.jackrabbit.JackrabbitSessionFactory} but known error is
 * suppressed and have an optional API registerNodeTypes(workspace) uncomment it to add Node Type one by one.
 */
public class JackRabbitSessionFactory extends JcrSessionFactory {
	private static final Logger LOG = LoggerFactory.getLogger(JackRabbitSessionFactory.class);
	private Resource nodeDefinitions[];
	private List<String> contentTypes;

	public JackRabbitSessionFactory() {
	}

	@Override
	public void destroy() throws Exception {
		super.destroy();
		((RepositoryImpl) this.getRepository()).shutdown();
	}

	/*
	 * protected void registerNodeTypes(Workspace ws, Resource resource, String contentType) throws Exception { NodeTypeRegistry ntReg = ((NodeTypeManagerImpl) ws.getNodeTypeManager()).getNodeTypeRegistry(); List<NodeTypeDef> types = new ArrayList<NodeTypeDef>(); if
	 * ("text/x-jcr-cnd".equals(contentType)) { BufferedReader cndReader = new BufferedReader(new FileReader(resource.getFile())); CompactNodeTypeDefReader compactNodeTypeDefReader = new CompactNodeTypeDefReader(cndReader, resource.getFilename()); types = compactNodeTypeDefReader.getNodeTypeDefs();
	 * } else if ("text/xml".equals(contentType)) { types = Arrays.asList(NodeTypeReader.read(resource.getInputStream())); } for (NodeTypeDef type : types) { if (!ntReg.isRegistered(type.getName())) { ntReg.registerNodeType(type); } } }
	 */

	@Override
	protected void registerNodeTypes() throws Exception {
		if (!ObjectUtils.isEmpty(this.nodeDefinitions)) {
			final Workspace ws = getSession().getWorkspace();
			final NodeTypeManagerImpl nodeTypeManager = (NodeTypeManagerImpl) ws.getNodeTypeManager();
			for (int index = 0; index < this.nodeDefinitions.length; index++) {
				final Resource resource = this.nodeDefinitions[index];
				try {
					nodeTypeManager.registerNodeTypes(resource.getInputStream(), this.contentTypes.get(index));
				} catch (final RepositoryException ex) {
					LOG.warn("Custom Node Type can not be registered from source " + resource.getFilename() + ", Create another cnd file");
					// LOG.info("Trying to register single Node Type at a time for : "+resource.getFilename());
					// registerNodeTypes(ws, resource, contentTypes.get(index));
				}
			}

		}
	}

	public void setNodeDefinitions(final Resource nodeDefinitions[]) {
		this.nodeDefinitions = nodeDefinitions;
	}

	public void setContentTypes(final List<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

}
