package com.santoch.optionpricing.common;

import java.time.ZonedDateTime;

public interface IGreeks {
    double getDelta();

    void setDelta(double delta);

    double getGamma();

    void setGamma(double gamma);

    double getTheta();

    void setTheta(double theta);

    double getVega();

    void setVega(double vega);

    double getRho();

    void setRho(double rho);

    double getBidIv();

    void setBidIv(double bid_iv);

    double getMidIv();

    void setMidIv(double mid_iv);

    double getAskIv();

    void setAskIv(double ask_iv);

    double getSmvVol();

    void setSmvVol(double smv_vol);

    ZonedDateTime getUpdateTime();

    void setUpdateTime(ZonedDateTime update_time);
}
