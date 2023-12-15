


API document for Nimesa assigment application:

Please go through the document with all scenarios working, And screenshots attached in screenshot folder of this repository. I did not get enough time to refractor this code and also to add custom Error exception, as i was busy in current company tasks and meeting.

1. Discover Services ALL;;

POST Endpoint: http://localhost:8010/discover

This is a POST request and takes list of services and discovers all ec2 instances as well as s3 buckets along with thier filenames and filesize
(currently irrespective of request body it fetches all, Just need to refractor and serperate out logic did not get time to do)

returns jobId

2. GetJobResult

GET endpoint: http://localhost:8010/get_result/{jobID}

this will return status of the jobId

3. GetDiscoveryResult

GET endpoint: http://localhost:8010/get_discovery_results

this will return all the discovered ec2 instances as well as s3 buckets in MUMBAI region

4. GetS3BucketObjects by bucket Name

GET endpoint: http://localhost:8010/get_s3_bucket_object/{s3_bucket_name}

This will return all the filenames from given s3Bucket name


5. GetS3BucketObjects by bucket Name count

GET endpoint: http://localhost:8010/get_s3_bucket_object_count/{s3_bucket_name}

This will return count of files from given s3Bucket name from DB

6. GetS3BucketObjectlike

Get endpoint: http://localhost:8010/get_s3_bucket_object_like

this takes two PARAM variables bucketName and pattern
based on the given params returns all the files matching the given pattern from given s3 bucketName

