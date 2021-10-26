package com.cgreen.ygocardtracker.remote;

public enum RemoteDBKey {
    NAME, PASSCODE;
    
    @Override
    public String toString() {
        return switch (this) {
            case NAME -> "name";
            case PASSCODE -> "id";
        };
    }
}
