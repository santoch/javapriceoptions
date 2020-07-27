package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.common.IGreeks;
import com.santoch.optionpricing.common.IOptionModel;
import com.santoch.optionpricing.common.utils;
import com.santoch.optionpricing.util.Constants;

import java.time.ZonedDateTime;

import static com.santoch.optionpricing.util.NormalDistribution.StandardNormal.cdf;

public class BlackScholes implements IOptionModel {
    // type == C(all) or P(ut)
    // s = stock price (current), k = strike price
    // t = expiry time (annualized where 1 = one year), v = volatility, r = risk-free rate
    // q = dividend yield
    public double priceOption(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                              double volatility, double interestRate, double dividendYield) {
        double sign;
        if ("C".equals(type)) {
            if (timeRemaining <= 0) {
                return Math.abs(underlyingPrice - strikePrice);
            }
            sign = 1;
        } else if ("P".equals(type)) {
            if (timeRemaining <= 0) {
                return Math.abs(strikePrice - underlyingPrice);
            }
            sign = -1;
        } else {
            return 0.0d;
        }

        double dr = Math.exp(-interestRate * timeRemaining), drq = Math.exp(-dividendYield * timeRemaining);
        double vt = volatility * Math.sqrt(timeRemaining);
        double d1 = d1(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield, vt);
        double d2 = d1 - vt;
        double nd1, nd2;

        d1 = sign * d1;
        d2 = sign * d2;
        nd1 = cdf(d1);
        nd2 = cdf(d2);

        return sign * ((underlyingPrice * drq * nd1) - (strikePrice * dr * nd2));
    }

    double d1(double s, double k, double t, double v, double r, double q, double vt) {
        double d1 = Math.log(s / k) + (t * (r - q + ((v * v) * 0.5d)));
        d1 = d1 / vt;
        return d1;
    }

    private double d1pdf(double s, double k, double v,
                         double t, double r, double q) {
        double vt = (v * (Math.sqrt(t)));
        double d1 = d1(s, k, t, v, r, q, vt);
        double etod1sqhalf = Math.exp(-(d1 * d1) * 0.5);
        etod1sqhalf = etod1sqhalf / Constants.SQRT_TWOPI;
        return etod1sqhalf;
    }

    public double delta(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                        double volatility, double interestRate, double dividendYield) {
        double drq = Math.exp(-dividendYield * timeRemaining);
        double zo = ("P".equals(type)) ? -1d : 0d;
        double vt = (volatility * (Math.sqrt(timeRemaining)));
        double d1 = d1(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield, vt);
        double cdfd1 = cdf(d1);
        return drq * (cdfd1 + zo);
    }

    public double gamma(double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                        double interestRate, double dividendYield) {
        double drq = Math.exp(-dividendYield * timeRemaining);
        double drd = (underlyingPrice * volatility * Math.sqrt(timeRemaining));
        double d1pdf = d1pdf(underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield);
        return (drq / drd) * d1pdf;
    }

    // Greeks (generally follows the macroption.com spreadsheet formula)
    public double vega(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                       double volatility, double interestRate, double dividendYield) {
        double d1pdf = d1pdf(underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield);
        double drq = Math.exp(-dividendYield * timeRemaining);
        double sqt = Math.sqrt(timeRemaining);
        return (d1pdf) * drq * underlyingPrice * sqt * 0.01;
    }

    public double theta(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                        double volatility,
                        double interestRate, double dividendYield) {
        double sign = ("P".equals(type)) ? -1d : 1d;
        double drq = Math.exp(-dividendYield * timeRemaining);
        double dr = Math.exp(-interestRate * timeRemaining);
        double d1pdf = d1pdf(underlyingPrice, strikePrice, volatility, timeRemaining, interestRate, dividendYield);
        double twosqt = 2 * Math.sqrt(timeRemaining);
        double p1 = -1 * ((underlyingPrice * volatility * drq) / twosqt) * d1pdf;

        double vt = (volatility * (Math.sqrt(timeRemaining)));
        double d1 = d1(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield, vt);
        double d2 = d1 - vt;
        double nd1, nd2;

        d1 = sign * d1;
        d2 = sign * d2;
        nd1 = cdf(d1);
        nd2 = cdf(d2);

        double p2 = -sign * interestRate * strikePrice * dr * nd2;
        double p3 = sign * dividendYield * underlyingPrice * drq * nd1;
        return (p1 + p2 + p3) / 365;
    }

    public double rho(String type, double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                      double interestRate, double dividendYield) {
        double sign = ("P".equals(type)) ? -1d : 1d;
        double dr = Math.exp(-interestRate * timeRemaining);
        double p1 = sign * (strikePrice * timeRemaining * dr) / 100;

        double vt = (volatility * (Math.sqrt(timeRemaining)));
        double d1 = d1(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate, dividendYield, vt);
        double d2 = sign * (d1 - vt);
        double nd2 = cdf(d2);
        return p1 * nd2;
    }

    // Implied vol
    public double impliedVolatility(String type, double optionPrice, double underlyingPrice,
                                    double strikePrice, double timeRemaining, double initialVolatility,
                                    double interestRate, double dividendYield) {
        initialVolatility = initialVolatility > 0d ? initialVolatility : 0.5;
        double maxloops = 100;
        double dv = Constants.IV_PRECISION + 1;
        double n = 0;
        while (Math.abs(dv) > Constants.IV_PRECISION && n < maxloops) {
            double difval = priceOption(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility,
                    interestRate, dividendYield) - optionPrice;
            double v1 = vega(type, underlyingPrice, strikePrice, timeRemaining, initialVolatility, interestRate,
                    dividendYield) / 0.01;
            dv = difval / v1;
            initialVolatility = initialVolatility - dv;
            n++;
        }
        return n < maxloops ? initialVolatility : Double.NaN;
    }

    @Override
    public IGreeks greeks(ZonedDateTime updateTime, String type, double bid, double ask, double smvPrice,
                          double s, double strikePrice, double timeRemaining, double initialVolatility,
                          double interestRate, double dividendYield) {
        return utils.greeks(this, updateTime, type, bid, ask, smvPrice, s, strikePrice, timeRemaining, initialVolatility, interestRate,
                dividendYield, true);
    }
}
