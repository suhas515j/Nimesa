package com.nimisa.s3fetch.s3fetch.responses;

import lombok.Data;

import java.util.Map;

@Data
public class GetS3BucketObjectlikeResponse {

    private Map<String,String> s3BucketToFileNameMap;
}
