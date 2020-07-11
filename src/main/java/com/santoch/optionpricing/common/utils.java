package com.santoch.optionpricing.common;

import com.santoch.optionpricing.util.GreeksImpl;

import java.time.ZonedDateTime;

public class utils {
    static public IGreeks greeks(IOptionModel model, ZonedDateTime updateTime, String type,
                                 double bid, double ask,
                                 double smv,
                                 double s, double k, double r, double t, double v, double q, boolean useSmvVol) {

        GreeksImpl greeks = new GreeksImpl();
        double mid = (bid + ask) / 2.0;
        smv = smv > 0 ? smv : mid;

        double smvVol = model.impliedVol(type, smv, s, k, r, t, v, q);
        greeks.setSmvVol(smvVol);
        if (useSmvVol) {
            v = smvVol;
        }

        greeks.setUpdate_time(updateTime);
        greeks.setAskIv(model.impliedVol(type, ask, s, k, r, t, v, q));
        greeks.setBidIv(model.impliedVol(type, bid, s, k, r, t, v, q));
        greeks.setMidIv(model.impliedVol(type, mid, s, k, r, t, v, q));
        greeks.setDelta(model.delta(type,s, k, v, t, r, q));
        greeks.setGamma(model.gamma(s, k, v, t, r, q));
        greeks.setDelta(model.delta(type,s, k, v, t, r, q));
        greeks.setTheta(model.theta(type,s, k, v, t, r, q));
        greeks.setVega(model.vega(type,s, k, v, t, r, q));
        greeks.setRho(model.rho(type,s, k, v, t, r, q));

        return greeks;
    }
}
