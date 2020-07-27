package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.common.IGreeks;
import com.santoch.optionpricing.common.IOptionModel;
import com.santoch.optionpricing.common.utils;
import com.santoch.optionpricing.util.Constants;

import java.time.ZonedDateTime;

import static com.santoch.optionpricing.util.NormalDistribution.StandardNormal.cdf;

public class BjerksundStensland implements IOptionModel {
    private static final BlackScholes s_blackScholes = new BlackScholes();

    public double priceOption(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                              double volatility, double interestRate, double dividendYield) {
        double costOfCarry = interestRate - dividendYield;
        double price = 0.0d;
        if ("P".equals(type)) {
            if (timeRemaining <= 0) {
                return Math.abs(strikePrice - underlyingPrice);
            }
            if (-costOfCarry > dividendYield || dividendYield == 0) {
                return s_blackScholes.priceOption(type, underlyingPrice, strikePrice, timeRemaining,
                        volatility, interestRate, dividendYield);
            }
            price = priceImpl(strikePrice, underlyingPrice, timeRemaining, volatility, -costOfCarry,
                    interestRate - costOfCarry, dividendYield);
        } else if ("C".equals(type)) {
            if (timeRemaining <= 0) {
                return Math.abs(underlyingPrice - strikePrice);
            }
            if (costOfCarry > interestRate || dividendYield == 0) {
                return s_blackScholes.priceOption(type, underlyingPrice, strikePrice, timeRemaining,
                        volatility, interestRate, dividendYield);
            }
            price = priceImpl(underlyingPrice, strikePrice, timeRemaining, volatility,
                    costOfCarry, interestRate, dividendYield);
        }
        return price;
    }

    private double priceImpl(double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                             double costOfCarry, double interestRate, double dividendYield) {
        double v2 = volatility * volatility;
        double beta =
                (0.5 - (costOfCarry / v2)) + Math.pow((Math.pow(((costOfCarry / v2) - 0.5), 2) + (2 * interestRate / v2)), 0.5);

        double betainfinity = (beta / (beta - 1)) * strikePrice;
        double betazero = Math.max(strikePrice, (interestRate / dividendYield) * strikePrice);

        double h = -((costOfCarry * timeRemaining) + (2 * volatility * Math.pow(timeRemaining, 0.5))) *
                   ((strikePrice * strikePrice) / ((betainfinity - betazero) * betazero));
        double X = betazero + ((betainfinity - betazero) * (1 - Math.exp(h)));

        if (X < underlyingPrice) {
            // price equals intrinsic value
            return Math.abs(underlyingPrice - strikePrice);
        } else {
            double alpha = (X - strikePrice) * Math.pow(X, -beta);
            double tmp1 = alpha * Math.pow(underlyingPrice, beta);
            double tmp2 = alpha * phi(underlyingPrice, timeRemaining, beta, X, X, volatility, interestRate,
                    costOfCarry);
            double tmp3 = phi(underlyingPrice, timeRemaining, 1, X, X, volatility, interestRate, costOfCarry);
            double tmp4 = phi(underlyingPrice, timeRemaining, 1, strikePrice, X, volatility, interestRate, costOfCarry);
            double tmp5 = strikePrice * phi(underlyingPrice, timeRemaining, 0, X, X, volatility, interestRate,
                    costOfCarry);
            double tmp6 = strikePrice * phi(underlyingPrice, timeRemaining, 0, strikePrice, X, volatility,
                    interestRate, costOfCarry);
            return tmp1 - tmp2 + tmp3 - tmp4 - tmp5 + tmp6;
        }
    }

    private double phi(double underlyingPrice, double timeRemaining, double gamma, double h, double X,
                       double volatility, double interestRate, double b) {
        double v2 = volatility * volatility;
        double K = ((2 * b) / v2) + (2 * gamma - 1);
        double lambda = -interestRate + (gamma * b) + ((0.5 * gamma * (gamma - 1)) * v2);
        final double vXsqrtT = volatility * Math.sqrt(timeRemaining);

        double tmp1 = ((Math.log(underlyingPrice / h)) + (b + (gamma - 0.5) * v2) * timeRemaining) / vXsqrtT;
        double tmp2 =
                (Math.log((X * X) / (underlyingPrice * h)) + (b + (gamma - 0.5) * v2) * timeRemaining) / (volatility * Math.sqrt(timeRemaining));

        return Math.exp(lambda * timeRemaining) * Math.pow(underlyingPrice, gamma) * (cdf(-tmp1) - (Math.pow(X / underlyingPrice, K)) * cdf(-tmp2));
    }

    public double delta(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                        double volatility, double interestRate, double dividendYield) {
        double umove = 1.01;
        double dmove = 1 / umove;
        double uval = priceOption(type, underlyingPrice * umove, strikePrice, timeRemaining, volatility, interestRate
                , dividendYield);
        double dval = priceOption(type, underlyingPrice * dmove, strikePrice, timeRemaining, volatility, interestRate
                , dividendYield);
        return (uval - dval) / (underlyingPrice * (umove - dmove));
    }

    public double gamma(double underlyingPrice, double strikePrice, double timeRemaining, double volatility,
                        double interestRate, double dividendYield) {
        return s_blackScholes.gamma(underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);
    }

    public double vega(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                       double volatility, double interestRate, double dividendYield) {
        double onePercent = 0.01;
        double val1 = priceOption(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);
        double val2 = priceOption(type, underlyingPrice, strikePrice, timeRemaining, volatility + onePercent,
                interestRate,
                dividendYield);
        return (val2 - val1) / onePercent / 100d;
    }

    public double theta(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                        double volatility,
                        double interestRate, double dividendYield) {
        double tomorrow = timeRemaining - Constants.ONE_DAY;
        tomorrow = (tomorrow < 0) ? 0 : tomorrow;
        double val = priceOption(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);
        double valt = priceOption(type, underlyingPrice, strikePrice, tomorrow, volatility, interestRate,
                dividendYield);
        return valt - val;
    }

    public double rho(String type, double underlyingPrice, double strikePrice, double timeRemaining,
                      double volatility, double interestRate, double dividendYield) {
        return s_blackScholes.rho(type, underlyingPrice, strikePrice, timeRemaining, volatility, interestRate,
                dividendYield);
    }

    public double impliedVolatility(String type, double optionPrice, double underlyingPrice, double strikePrice,
                                    double timeRemaining, double initialVolatility, double interestRate,
                                    double dividendYield) {
        initialVolatility = initialVolatility == 0d ? 0.5 : initialVolatility;
        double errlimit = Constants.IV_PRECISION;
        double maxloops = 100;
        double dv = errlimit + 1;
        double n = 0;
        while (Math.abs(dv) > errlimit && n < maxloops) {
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
        return utils.greeks(this, updateTime, type, bid, ask, smvPrice, s, strikePrice, timeRemaining,
                initialVolatility, interestRate,
                dividendYield, true);
    }
}
