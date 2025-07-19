package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class VisitScheduler {

    public Date scheduleVisit(VendorApplication vendor) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7); // +7 days from now
        return cal.getTime();
    }
}
