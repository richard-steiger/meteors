package com.aim.meteors.flows;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Supplier;

import com.aim.meteors.data.FallEventChunk;
import com.aim.meteors.models.FallEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FallEventFlow extends Flow<FallEvent>
{
  // ======================================================================
  // Fields
  // ======================================================================
  /*----- constants -----*/
  private static final ObjectMapper mapper = new ObjectMapper();

  /*----- working state -----*/
  private ChunkFlow chunks;     // the flow of chunks in the bucket
  private FallEventChunk chunk; // the current chunk containing events
  private FallEvent[] events;   // chunk's events
  private int eventIndex;       // next event index

  private boolean trapping;
  
  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  static {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
  
  public FallEventFlow(Supplier<ChunkFlow> chunkSource) {
    chunks = chunkSource.get();
  }

  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Flow Operations
  // ---------------------------
  @Override
  protected void findNext() {
    int x = 0;  // trap breakpoint target
    // if either just starting, or off the end of the current chunk's events,
    // fetch the next chunk and it's events
    if((events == null || eventIndex >= events.length) && chunks.hasNext()) {
      chunk = (FallEventChunk) chunks.next();
      if(chunk.path().equals("data9000.json")) {
        trapping = true;
      }
      if(trapping)
        x = 1;
      try (BufferedReader reader = chunk.reader()) {
        events = mapper.readValue(reader, FallEvent[].class);
        if(events.length == 0)
          return;
        eventIndex = 0;
        System.out.format("fetched %s events from %s\n", events.length, chunk);
//      } catch(IOException ioe) {
//        ioe.printStackTrace();
      } catch(Exception ex1) {
        ex1.printStackTrace();
        System.err.println(ex1.getMessage());
        System.out.format("skipping over all events in %s\n", chunk);
        
        // skip past the current chunk
        findNext();
        return;
      }
    }
    next = events[eventIndex++];
  }

}
