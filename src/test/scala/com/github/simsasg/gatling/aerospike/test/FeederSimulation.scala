package com.github.simsasg.gatling.aerospike.test

import com.aerospike.client.async.AsyncClientPolicy
import com.aerospike.client.{Host, Key}
import com.github.simsasg.gatling.aerospike.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration._

class FeederSimulation extends Simulation {

  val aerospikeConfig = aerospike
    .hosts(new Host("localhost", 3000))
    .clientPolicy(new AsyncClientPolicy())

  val scn = scenario("Aerospike Test")
    .feed(csv("test.csv").circular)
    .exec(aerospike("request").get(new Key("test", "test", "${foo}")))

  setUp(
    scn.inject(constantUsersPerSec(10) during (90 seconds)))
    .protocols(aerospikeConfig)
}
