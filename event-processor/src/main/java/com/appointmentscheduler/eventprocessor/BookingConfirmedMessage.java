package com.appointmentscheduler.eventprocessor;

import java.time.Instant;

public record BookingConfirmedMessage(
        String bookingId,
        String slotId,
        String studentId,
        Instant timestamp
) {}