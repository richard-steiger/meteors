package com.aim.meteors.app;

import java.io.IOException;

import com.aim.meteors.analytics.FallStatistics;
import com.aim.meteors.data.Bucket;
import com.aim.meteors.flows.FallEventFlow;
import com.aim.meteors.services.AwsS3Service;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class MeteorFallReporter
{
  // ======================================================================
  // Fields
  // ======================================================================
  /*----- configuration state -----*/
  private static String bucketName;
  private static String AwsAccesskey;
  private static String AwsSecretkey;

  // ======================================================================
  // Methods
  // ======================================================================
  // ---------------------------
  // Entry Point
  // ---------------------------
  public static void main(String[] args) throws IOException {
    parseArgs(args);

    // instantiate and run the app
    run();
  }

  private static void parseArgs(String[] args) {
    int nargs = args.length;
    if(nargs < 3)
      usage();

    int i = 0;
    bucketName = args[i++];
    AwsAccesskey = args[i++];
    AwsSecretkey = args[i++];
  }

  private static void usage() {
    System.out.println("MeteorDigester <region> <bucket> <accessKey> <secretKey>");
    System.exit(1);
  }

  // ---------------------------
  // Lifecycle Management
  // ---------------------------
  /**
   * Executes the application, building the pipeline from the designated 
   * bucket in S3, through progressively finer flows, culminating in 
   * instantiating a {@link FallStatistics} object, which is then printed
   * to the console.
   */
  public static void run() throws IOException {
    AWSCredentials credentials = new BasicAWSCredentials(AwsAccesskey, AwsSecretkey);
    AwsS3Service service = new AwsS3Service(credentials);
    Bucket bucket = new Bucket(service, bucketName);
    FallEventFlow events = new FallEventFlow(bucket);
    FallStatistics stats = new FallStatistics(events);
    System.out.format("Fall Event statistics: %s\n", stats);
  }

}
