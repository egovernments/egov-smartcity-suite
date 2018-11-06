ALTER TABLE eg_user ADD COLUMN usemultifa BOOLEAN;
ALTER TABLE eg_user_aud ADD COLUMN usemultifa BOOLEAN;
update eg_user set usemultifa=false;
update eg_user_aud set usemultifa=false;
