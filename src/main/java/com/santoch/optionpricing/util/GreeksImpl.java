package com.santoch.optionpricing.util;

import com.santoch.optionpricing.common.IGreeks;

import java.time.ZonedDateTime;

public class GreeksImpl implements IGreeks {
    private double delta;
    private double gamma;
    private double theta;
    private double vega;
    private double rho;
    private double bid_iv;
    private double mid_iv;
    private double ask_iv;
    private double smv_vol;
    private ZonedDateTime update_time;

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
        return bid_iv;
    }

    @Override
    public void setBidIv(double bid_iv) {
        this.bid_iv = bid_iv;
    }

    @Override
    public double getMidIv() {
        return mid_iv;
    }

    @Override
    public void setMidIv(double mid_iv) {
        this.mid_iv = mid_iv;
    }

    @Override
    public double getAskIv() {
        return ask_iv;
    }

    @Override
    public void setAskIv(double ask_iv) {
        this.ask_iv = ask_iv;
    }

    @Override
    public double getSmvVol() {
        return smv_vol;
    }

    @Override
    public void setSmvVol(double smv_vol) {
        this.smv_vol = smv_vol;
    }

    @Override
    public ZonedDateTime getUpdate_time() {
        return update_time;
    }

    @Override
    public void setUpdate_time(ZonedDateTime update_time) {
        this.update_time = update_time;
    }
}
