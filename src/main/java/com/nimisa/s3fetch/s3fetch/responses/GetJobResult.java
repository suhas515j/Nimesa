package com.nimisa.s3fetch.s3fetch.responses;

import lombok.Data;

@Data
public class GetJobResult {

    String S3Status;
    String EC2Status;

}
