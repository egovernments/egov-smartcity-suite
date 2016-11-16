-----------------------------------------------------------ADDING FEATURE STARTS-------------------------------------------------------------


					---------------------------Committee Type----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create CommitteeType And Add Members','Create CommitteeType And Add Members',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update CommitteeType And Members','Update CommitteeType And Members',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View CommitteeType And Members','View CommitteeType And Members',(select id from eg_module  where name = 'Council Management'));	

					---------------------------Preamble----------------------------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Preamble','Create Preamble',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Preamble','Update Preamble',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Preamble','View Preamble',(select id from eg_module  where name = 'Council Management'));					


					---------------------------Agenda----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Agenda','Create Agenda',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Agenda','Update Agenda',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Agenda','View Agenda',(select id from eg_module  where name = 'Council Management'));

					---------------------------Meeting----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Meeting','Create Meeting',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update Meeting','Update Meeting',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Meeting','View Meeting',(select id from eg_module  where name = 'Council Management'));

				---------------------------Meeting MOM----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create MOM','Create MOM',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update MOM','Update MOM',(select id from eg_module  where name = 'Council Management'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View MOM','View MOM',(select id from eg_module  where name = 'Council Management'));

				---------------------------Meeting Attendance----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Search Meeting Attendance','Search Meeting Attendance',(select id from eg_module  where name = 'Council Management'));

				---------------------------Send Sms And Email----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Send Sms And Email','Send Sms And Email',(select id from eg_module  where name = 'Council Management'));

			---------------------------Attendance Report----------------------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Attendance Report','Attendance Report',(select id from eg_module  where name = 'Council Management'));

-----------------------------------------------------------ADDING FEATURE ENDS-------------------------------------------------------------


-----------------------------------------------------------ADDING FEATURE ACTION STARTS----------------------------------------------------

------------------------ Council CommitteeType -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-Committteetype') ,(select id FROM eg_feature WHERE name = 'Create CommitteeType And Add Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'Create CommitteeType And Add Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'Create CommitteeType And Add Members'));

 				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilCommitteetype') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilCommitteetype') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update-CouncilCommitteetype') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

				------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'View CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilCommitteetype') ,(select id FROM eg_feature WHERE name = 'View CommitteeType And Members'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilCommittee') ,(select id FROM eg_feature WHERE name = 'View CommitteeType And Members'));


------------------------ Council Preamble -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Download-documnets') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Preamble') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));


				------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'View Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'View Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilPreamble') ,(select id FROM eg_feature WHERE name = 'View Preamble'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Download-documnets') ,(select id FROM eg_feature WHERE name = 'View Preamble'));


------------------------ Council Agenda -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New Agenda') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AgendaAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'createAgenda') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-Agenda') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilAgenda') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchAgendaToCreateMeeting') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilAgenda') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AgendaAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-Agenda') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Agenda') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));
				
					------------------------ View -----------------


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilAgenda') ,(select id FROM eg_feature WHERE name = 'View Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilAgenda') ,(select id FROM eg_feature WHERE name = 'View Agenda'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilAgenda') ,(select id FROM eg_feature WHERE name = 'View Agenda'));


------------------------ Council Meeting -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create Meeting invitation') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchAgendaToCreateMeeting') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Save Meeting invitation') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

				 ------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-Councilmeeting') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit Result-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Edit-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Meeting') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

 				------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'View Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View Result-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'View Meeting'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilMeeting') ,(select id FROM eg_feature WHERE name = 'View Meeting'));


------------------------ Council Meeting MOM -----------------

                                  ------------------------ Create -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMeetingToCreateMOM') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-wardlist') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-resolutionlist') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-Departmentlist') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create-MOM') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-MOM') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

				------------------------ Update -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and Edit-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchCreatedMOM Edit') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'New-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-wardlist') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-resolutionlist') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view-Departmentlist') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Update Mom') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Result-MOM') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

				------------------------ View -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Search and View-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'View MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchCreatedMOM View') ,(select id FROM eg_feature WHERE name = 'View MOM'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'View-CouncilMOM') ,(select id FROM eg_feature WHERE name = 'View MOM'));

				------------------------ Search Meeting Attendance -----------------

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchAttendanceForMeeting') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ShowAttendanceSearchResult') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AttendanceAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

				------------------------ Send Sms And Email -----------------
	
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'RetrieveSmsAndEmailForMeeting') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchMeetingToCreateMOM') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SendSmsAndEmailForMeeting') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));



------------------------ Attendance Report -----------------
	
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Attendance Report') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SearchAttendanceForMeeting') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ShowAttendanceSearchResult') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AttendanceAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));


-----------------------------------------------------------ADDING FEATURE ROLE BEGINS----------------------------------------------------

				------------------------ Council CommitteeType -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create CommitteeType And Add Members'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View CommitteeType And Members'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create CommitteeType And Add Members'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update CommitteeType And Members'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View CommitteeType And Members'));


				------------------------ Preamble -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Preamble'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Preamble'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Preamble'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Preamble'));


				------------------------ Agenda -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Create Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Update Agenda'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'View Agenda'));


				------------------------ Meeting -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Create Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Update Meeting'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'View Meeting'));


				------------------------ Meeting MOM -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'View MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Create MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Update MOM'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'View MOM'));


				------------------------ Meeting Attendance-----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Search Meeting Attendance'));

				------------------------ Send Sms And Email -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Send Sms And Email'));


				------------------------ Attendance Report -----------------

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Management Admin') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Council Clerk') ,(select id FROM eg_feature WHERE name = 'Attendance Report'));

