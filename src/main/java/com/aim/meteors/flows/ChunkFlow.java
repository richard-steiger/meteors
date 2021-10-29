package com.aim.meteors.flows;

import java.util.Iterator;

import com.aim.meteors.data.Bucket;
import com.aim.meteors.data.Chunk;
import com.aim.meteors.data.FallEventChunk;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ChunkFlow<T> extends Flow<Chunk<T>>
{
  // ======================================================================
  // Fields
  // ======================================================================
  private Bucket bucket;
  private ObjectListing objectListing;
  private Iterator<S3ObjectSummary> summaryIter;

  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public ChunkFlow(Bucket bucket) {
    this.bucket = bucket;
    objectListing = bucket.objectListing();
    summaryIter = objectListing.getObjectSummaries().iterator();
  }

  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Flow Operations
  // ---------------------------
  @Override
  protected void findNext() {
    if(summaryIter.hasNext()) {
      S3ObjectSummary os = summaryIter.next();
      
      // smells funky, FIX
      next = (Chunk<T>) new FallEventChunk(bucket, os.getKey());
      
      System.out.format("yielding %s\n", next);
    }
  }

}

