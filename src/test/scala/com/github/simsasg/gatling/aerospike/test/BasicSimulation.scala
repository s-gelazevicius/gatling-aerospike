package com.github.simsasg.gatling.aerospike.test

import com.aerospike.client.async.AsyncClientPolicy
import com.aerospike.client.{Host, Key, Record}
import com.github.simsasg.gatling.aerospike.Predef._
import io.gatling.core.Predef._

import scala.collection.JavaConversions._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val aerospikeConfig = aerospike
    .hosts(new Host("localhost", 3000))
    .clientPolicy(new AsyncClientPolicy())
    .asyncClient(true)

  val key = new Key("test", "test", 123456789)
  val record = new Record(Map("dataBin" -> "thisIsData".getBytes), 0, 1)

  val scn = scenario("Aerospike Test")
    .exec(aerospike("PUT request").put(key, record))
    .exec(aerospike("GET request").get(key))

  setUp(
    scn.inject(constantUsersPerSec(10) during (90 seconds))).protocols(aerospikeConfig)
}
