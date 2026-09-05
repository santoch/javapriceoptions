package com.santoch.optionpricing.vanilla;

import com.santoch.optionpricing.common.IOptionModel;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class OptionModelFactoryTest {
    @Test
    public void create_returnsBlackScholes() {
        IOptionModel model = OptionModelFactory.create("BlackScholes");
        assertInstanceOf(BlackScholes.class, model);
    }

    @Test
    public void create_returnsBjerksundStenslandByName() {
        IOptionModel model = OptionModelFactory.create("BjerksundStensland");
        assertInstanceOf(BjerksundStensland.class, model);
    }

    @Test
    public void create_returnsBjerksundStenslandForUnknownName() {
        IOptionModel model = OptionModelFactory.create("NoSuchModel");
        assertInstanceOf(BjerksundStensland.class, model);
    }

    @Test
    public void getDefaultName_returnsBjerksundStensland() {
        assertEquals("BjerksundStensland", OptionModelFactory.getDefaultName());
    }
}
