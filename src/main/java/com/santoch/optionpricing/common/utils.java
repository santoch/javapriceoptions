package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.GreeksImpl;

import java.time.ZonedDateTime;

public class utils {
    static public double OneSigma(double underlyingPrice, double volatility, double timeRemaining) {
        return (underlyingPrice) * (volatility) * (Math.sqrt(timeRemaining / 365.0));
    }

    static public IGreeks greeks(IOptionModel model, ZonedDateTime updateTime, String type, double bid, double ask,
                                 double smv,  double underlyingPrice, double strikePrice, double interestRate,
                                 double timeRemaining, double volatility, double dividendYield, boolean useSmvVol) {

        GreeksImpl greeks = new GreeksImpl();
        double mid = (bid + ask) / 2.0;
        smv = smv > 0 ? smv : mid;

        double smvVol = model.impliedVolatility(type, smv, underlyingPrice, strikePrice, interestRate, timeRemaining, volatility, dividendYield);
        greeks.setSmvVol(smvVol);
        if (useSmvVol) {
            volatility = smvVol;
        }

        greeks.setUpdate_time(updateTime);
        greeks.setAskIv(model.impliedVolatility(type, ask, underlyingPrice, strikePrice, interestRate, timeRemaining, volatility, dividendYield));
        greeks.setBidIv(model.impliedVolatility(type, bid, underlyingPrice, strikePrice, interestRate, timeRemaining, volatility, dividendYield));
        greeks.setMidIv(model.impliedVolatility(type, mid, underlyingPrice, strikePrice, interestRate, timeRemaining, volatility, dividendYield));
        greeks.setDelta(model.delta(type, underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));
        greeks.setGamma(model.gamma(underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));
        greeks.setDelta(model.delta(type, underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));
        greeks.setTheta(model.theta(type, underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));
        greeks.setVega(model.vega(type, underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));
        greeks.setRho(model.rho(type, underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield));

        return greeks;
    }
}
