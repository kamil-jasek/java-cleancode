package pl.sda.cleancode.application.util;

import java.util.UUID;

final public class UUIDExtension {
    public static UUID uuid(String name) {
        return UUID.fromString(name);
    }
}
