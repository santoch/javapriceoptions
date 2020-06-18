package com.santoch.optionpricing.black;

import static com.santoch.optionpricing.util.NormalDistribution.StandardNormal.cdf;

public class BjerksundStensland implements IOptionModel{
    private static final BlackScholes s_blackScholes = new BlackScholes();

    public double priceOption(String type, double s, double k, double t, double v, double r, double q) {
        double b = r - q; // b is the cost of carry
        double price = 0.0d;
        if ("P".equals(type)) {
            if (t <= 0) {
                return Math.abs(k - s);
            }
            if (-b > q || q == 0) {
                return s_blackScholes.priceOption(type, s, k, t, v, r, q);
            }
            price = priceImpl(k, s, t, v, -b, r - b, q);
        } else if ("C".equals(type)) {
            if (t <= 0) {
                return Math.abs(s - k);
            }
            if (b > r || q == 0) {
                return s_blackScholes.priceOption(type, s, k, t, v, r, q);
            }
            price = priceImpl(s, k, t, v, b, r, q);
        }
        return price;
    }

    private double priceImpl(double s, double k, double t, double v, double b, double r, double q) {
        double v2 = v * v;
        double beta = (0.5 - (b / v2)) + Math.pow((Math.pow(((b / v2) - 0.5), 2) + (2 * r / v2)), 0.5);

        double betainfinity = (beta / (beta - 1)) * k;
        double betazero = Math.max(k, (r / q) * k);

        double h = -((b * t) + (2 * v * Math.pow(t, 0.5))) * ((k * k) / ((betainfinity - betazero) * betazero));
        double X = betazero + ((betainfinity - betazero) * (1 - Math.exp(h)));

        if (X < s) {
            // price equals intrinsic value
            return Math.abs(s - k);
        } else {
            double alpha = (X - k) * Math.pow(X, -beta);
            double tmp1 = alpha * Math.pow(s, beta);
            double tmp2 = alpha * phi(s, t, beta, X, X, v, r, b);
            double tmp3 = phi(s, t, 1, X, X, v, r, b);
            double tmp4 = phi(s, t, 1, k, X, v, r, b);
            double tmp5 = k * phi(s, t, 0, X, X, v, r, b);
            double tmp6 = k * phi(s, t, 0, k, X, v, r, b);
            return tmp1 - tmp2 + tmp3 - tmp4 - tmp5 + tmp6;
        }
    }

    private double phi(double s, double t, double gamma, double h, double X, double v, double r, double b) {
        double v2 = v * v;
        double K = ((2 * b) / v2) + (2 * gamma - 1);
        double lambda = -r + (gamma * b) + ((0.5 * gamma * (gamma - 1)) * v2);
        final double vXsqrtT = v * Math.sqrt(t);
        double tmp1 = ((Math.log(s / h)) + (b + (gamma - 0.5) * v2) * t) / vXsqrtT;
        //double tmp2 = (Math.log((X * X) / (s * h)) + (b + (gamma - 0.5) * v2) * t) / (v * Math.pow(t, 0.5));
        double tmp2 = tmp1 + 2 * Math.log(X / s) / vXsqrtT;

        return Math.exp(lambda * t) * Math.pow(s, gamma) * (cdf(-tmp1) - (Math.pow(X / s, K)) * cdf(-tmp2));
    }

    public double delta(String type, double s, double k, double v, double t, double r, double q) {
        double umove = 1.01;
        double dmove = 1 / umove;
        double uval = priceOption(type, s * umove, k, t, v, r, q);
        double dval = priceOption(type, s * dmove, k, t, v, r, q);
        return (uval - dval) / (s * (umove - dmove));
    }

    public double gamma(double s, double k, double v, double t, double r, double q) {
        return s_blackScholes.gamma(s, k, v, t, r, q);
    }

    public double vega(String type, double s, double k, double v, double t, double r, double q) {
        double m = 0.01;
        double val1 = priceOption(type, s, k, t, v, r, q);
        double val2 = priceOption(type, s, k, t, v + m, r, q);
        return (val2 - val1) / m / 100d;
    }

    public double theta(String type, double s, double k, double v,
                        double t, double r, double q) {
        double y = t - Constants.ONE_DAY;
        y = (y < 0) ? 0 : y;
        double val = priceOption(type, s, k, t, v, r, q);
        double valt = priceOption(type, s, k, y, v, r, q);
        return valt - val;
    }

    public double rho(String type, double s, double k, double v, double t, double r, double q) {
        return s_blackScholes.rho(type, s, k, v, t, r, q);
    }

    public double impliedVol(String type, double p, double s, double k, double r, double t, double v, double q) {
        v = v == 0d ? 0.5 : v;
        double errlimit = Constants.IV_PRECISION;
        double maxloops = 100;
        double dv = errlimit + 1;
        double n = 0;
        while (Math.abs(dv) > errlimit && n < maxloops) {
            double difval = priceOption(type, s, k, t, v, r, q) - p;
            double v1 = vega(type, s, k, v, t, r, q) / 0.01;
            dv = difval / v1;
            v = v - dv;
            n++;
        }
        return n < maxloops ? v : Double.NaN;
    }
}
