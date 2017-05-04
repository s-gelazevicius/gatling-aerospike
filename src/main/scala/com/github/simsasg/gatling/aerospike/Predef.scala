package com.github.simsasg.gatling.aerospike

import com.github.simsasg.gatling.aerospike.protocol.AerospikeProtocolBuilder
import com.github.simsasg.gatling.aerospike.request.builder.AerospikeRequestBuilder
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session.Expression


object Predef {

  def aerospike(implicit configuration: GatlingConfiguration) = AerospikeProtocolBuilder(configuration)
  def aerospike(requestName: Expression[String]) = new AerospikeRequestBuilder(requestName)

}