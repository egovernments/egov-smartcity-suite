/**
 * FetchData_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */   

package org.egov.eb.webservice.tneb.corp;

public interface FetchData_PortType extends java.rmi.Remote {
    public TempPostRecords3[] fetchData(java.lang.String cuscode, java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;
} 
