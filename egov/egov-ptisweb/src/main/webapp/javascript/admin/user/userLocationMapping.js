#-------------------------------------------------------------------------------
# /**
#  * eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  */
#-------------------------------------------------------------------------------
Ext.onReady(function() {
	
	//Location Data to be shown in Location Drop down.
	var location = Ext.create('Ext.data.Store', {fields: ['locationId', 'locationName'],data : locationData});
	
	//Initial submit form to view the Mapping
	var inputForm = Ext.widget('form', {
		title: 'User Location Mapping',
		renderTo: 'formContainer',
		frame: true,
		width: 400,
		bodyPadding: 10,
		bodyBorder: true,
		margin:'0 0 0 400',
		url: 'userLocationMapping!show.action',
		buttons: [{
			text: 'Show',
			handler: function() {
				var form = this.up('form').getForm();
				if (form.isValid()) {
					inputForm.hide();
					Ext.getBody().mask('Loading data...');
					form.submit({
						success: function(form, action) {
							reloadView(action.result.data);									
						},
						failure: function(form, action) {
							Ext.Msg.alert('Failed', 'There is no record exist...!');
							reloadView([]);
						}
					});					
				} 			
			}
		},
    {
        text:'Exit',
        width:80,
        handler:function() {
            window.close();
        }
    }],
		items: [{
			xtype: 'combo',
			id:'location',
			name:'location',
			fieldLabel: 'Location',
			store: location,
			queryMode: 'local',
			allowBlank:false,
			displayField: 'locationName',
			valueField: 'locationId'
		}, {
			xtype: 'checkboxfield',
			boxLabel  : 'Show All',
			name : 'showAll',
			inputValue: '1',
			id : 'checkbox1'
		}]
	});	
	
	//Reloading Data grid for new search
	var reloadView = function (record) {
		userLocationMappingStore.loadData(record);
		container.render('editor-grid');
		container.items.getAt(0).getView().refresh();
		container.show();
		Ext.getBody().unmask();
		
	};
	
	// User Location Mapping Data Model
    Ext.regModel('userLocationMapping', {
        fields: [
            {name:'id', type:'int'},
            {name:'userName', type:'string'},
            {name:'fromDate', type:'date',dateFormat:'d/m/Y'},
            {name:'toDate', type:'date',dateFormat:'d/m/Y'}
        ]
    });
    
    // Instance of a Data Store to hold User Location Mapping records
    var userLocationMappingStore = new Ext.data.Store({
        storeId:'userLocationMappingStore',
        model:'userLocationMapping',
        autoLoad:false        
    });    
    
    //Extjs Model for User drop down
    Ext.regModel('Users', {fields: [{name:'id', type:'string'},{name:'userName', type:'string'}]});
    
    //Extjs Store for User Model.
    var userStore = new Ext.data.Store({storeId:'userStore',model:'Users',autoLoad: false,
    	proxy: {type: 'ajax',url: 'userLocationMapping!showUsers.action',method: 'GET',reader: {type: 'json',root: 'data',successProperty: 'success'}}
    });
        
  //Checking User Location mapping exist for the given date range
    function checkExitingRecordOverlaps(store,vals) {
 	   var existingRecord = store.findRecord('userName',vals['userName']);
 	   	var isOverlaps = false;
 	   	if (existingRecord) {
 	   		var str = vals['fromDate'].split("/");
 	   		var str1 = vals['toDate'].split("/");
 	   		var fromDate = new Date(parseInt(str[2]),parseInt(str[1])-1,parseInt(str[0]));
 	   		var toDate = new Date(parseInt(str1[2]),parseInt(str1[1])-1,parseInt(str1[0]));
 	   		store.each(function(currentRec) {
 	   			if(currentRec.get('userName') == vals['userName'] && currentRec.get('id') != vals['id']) {
 	   				if (doesDateOverlap(currentRec.get('fromDate'),currentRec.get('toDate'),fromDate,toDate)) {
 	   					isOverlaps = true;
 	   		    		return true;
 	   		    	}
 	   		    }
 	   		});
 	   	}
 	   	return isOverlaps;
    }
    
   
    // Date range overlapping checker
    function doesDateOverlap(e1,e2,e3,e4) {
    	var fromDate = e1.getTime();
        var toDate = e2.getTime();
        var fromDate1 = e3.getTime();
        var toDate1 = e4.getTime();
        return (fromDate >= fromDate1 && fromDate <= toDate1 || fromDate1 >= fromDate && fromDate1 <= toDate);
      }
    
    
    // Format incoming date data
    function formatDate(value){
    	return value ? Ext.Date.dateFormat(value, 'M d, Y') : '';
    }
    
    //Extending validation for Date field
   Ext.apply(Ext.form.field.VTypes, {
        daterange: function(val, field) {
            var date = field.parseDate(val);
            if (!date) {
                return false;
            }
            if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                var start = field.up('form').down('#' + field.startDateField);
                start.setMaxValue(date);
                start.validate();
                this.dateRangeMax = date;
            }
            else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
                var end = field.up('form').down('#' + field.endDateField);
                end.setMinValue(date);
                end.validate();
                this.dateRangeMin = date;
            }
            return true;
        },

        daterangeText: 'From Date must be less than To Date'
    });
   
	// Window to Add or Edit existing User Location Mapping
    Ext.define('App.ReviewWindow', {
        extend: 'Ext.window.Window',
        constructor: function(config) {        
            config = config || {};
            Ext.apply(config, {        
                title:'User Location Mapping',
                width:380,
                height:210,
                layout:'fit',        
                items:[{
                    xtype:'form',
                    id:'userlocationmappingform',
                    fieldDefaults: {
                        labelAlign: 'left',
                        labelWidth: 90,
                        anchor: '100%'
                    },            
                    bodyPadding:5,
                    items:[{
                        xtype:'fieldset',
                        title:'Mapping Info',
                        items:[{
                            xtype:'hiddenfield',
                            name:'id'
                        },{
                            xtype:'combo',
                            store: userStore,
                            minChars:1,
						    displayField:'userName',
						    emptyText:'Select a User',
						    typeAhead: true,
						    mode: 'local',
						    queryParam: 'query',  //contents of the field sent to server.
						    hideTrigger: true,    //hide trigger so it doesn't look like a combobox.
						    selectOnFocus:true,
                            name:'userName',
                            fieldLabel:'User Name',
                            allowBlank:false
                        },{
                        	xtype:'datefield',
                            fieldLabel: 'From Date',
			                name: 'fromDate',
			                id:'fromDate',
			                format:'d/m/Y',
			                vtype: 'daterange',
			                minValue:new Date(),
			                endDateField: 'toDate',
                            allowBlank:false                              
                        },{
                        	xtype:'datefield',
                            fieldLabel: 'To Date',
			                name: 'toDate',
			                id:'toDate',
			                format:'d/m/Y',
			                minValue:new Date(),
			                vtype: 'daterange',
			                startDateField: 'fromDate',
                            allowBlank:false                              
                        }]
                    }]
                }],
                buttons:[{
                    text:'Cancel',
                    width:80,
                    handler:function() {
                        this.up('window').close();
                    }
                },
                {
                    text:'Save',
                    width:80,
                    handler:function(btn, eventObj) {
                        var window = btn.up('window');
                        var form = window.down('form').getForm();
                        
                        if (form.isValid()) {
                            window.getEl().mask('saving data...');
                            form.findField('userName').enable();
                            var vals = form.getValues();
                            var currentMapping = userLocationMappingStore.findRecord('id', vals['id']);
                            // look up id for this location to see if they already exist
                            if(vals['id'] && currentMapping) {
                            	if (!checkExitingRecordOverlaps(userLocationMappingStore,vals)) {
                            	   Ext.getBody().mask('Saving data...');
	                               Ext.Ajax.request({
							         url : 'userLocationMapping!updateMapping.action',
							                  method: 'POST',
							                  params :{'id':vals['id'],'userName':vals['userName'],'fromDate':vals['fromDate'], 'toDate':vals['toDate'],'location':Ext.getCmp('location').getValue()},
							                  success: function ( result, request ) {
							                	  var jsonData = Ext.decode(result.responseText);
							                	  if (jsonData.success){
							                		  currentMapping.set('fromDate', vals['fromDate']);
							                		  currentMapping.set('toDate', vals['toDate']);
							                		  container.items.getAt(0).getView().refresh();
							                		  Ext.getBody().unmask();
							                		  Ext.Msg.alert('Success', 'Record updated successfully...!');							                		  
							                	  } else {
							                		  Ext.getBody().unmask();
							                		  Ext.Msg.alert('Error', "Can not edit, "+vals['userName']+" already mapped for  for the given period!");
							                	  }
							                  },
							               	  failure: function ( result, request ) {
							               		Ext.getBody().unmask();
							                  	Ext.Msg.alert('Error', 'Record update failed...!');
							                  }
							          });
                            	} else {
                            		Ext.Msg.alert('Error', "Can not edit, "+vals['userName']+" already mapped for  for the given period!");
                            	}
                            } else {
                            	if (!checkExitingRecordOverlaps(userLocationMappingStore,vals)) {
                            		Ext.getBody().mask('Saving data...');
                                	Ext.Ajax.request({
								          url : 'userLocationMapping!addMapping.action',
						                  method: 'POST',
						                  params :{'id':vals['id'],'userName':vals['userName'],'fromDate':vals['fromDate'], 'toDate':vals['toDate'],'location':Ext.getCmp('location').getValue()},
						                  success: function ( result, request ) {
						                	  var jsonData = Ext.decode(result.responseText);
						                	  if (jsonData.success){
						                		  userLocationMappingStore.add({
				                                    	id: jsonData.id,
				                                    	userName: vals['userName'],
				                                    	fromDate: vals['fromDate'],
				                                    	toDate: vals['toDate']
				                                	});
							                	   container.items.getAt(0).getView().refresh();
							                	   Ext.getBody().unmask();
							                       Ext.Msg.alert('Success', 'Record inserted successfully...!');						                		  
						                	  } else {
						                		  Ext.getBody().unmask();
						                		  Ext.Msg.alert('Error', "Can not add...! Location mapping already exist for "+vals['userName']+" for the given period!");
						                	  }
							               },
							               failure: function ( result, request ) {
							            	   Ext.getBody().unmask();
							                   Ext.Msg.alert('Error', 'Record insert failed...!');
							               }
								       });
                                } else {
                                	Ext.Msg.alert('Error', "Can not add...! Location mapping already exist for "+vals['userName']+" for the given period!");
                                }                      
                            }
                            window.getEl().unmask();
                            window.close();
                        }
                    }
                }]
            }); // end Ext.apply
            
            App.ReviewWindow.superclass.constructor.call(this, config);
            
        } // end constructor
        
    });

    

    var grid = Ext.define('UserLocationMapping.Grid', {
        extend: 'Ext.grid.Panel',
        alias: 'widget.userlocationgrid',
        id:'userlocationgrid',
        initComponent: function(){
             Ext.apply(this, {
                iconCls: 'icon-grid',
                frame: true,
                dockedItems: [{
                    xtype: 'toolbar',
                    items: [{
                        text:'Add Mapping',
                        tooltip:'Add a new Mapping',
                        iconCls:'add',
                        icon:'/egi/images/add.png',
                        handler:function() { 
                            var win = new App.ReviewWindow().show();
                        }
                    }]
                },{
                    weight: 1,
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    items: ['->',{
                        text:'Search Again',
                        width:80,
                        handler:function() {
                        	container.hide();
                            inputForm.show();
                        }
                    },
                    {
                        text:'Exit',
                        width:80,
                        handler:function() {
                            window.close();
                        }
                    }]
                }],
                columns: [
                {
                    text:'User Name',
                    dataIndex:'userName',
                    width:75,
                    flex:3
                },
                
                {
                    text:'From Date',
                    dataIndex:'fromDate',
                    renderer: formatDate,
                    width:25,
                    flex:1
                },
                {
                    text:'To Date',
                    dataIndex:'toDate',
                    renderer: formatDate,
                    width:25,
                    flex:1
                },
                {
                    xtype:'actioncolumn',
                    width:45,
                    align: 'center',
                    hideable: false,
                    items:[{
                        icon:'/egi/images/edit.png',
                        tooltip:'Edit Mapping',
                        handler:function(grid, rowIndex, colIndex) {
                        	  var employee = grid.getStore().getAt(rowIndex);
                              var win = new App.ReviewWindow({hidden:true});
                              var form = win.down('form').getForm();
                              form.findField('userName').disable();
                              form.findField('fromDate').minValue='';
                              form.findField('toDate').minValue='';
                              form.loadRecord(employee);
                              win.show();
                        }
                    },{
                        icon:'/egi/images/delete.png',
                        tooltip:'Delete Mapping',
                        handler:function(grid, rowIndex, colIndex) {
                        	Ext.MessageBox.confirm('Confirmation', 'Do you want to delete this record...?', function(btn){
							      if (btn == 'yes') {
							    	  var employee = grid.getStore().getAt(rowIndex);	                              	  
							    	  Ext.Ajax.request({
								         url : 'userLocationMapping!deleteMapping.action',
								                  method: 'POST',
								                  params :{'id':employee.get('id'),'location':Ext.getCmp('location').getValue()},
								                  success: function ( result, request ) {
								                	  grid.store.remove(employee); 
								                	  container.items.getAt(0).getView().refresh();
								                      Ext.Msg.alert('Success', 'Record deleted successfully...!');
								               },
								                  failure: function ( result, request ) {
								                   Ext.Msg.alert('Error', 'Record delete failed...!');
								               }
								       });
							       
							      }
							         
							  });
                                                         
                        }
                    }]
                }],
                columnLines: true,
                viewConfig: {stripeRows:true}
            });
            this.callParent();
        }
    }); 
    
    var container = Ext.create('Ext.container.Container', {
    	margin:'0 0 0 300',
        width: 700,
        height: 325,
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        items: [{
            itemId: 'grid',
            xtype: 'userlocationgrid',
            title: 'User Location Mapping',
            flex: 1,
            store: userLocationMappingStore
        }]
    });
 	
});
