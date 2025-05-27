package com.epam.restaurant.util.api;

import java.util.HashMap;
import java.util.Map;

public class TestDataUtil {

    private static final Map<String, String> mockReservationStatusMap = new HashMap<>();

    static {
        mockReservationStatusMap.put("864e6c1d", "Finished");
        mockReservationStatusMap.put("b1102dfc", "In Progress");
        mockReservationStatusMap.put("inProgressId", "In Progress");
        mockReservationStatusMap.put("finishedId", "Finished");
    }

    public static void setReservationStatus(String reservationId, String status) {
        mockReservationStatusMap.put(reservationId, status);
    }

    public static String getReservationStatus(String reservationId) {
        return mockReservationStatusMap.get(reservationId);
    }

    public static boolean hasReservation(String reservationId) {
        return mockReservationStatusMap.containsKey(reservationId);
    }

    public static void clear() {
        mockReservationStatusMap.clear();
    }
}
