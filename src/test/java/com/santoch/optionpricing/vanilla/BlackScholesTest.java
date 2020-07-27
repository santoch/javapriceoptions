package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.util.Constants;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BlackScholesTest {
	static final BlackScholes s_BlackScholes = new BlackScholes();

	@Test
	public void testBlackScholesCall1() {
		// Result       /  Online calculator
		// ---------------------------------------------
		// 20.037       / https://www.mystockoptions.com/black-scholes.cfm
		// 20.2961      / https://www.erieri.com/blackscholes
		// 20.2961667   / (excel spreadsheet)
		// 20.2961      / http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/

		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double volatility = 0.20d;
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double bsprice = s_BlackScholes.priceOption("C", underlyingPrice, strikePrice, timeRemaining,
				volatility, interestRate, dividendYield);
		System.out.println("testBlackScholesCall1 bsprice=" + bsprice);
		assertEquals(20.29616303951127d, bsprice, Constants.PRICE_PRECISION);
	}

	@Test
	public void testBlackScholesPut1() {
		// Result       /  Online calculator
		// ---------------------------------------------
		// n/a          / https://www.mystockoptions.com/black-scholes.cfm
		// 0.2708       / https://www.erieri.com/blackscholes
		// ?????        / (excel spreadsheet)
		// 0,2708       / http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/

		double underlyingPrice = 214.76d;
		double strikePrice = 190.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double volatility = 0.25d;
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double bsprice = s_BlackScholes.priceOption("P", underlyingPrice, strikePrice, timeRemaining,
				volatility, interestRate, dividendYield);
		System.out.println("testBlackScholesPut1 bsprice=" + bsprice);
		assertEquals(0.2707906395245452d, bsprice, Constants.PRICE_PRECISION);
	}

	@Test
	public void testBlackScholesCallImpVol() {
		double price = 20.29616;
		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double bsiv = s_BlackScholes.impliedVolatility("C", price, underlyingPrice, strikePrice, timeRemaining, 0.5, interestRate,
				dividendYield);
		System.out.println("testBlackScholesCallImpVol bsiv=" + bsiv);
		assertEquals(0.20d, bsiv, Constants.IV_PRECISION);
	}

	@Test
	public void testBlackScholesPutImpVol() {
		double price = 0.2708d;
		double underlyingPrice = 214.76d;
		double strikePrice = 190.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double bsiv = s_BlackScholes.impliedVolatility("P", price, underlyingPrice, strikePrice, timeRemaining, 0.5, interestRate,
				dividendYield);
		System.out.println("testBlackScholesPutImpVol bsiv=" + bsiv);
		assertEquals(0.25d, bsiv, Constants.IV_PRECISION);
	}

	@Test
	public void testBlackScholesCallGreeks() {

		// online calculator comparisons
		// http://www.cboe.com/framed/IVolframed.aspx?content=http%3a%2f%2fcboe.ivolatility.com%2fcalc%2findex.j%3fcontract%3dAE172F0B-BFE3-4A3D-B5A3-6085B2C4F088&sectionName=SEC_TRADING_TOOLS&title=CBOE%20-%20IVolatility%20Services
		// delta = 0.4198, gamma = 0.0057, vega = 1.3414, theta = -0.4505, rho = 0.4027
		// http://www.option-price.com/
		// delta = 0.42, gamma = 0.006, vega = 1.341, theta = -0.45, rho = 0.403
		// http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
		// delta = 0.4197, gamma = 0.0057, vega = 1.3413, theta = -0.4502, rho = 0.4026

		double underlyingPrice = 1177.62d;
		double strikePrice = 1195.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double volatility = 0.20d;
		String type = "C";

		double delta = s_BlackScholes.delta(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double gamma = s_BlackScholes.gamma(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double vega = s_BlackScholes.vega(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double theta = s_BlackScholes.theta(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double rho = s_BlackScholes.rho(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);

		System.out.println("testBlackScholesCallGreeks"
				+ " delta=" + delta
				+ ", gamma=" + gamma
				+ ", vega=" + vega
				+ ", theta=" + theta
				+ ", rho=" + rho);

		assertEquals(0.41974, delta, 0.0001d);
		assertEquals(0.00569, gamma, 0.0001d);
		assertEquals(1.34134, vega, 0.0001d);
		assertEquals(-0.45022, theta, 0.0001d);
		assertEquals(0.40257, rho, 0.0001d);
	}

	@Test
	public void testBlackScholesPutGreeks() {

		// online calculator comparisons
		// http://www.cboe.com/framed/IVolframed.aspx?content=http%3a%2f%2fcboe.ivolatility.com%2fcalc%2findex.j%3fcontract%3dAE172F0B-BFE3-4A3D-B5A3-6085B2C4F088&sectionName=SEC_TRADING_TOOLS&title=CBOE%20-%20IVolatility%20Services
		// delta = -0.0415, gamma = 0.0057, vega = 0.0556, theta = -0.0221, rho = -0.0078
		// http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
		// delta = -0.0415, gamma = 0.0057, vega = 0.0556, theta = -0.0221, rho = -0.0078

		double underlyingPrice = 214.76d;
		double strikePrice = 190.00d;
		double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double interestRate = 0.0135d;
		double dividendYield = 0.0d;
		double volatility = 0.25d;
		String type = "P";
		double delta = s_BlackScholes.delta(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double gamma = s_BlackScholes.gamma(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double vega = s_BlackScholes.vega(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double theta = s_BlackScholes.theta(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		double rho = s_BlackScholes.rho(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield);
		System.out.println("testBlackScholesPutGreeks"
				+ " delta=" + delta
				+ ", gamma=" + gamma
				+ ", vega=" + vega
				+ ", theta=" + theta
				+ ", rho=" + rho);
		assertEquals(-0.04150, delta, 0.0001d);
		assertEquals(0.00567, gamma, 0.0001d);
		assertEquals(0.05557, vega, 0.0001d);
		assertEquals(-0.02206, theta, 0.0001d);
		assertEquals(-0.00780, rho, 0.0001d);
	}
}

