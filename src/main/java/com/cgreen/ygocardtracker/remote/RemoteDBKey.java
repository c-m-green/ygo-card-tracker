package com.cgreen.ygocardtracker.remote;

public enum RemoteDBKey {
    NAME, PASSCODE;
    
    @Override
    public String toString() {
        switch(this) {
        case NAME: return "name";
        case PASSCODE: return "id";
        default: throw new IllegalArgumentException();
        }
    }
}
