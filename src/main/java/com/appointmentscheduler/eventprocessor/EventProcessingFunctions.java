package com.appointmentscheduler.eventprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class EventProcessingFunctions {

    private static final Logger log =
            LoggerFactory.getLogger(EventProcessingFunctions.class);

    /**
     * Consumes BookingCreated, sends notification, returns BookingConfirmed.
     *
     * Binding names (ch. 10 convention):
     *   input:  processBooking-in-0  → destination: booking.created
     *   output: processBooking-out-0 → destination: booking.confirmed
     */
    @Bean
    public Function<BookingCreatedMessage, BookingConfirmedMessage> processBooking() {
        return message -> {
            log.info("Consumed BookingCreated — bookingId={}, slotId={}, studentId={}",
                    message.bookingId(), message.slotId(), message.studentId());

            // Notification stub — replace with real strategy later
            log.info("[NOTIFY] Booking confirmed for studentId={}, bookingId={}",
                    message.studentId(), message.bookingId());

            var confirmed = new BookingConfirmedMessage(
                    message.bookingId(),
                    message.slotId(),
                    message.studentId(),
                    Instant.now()
            );

            log.info("Publishing BookingConfirmed — bookingId={}", confirmed.bookingId());
            return confirmed;
        };
    }

    /**
     * Consumes SlotCancelled and notifies all affected students.
     * Consumer<Flux<T>> = reactive consumer, per ch. 10 listing 10.19.
     *
     * Binding name:
     *   input: notifySlotCancelled-in-0 → destination: slot.cancelled
     *
     * Note: .subscribe() is required to activate the reactive stream (ch. 10, p.357).
     */
    @Bean
    public Consumer<Flux<SlotCancelledMessage>> notifySlotCancelled() {
        return flux -> flux
                .doOnNext(message -> {
                    log.info("Consumed SlotCancelled — slotId={}, affectedStudents={}",
                            message.slotId(), message.affectedStudentIds());
                    message.affectedStudentIds().forEach(studentId ->
                            log.info("[NOTIFY] Slot {} cancelled, notifying studentId={}",
                                    message.slotId(), studentId)
                    );
                })
                .subscribe();  // required — no subscriber = no data flows (ch. 10, p.357)
    }
}