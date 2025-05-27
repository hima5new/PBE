package com.epam.restaurant.pojos;


public class CustomerTableReservationPojo {
    private String locationId;
    private String tableNumber;
    private String date;
    private String guestsNumber;
    private String timeFrom;
    private String timeTo;

    public CustomerTableReservationPojo() {}

    public String getLocationId() { return locationId; }
    public String getTableNumber() { return tableNumber; }
    public String getDate() { return date; }
    public String getGuestsNumber() { return guestsNumber; }
    public String getTimeFrom() { return timeFrom; }
    public String getTimeTo() { return timeTo; }

    public static class Builder {
        private String locationId;
        private String tableNumber;
        private String date;
        private String guestsNumber;
        private String timeFrom;
        private String timeTo;

        public Builder setLocationId(String locationId) { this.locationId = locationId; return this; }
        public Builder setTableNumber(String tableNumber) { this.tableNumber = tableNumber; return this; }
        public Builder setDate(String date) { this.date = date; return this; }
        public Builder setGuestsNumber(String guestsNumber) { this.guestsNumber = guestsNumber; return this; }
        public Builder setTimeFrom(String timeFrom) { this.timeFrom = timeFrom; return this; }
        public Builder setTimeTo(String timeTo) { this.timeTo = timeTo; return this; }

        public CustomerTableReservationPojo build() {
            CustomerTableReservationPojo reservation = new CustomerTableReservationPojo();
            reservation.locationId = this.locationId;
            reservation.tableNumber = this.tableNumber;
            reservation.date = this.date;
            reservation.guestsNumber = this.guestsNumber;
            reservation.timeFrom = this.timeFrom;
            reservation.timeTo = this.timeTo;
            return reservation;
        }
    }

    @Override
    public String toString() {
        return "CustomerTableReservationPojo{" +
                "locationId='" + locationId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", date='" + date + '\'' +
                ", guestsNumber='" + guestsNumber + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                '}';
    }
}