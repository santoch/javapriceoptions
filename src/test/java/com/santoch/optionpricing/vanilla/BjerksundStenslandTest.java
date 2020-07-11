package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.util.Constants;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BjerksundStenslandTest {

	static final BjerksundStensland s_BjerksundStensland = new BjerksundStensland();

	@Test
	public void testBjerksundStenslandImpVol1() {
		double p = 20.29616;
		double s = 1177.62d;
		double k = 1195.00d;
		double t = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double r = 0.0135d;
		double q = 0.0d;
		double bjiv = s_BjerksundStensland.impliedVol("C", p, s, k, r, t, 0.5, q);
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

		double s = 1177.62d;
		double k = 1195.00d;
		double t = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double v = 0.20d;
		double r = 0.0135d;
		double q = 0.03d;
		double bsprice = s_BjerksundStensland.priceOption("C", s, k, t, v, r, q);
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

		double s = 1177.62d;
		double k = 1165.00d;
		double t = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double v = 0.20d;
		double r = 0.0135d;
		double q = 0.03d;
		double bsprice = s_BjerksundStensland.priceOption("P", s, k, t, v, r, q);
		System.out.println("testBjerksundStenslandPut1 bsprice=" + bsprice);
		assertEquals(22.03875264497185d, bsprice, 0.00000000000d);
	}

	@Test
	public void testBjerksundStenslandCallGreeks() {
		// ??? did not find an exact equivalent for testing,
		// but assumed to be pretty close to the bs Greeks

		double s = 1177.62d;
		double k = 1195.00d;
		double t = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
		double r = 0.0135d;
		double q = 0.0d;
		double v = 0.20d;
		String type = "C";
		double delta = s_BjerksundStensland.delta(type, s, k, v, t, r, q);
		double gamma = s_BjerksundStensland.gamma(s, k, v, t, r, q);
		double vega = s_BjerksundStensland.vega(type, s, k, v, t, r, q);
		double theta = s_BjerksundStensland.theta(type, s, k, v, t, r, q);
		double rho = s_BjerksundStensland.rho(type, s, k, v, t, r, q);
		System.out.println("testBjerksundStenslandCallGreeks"
						+ " delta=" + delta
						+ ", gamma=" + gamma
						+ ", vega=" + vega
						+ ", theta=" + theta
						+ ", rho=" + rho
		);
	}
}
