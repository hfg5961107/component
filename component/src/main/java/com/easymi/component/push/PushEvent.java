package com.easymi.component.push;

import com.easymi.component.entity.PassengerLocation;

public class PushEvent {
    private PassengerLocation passengerLocation;

    public PushEvent(PassengerLocation passengerLocation) {
        this.passengerLocation = passengerLocation;
    }

    public PassengerLocation getPassengerLocation() {
        return passengerLocation;
    }
}
