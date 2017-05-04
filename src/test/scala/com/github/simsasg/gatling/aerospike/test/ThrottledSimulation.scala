package com.github.simsasg.gatling.aerospike.test

import com.aerospike.client.async.AsyncClientPolicy
import com.aerospike.client.{Host, Key}
import com.github.simsasg.gatling.aerospike.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration._

class ThrottledSimulation extends Simulation {

  val aerospikeConfig = aerospike
    .hosts(new Host("localhost", 3000))
    .clientPolicy(new AsyncClientPolicy())

  val key = new Key("test", "test", 123456789)
  val scn = scenario("Aerospike Test")
    .forever(
      exec(aerospike("GET request").get(key))
    )

  setUp(
    scn.inject(atOnceUsers(10)))
    .throttle(jumpToRps(10), holdFor(30 seconds))
    .protocols(aerospikeConfig)
}
