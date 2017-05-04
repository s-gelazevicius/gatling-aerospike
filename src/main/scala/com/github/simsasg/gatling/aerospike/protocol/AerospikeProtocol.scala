package com.github.simsasg.gatling.aerospike.protocol

import akka.actor.ActorSystem
import com.aerospike.client.Host
import com.aerospike.client.async.AsyncClientPolicy
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{Protocol, ProtocolKey}

object AerospikeProtocol {

  def apply(configuration: GatlingConfiguration): AerospikeProtocol = AerospikeProtocol(
    hosts = Seq.empty,
    clientPolicy = null,
    asyncClient = false
  )

  val AerospikeProtocolKey = new ProtocolKey {

    type Protocol = AerospikeProtocol
    type Components = AerospikeComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] =
      classOf[AerospikeProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): AerospikeProtocol =
      AerospikeProtocol(configuration)

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): AerospikeProtocol => AerospikeComponents = {
      aerospikeProtocol => {
        val aerospikeComponents = AerospikeComponents(
          aerospikeProtocol
        )
        aerospikeComponents
      }
    }
  }
}

case class AerospikeProtocol(
  hosts: Seq[Host],
  clientPolicy: AsyncClientPolicy,
  asyncClient: Boolean) extends Protocol {

  def hosts(hosts: Host*): AerospikeProtocol = copy(hosts = hosts)
  def clientPolicy(clientPolicy: AsyncClientPolicy): AerospikeProtocol = copy(clientPolicy = clientPolicy)
  def asyncClient(asyncClient: Boolean): AerospikeProtocol = copy(asyncClient = asyncClient)

}
