package com.aim.meteors.services;

import java.io.File;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public class AwsS3Service
{
  // ======================================================================
  // Fields
  // ======================================================================
  private AmazonS3 s3client;

  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public AwsS3Service() {
    this(AmazonS3ClientBuilder.defaultClient());
  }

  public AwsS3Service(AmazonS3 s3client) {
    this.s3client = s3client;
  }

  public AwsS3Service(AWSCredentials credentials) {
    this(AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.US_WEST_2).build());
  }
  
  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Bucket Access
  // ---------------------------
  // is bucket exist?
  public boolean doesBucketExist(String bucketName) {
    return s3client.doesBucketExistV2(bucketName);
  }

  // create a bucket
  public Bucket createBucket(String bucketName) {
    return s3client.createBucket(bucketName);
  }

  // list all buckets
  public List<Bucket> listBuckets() {
    return s3client.listBuckets();
  }

  // delete a bucket
  public void deleteBucket(String bucketName) {
    s3client.deleteBucket(bucketName);
  }

  // ---------------------------
  // Object Access
  // ---------------------------
  /**
   * Uploads {@link file} to the bucket named {@link bucketName}, registered 
   * under {@link key}, returning a {@link PutObjectResult}.
   */
  public PutObjectResult putObject(String bucketName, String key, File file) {
    return s3client.putObject(bucketName, key, file);
  }

  // listing objects
  public ObjectListing listObjects(String bucketName) {
    return s3client.listObjects(bucketName);
  }

  // get an object
  public S3Object getObject(String bucketName, String objectKey) {
    return s3client.getObject(bucketName, objectKey);
  }

  // copying an object
  public CopyObjectResult copyObject(String sourceBucketName, String sourceKey,
      String destinationBucketName, String destinationKey) {
    return s3client.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
  }

  // deleting an object
  public void deleteObject(String bucketName, String objectKey) {
    s3client.deleteObject(bucketName, objectKey);
  }

  // deleting multiple Objects
  public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
    return s3client.deleteObjects(delObjReq);
  }
}
