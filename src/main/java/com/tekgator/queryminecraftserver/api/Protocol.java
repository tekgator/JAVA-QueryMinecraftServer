package com.tekgator.queryminecraftserver.api;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public enum Protocol {
    TCP(47),                // query via TCP for every Minecraft version starting at 1.7 and above
    TCP_DEPRECIATED(74),    // query via TCP for every Minecraft version including and below 1.6
    UDP_BASIC(1),           // basic information query via UDP
    UDP_FULL(2);            // full information query via UDP

    private final int value;

    Protocol(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}