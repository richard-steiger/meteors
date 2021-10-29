package com.aim.meteors.analytics;

import com.aim.meteors.flows.FallEventFlow;

public class FallStatistics 
{
  // ======================================================================
  // Fields
  // ======================================================================
  /*----- flow state -----*/
  private FallEventFlow eventsIn;
  
  /*----- stats state -----*/
  /* The average mass of a meteor fall. */
  public double totalFallMass;

  /* The average mass of a meteor fall. */
  public double averageFallMass;

  /* The year of the highest number of meteors falls. */
  public int highestFallYear;
  
  public int count;

  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public FallStatistics(FallEventFlow eventsIn) {
    this.eventsIn = eventsIn;
    compute();
  }
  
  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Computation and Presentation
  // ---------------------------
  public void compute() {
    eventsIn.forEach(e -> {
      count++;
      totalFallMass += e.mass;
      if(e.year != null)
        highestFallYear = Math.max(highestFallYear, e.year.getYear());
    });
    averageFallMass = totalFallMass / count;
  }
  
  public String toString() {    
    return String.format(
        "averageFallMass: %f, highestFallYear: %d", 
        averageFallMass, highestFallYear);
  }

}


