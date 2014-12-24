var defaultAccCode = '333'
// <![CDATA[
  new Test.Unit.Runner({
	    
    setup: function() {
	  mockControl = new MockControl();
      dataServiceMock = mockControl.createMock(DataService);
      PageManager.DataService = dataServiceMock;
  	},
    
    teardown: function() {
    },
    
    testSelectDefaultAccountCode: function() { with(this) {
      dataServiceMock.expects().setQueryField("parent",2);
      dataServiceMock.expects().callDataService('getAccountDescription');
      selectDefaultAccountCode();
      assertNotEqual(3,document.getElementById('chartOfAccounts_glConetPay').selectedIndex)
      assertEqual(2,document.getElementById('chartOfAccounts_glConetPay').selectedIndex)
      mockControl.verify();
    }},

    testUpdateAccountDescription: function() { with(this) {
        dataServiceMock.expects().setQueryField("parent",1);
        dataServiceMock.expects().callDataService('getAccountDescription');
        document.getElementById('chartOfAccounts_glConetPay').selectedIndex = 1;
        updateAccountDescription();
        mockControl.verify();
    }},

    testClearCombo: function() { with(this) {
    	assertEqual(4,document.getElementById('chartOfAccounts_glConetPay').length);
        clearCombo('chartOfAccounts_glConetPay');
        assertEqual(0,document.getElementById('chartOfAccounts_glConetPay').length);
    }},
    
    testGetSubSchemeList: function() { with(this) {
        dataServiceMock.expects().setQueryField("schemeId",3);
        dataServiceMock.expects().callDataService('subschemelist');
        getsubschemelist(document.getElementById('subscheme').options[3])
    	assertEqual('subschemelist',document.getElementById('subscheme').getAttribute('exilListSource'));
        mockControl.verify();
    }},

    testGetSchemeList: function() { with(this) {
        dataServiceMock.expects().setQueryField("fundId",3);
        dataServiceMock.expects().callDataService('schemelist');
    	assertEqual(4,document.getElementById('scheme').length);
    	assertEqual(4,document.getElementById('subscheme').length);
    	getschemelist(document.getElementById('scheme').options[3])
    	assertEqual(0,document.getElementById('scheme').length);
    	assertEqual(0,document.getElementById('subscheme').length);
    	assertEqual('schemelist',document.getElementById('scheme').getAttribute('exilListSource'));
        mockControl.verify();
    }},
    
    testCalculateTotal: function() { with(this) {
    	calculateTotal();
    	assertEqual(1000,document.getElementById('netPayAmount').value);
    	assertEqual(1000,document.getElementById('netPay').value);
    }},
  }); 
// ]]>

