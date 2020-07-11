package com.santoch.optionpricing.common;

import java.time.ZonedDateTime;

public interface IOptionModel {

    // type == C(all) or P(ut)
    // s = stock price (current), k = strike price
    // t = expiry time (annualized where 1 = one year), v = volatility, r = risk-free rate
    // q = dividend yield
    double priceOption(String type, double s, double k, double t, double v, double r, double q);

    double delta(String type, double s, double k, double v, double t, double r, double q);
    double gamma(double s, double k, double v, double t, double r, double q);
    double vega(String type, double s, double k, double v, double t, double r, double q);
    double theta(String type, double s, double k, double v, double t, double r, double q);
    double rho(String type, double s, double k, double v, double t, double r, double q);
    double impliedVol(String type, double p, double s, double k, double r, double t, double v, double q);
    IGreeks greeks(ZonedDateTime updateTime, String type, double bid, double ask, double smv,
                   double underlying, double k, double r, double t, double v, double q);
}
