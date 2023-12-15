package com.nimisa.s3fetch.s3fetch.dao;

import com.nimisa.s3fetch.s3fetch.entity.AmazonS3EC2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmazonS3EC2Repository extends JpaRepository<AmazonS3EC2, Integer> {
}
