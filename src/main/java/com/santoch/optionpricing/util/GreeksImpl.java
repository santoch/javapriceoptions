package com.santoch.optionpricing.util;

import com.santoch.optionpricing.common.IGreeks;

import java.time.ZonedDateTime;

public class GreeksImpl implements IGreeks {
    private double delta;
    private double gamma;
    private double theta;
    private double vega;
    private double rho;
    private double bidIv;
    private double midIv;
    private double askIv;
    private double smvVol;
    private ZonedDateTime updateTime;

    @Override
    public double getDelta() {
        return delta;
    }

    @Override
    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public double getGamma() {
        return gamma;
    }

    @Override
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public double getTheta() {
        return theta;
    }

    @Override
    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public double getVega() {
        return vega;
    }

    @Override
    public void setVega(double vega) {
        this.vega = vega;
    }

    @Override
    public double getRho() {
        return rho;
    }

    @Override
    public void setRho(double rho) {
        this.rho = rho;
    }

    @Override
    public double getBidIv() {
        return bidIv;
    }

    @Override
    public void setBidIv(double bid_iv) {
        this.bidIv = bid_iv;
    }

    @Override
    public double getMidIv() {
        return midIv;
    }

    @Override
    public void setMidIv(double mid_iv) {
        this.midIv = mid_iv;
    }

    @Override
    public double getAskIv() {
        return askIv;
    }

    @Override
    public void setAskIv(double ask_iv) {
        this.askIv = ask_iv;
    }

    @Override
    public double getSmvVol() {
        return smvVol;
    }

    @Override
    public void setSmvVol(double smv_vol) {
        this.smvVol = smv_vol;
    }

    @Override
    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(ZonedDateTime update_time) {
        this.updateTime = update_time;
    }
}
