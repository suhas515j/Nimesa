package com.nimisa.s3fetch.s3fetch.service;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.nimisa.s3fetch.s3fetch.dao.AmazonS3EC2Repository;
import com.nimisa.s3fetch.s3fetch.dao.S3FilesRepository;
import com.nimisa.s3fetch.s3fetch.entity.AmazonS3EC2;
import com.nimisa.s3fetch.s3fetch.entity.S3Files;
import com.nimisa.s3fetch.s3fetch.responses.GetDiscoveryResults;
import com.nimisa.s3fetch.s3fetch.responses.GetJobResult;
import com.nimisa.s3fetch.s3fetch.responses.GetS3BucketObjectlikeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class GetS3InfoService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonEC2 amazonEC2;

    @Autowired
    private AmazonS3EC2Repository amazonS3EC2Repository;

    @Autowired
    private S3FilesRepository s3FilesRepository;


    @Async
    public CompletableFuture<String> discoverEC2Instances(AmazonS3EC2 amazonS3EC2) {
        // Asynchronously discover EC2 instances in the Mumbai region
        // Perform necessary operations and persist results in the DB
        StringBuilder instancesList = new StringBuilder();

        // Describe instances in the us-east-1 region
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();

        // Get the response
        DescribeInstancesResult describeInstancesResult = amazonEC2.describeInstances(describeInstancesRequest);

        // Get the reservations
        for (Reservation reservation : describeInstancesResult.getReservations()) {
            // Get the instances
            for (Instance instance : reservation.getInstances()) {
                // Print the instance ID
                System.out.println(instance.getInstanceId());
                instancesList.append(":").append(instance.getInstanceId());
            }
        }
        amazonS3EC2.setEc2_instance(instancesList.toString());
        amazonS3EC2.setEc2FetchStatus("COMPLETED");
        saveOrUpdateJob(amazonS3EC2);
        return CompletableFuture.completedFuture("EC2 fetching completed");
    }

    @Async
    public CompletableFuture<String> discoverS3Buckets(AmazonS3EC2 amazonS3EC2) {
        // Asynchronously discover S3 buckets
            List<Bucket> buckets = amazonS3.listBuckets();
            System.out.println(buckets);
            for (Bucket bucket : buckets) {
                ObjectListing objectListing = null;
                try {
                    objectListing = amazonS3.listObjects(bucket.getName());
                } catch (AmazonS3Exception e) {
                    if (e.getErrorCode().equalsIgnoreCase("IllegalLocationConstraintException")) {
                        System.out.println("Not storing above bucket as not in mumbai region");
                        continue;
                    }
                    throw e;
                }

                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    S3Files s3Files = new S3Files();
                    s3Files.setAmazonS3EC2(amazonS3EC2);
                    s3Files.setS3Bucket(bucket.getName());
                    System.out.println("Object Key: " + objectSummary.getKey());
                    System.out.println("Size: " + objectSummary.getSize());
                    s3Files.setFileName(objectSummary.getKey());
                    s3Files.setFileSize(objectSummary.getSize());
                    saveOrUpdateS3Info(s3Files);
                }
            }
            System.out.println("Adding delay to simulate pending status");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Perform necessary operations and persist results in the DB
            amazonS3EC2.setS3FetchStatus("COMPLETED");
            saveOrUpdateJob(amazonS3EC2);
            return CompletableFuture.completedFuture("Task Result");
    }

    public AmazonS3EC2 saveOrUpdateJob(AmazonS3EC2 amazonS3EC2) {
        return amazonS3EC2Repository.save(amazonS3EC2);
    }

    public S3Files saveOrUpdateS3Info(S3Files s3Files) {
        return s3FilesRepository.save(s3Files);
    }

    public GetJobResult getAmazonS3ById(int jobId) {
        GetJobResult getJobResult = new GetJobResult();
        AmazonS3EC2 amazonS3EC2=  amazonS3EC2Repository.getById(jobId);
        getJobResult.setEC2Status(amazonS3EC2.getEc2FetchStatus());
        getJobResult.setS3Status(amazonS3EC2.getS3FetchStatus());
        return getJobResult;
    }

    public GetDiscoveryResults getDiscoveryResults() {
        GetDiscoveryResults getDiscoveryResults = new GetDiscoveryResults();
        List<AmazonS3EC2> amazonS3EC2List=  amazonS3EC2Repository.findAll();
        List<String> allEC2instances = new ArrayList<>();
        for(AmazonS3EC2 amazonS3EC2: amazonS3EC2List) {
            if(amazonS3EC2.getEc2_instance()!=null){
                allEC2instances.addAll(Arrays.asList(amazonS3EC2.getEc2_instance().split(":")));
            }
            getDiscoveryResults.setEc2Instances(allEC2instances);
            List<S3Files> s3Files = s3FilesRepository.findS3FilesByAmazonS3EC2(amazonS3EC2);
            Set<String> s3InstanceName = new HashSet<>();
            for(S3Files s3File : s3Files) {
                s3InstanceName.add(s3File.getS3Bucket());
            }
            getDiscoveryResults.setS3BucketNames(new ArrayList<>(s3InstanceName));

        }
        return getDiscoveryResults;
    }

    public Integer getS3BucketObjectCount(String bucketName) {
        return s3FilesRepository.countByS3Bucket(bucketName);
    }

    public List<String> getS3BucketObject(String bucketName) {
        return s3FilesRepository.findByS3Bucket(bucketName).stream().map(S3Files::getFileName).collect(Collectors.toList());
    }

    public GetS3BucketObjectlikeResponse getS3BucketObjectLike(String bucketName, String pattern) {
        GetS3BucketObjectlikeResponse getS3BucketObjectlikeResponse = new GetS3BucketObjectlikeResponse();
        Map<String, String> s3ListMap = new HashMap<>();
        List<S3Files> s3FilesList= s3FilesRepository.getS3BucketObjectLike(bucketName,pattern);
        for(S3Files s3File: s3FilesList) {
            s3ListMap.put(s3File.getFileName(),s3File.getS3Bucket());
        }
        getS3BucketObjectlikeResponse.setS3BucketToFileNameMap(s3ListMap);
        return getS3BucketObjectlikeResponse;
    }



}
