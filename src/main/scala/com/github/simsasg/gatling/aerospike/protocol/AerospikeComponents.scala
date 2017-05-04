package com.github.simsasg.gatling.aerospike.protocol

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session


case class AerospikeComponents(aerospikeProtocol: AerospikeProtocol) extends ProtocolComponents {

  override def onStart: Option[(Session) => Session] = None
  override def onExit: Option[(Session) => Unit] = None

}
