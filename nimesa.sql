create database nimesa;

show databases;
use nimesa;

create table amazon_s3_ec2(job_id int NOT NULL AUTO_INCREMENT,s3_fetch_status varchar(15), ec2_fetch_status varchar(15)  ,ec2_instance TEXT,PRIMARY KEY (job_id));



CREATE TABLE s3_files (
    file_id INT AUTO_INCREMENT PRIMARY KEY,
    job_id int,
    s3_bucket VARCHAR(100),
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT,
    CONSTRAINT fk_job_id FOREIGN KEY (job_id) REFERENCES amazon_s3_ec2(job_id)
);


truncate s3_files;
truncate amazon_s3_ec2;
SET FOREIGN_KEY_CHECKS = 0;

select * from amazon_s3_ec2;
select * from s3_files;


select s3_bucket, file_name from s3_files where s3_bucket='nimesaassignmentbucket1' and file_name like '%New folder%'