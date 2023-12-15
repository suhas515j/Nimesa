package com.nimisa.s3fetch.s3fetch.dao;

import com.nimisa.s3fetch.s3fetch.entity.AmazonS3EC2;
import com.nimisa.s3fetch.s3fetch.entity.S3Files;
import com.nimisa.s3fetch.s3fetch.responses.GetS3BucketObjectlikeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface S3FilesRepository extends JpaRepository<S3Files, Integer> {
    List<S3Files> findS3FilesByAmazonS3EC2(AmazonS3EC2 amazonS3EC2);
    Integer countByS3Bucket(String s3Bucket);
    List<S3Files> findByS3Bucket(String s3Bucket);
    @Query("SELECT s FROM S3Files s WHERE s.s3Bucket = :bucketName AND s.fileName LIKE CONCAT('%', :pattern, '%')")
    List<S3Files> getS3BucketObjectLike(@Param("bucketName") String bucketName, @Param("pattern") String pattern);
}
