package com.nimisa.s3fetch.s3fetch.entity;

import jakarta.persistence.*;

@Entity
@Table(name="s3_files")
public class S3Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private AmazonS3EC2 amazonS3EC2;

    @Column(name = "s3_bucket", nullable = false)
    private String s3Bucket;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public AmazonS3EC2 getAmazonS3EC2() {
        return amazonS3EC2;
    }

    public void setAmazonS3EC2(AmazonS3EC2 amazonS3EC2) {
        this.amazonS3EC2 = amazonS3EC2;
    }
}
