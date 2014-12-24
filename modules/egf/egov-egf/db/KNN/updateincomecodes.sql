
update chartofaccounts set name='Stamp Duty for Transfer of Immovable Properties' where glcode=1201001;
update chartofaccounts set name='Nurseries' where glcode=1301007;
update chartofaccounts set name='Premium' where glcode=1301008;
update chartofaccounts set name='Lease/Rent from  Parking Places' where glcode=1301009;
update chartofaccounts set name='Quarters' where glcode=1302001;
update chartofaccounts set name='Rickshaw and Cycle' where glcode=1401006;
update chartofaccounts set name='Animal Slaughtering Fee' where glcode=1401204;
update chartofaccounts set name='Demolition Charges' where glcode=1401403;
update chartofaccounts set name='Open Space Contribution' where glcode=1401404;
update chartofaccounts set name='Parking Contribution' where glcode=1401405;
update chartofaccounts set name='Pathology Charges' where glcode=1405002;
update chartofaccounts set name='Room/Bed Charges' where glcode=1405003;
update chartofaccounts set name='Operation theatre Charges' where glcode=1405004;
update chartofaccounts set name='Ambulance' where glcode=1405005;
update chartofaccounts set name='Funeral Van' where glcode=1405006;
update chartofaccounts set name='Garbage Collection Charges' where glcode=1405007;
update chartofaccounts set name='Littering and Debris collection' where glcode=1405008;
update chartofaccounts set name='Septic Tank clearance' where glcode=1405009;
update chartofaccounts set name='Special Sanitation Charges' where glcode=1405010;
update chartofaccounts set name='Sewerage clearance charges' where glcode=1405011;
update chartofaccounts set name='Crematorium Charges' where glcode=1405012;
update chartofaccounts set name='Burial Ground Charges' where glcode=1405013;
update chartofaccounts set name='Pay and use toilets' where glcode=1405014;
update chartofaccounts set name='Water Supply' where glcode=1405015;
update chartofaccounts set name='Sale of Electricity' where glcode=1405016;
update chartofaccounts set name='Water Tanker' where glcode=1405017;
update chartofaccounts set name='Meter charges' where glcode=1405018;
update chartofaccounts set name='Fire Extinguishing' where glcode=1405019;
update chartofaccounts set name='Lighting charges' where glcode=1405020;
update chartofaccounts set name='Ticket charges' where glcode=1405021;
update chartofaccounts set name='Luggage charges' where glcode=1405022;
update chartofaccounts set name='Parking fees' where glcode=1405023;
update chartofaccounts set name='Library' where glcode=1406006;
update chartofaccounts set name='Parking Lots' where glcode=1406007;
update chartofaccounts set name='Service Charges' where glcode=1407002;
update chartofaccounts set name='Rebate from State Government' where glcode=1407009;
update chartofaccounts set name='Nursery plants' where glcode=1501002;
update chartofaccounts set name='Grass, Flowers and Fruits' where glcode=1501003;
update chartofaccounts set name='Trees' where glcode=1501004;
update chartofaccounts set name='Rubbish' where glcode=1501005;
update chartofaccounts set name='Garbage ' where glcode=1501006;
update chartofaccounts set name='Manure' where glcode=1501007;
update chartofaccounts set name='Compost' where glcode=1501008;
update chartofaccounts set name='Tenders and Forms' where glcode=1501101;
update chartofaccounts set name='Obsolete Scrap' where glcode=1501202;
update chartofaccounts set name='Assets or Properties' where glcode=1503001;
update chartofaccounts set name='Road Developments / Maintenance Grant' where glcode=1601001;
update chartofaccounts set name='Census Grant' where glcode=1601002;
update chartofaccounts set name='Election Grants' where glcode=1601003;
update chartofaccounts set name='Family Welfare Grant' where glcode=1601004;
update chartofaccounts set name='State Government' where glcode=1603001;
update chartofaccounts set name='Others' where glcode=1603002;
update chartofaccounts set name='Others' where glcode=1806005;
update chartofaccounts set name='Entry Charges from Mela and Utsav' where glcode=1808001;
update chartofaccounts set name='Stall Charges' where glcode=1808002;
update chartofaccounts set name='Pension and Leave Salary Contribution' where glcode=1808003;
update chartofaccounts set name='Bounced Cheques Realization Charges' where glcode=1808004;
update chartofaccounts set name='Fines Imposed by the Court' where glcode=1808005;
update chartofaccounts set name='Prior Period Income' where glcode=1808006;




---update vouchers

update voucherdetail set glcode=1404002 where glcode=1404013;
update generalledger set glcode=1404002,glcodeid=415 where glcode=1404013;


update voucherdetail set glcode=1100200 where glcode=1100201;
update generalledger set glcode=1100200,glcodeid=1618 where glcode=1100201;


update voucherdetail set glcode=1100300 where glcode=1100301;
update generalledger set glcode=1100300,glcodeid=1619 where glcode=1100301;