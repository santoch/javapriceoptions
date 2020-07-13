package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.util.Constants;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BjerksundStenslandTest {

	static final BjerksundStensland s_BjerksundStensland = new BjerksundStensland();

	@Test
	public void testBjerksundStenslandCallImpVol() {
		double price = 20.29616;
		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double bjiv = s_BjerksundStensland.impliedVolatility("C", price, underlyingPrice, strikePrice,
				interestRate, timeRemaining, 0.5, dividendYield);
		System.out.println("testBjerksundStenslandImpVol1 bjiv=" + bjiv);
		assertEquals(0.20d, bjiv, Constants.IV_PRECISION);
	}

	@Test
	public void testBjerksundStenslandPutImpVol() {
		double price = 22.03875264497185d;
		double underlyingPrice = 1177.62d;
		double strikePrice = 1165.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.03d;
		double bjiv = s_BjerksundStensland.impliedVolatility("P", price, underlyingPrice, strikePrice,
				interestRate, timeRemaining, 0.5, dividendYield);
		System.out.println("testBjerksundStenslandImpVol1 bjiv=" + bjiv);
		assertEquals(0.20d, bjiv, Constants.IV_PRECISION);
	}

	@Test
	public void testBjerksundStenslandCall1() {
		// Result       /  Online calculator
		// ---------------------------------------------
		// 19.0638      / http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
		// 20.422384    / http://janroman.dhis.org/calc/BjerksundStensland.php
		// 19.082612    / (excel spreadsheet)

		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double volatility = 0.20d;
		double interestRate = 0.0135d;
		double dividendYield = 0.03d;
		double bsprice = s_BjerksundStensland.priceOption("C", underlyingPrice, strikePrice, timeRemaining,
				volatility, interestRate, dividendYield);
		System.out.println("testBjerksundStenslandCall1 bsprice=" + bsprice);
		assertEquals(19.082618995152643d, bsprice, 0.00000000000d);
	}

	@Test
	public void testBjerksundStenslandPut1() {
		// Result       /  Online calculator
		// ---------------------------------------------
		// 22.0534      / http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
		// 20.702770    / http://janroman.dhis.org/calc/BjerksundStensland.php
		// 22.0387792   / (excel spreadsheet)

		double underlyingPrice = 1177.62d;
		double strikePrice = 1165.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double volatility = 0.20d;
		double intererstRate = 0.0135d;
		double dividendYield = 0.03d;
		double bsprice = s_BjerksundStensland.priceOption("P", underlyingPrice, strikePrice, timeRemaining,
				volatility, intererstRate, dividendYield);
		System.out.println("testBjerksundStenslandPut1 bsprice=" + bsprice);
		assertEquals(22.03875264497185d, bsprice, 0.00000000000d);
	}

	@Test
	public void testBjerksundStenslandCallGreeks() {
		// ??? did not find an exact equivalent for testing,
		// but assumed to be pretty close to the bs Greeks

		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double volatility = 0.20d;
		String type = "C";
		double delta = s_BjerksundStensland.delta(type, underlyingPrice, strikePrice, volatility, timeRemaining,
				interestRate, dividendYield);
		double gamma = s_BjerksundStensland.gamma(underlyingPrice, strikePrice, volatility, timeRemaining,
				interestRate, dividendYield);
		double vega = s_BjerksundStensland.vega(type, underlyingPrice, strikePrice, volatility, timeRemaining,
				interestRate, dividendYield);
		double theta = s_BjerksundStensland.theta(type, underlyingPrice, strikePrice, volatility, timeRemaining,
				interestRate, dividendYield);
		double rho = s_BjerksundStensland.rho(type, underlyingPrice, strikePrice, volatility, timeRemaining,
				interestRate, dividendYield);
		System.out.println("testBjerksundStenslandCallGreeks"
						+ " delta=" + delta
						+ ", gamma=" + gamma
						+ ", vega=" + vega
						+ ", theta=" + theta
						+ ", rho=" + rho
		);
	}
}
