/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/**
 * FetchData_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.egov.eb.webservice.tneb.corp;


public class FetchData_ServiceLocator {
/*extends org.apache.axis.client.Service implements FetchData_Service {

    public FetchData_ServiceLocator() {
    }


    public FetchData_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FetchData_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }*/

    // Use to get a proxy class for FetchDataPort
    private java.lang.String FetchDataPort_address = "https://www.tnebnet.org:443/CorpChennai/FetchData";

    public java.lang.String getFetchDataPortAddress() {
        return FetchDataPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FetchDataPortWSDDServiceName = "FetchDataPort";

    public java.lang.String getFetchDataPortWSDDServiceName() {
        return FetchDataPortWSDDServiceName;
    }

    public void setFetchDataPortWSDDServiceName(java.lang.String name) {
        FetchDataPortWSDDServiceName = name;
    }

    public FetchData_PortType getFetchDataPort() throws javax.xml.rpc.ServiceException {
    
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FetchDataPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFetchDataPort(endpoint);
    }

    public FetchData_PortType getFetchDataPort(java.net.URL portAddress ){
    	/*throws javax.xml.rpc.ServiceException {
    }
        try {
            FetchDataPortBindingStub _stub = new FetchDataPortBindingStub(portAddress, this);
            _stub.setPortName(getFetchDataPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {  
            return null;
        }*/
    	return null;
    }

    public void setFetchDataPortEndpointAddress(java.lang.String address) {
        FetchDataPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) {
    	/*throws javax.xml.rpc.ServiceException {
    }
        try {
            if (FetchData_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                FetchDataPortBindingStub _stub = new FetchDataPortBindingStub(new java.net.URL(FetchDataPort_address), this);
                _stub.setPortName(getFetchDataPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));*/
        
        return null;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
    
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("FetchDataPort".equals(inputPortName)) {
            return getFetchDataPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
           // ((Object) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://corp.tneb.com/", "FetchData");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://corp.tneb.com/", "FetchDataPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address){
    /*	throws javax.xml.rpc.ServiceException {
    }
        
if ("FetchDataPort".equals(portName)) {
            setFetchDataPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    *//**
    * Set the endpoint address for the specified port name.
    *//*
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }*/

    }
}
