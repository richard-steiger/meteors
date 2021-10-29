package com.expedia.meteors.app

import java.io.InputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.Iterator
import java.util.List
import com.expedia.meteors.models.MeteorFall
import com.expedia.meteors.services.AwsS3Service
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

object MeteorDigester extends App {
  // ======================================================================
  // Fields
  // ======================================================================
  /*----- configuration state -----*/
  var bucketName = "majorly-meteoric"
  var AwsAccesskey = ""
  var AwsSecretkey = ""

  /*----- working state -----*/
  var awsService = new AwsS3Service()
  var mapper = new ObjectMapper()

  def main(args: String[]): Unit = {

  }
  println("Hey, Sky!");


  def readBucketFiles(path: String) () {
    val s3Client: Nothing = new Nothing(new Nothing)
    val `object`: Nothing = s3Client.getObject(new Nothing(bucketName, key))
    val objectData: InputStream = `object`.getObjectContent
    // Process the objectData stream.
  }

}