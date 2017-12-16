CREATE DATABASE apptrackdb;
CREATE USER apptrackUser WITH password 'apptrack';
GRANT ALL ON DATABASE apptrackDb TO apptrackUser;
CREATE ROLE apptrackrole;
GRANT apptrackRole TO apptrackUser;
SHOW hba_file;
ALTER ROLE apptrackrole WITH LOGIN;