package org.egov.eb.webservice.tneb.corp;

public class FetchDataProxy implements FetchData_PortType {
  private String _endpoint = null;
  private FetchData_PortType fetchData_PortType = null;
  
  public FetchDataProxy() {
    _initFetchDataProxy();
  }
  
  public FetchDataProxy(String endpoint) {
    _endpoint = endpoint;
    _initFetchDataProxy();
  }
  
  private void _initFetchDataProxy() {
    /*try {
      fetchData_PortType = (new FetchData_ServiceLocator()).getFetchDataPort();
      if (fetchData_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)fetchData_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)fetchData_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}*///This fix is for Phoenix Migration.
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
   /* _endpoint = endpoint;
    if (fetchData_PortType != null)
      ((javax.xml.rpc.Stub)fetchData_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    *///This fix is for Phoenix Migration.
  }
  
  public FetchData_PortType getFetchData_PortType() {
    if (fetchData_PortType == null)
      _initFetchDataProxy();
    return fetchData_PortType;
  }
  
  public org.egov.eb.webservice.tneb.corp.TempPostRecords3[] fetchData(java.lang.String cuscode, java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException{
    if (fetchData_PortType == null)
      _initFetchDataProxy();
    return fetchData_PortType.fetchData(cuscode, userName, password);
  }
  
  
}