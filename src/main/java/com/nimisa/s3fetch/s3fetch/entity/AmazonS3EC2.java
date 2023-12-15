package com.nimisa.s3fetch.s3fetch.entity;

import jakarta.persistence.*;

import java.lang.annotation.Target;
import java.util.List;

@Entity
@Table(name="amazon_s3_ec2", uniqueConstraints = {@UniqueConstraint(columnNames = "s3_bucket", name = "uc_s3_bucket")})
public class AmazonS3EC2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="job_id")
    private int jobId;


    @Column(name="ec2_instance")
    private String ec2_instance;

    @Column(name="s3_fetch_status")
    private String s3FetchStatus;

    @Column(name="ec2_fetch_status")
    private String ec2FetchStatus;

    @OneToMany(mappedBy = "amazonS3EC2", cascade = CascadeType.ALL)
    private List<S3Files> s3Files;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getEc2_instance() {
        return ec2_instance;
    }

    public void setEc2_instance(String ec2_instance) {
        this.ec2_instance = ec2_instance;
    }

    public String getS3FetchStatus() {
        return s3FetchStatus;
    }

    public void setS3FetchStatus(String s3FetchStatus) {
        this.s3FetchStatus = s3FetchStatus;
    }

    public String getEc2FetchStatus() {
        return ec2FetchStatus;
    }

    public void setEc2FetchStatus(String ec2FetchStatus) {
        this.ec2FetchStatus = ec2FetchStatus;
    }

    public List<S3Files> getS3Files() {
        return s3Files;
    }

    public void setS3Files(List<S3Files> s3Files) {
        this.s3Files = s3Files;
    }
}
