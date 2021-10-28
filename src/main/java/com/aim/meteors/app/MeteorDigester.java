package com.aim.meteors.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aim.meteors.models.MeteorFall;
import com.aim.meteors.services.AwsS3Service;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class MeteorDigester
{
  // ======================================================================
  // Fields
  // ======================================================================
  /*----- configuration state -----*/
  private static String bucketName;
  private static String AwsAccesskey;
  private static String AwsSecretkey;

  /*----- working state -----*/
  private AwsS3Service awsService = new AwsS3Service();
  private ObjectMapper mapper = new ObjectMapper();
  
  // ======================================================================
  // Constructors and Initializers
  // ======================================================================
  public MeteorDigester() {}

  public MeteorDigester(AwsS3Service service) {
    this.awsService = service;
  }

  // ======================================================================
  // Methods
  // ======================================================================
  public static void main(String[] args) throws IOException {
    // parse bucket and keys
    int nargs = args.length;
    if(nargs < 3)
      usage();
    
    int i = 0;
    bucketName = args[i++];
    AwsAccesskey = args[i++];
    AwsSecretkey = args[i++];

    // create credentials
    AWSCredentials credentials = new BasicAWSCredentials(AwsAccesskey, AwsSecretkey);

    // instantiate and run the app
    // create a client
    AmazonS3 client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.US_WEST_2)
        .build();

    new MeteorDigester(new AwsS3Service(client)).run();
  }

  private static void usage() {
    System.out.println("MeteorDigester <region> <bucket> <accessKey> <secretKey>");
    System.exit(1);
  }

  /*
   * Executes the application.
   */
  private void run() throws IOException {
    List<MeteorFall> falls = getFalls();
    System.out.println(falls.size());

  }
  
  private List<MeteorFall> getFalls() throws IOException {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    String filepath;
    List<MeteorFall> allFalls = new ArrayList();
    ObjectListing objectListing = awsService.listObjects(bucketName);
    for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
      filepath = os.getKey();
      S3Object file = awsService.getObject(bucketName, filepath);
      S3ObjectInputStream is = file.getObjectContent();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        int lineNumber = 0;
        while(true) {
          String line = reader.readLine();
          if(line == null)
            break;
          line = line.trim();
          lineNumber++;
          System.out.format("%d: %s\n", lineNumber, line);
        }
      } catch(JsonParseException ex1) {
        System.err.format(
            "ignoring record at location %s in %s due to %s\n", 
            ex1.getLocation(),
            filepath,
            ex1.getMessage());
        System.err.println(ex1.getMessage());
        System.err.println();
      }

      List<MeteorFall> inFileFalls = new ArrayList();
      while(itr.hasNext()) {
        JsonNode node = itr.next();
        String jsonString = node.toString();
        try {
          MeteorFall fall = mapper.readValue(jsonString, MeteorFall.class);
          inFileFalls.add(fall);
        } catch(InvalidFormatException ex) {
          System.err.format(
              "ignoring record %d in %s due to %s\n", 
              inFileFalls.size(), 
              filepath,
              ex.getMessage());
          System.err.println();
        }
      }
      
      allFalls.addAll(inFileFalls);
    }
    return allFalls;
  }

}
//// copying an object
// awsService.copyObject("baeldung-bucket", "picture/pic.png", "baeldung-bucket2",
// "Document/picture.png");
//
//// deleting an object
// awsService.deleteObject(bucketName, "Document/hello.txt");
//
//// deleting multiple objects
// String objkeyArr[] = {"Document/hello2.txt", "Document/picture.png"};
//
// DeleteObjectsRequest delObjReq = new DeleteObjectsRequest("baeldung-bucket")
// .withKeys(objkeyArr);
// awsService.deleteObjects(delObjReq);

//private List<MeteorFall> getFalls() throws IOException {
//  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//  String filepath;
//  List<MeteorFall> allFalls = new ArrayList();
//  ObjectListing objectListing = awsService.listObjects(bucketName);
//  for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
//    filepath = os.getKey();
//    S3Object file = awsService.getObject(bucketName, filepath);
//    S3ObjectInputStream is = file.getObjectContent();
//    JsonNode tree = null;
//    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
//      
//      tree = mapper.readTree(reader);
//    } catch(JsonParseException ex1) {
//      System.err.format(
//          "ignoring record at location %s in %s due to %s\n", 
//          ex1.getLocation(),
//          filepath,
//          ex1.getMessage());
//      System.err.println(ex1.getMessage());
//      System.err.println();
//    }
//
//    List<MeteorFall> inFileFalls = new ArrayList();
//
//    Iterator<JsonNode> itr = tree.elements();
//    while(itr.hasNext()) {
//      JsonNode node = itr.next();
//      String jsonString = node.toString();
//      try {
//        MeteorFall fall = mapper.readValue(jsonString, MeteorFall.class);
//        inFileFalls.add(fall);
//      } catch(InvalidFormatException ex) {
//        System.err.format(
//            "ignoring record %d in %s due to %s\n", 
//            inFileFalls.size(), 
//            filepath,
//            ex.getMessage());
//        System.err.println();
//      }
//    }
//    
//    allFalls.addAll(inFileFalls);
////    int lineNumber = 0;
////    while(true) {
////      String line = reader.readLine();
////      if(line == null)
////        break;
////      line = line.trim();
////      lineNumber++;
////      System.out.format("%d: %s\n", lineNumber, line);
////    }
//  }
//  return allFalls;
//}


