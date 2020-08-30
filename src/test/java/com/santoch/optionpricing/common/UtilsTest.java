package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.Constants;
import com.santoch.optionpricing.vanilla.BlackScholes;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class UtilsTest {
    @Test
    public void testBlackScholesCallGreeks() {

        // online calculator comparisons
        // http://www.cboe.com/framed/IVolframed.aspx?content=http%3a%2f%2fcboe.ivolatility.com%2fcalc%2findex.j%3fcontract%3dAE172F0B-BFE3-4A3D-B5A3-6085B2C4F088&sectionName=SEC_TRADING_TOOLS&title=CBOE%20-%20IVolatility%20Services
        // delta = 0.4198, gamma = 0.0057, vega = 1.3414, theta = -0.4505, rho = 0.4027
        // http://www.option-price.com/
        // delta = 0.42, gamma = 0.006, vega = 1.341, theta = -0.45, rho = 0.403
        // http://www.fintools.com/resources/online-calculators/options-calcs/options-calculator/
        // delta = 0.4197, gamma = 0.0057, vega = 1.3413, theta = -0.4502, rho = 0.4026

        final IOptionModel blackScholesModel = new BlackScholes();
        double price = 20.29616;
        double underlyingPrice = 1177.62d;
        double strikePrice = 1195.00d;
        double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
        double interestRate = 0.0135d;
        double dividendYield = 0.0d;
        double volatility = 0.20d;
        String type = "C";

        ZonedDateTime updateTime = ZonedDateTime.now();
        IGreeks greeks = blackScholesModel.greeks(updateTime,
                type,
                /* bid */ price,
                /* ask */ price,
                /* smv */ price,
                underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);

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

        final IOptionModel blackScholesModel = new BlackScholes();
        double price = 0.2708d;
        double underlyingPrice = 214.76d;
        double strikePrice = 190.00d;
        double timeRemaining = 0.084931506849315d; // date 12/19/2017, expiration 1/19/2018, 31 days
        double interestRate = 0.0135d;
        double dividendYield = 0.0d;
        double volatility = 0.25d;
        String type = "P";

        ZonedDateTime updateTime = ZonedDateTime.now();
        IGreeks greeks = blackScholesModel.greeks(updateTime, type,
                /* bid */ price,
                /* ask */ price,
                /* smv */ price,
                underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);

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

    @Test
    public void TestFindClosestExpirations() {

        // create arraylist
        var theList = new ArrayList<ZonedDateTime>();
        var today = ZonedDateTime.now();

        // test empty list
        assertNull(Utils.findClosestExpiration(theList, today));
        assertNull(Utils.findClosestExpiration(theList, 0));

        var _5d = today.plusDays(5);
        var _10d = today.plusDays(10);
        var _15d = today.plusDays(15);
        var _20d = today.plusDays(20);

        // populate the list
        theList.add(_5d);
        theList.add(_10d);
        theList.add(_15d);
        theList.add(_20d);

        // search for all of them
        assertEquals(_5d, Utils.findClosestExpiration(theList,_5d));
        assertEquals(_10d, Utils.findClosestExpiration(theList,_10d));
        assertEquals(_15d, Utils.findClosestExpiration(theList,_15d));
        assertEquals(_20d, Utils.findClosestExpiration(theList,_20d));

        // miss below and above
        assertEquals(_5d, Utils.findClosestExpiration(theList,today.plusDays(3)));
        assertEquals(_20d, Utils.findClosestExpiration(theList,today.plusDays(25)));

        // look around 5
        assertEquals(_5d, Utils.findClosestExpiration(theList,today.plusDays(4)));
        assertEquals(_5d, Utils.findClosestExpiration(theList,today.plusDays(6)));

        // look around 10
        assertEquals(_10d, Utils.findClosestExpiration(theList,today.plusDays(9)));
        assertEquals(_10d, Utils.findClosestExpiration(theList,today.plusDays(11)));

        // look around 15
        assertEquals(_15d, Utils.findClosestExpiration(theList,today.plusDays(14)));
        assertEquals(_15d, Utils.findClosestExpiration(theList,today.plusDays(16)));

        // look around 20
        assertEquals(_20d, Utils.findClosestExpiration(theList,today.plusDays(19)));
        assertEquals(_20d, Utils.findClosestExpiration(theList,today.plusDays(21)));

        // search for all of them
        assertEquals(_5d, Utils.findClosestExpiration(theList,5));
        assertEquals(_10d, Utils.findClosestExpiration(theList,10));
        assertEquals(_15d, Utils.findClosestExpiration(theList,15));
        assertEquals(_20d, Utils.findClosestExpiration(theList,20));

        // miss below and above
        assertEquals(_5d, Utils.findClosestExpiration(theList,3));
        assertEquals(_20d, Utils.findClosestExpiration(theList,25));

        // look around 5
        assertEquals(_5d, Utils.findClosestExpiration(theList,4));
        assertEquals(_5d, Utils.findClosestExpiration(theList,6));

        // look around 10
        assertEquals(_10d, Utils.findClosestExpiration(theList,9));
        assertEquals(_10d, Utils.findClosestExpiration(theList,11));

        // look around 15
        assertEquals(_15d, Utils.findClosestExpiration(theList,14));
        assertEquals(_15d, Utils.findClosestExpiration(theList,16));

        // look around 20
        assertEquals(_20d, Utils.findClosestExpiration(theList,19));
        assertEquals(_20d, Utils.findClosestExpiration(theList,21));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void TestFindClosestValue() {

        // create arraylist
        var theList = new ArrayList<Double>();

        // test empty list
        assertNull(Utils.findClosestValue(theList, 5.0d));

        // populate the list
        theList.add(5.0d);
        theList.add(10.0d);
        theList.add(15.0d);
        theList.add(20.0d);

        // search for all of them
        assertEquals(5.0d, Utils.findClosestValue(theList,5.0d), 0d);
        assertEquals(10.0d, Utils.findClosestValue(theList,10.0d), 0d);
        assertEquals(15.0d, Utils.findClosestValue(theList,15.0d), 0d);
        assertEquals(20.0d, Utils.findClosestValue(theList,20.0d), 0d);

        // miss below and above
        assertEquals(5.0d, Utils.findClosestValue(theList,3.0d), 0d);
        assertEquals(20.0d, Utils.findClosestValue(theList,25.0d), 0d);

        // look around 5
        assertEquals(5.0d, Utils.findClosestValue(theList,4.0d), 0d);
        assertEquals(5.0d, Utils.findClosestValue(theList,6.0d), 0d);

        // look around 10
        assertEquals(10.0d, Utils.findClosestValue(theList,9.0d), 0d);
        assertEquals(10.0d, Utils.findClosestValue(theList,11.0d), 0d);

        // look around 15
        assertEquals(15.0d, Utils.findClosestValue(theList,14.0d), 0d);
        assertEquals(15.0d, Utils.findClosestValue(theList,16.0d), 0d);

        // look around 20
        assertEquals(20.0d, Utils.findClosestValue(theList,19.0d), 0d);
        assertEquals(20.0d, Utils.findClosestValue(theList,21.0d), 0d);
    }
}
