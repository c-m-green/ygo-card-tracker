package com.cgreen.ygocardtracker.db.exports;

public class CardImportEntry {
    private int groupId, deckId, passcode;
    private String name, setCode;
    
    CardImportEntry() { }
    
    public int getGroupId() {
        return groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public int getDeckId() {
        return deckId;
    }
    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }
    public int getPasscode() {
        return passcode;
    }
    public void setPasscode(int passcode) {
        this.passcode = passcode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSetCode() {
        return setCode;
    }
    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }
}
