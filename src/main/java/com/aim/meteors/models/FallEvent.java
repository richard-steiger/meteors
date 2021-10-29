package com.aim.meteors.models;

import java.util.Date;

//@JsonDeserialize(using = FallEventDeserializer.class)
public class FallEvent
{
  public String name;
  public int id;
  public String nametype;
  public String recclass;
  public float mass;
  public String fall;
  public Date year;
  public float reclat;
  public float reclong;
  public Geolocation geolocation;

  public FallEvent() {}
  
}


