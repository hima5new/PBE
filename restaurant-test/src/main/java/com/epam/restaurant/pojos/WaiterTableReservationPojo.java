package com.epam.restaurant.pojos;

public class WaiterTableReservationPojo {

    private String clientType;
    private String customerName;
    private String locationId;
    private String tableNumber;
    private String date;
    private String timeFrom;
    private String timeTo;
    private String guestsNumber;


    public WaiterTableReservationPojo(Builder builder) {
        this.clientType = builder.clientType;
        this.customerName = builder.customerName;
        this.locationId = builder.locationId;
        this.tableNumber = builder.tableNumber;
        this.date = builder.date;
        this.timeFrom = builder.timeFrom;
        this.timeTo = builder.timeTo;
        this.guestsNumber = builder.guestsNumber;
    }


    public String getClientType() {
        return clientType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public String getDate() {
        return date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getGuestsNumber() {
        return guestsNumber;
    }

    @Override
    public String toString() {
        return "WaiterTableReservationPojo{" +
                "clientType='" + clientType + '\'' +
                ", customerName='" + customerName + '\'' +
                ", date='" + date + '\'' +
                ", guestsNumber='" + guestsNumber + '\'' +
                ", locationId='" + locationId + '\'' +
                ", tableNumber='" + tableNumber + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                '}';
    }


    public static class Builder {
        private String clientType;
        private String customerName;
        private String locationId;
        private String tableNumber;
        private String date;
        private String timeFrom;
        private String timeTo;
        private String guestsNumber;

        public Builder setClientType(String clientType) {
            this.clientType = clientType;
            return this;
        }

        public Builder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder setLocationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Builder setTableNumber(String tableNumber) {
            this.tableNumber = tableNumber;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setTimeFrom(String timeFrom) {
            this.timeFrom = timeFrom;
            return this;
        }

        public Builder setTimeTo(String timeTo) {
            this.timeTo = timeTo;
            return this;
        }

        public Builder setGuestsNumber(String guestsNumber) {
            this.guestsNumber = guestsNumber;
            return this;
        }

        public WaiterTableReservationPojo build() {
            return new WaiterTableReservationPojo(this);
        }
    }
}
