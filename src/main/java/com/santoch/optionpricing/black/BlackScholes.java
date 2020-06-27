package com.santoch.optionpricing.black;

import com.santoch.optionpricing.util.Constants;
import com.santoch.optionpricing.common.IOptionModel;

import static com.santoch.optionpricing.util.NormalDistribution.StandardNormal.cdf;

public class BlackScholes implements IOptionModel {
    // type == C(all) or P(ut)
    // s = stock price (current), k = strike price
    // t = expiry time (annualized where 1 = one year), v = volatility, r = risk-free rate
    // q = dividend yield
    public double priceOption(String type, double s, double k, double t, double v, double r, double q) {
        double sign;
        if ("C".equals(type)) {
            if (t <= 0) {
                return Math.abs(s - k);
            }
            sign = 1;
        } else if ("P".equals(type)) {
            if (t <= 0) {
                return Math.abs(k - s);
            }
            sign = -1;
        } else {
            return 0.0d;
        }

        double dr = Math.exp(-r * t), drq = Math.exp(-q * t);
        double vt = v * Math.sqrt(t);
        double d1 = d1(s, k, t, v, r, q, vt);
        double d2 = d1 - vt;
        double nd1, nd2;

        d1 = sign * d1;
        d2 = sign * d2;
        nd1 = cdf(d1);
        nd2 = cdf(d2);

        return sign * ((s * drq * nd1) - (k * dr * nd2));
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
        etod1sqhalf = etod1sqhalf / Constants.SQ_TWOPI;
        return etod1sqhalf;
    }

    public double delta(String type, double s, double k, double v, double t, double r, double q) {
        double drq = Math.exp(-q * t);
        double zo = ("P".equals(type)) ? -1d : 0d;
        double vt = (v * (Math.sqrt(t)));
        double d1 = d1(s, k, t, v, r, q, vt);
        double cdfd1 = cdf(d1);
        return drq * (cdfd1 + zo);
    }

    public double gamma(double s, double k, double v,
                        double t, double r, double q) {
        double drq = Math.exp(-q * t);
        double drd = (s * v * Math.sqrt(t));
        double d1pdf = d1pdf(s, k, v, t, r, q);
        return (drq / drd) * d1pdf;
    }

    // Greeks (generally follows the macroption.com spreadsheet formula)
    public double vega(String type, double s, double k, double v, double t, double r, double q) {
        double d1pdf = d1pdf(s, k, v, t, r, q);
        double drq = Math.exp(-q * t);
        double sqt = Math.sqrt(t);
        return (d1pdf) * drq * s * sqt * 0.01;
    }

    public double theta(String type, double s, double k, double v,
                        double t, double r, double q) {
        double sign = ("P".equals(type)) ? -1d : 1d;
        double drq = Math.exp(-q * t);
        double dr = Math.exp(-r * t);
        double d1pdf = d1pdf(s, k, v, t, r, q);
        double twosqt = 2 * Math.sqrt(t);
        double p1 = -1 * ((s * v * drq) / twosqt) * d1pdf;

        double vt = (v * (Math.sqrt(t)));
        double d1 = d1(s, k, t, v, r, q, vt);
        double d2 = d1 - vt;
        double nd1, nd2;

        d1 = sign * d1;
        d2 = sign * d2;
        nd1 = cdf(d1);
        nd2 = cdf(d2);

        double p2 = -sign * r * k * dr * nd2;
        double p3 = sign * q * s * drq * nd1;
        return (p1 + p2 + p3) / 365;
    }

    public double rho(String type, double s, double k, double v,
                      double t, double r, double q) {
        double sign = ("P".equals(type)) ? -1d : 1d;
        double dr = Math.exp(-r * t);
        double p1 = sign * (k * t * dr) / 100;

        double vt = (v * (Math.sqrt(t)));
        double d1 = d1(s, k, t, v, r, q, vt);
        double d2 = sign * (d1 - vt);
        double nd2 = cdf(d2);
        return p1 * nd2;
    }

    // Implied vol
    public double impliedVol(String type, double p, double s,
                             double k, double r, double t, double v, double q) {
        v = v > 0d ? v : 0.5;
        double maxloops = 100;
        double dv = Constants.IV_PRECISION + 1;
        double n = 0;
        while (Math.abs(dv) > Constants.IV_PRECISION && n < maxloops) {
            double difval = priceOption(type, s, k, t, v, r, q) - p;
            double v1 = vega(type, s, k, v, t, r, q) / 0.01;
            dv = difval / v1;
            v = v - dv;
            n++;
        }
        return n < maxloops ? v : Double.NaN;
    }
}
