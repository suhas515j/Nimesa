package com.nimisa.s3fetch.s3fetch.controller;

import com.nimisa.s3fetch.s3fetch.entity.AmazonS3EC2;
import com.nimisa.s3fetch.s3fetch.responses.GetDiscoveryResults;
import com.nimisa.s3fetch.s3fetch.responses.GetJobResult;
import com.nimisa.s3fetch.s3fetch.responses.GetS3BucketObjectlikeResponse;
import com.nimisa.s3fetch.s3fetch.service.GetS3InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping
public class GetS3InfoController {


    @Autowired
    private GetS3InfoService getS3InfoService;

    @PostMapping("/discover")
    public ResponseEntity<Integer> discoverServices(@RequestBody List<String> services) throws ExecutionException, InterruptedException {
        // Implement logic to asynchronously discover EC2 instances and S3 buckets
        AmazonS3EC2 amazonS3EC2 = new AmazonS3EC2();
        discoverInAsync(amazonS3EC2);
        System.out.println("working here ");

        // Use discoveryService to trigger the discovery process asynchronously
        getS3InfoService.saveOrUpdateJob(amazonS3EC2);
        // Return JobId
        return ResponseEntity.ok(amazonS3EC2.getJobId());
    }


    @GetMapping("/get_result/{id}")
    public ResponseEntity<GetJobResult> getJobResult(@PathVariable int id) {
        return ResponseEntity.ok(getS3InfoService.getAmazonS3ById(id));
    }

    @GetMapping("/get_discovery_results")
    public ResponseEntity<GetDiscoveryResults> getDiscoveryResult() {
        return ResponseEntity.ok(getS3InfoService.getDiscoveryResults());
    }

    @GetMapping("/get_s3_bucket_object/{bucketName}")
    public ResponseEntity<List<String>> getS3BucketObjects(@PathVariable String bucketName) {
        return ResponseEntity.ok(getS3InfoService.getS3BucketObject(bucketName));
    }

    @GetMapping("/get_s3_bucket_object_count/{bucketName}")
    public ResponseEntity<Integer> getS3BucketObjectCount(@PathVariable String bucketName) {
        return ResponseEntity.ok(getS3InfoService.getS3BucketObjectCount(bucketName));
    }

    @GetMapping("/get_s3_bucket_object_like")
    public ResponseEntity<GetS3BucketObjectlikeResponse> getS3BucketObjectsLike(@RequestParam String bucketName, @RequestParam String pattern) {
        return ResponseEntity.ok(getS3InfoService.getS3BucketObjectLike(bucketName, pattern));
    }


    private void discoverInAsync(AmazonS3EC2 amazonS3EC2) {
        amazonS3EC2.setS3FetchStatus("PENDING");
        amazonS3EC2.setEc2FetchStatus("PENDING");
        CompletableFuture<String> futureResultForS3 = getS3InfoService.discoverS3Buckets(amazonS3EC2);
        CompletableFuture<String> futureResultForEC2= getS3InfoService.discoverEC2Instances(amazonS3EC2);
    }
}
