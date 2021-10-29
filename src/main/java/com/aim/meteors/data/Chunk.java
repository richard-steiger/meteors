package com.aim.meteors.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Supplier;

import com.aim.meteors.flows.Flow;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public abstract class Chunk<T> 
  implements Supplier<Flow<T>>
{
  // ======================================================================
  // Fields
  // ======================================================================
  protected Bucket bucket;
  protected String path;
  protected S3Object object;
  
  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public Chunk(Bucket bucket, String path) {
    this.bucket = bucket;
    this.path = path;
    object = bucket.s3.getObject(bucket.bucketName, path);
  }
  
  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Entry Point
  // ---------------------------
  /**
   * Returns this chunk's path in its bucket.
   */
  public String path() {
    return path;
  }
  
  /**
   * Returns a flow over this chunk's elements.
   */
  @Override
  public abstract Flow<T> get();
  
  public BufferedReader reader() {
    return new BufferedReader(new InputStreamReader(object.getObjectContent()));
  }
  
  public String toString() {
    return String.format("Chunk(%s)", path);
  }
  
}


