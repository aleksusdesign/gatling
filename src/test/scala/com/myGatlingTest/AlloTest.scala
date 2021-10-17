package com.myGatlingTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.concurrent.duration.DurationInt

class AlloTest extends Simulation {

	val httpProtocol = http
		.baseUrl("https://allo.ua/")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val scn = scenario("Allo")
			.exec(http("LoadHomePage")
				.get("ua/xiaomi-store/"))
				.pause(5)
			.exec(http("PriceRange")
				.get("ua/products/mobile/price_from-6360/price_to-19533/proizvoditel-xiaomi/"))
		  	.pause(10)
			.exec(http("PriceRangeSort")
				.get("ua/products/mobile/dir-asc/order-name/price_from-6360/price_to-24999/proizvoditel-xiaomi/"))
		  	.pause(10)

	setUp(
		scn.inject(
			nothingFor(1 seconds),
			atOnceUsers(1),
			rampUsers(2) during (4 seconds),
			constantUsersPerSec(20) during (10 seconds),
			heavisideUsers(10) during (5 seconds)
		).protocols(httpProtocol)
	)

}