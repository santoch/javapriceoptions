package com.santoch.optionpricing.common;

import java.time.ZonedDateTime;

public interface IOptionModel {

    // type == C(all) or P(ut)
    // s = stock price (current), k = strike price
    // t = expiry time (annualized where 1 = one year), v = volatility, r = risk-free rate
    // q = dividend yield
    double priceOption(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                       double volatility, double interestRate, double dividendYield);

    double delta(String type, double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                 double interestRate, double dividendYield);

    double gamma(double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                 double interestRate, double dividendYield);

    double vega(String type, double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                double interestRate, double dividendYield);

    double theta(String type, double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                 double interestRate, double dividendYield);

    double rho(String type, double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
               double interestRate, double dividendYield);

    double impliedVolatility(String type, double optionPrice, double underlyingPrice, double strikePrice,
                             double timeRemaining, double initialVolatility, double interestRate, double dividendYield);

    IGreeks greeks(ZonedDateTime updateTime, String type, double bid, double ask, double smvPrice,
                   double s, double strikePrice, double timeRemaining, double initialVolatility,
                   double interestRate, double dividendYield);
}
