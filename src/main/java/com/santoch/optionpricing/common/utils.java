package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.GreeksImpl;

import java.time.ZonedDateTime;

public class utils {
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
        if (useSmvVol) {
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
}
