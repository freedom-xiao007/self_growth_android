package com.example.selfgrowth.enums;

import org.junit.Test;

import java.util.List;

public class LabelEnumTest {

    @Test
    public void test() {
        assert LabelEnum.valueOf("LEARN").equals(LabelEnum.LEARN);
        assert LabelEnum.fromString("学习").equals(LabelEnum.LEARN);
        List<String> labels = LabelEnum.names();
        assert labels.get(0).equals("其他");
    }
}
