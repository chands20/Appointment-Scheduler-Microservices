package com.appointmentscheduler.eventprocessor;

import java.time.Instant;

public record BookingCreatedMessage(
        String bookingId,
        String slotId,
        String studentId,
        Instant timestamp
) {}