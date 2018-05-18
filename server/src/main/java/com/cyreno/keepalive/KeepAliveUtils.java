package com.cyreno.keepalive;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;

@Getter
@Component
public class KeepAliveUtils {

    private final ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final String brokerUrl;
    private final String topic;
    private final int ttlInSeconds;
    private final int qos;

    public KeepAliveUtils(
            @Value("${keepalive.openingTime}") String openingTime,
            @Value("${keepalive.closingTime}") String closingTime,
            @Value("${keepalive.receiver.brokerUrl}") String brokerUrl,
            @Value("${keepalive.receiver.topic}") String topic,
            @Value("${keepalive.ttlInSeconds}") int ttlInSeconds,
            @Value("${keepalive.receiver.qos}") int qos) {
        this.openingTime = LocalTime.parse(openingTime);
        this.closingTime = LocalTime.parse(closingTime);
        this.ttlInSeconds = ttlInSeconds;
        this.brokerUrl = brokerUrl;
        this.topic = topic;
        this.qos = qos;
    }

    // TODO: Move methods below to a proper class

    public Instant getTodayOpening() {
        return getInstant(LocalDate.now(), getOpeningTime());
    }

    public Instant getOpening(LocalDate date) {
        return getInstant(date, getOpeningTime());
    }

    public Instant getTodayClosing() {
        return getInstant(LocalDate.now(), getClosingTime());
    }

    public Instant getClosing(LocalDate date) {
        return getInstant(date, getClosingTime());
    }

    private Instant getInstant(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).toInstant(getZoneOffset());
    }

    public LocalDateTime getOpening(LocalDateTime start) {
        return LocalDateTime.ofInstant(getOpening(start.toLocalDate()), zoneOffset);
    }

    public LocalDateTime getClosing(LocalDateTime end) {
        return LocalDateTime.ofInstant(getClosing(end.toLocalDate()), zoneOffset);
    }

}


