package com.appointmentscheduler.eventprocessor;

import java.util.List;

public record SlotCancelledMessage(
        String slotId,
        List<String> affectedStudentIds
) {}