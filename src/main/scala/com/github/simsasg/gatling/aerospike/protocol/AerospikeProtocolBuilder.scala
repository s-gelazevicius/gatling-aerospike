package com.github.simsasg.gatling.aerospike.protocol

import io.gatling.core.config.GatlingConfiguration


object AerospikeProtocolBuilder {

  implicit def toAerospikeProtocol(builder: AerospikeProtocolBuilder): AerospikeProtocol = builder.build

  def apply(configuration: GatlingConfiguration) : AerospikeProtocolBuilder =
    AerospikeProtocolBuilder(AerospikeProtocol(configuration))

}

case class AerospikeProtocolBuilder(aerospikeProtocol: AerospikeProtocol) {
  def build = aerospikeProtocol
}