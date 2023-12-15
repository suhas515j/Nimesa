package com.nimisa.s3fetch.s3fetch.responses;

import lombok.Data;

import java.util.List;

@Data
public class GetDiscoveryResults {

    List<String> ec2Instances;
    List<String> s3BucketNames;
}
