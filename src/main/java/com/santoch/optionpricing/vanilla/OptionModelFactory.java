package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.common.IOptionModel;

/**
 * Factory for option pricing models. Supports "BlackScholes", "BjerksundStensland",
 * and default (always returns BjerksundStensland). Name registry added later when 
 * additional models are introduced.
 */
public final class OptionModelFactory {

    private OptionModelFactory() {}

    public static IOptionModel create(String name) {
        switch (name) {
            case "BlackScholes":
                return new BlackScholes();
            case "BjerksundStensland":
                return new BjerksundStensland();
            default:
                return new BjerksundStensland();
        }
    }

    public static String getDefaultName() {
        return "BjerksundStensland";
    }
}
