drop table testArrayEntity;
drop type 

create or replace type data_array is varray(1000) of number;

create table testArrayEntity (
	name varchar(30) primary key not null,
	contents data_array
)

INSERT INTO testArrayEntity values
(
	'zero', 
	data_array(1, 2, 3)
);


drop table orders;
drop type sale_entry;
drop type sale_array;

CREATE OR REPLACE TYPE sale_entry AS OBJECT (item varchar(30), quantity NUMBER);
CREATE OR REPLACE TYPE sale_array AS VARRAY(10) OF sale_entry;

CREATE TABLE ORDERS (
	id NUMBER,                      
	customer VARCHAR2(100),
    sales sale_array
);
 
INSERT INTO ORDERS values 
(6, 'linguine', 
	sale_array(
		sale_entry('noodles',130)),
		sale_entry('sauce',1))
);
select * from orders;


