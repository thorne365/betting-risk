package dk.betex.risk.hedge

import org.junit._
import Assert._
import dk.betex._
import dk.betex.api._
import dk.betex.api.IBet.BetTypeEnum._
import dk.betex.api.IBet.BetStatusEnum._
import org.junit._
import HedgeBetsCalculator._
import dk.betex.risk.prob._
import dk.betex.risk.liability._
import org.apache.commons.math.util.MathUtils
import scala.collection._
import HedgeUtil._

class TwoRunnersMarketWithLayBetTest {

  private val bets = new Bet(1, 123, 10, 2, LAY, M, 1, 11, 1000, Option(2000)) :: Nil
  private val marketPrices = Map(11l -> Tuple2(1.5, 1.51), 12l -> Tuple2(2.99, 3.0))

  @Before
  def before {
    val epBefore = getExpectedProfit(bets, marketPrices)
    assertEquals(-3.315, epBefore.marketExpectedProfit, 0.001)
    assertEquals(Map(11l -> -10d, 12l -> 10d), epBefore.runnersIfWin)
  }

  @Test
  def hedge_runner11 {

    val hedgeBet = HedgeBetsCalculator.calculateHedgeBet(bets, marketPrices, 11).get
    assertEquals(HedgeBet(13.33, 1.5, BACK, 1, 11), round(hedgeBet))

    val epAfter = getExpectedProfit(hedgeBet, bets, marketPrices)
    assertEquals(-3.333, epAfter.marketExpectedProfit, 0.001)
    assertEquals(Map(11l -> -3.33d, 12l -> -3.33d), round(epAfter.runnersIfWin))
  }

  @Test
  def hedge_runner12 {

    val hedgeBet = HedgeBetsCalculator.calculateHedgeBet(bets, marketPrices, 12).get
    assertEquals(HedgeBet(6.67, 3, LAY, 1, 12), round(hedgeBet))

    val epAfter = getExpectedProfit(hedgeBet, bets, marketPrices)
    assertEquals(-3.333, epAfter.marketExpectedProfit, 0.001)
    assertEquals(Map(11l -> -3.33d, 12l -> -3.33d), round(epAfter.runnersIfWin))
  }

}