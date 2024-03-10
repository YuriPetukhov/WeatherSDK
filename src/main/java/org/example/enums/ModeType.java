package org.example.enums;

import lombok.Getter;

@Getter
public enum ModeType {
    ON_DEMAND,
    POLLING;

    public ModeType toggle() {
        if (this == ON_DEMAND) {
            return POLLING;
        } else {
            return ON_DEMAND;
        }
    }
}
