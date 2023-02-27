package pl.sda.cleancode.application.util;

import pl.sda.cleancode.application.exception.DomainIllegalArgumentException;

public final class DomainArgumentCheck {

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new DomainIllegalArgumentException(message);
        }
    }
}
