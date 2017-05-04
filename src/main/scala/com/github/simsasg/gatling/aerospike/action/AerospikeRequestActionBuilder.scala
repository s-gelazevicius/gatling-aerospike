package com.github.simsasg.gatling.aerospike.action

import com.aerospike.client.async.AsyncClient
import com.github.simsasg.gatling.aerospike.protocol.AerospikeProtocol
import com.github.simsasg.gatling.aerospike.request.builder.AerospikeRequest
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.structure.ScenarioContext

class AerospikeRequestActionBuilder(aerospikeAttributes: AerospikeRequest) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx.{coreComponents, protocolComponentsRegistry, system, throttled}
    val aerospikeComponents = protocolComponentsRegistry.components(AerospikeProtocol.AerospikeProtocolKey)
    val config = aerospikeComponents.aerospikeProtocol
    val client = new AsyncClient(config.clientPolicy, config.hosts: _*)
    system.registerOnTermination(client.close())

    new AerospikeRequestAction(
      client,
      aerospikeAttributes,
      coreComponents,
      aerospikeComponents.aerospikeProtocol,
      throttled,
      next
    )

  }

}