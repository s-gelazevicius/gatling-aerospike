package com.github.simsasg.gatling.aerospike.request.builder

import com.aerospike.client.{Key, Record}
import com.github.simsasg.gatling.aerospike.action.AerospikeRequestActionBuilder
import io.gatling.core.session._

sealed trait Operation
case object GetOperation extends Operation
case object PutOperation extends Operation

case class AerospikeRequest(
  operation: Operation,
  requestName: Expression[String],
  key: Expression[Key],
  record: Option[Expression[Record]] = None)

case class AerospikeRequestBuilder(requestName: Expression[String]) {

  def put(key: Expression[Key], record: Expression[Record]): AerospikeRequestActionBuilder =
    new AerospikeRequestActionBuilder(AerospikeRequest(PutOperation, requestName, key, Some(record)))

  def get(key: Expression[Key]): AerospikeRequestActionBuilder =
    new AerospikeRequestActionBuilder(AerospikeRequest(GetOperation, requestName, key))

}
