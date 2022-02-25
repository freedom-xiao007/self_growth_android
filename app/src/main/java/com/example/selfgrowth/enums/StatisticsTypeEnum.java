package com.example.selfgrowth.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StatisticsTypeEnum {
    
    DEFAULT("天"),
    DAY("天"),
    WEEK("周"),
    MONTH("月"),
    YEAR("年"),
    ;
    
    private String name;

    public static StatisticsTypeEnum fromString(final String name) {
        for (StatisticsTypeEnum label: StatisticsTypeEnum.values()) {
            if (label.name.equalsIgnoreCase(name)) {
                return label;
            }
        }
        return DEFAULT;
    }

    public static List<String> names() {
        return Arrays.stream(StatisticsTypeEnum.values()).map(e -> e.name).collect(Collectors.toList());
    }
}
