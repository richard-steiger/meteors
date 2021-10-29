package com.aim.meteors.data;

import java.util.function.Supplier;

import com.aim.meteors.flows.ChunkFlow;
import com.aim.meteors.services.AwsS3Service;
import com.amazonaws.services.s3.model.ObjectListing;

public class Bucket
  implements Supplier<ChunkFlow>
{
  // ======================================================================
  // Fields
  // ======================================================================
  /* Package-scoped for simplicity. */
  AwsS3Service s3;
  String bucketName;
  
  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public Bucket(AwsS3Service s3, String bucketName) {
    this.s3 = s3;
    this.bucketName = bucketName; 
  }
  
  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Entry Point
  // ---------------------------
  public ChunkFlow get() {
    return new ChunkFlow(this);
  }
  
  public ObjectListing objectListing() {
    return s3.listObjects(bucketName);
  }
  
}

