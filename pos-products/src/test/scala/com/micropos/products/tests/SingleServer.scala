package com.micropos.products.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class SingleServer extends Simulation {
  private val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  private val scn = scenario("Testing Single Server")
    .exec(http("product_request").get("/api/product"))
    .exec(http("cart_request").get("/api/cart"))
    .exec(http("cart_add").post("/api/cart/1"))
    .exec(http("checkout").post("/api/cart"))

  setUp(scn.inject(atOnceUsers(1000)).protocols(httpProtocol))
}
