package com.github.simsasg.gatling.aerospike.action

import com.aerospike.client._
import com.aerospike.client.async.AsyncClient
import com.aerospike.client.listener.{RecordListener, WriteListener}
import com.aerospike.client.policy.{Policy, WritePolicy}
import com.github.simsasg.gatling.aerospike.protocol.AerospikeProtocol
import com.github.simsasg.gatling.aerospike.request.builder.{AerospikeRequest, GetOperation, PutOperation}
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.ClockSingleton._
import io.gatling.commons.validation.Validation
import io.gatling.core.CoreComponents
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.session._

import scala.collection.JavaConversions._
import io.gatling.core.stats.message.ResponseTimings
import io.gatling.core.util.NameGen

import scala.util.{Failure, Success, Try}

class AerospikeRequestAction(
  val client: AsyncClient,
  val aerospikeAttributes: AerospikeRequest,
  val coreComponents: CoreComponents,
  val aerospikeProtocol: AerospikeProtocol,
  val throttled: Boolean,
  val next: Action)
  extends ExitableAction with NameGen {

  val statsEngine = coreComponents.statsEngine
  override val name = genName("aerospikeRequest")

  override def execute(session: Session): Unit = recover(session) {
    aerospikeAttributes requestName session flatMap { requestName =>

      val outcome = aerospikeAttributes.operation match {
        case GetOperation => getRequest(requestName, client, aerospikeAttributes, throttled, session)
        case PutOperation => putRequest(requestName, client, aerospikeAttributes, throttled, session)
      }

      outcome.onFailure(
        errorMessage => statsEngine.reportUnbuildableRequest(session, requestName, errorMessage)
      )

      outcome
    }
  }

  private def getRequest(
    requestName: String,
    client: AsyncClient,
    aerospikeAttributes: AerospikeRequest,
    throttled: Boolean,
    session: Session): Validation[Unit] = aerospikeAttributes key session map { key =>

    val requestStartDate = nowMillis
    if (aerospikeProtocol.asyncClient) {
      val listener = new RecordListener {
        override def onFailure(e: AerospikeException) = logResponse(requestName, session, requestStartDate, Some(e))
        override def onSuccess(key: Key, record: Record) = logResponse(requestName, session, requestStartDate, None)
      }
      client.get(new Policy(), listener, key)
    } else {
      Try { client.get(new Policy(), key) } match {
        case Success(_) => logResponse(requestName, session, requestStartDate, None)
        case Failure(e) => logResponse(requestName, session, requestStartDate, Some(e))
      }
    }

    if (throttled) {
      coreComponents.throttler.throttle(session.scenario, () => next ! session)
    } else {
      next ! session
    }

  }

  private def putRequest(
    requestName: String,
    client: AsyncClient,
    aerospikeAttributes: AerospikeRequest,
    throttled: Boolean,
    session: Session): Validation[Unit] = aerospikeAttributes key session map { key =>

    val record = aerospikeAttributes.record.get(session).get
    val bins = record.bins.map { case (name, value) => new Bin(name, value) }.toSeq

    val requestStartDate = nowMillis

    if (aerospikeProtocol.asyncClient) {
      val listener = new WriteListener {
        override def onFailure(e: AerospikeException) = logResponse(requestName, session, requestStartDate, Some(e))
        override def onSuccess(key: Key) = logResponse(requestName, session, requestStartDate, None)
      }
      client.put(new WritePolicy(), listener, key, bins: _*)
    } else {
      Try { client.put(new WritePolicy(), key, bins: _*) } match {
        case Success(_) => logResponse(requestName, session, requestStartDate, None)
        case Failure(e) => logResponse(requestName, session, requestStartDate, Some(e))
      }
    }

    if (throttled) {
      coreComponents.throttler.throttle(session.scenario, () => next ! session)
    } else {
      next ! session
    }
  }

  private def logResponse(
    requestName: String,
    session: Session,
    requestStartDate: Long,
    ex: Option[Throwable] = None) = {

    val requestEndDate = nowMillis
    val resultStatus = if (ex.isDefined) KO else OK

    statsEngine.logResponse(
      session, requestName,
      ResponseTimings(startTimestamp = requestStartDate, endTimestamp = requestEndDate),
      resultStatus, None, ex.map(_.getMessage))
  }


}
