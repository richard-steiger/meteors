package com.aim.meteors.data;

import com.aim.meteors.flows.FallEventFlow;
import com.aim.meteors.models.FallEvent;

public class FallEventChunk
  extends Chunk<FallEvent> 
{
  
  public FallEventChunk(Bucket bucket, String path) {
    super(bucket, path);
  }

  @Override
  public FallEventFlow get() {
//    return new FallEventFlow(bucket, path);
    return null;
  }
  
}


