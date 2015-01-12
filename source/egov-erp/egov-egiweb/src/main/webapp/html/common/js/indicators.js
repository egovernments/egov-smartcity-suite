

var indicators=[
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Statewise/India',chart_url:"http://spreadsheets.google.com/pub?key=rEAysGANp4BN7A9gpPqeSdg&oid=3&output=image",boundary:0,type:'chart'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Districtwise/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rtkVrV1hNA5tiGUAf0h9r5g&oid=1&output=image",boundary:10,type:'chart'},
{name:'IAY',indicator:'SCST/Minority/Others',title:'IAY - Housing Project- Distribution among SC/ST vs Minorities/Others - Districtwise/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rtkVrV1hNA5tiGUAf0h9r5g&oid=4&output=image",boundary:10,type:'chart'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Districtwise/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=rq5hXWfbLQMxkzplccbMbrQ&oid=4&output=image",boundary:29,type:'chart'},
{name:'IAY',indicator:'SCST/Minority/Others',title:'IAY - Housing Project- Distribution among SC/ST vs Minorities/Others - Districtwise/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=rq5hXWfbLQMxkzplccbMbrQ&oid=5&output=image",boundary:29,type:'chart'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Darbhanga/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rq7K4pWpmv-Um9jnnxRuTEw&oid=1&output=image",boundary:1013,type:'chart'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Patna/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=r2Po8cXYVAvXKGndpxvq4Rg&oid=1&output=image",boundary:1028,type:'chart'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Mysore/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=r_GqKqQG66G7bgsmJ0U9QLQ&oid=1&output=image",boundary:2926,type:'chart'},

{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Statewise/India',table_url:"/dashboard/html/common/html/StatesIAYTargetAchieved.html",boundary:0,type:'table'},
{name:'IAY',indicator:'Target/Achieved',title:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Districtwise/Bihar',table_url:"/dashboard/html/common/html/BiharDistIAYTargetAchieved.html",boundary:10,type:'table'},
{name:'IAY',indicator:'SCST/Minority/Others',title:'IAY - Housing Project- Distribution among SC/ST vs Minorities/Others - Districtwise/Bihar',table_url:"/dashboard/html/common/html/BiharDistIAYSCSTMinorities.html",boundary:10,type:'table'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Districtwise/Karnataka',table_url:"/dashboard/html/common/html/KarDistIAYTargetAchieved.html",boundary:29,type:'table'},
{name:'IAY',indicator:'SCST/Minority/Others',title:'IAY - Housing Project- Distribution among SC/ST vs Minorities/Others - Districtwise/Karnataka',table_url:"/dashboard/html/common/html/KarDistIAYSCSTMinorities.html",boundary:29,type:'table'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Darbhanga/Bihar',table_url:"/dashboard/html/common/html/BiharDarbhangaTalukIAYTargetAchieved.html",boundary:1013,type:'table'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Patna/Bihar',table_url:"/dashboard/html/common/html/BiharTalukPatnaIAYTargetAchieved.html",boundary:1028,type:'table'},
{name:'IAY',indicator:'Target/Achieved',title:'IAY - Houses Under construction vs completed - Talukwise/Mysore/Karnataka',table_url:"/dashboard/html/common/html/KarTalukMysoreIAYTargetAchieved.html",boundary:2926,type:'table'},

{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Statewise/India',chart_url:"http://spreadsheets.google.com/pub?key=rY0EeqKqDga1nF04feFxFGw&oid=1&output=image",boundary:0,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Statewise/India',chart_url:"http://spreadsheets.google.com/pub?key=rY0EeqKqDga1nF04feFxFGw&oid=2&output=image",boundary:0,type:'chart'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Districtwise/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=2&output=image",boundary:10,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Districtwise/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rP6HRPFPs2PWDRV1I5dRQYA&oid=1&output=image",boundary:10,type:'chart'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Districtwise/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=2&output=image",boundary:29,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Districtwise/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=rdpYareG7wOsHbMP8s9Z28w&oid=1&output=image",boundary:29,type:'chart'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Darbhanga/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=1&output=image",boundary:1013,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Darbhanga/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=r81C2vlQQu4Udn46gP46bDQ&oid=3&output=image",boundary:1013,type:'chart'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Patna/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rIgvPmOvxmZ4AG5E0KkoemQ&oid=5&output=image",boundary:1028,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Patna/Bihar',chart_url:"http://spreadsheets.google.com/pub?key=rIgvPmOvxmZ4AG5E0KkoemQ&oid=6&output=image",boundary:1028,type:'chart'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Mysore/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=r_fvEIPUo7auedSbvjZkt4Q&oid=1&output=image",boundary:2926,type:'chart'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Mysore/Karnataka',chart_url:"http://spreadsheets.google.com/pub?key=r_fvEIPUo7auedSbvjZkt4Q&oid=4&output=image",boundary:2926,type:'chart'},

{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Statewise/India',table_url:"/dashboard/html/common/html/StatesNREGAMoneySpentTable.html",boundary:0,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Statewise/India',table_url:"/dashboard/html/common/html/StatesNREGAWorksTable.html",boundary:0,type:'table'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Districtwise/Bihar',table_url:"/dashboard/html/common/html/BiharDistNREGAMoneySpentTable.html",boundary:10,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Districtwise/Bihar',table_url:"/dashboard/html/common/html/BiharDistNREGAWorksTable.html",boundary:10,type:'table'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Districtwise/Karnataka',table_url:"/dashboard/html/common/html/KarDistNREGAMoneySpentTable.html",boundary:29,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Districtwise/Karnataka',table_url:"/dashboard/html/common/html/KarDistNREGAWorksTable.html",boundary:29,type:'table'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Darbhanga/Bihar',table_url:"/dashboard/html/common/html/BiharDarbangaTalukNREGAMoneySpentTbl.html",boundary:1013,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Darbhanga/Bihar',table_url:"/dashboard/html/common/html/BiharDarbangaTalukNREGAWorksTbl.html",boundary:1013,type:'table'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Patna/Bihar',table_url:"/dashboard/html/common/html/BiharPatnaTalukNREGAMoneySpentTbl.html",boundary:1028,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Patna/Bihar',table_url:"/dashboard/html/common/html/BiharPatnaTalukNREGAWorksTbl.html",boundary:1028,type:'table'},
{name:'NREGA',indicator:'MoneySpent',title:'NREGA - Money Spent - Talukwise/Mysore/Karnataka',table_url:"/dashboard/html/common/html/KarMysoreTalukNREGAMoneySpentTbl.html",boundary:2926,type:'table'},
{name:'NREGA',indicator:'Works(TakenUp Vs Completed)',title:'NREGA - Work Taken-up vs Completed - Talukwise/Mysore/Karnataka',table_url:"/dashboard/html/common/html/KarMysoreTalukNREGAWorksTbl.html",boundary:2926,type:'table'},

{name:'IAY',indicator:'Gradation',title:'IAY- Housing Target Achieved',type:'map',boundary:'dynamic'},
{name:'NREGA',indicator:'Gradation',title:'NREGA - Persondays',type:'map',boundary:'dynamic'},
{name:'PGR',indicator:'Gradation',title:'PGR',type:'map',boundary:'dynamic'},
{name:'SANITATION',indicator:'Gradation',title:'Total Sanitation Campaign (NGP Gram Panchayats)',type:'map',boundary:'dynamic'},
{name:'BPL',indicator:'Gradation',title:'BPL Families',type:'map',boundary:'dynamic'},

{name:'NREGA',indicator:'Person Days',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/NREGA_India.jpg',boundary:'0'},
{name:'NREGA',indicator:'Person Days',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/NREGA_Karnataka.jpg',boundary:'29'},
{name:'NREGA',indicator:'Person Days',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/NREGA_Mysore.jpg',boundary:'2926'},

{name:'NREGA',indicator:'Person Days',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/NREGA_Bihar.jpg',boundary:'10'},
{name:'NREGA',indicator:'Person Days',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/NREGA_Dharbanga.jpg',boundary:'1013'},


{name:'IAY',indicator:'Houses Completed',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/IAY_India.jpg',boundary:'0'},
{name:'IAY',indicator:'Houses Completed',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/IAY_Karnataka.jpg',boundary:'29'},
{name:'IAY',indicator:'Houses Completed',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/IAY_Mysore.jpg',boundary:'2926'},


{name:'IAY',indicator:'Houses Completed',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/IAY_Bihar.jpg',boundary:'10'},
{name:'IAY',indicator:'Houses Completed',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/IAY_Dharbanga.jpg',boundary:'1013'},


{name:'SANITATION',indicator:'NGP',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/SANITATION_India.jpg',boundary:'0'},
{name:'SANITATION',indicator:'NGP',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/SANITATION_Karnataka.jpg',boundary:'29'},
{name:'SANITATION',indicator:'NGP',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/SANITATION_Mysore.jpg',boundary:'2926'},

{name:'SANITATION',indicator:'NGP',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/SANITATION_Bihar.jpg',boundary:'10'},
{name:'SANITATION',indicator:'NGP',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/SANITATION_Dharbanga.jpg',boundary:'1013'},


{name:'BPL',indicator:'BPL Families',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/BPL_Bihar.jpg',boundary:'10'},
{name:'BPL',indicator:'BPL Families',title:'',type:'legend',legend_url:'/dashboard/html/common/gisimages/BPL_Dharbanga.jpg',boundary:'1013'},


{name:'PGR',indicator:'NREGA',title:'',marker_url:"/dashboard/external/pgr/citizen/georeports.jsp",marker_params:"type=4",boundary:'dynamic',type:'markers'},
{name:'PGR',indicator:'IAY',title:'',marker_url:"/dashboard/external/pgr/citizen/georeports.jsp",marker_params:"type=2",boundary:'dynamic',type:'markers'},
{name:'PGR',indicator:'Sanitation',title:'',marker_url:"/dashboard/external/pgr/citizen/georeports.jsp",marker_params:"type=3",boundary:'dynamic',type:'markers'},
{name:'PGR',indicator:'General',title:'',marker_url:"/dashboard/external/pgr/citizen/georeports.jsp",marker_params:"type=1",boundary:'dynamic',type:'markers'},
{name:'PGR',indicator:'Education',title:'',marker_url:"/dashboard/external/pgr/citizen/georeports.jsp",marker_params:"type=7",boundary:'dynamic',type:'markers'},


{name:'SANITATION',indicator:'TSC (2008) vs Rural Female Literacy',title:'',chart_url:"/dashboard/html/common/gisimages/TSC_2008_RFL.jpg",boundary:0,type:'chart'},
{name:'SANITATION',indicator:'TSC (2008) vs Infant Mortality',title:'',chart_url:"/dashboard/html/common/gisimages/TSC_IFM_2008.jpg",boundary:0,type:'chart'},
{name:'SANITATION',indicator:'TSC Coverage vs Infant Mortality (Scatter Plot)',title:'',chart_url:"http://www.egovernments.org/dashboard/html/common/gisimages/TSC_vs_IFM.jpg",boundary:0,type:'chart'},


{name:'BENEFICIARY',indicator:'BPL',title:'',chart_url:"http://spreadsheets.google.com/pub?key=rSXJgEqY15Woxr9HH-MGIWQ&oid=1&output=image",boundary:10,type:'chart'}
];


var action_map=[
];
action_map['PGR_Register']={name:'PGR_Register',target_url:'http://125.99.252.89:9080/pgr/citizen/BeforeReg.do',text:'Zoom and click on a location to register a complaint'}
