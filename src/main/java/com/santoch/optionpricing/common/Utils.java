package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.GreeksImpl;
import com.santoch.optionpricing.util.NormalDistribution;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.MILLIS;

@SuppressWarnings("unused")
public class Utils {
    static public IGreeks greeks(IOptionModel model, ZonedDateTime updateTime, String type, double bid, double ask,
                                 double smv, double underlyingPrice, double strikePrice, double timeRemaining,
                                 double initialVolatility, double interestRate, double dividendYield,
                                 boolean useSmvVol) {

        GreeksImpl greeks = new GreeksImpl();
        double mid = (bid + ask) / 2.0;
        smv = smv > 0 ? smv : mid;

        double smvVol = model.impliedVolatility(type, smv, underlyingPrice, strikePrice, timeRemaining,
                initialVolatility,
                interestRate, dividendYield);
        greeks.setSmvVol(smvVol);
        if (useSmvVol && smvVol != Double.NaN && smvVol > 0) {
            initialVolatility = smvVol;
        }

        greeks.setUpdate_time(updateTime);
        greeks.setAskIv(model.impliedVolatility(type, ask, underlyingPrice, strikePrice, timeRemaining,
                initialVolatility, interestRate, dividendYield));
        greeks.setBidIv(model.impliedVolatility(type, bid, underlyingPrice, strikePrice, timeRemaining,
                initialVolatility, interestRate, dividendYield));
        greeks.setMidIv(model.impliedVolatility(type, mid, underlyingPrice, strikePrice, timeRemaining,
                initialVolatility, interestRate, dividendYield));
        greeks.setDelta(model.delta(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));
        greeks.setGamma(model.gamma(underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));
        greeks.setDelta(model.delta(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));
        greeks.setTheta(model.theta(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));
        greeks.setVega(model.vega(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));
        greeks.setRho(model.rho(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield));

        return greeks;
    }

    static public double probabilityBelow(double underlyingPrice, double strikePrice,
                                               double timeRemaining, double atTheMoneyVolatility) {
        if (underlyingPrice == strikePrice) {
            return .5d;
        }

        NormalDistribution dist = NormalDistribution.Standard();
        double probabilityBelow = dist.cdf(Math.log(strikePrice / underlyingPrice) /
                                           (atTheMoneyVolatility * Math.sqrt(timeRemaining)));
        return probabilityBelow;
    }

    static public double probabilityInTheMoney(String type, double underlyingPrice, double strikePrice,
                                               double timeRemaining, double atTheMoneyVolatility) {
        if (underlyingPrice == strikePrice) {
            return .5d;
        }

        NormalDistribution dist = NormalDistribution.Standard();
        double probabilityBelow = dist.cdf(Math.log(strikePrice / underlyingPrice) /
                                           (atTheMoneyVolatility * Math.sqrt(timeRemaining)));
        if ("C".equals(type)) {
            probabilityBelow = (1.0d - probabilityBelow);
        }

        return probabilityBelow;
    }

    // returns the time between two dates as a fraction of a year.
    static public double timeBetween(ZonedDateTime lastDate, ZonedDateTime firstDate) {
        long diff = firstDate.until(lastDate, MILLIS);
        return diff / (365.0d *24.0d *60.0d *60.0d *1000.0d);
    }

    // returns the expiration date that is closest to the date today+daysFromNow
    public static ZonedDateTime findClosestExpiration(@NotNull List<ZonedDateTime> expirationList, int daysFromNow) {
        if (expirationList.isEmpty()) {
            return null;
        }

        ZoneId zoneId = expirationList.get(0).getZone();
        ZonedDateTime targetDate = ZonedDateTime.now(zoneId).plusDays(daysFromNow);
        return findClosestExpiration(expirationList, targetDate);
    }

    // returns the expiration date that is closest to the target expiration date
    static public ZonedDateTime findClosestExpiration(@NotNull List<ZonedDateTime> searchList, ZonedDateTime target) {
        if (searchList.isEmpty()) {
            return null;
        }

        var index = Collections.binarySearch(searchList, target);
        if (index >= 0) {
            return searchList.get(index);
        } else {
            index = (index * -1) - 1;

            ZonedDateTime foundBefore = (index > 0) ? searchList.get(Math.min(index - 1, searchList.size()-1)) : null;
            ZonedDateTime foundAfter = (index < searchList.size()) ? searchList.get(index) : null;

            var diff1 = (foundBefore == null) ? Double.MAX_VALUE : Math.abs(timeBetween(foundBefore, target));
            var diff2 = (foundAfter == null) ? Double.MAX_VALUE : Math.abs(timeBetween(target, foundAfter));

            return (diff1 < diff2) ? foundBefore : foundAfter;
        }
    }

    // returns the expiration date that is closest to the target expiration date
    static public Double findClosestValue(@NotNull List<Double> searchList, Double target) {
        if (searchList.isEmpty()) {
            return null;
        }

        var index = Collections.binarySearch(searchList, target);
        if (index >= 0) {
            return searchList.get(index);
        } else {
            index = (index * -1) - 1;

            Double foundBefore = (index > 0) ? searchList.get(Math.min(index - 1, searchList.size()-1)) : null;
            Double foundAfter = (index < searchList.size()) ? searchList.get(index) : null;

            var diff1 = (foundBefore == null) ? Double.MAX_VALUE : Math.abs(foundBefore - target);
            var diff2 = (foundAfter == null) ? Double.MAX_VALUE : Math.abs(target - foundAfter);

            return (diff1 < diff2) ? foundBefore : foundAfter;
        }
    }
}
