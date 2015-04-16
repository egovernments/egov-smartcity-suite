/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
