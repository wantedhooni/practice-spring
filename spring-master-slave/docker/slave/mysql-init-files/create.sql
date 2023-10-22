CREATE DATABASE test_db;

#create masteruser and grant privileges
create user test_db@'%' identified by 'rootpassword';

grant all privileges on test_db.* to db_name@'%' identified by 'rootpassword';

grant all privileges ON *.* TO 'root'@'%' IDENTIFIED BY '1234';


GRANT USAGE ON *.* TO 'root'@'%' IDENTIFIED BY '1234';

## flush

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '1234';
FLUSH PRIVILEGES;