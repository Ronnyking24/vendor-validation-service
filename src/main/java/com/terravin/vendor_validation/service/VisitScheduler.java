package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class VisitScheduler {

    public void scheduleVisit(VendorApplication vendor) {
        LocalDate today = LocalDate.now();
        LocalDate visitDate = getNextWeekday(today);

        System.out.println("Scheduled visit for " + vendor.getCompanyName() + " on " + visitDate);
        // In a real system, this could save to a "visits" table or notify an agent.
    }

    private LocalDate getNextWeekday(LocalDate date) {
        LocalDate next = date.plusDays(1);
        while (next.getDayOfWeek() == DayOfWeek.SATURDAY || next.getDayOfWeek() == DayOfWeek.SUNDAY) {
            next = next.plusDays(1);
        }
        return next;
    }
}
