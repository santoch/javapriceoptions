package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.Constants;
import com.santoch.optionpricing.vanilla.BjerksundStensland;
import com.santoch.optionpricing.vanilla.BlackScholes;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.TestCase.assertEquals;

public class utilsTest {
    @Test
    public void testBlackScholesCallGreeks() {

        // online calculator comparisons
        // http://www.cboe.com/framed/IVolframed.aspx?content=http%3a%2f%2fcboe.ivolatility.com%2fcalc%2findex.j%3fcontract%3dAE172F0B-BFE3-4A3D-B5A3-6085B2C4F088&sectionName=SEC_TRADING_TOOLS&title=CBOE%20-%20IVolatility%20Services
        // delta = 0.4198, gamma = 0.0057, vega = 1.3414, theta = -0.4505, rho = 0.4027
        // http://www.option-price.com/
        // delta = 0.42, gamma = 0.006, vega = 1.341, theta = -0.45, rho = 0.403
        // http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
        // delta = 0.4197, gamma = 0.0057, vega = 1.3413, theta = -0.4502, rho = 0.4026

        final BlackScholes blackScholesModel = new BlackScholes();
        double price = 20.29616;
        double underlyingPrice = 1177.62d;
        double strikePrice = 1195.00d;
        double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
        double interestRate = 0.0135d;
        double dividendYield = 0.0d;
        double volatility = 0.20d;
        String type = "C";

        ZonedDateTime updateTime = ZonedDateTime.now();
        double bid = price;
        double ask = price;
        double smv = price;
        IGreeks greeks = utils.greeks(blackScholesModel, updateTime, type, bid, ask,
                smv,  underlyingPrice, strikePrice, interestRate,
                timeRemaining, volatility, dividendYield, true);

        System.out.println("testBlackScholesCallGreeks"
                + " delta=" + greeks.getDelta()
                + ", gamma=" + greeks.getGamma()
                + ", vega=" + greeks.getVega()
                + ", theta=" + greeks.getTheta()
                + ", rho=" + greeks.getRho()
                + ", bidIv=" + greeks.getBidIv()
                + ", askIv=" + greeks.getAskIv()
                + ", midIv=" + greeks.getMidIv()
                + ", smvVol=" + greeks.getSmvVol()
                + ", updateTime=" + greeks.getUpdate_time().format(DateTimeFormatter.ISO_DATE_TIME)
        );

        assertEquals(0.41974, greeks.getDelta(), 0.0001d);
        assertEquals(0.00569, greeks.getGamma(), 0.0001d);
        assertEquals(1.34134, greeks.getVega(), 0.0001d);
        assertEquals(-0.45022, greeks.getTheta(), 0.0001d);
        assertEquals(0.40257, greeks.getRho(), 0.0001d);
        assertEquals(0.20d, greeks.getAskIv(), Constants.IV_PRECISION);
        assertEquals(0.20d, greeks.getBidIv(), Constants.IV_PRECISION);
        assertEquals(0.20d, greeks.getMidIv(), Constants.IV_PRECISION);
        assertEquals(0.20d, greeks.getSmvVol(), Constants.IV_PRECISION);
        assertEquals(updateTime, greeks.getUpdate_time());
    }

    @Test
    public void testBlackScholesPutGreeks() {

        // online calculator comparisons
        // http://www.cboe.com/framed/IVolframed.aspx?content=http%3a%2f%2fcboe.ivolatility.com%2fcalc%2findex.j%3fcontract%3dAE172F0B-BFE3-4A3D-B5A3-6085B2C4F088&sectionName=SEC_TRADING_TOOLS&title=CBOE%20-%20IVolatility%20Services
        // delta = -0.0415, gamma = 0.0057, vega = 0.0556, theta = -0.0221, rho = -0.0078
        // http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
        // delta = -0.0415, gamma = 0.0057, vega = 0.0556, theta = -0.0221, rho = -0.0078

        final BlackScholes blackScholesModel = new BlackScholes();
        double price = 0.2708d;
        double underlyingPrice = 214.76d;
        double strikePrice = 190.00d;
        double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
        double interestRate = 0.0135d;
        double dividendYield = 0.0d;
        double volatility = 0.25d;
        String type = "P";

        ZonedDateTime updateTime = ZonedDateTime.now();
        double bid = price;
        double ask = price;
        double smv = price;
        IGreeks greeks = utils.greeks(blackScholesModel, updateTime, type, bid, ask,
                smv,  underlyingPrice, strikePrice, interestRate,
                timeRemaining, volatility, dividendYield, true);

        System.out.println("testBlackScholesPutGreeks"
                + " delta=" + greeks.getDelta()
                + ", gamma=" + greeks.getGamma()
                + ", vega=" + greeks.getVega()
                + ", theta=" + greeks.getTheta()
                + ", rho=" + greeks.getRho()
                + ", bidIv=" + greeks.getBidIv()
                + ", askIv=" + greeks.getAskIv()
                + ", midIv=" + greeks.getMidIv()
                + ", smvVol=" + greeks.getSmvVol()
                + ", updateTime=" + greeks.getUpdate_time().format(DateTimeFormatter.ISO_DATE_TIME)
        );
        assertEquals(-0.04150, greeks.getDelta(), 0.0001d);
        assertEquals(0.00567, greeks.getGamma(), 0.0001d);
        assertEquals(0.05557, greeks.getVega(), 0.0001d);
        assertEquals(-0.02206, greeks.getTheta(), 0.0001d);
        assertEquals(-0.00780, greeks.getRho(), 0.0001d);
        assertEquals(0.25d, greeks.getAskIv(), Constants.IV_PRECISION);
        assertEquals(0.25d, greeks.getBidIv(), Constants.IV_PRECISION);
        assertEquals(0.25d, greeks.getMidIv(), Constants.IV_PRECISION);
        assertEquals(0.25d, greeks.getSmvVol(), Constants.IV_PRECISION);
        assertEquals(updateTime, greeks.getUpdate_time());
    }
}
