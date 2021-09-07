package com.cgreen.ygocardtracker.card.model;

public abstract class CardModel {
    // Xyz
    public abstract boolean hasRank();
    // Basic monster property
    public abstract boolean hasLevel();
    // Pendulum
    public abstract boolean hasScale();
    public abstract boolean isLink();
    public abstract boolean isMonster();
    public abstract boolean isSpell();
    public abstract boolean isTrap();
    public abstract boolean isXyz();
    public abstract boolean isSynchro();
    public abstract boolean hasEffect();
    public abstract boolean isFlip();
    public abstract boolean isTuner();
    public abstract boolean isGemini();
    public abstract boolean isPendulum();
    public abstract boolean isRitualSummoned();
    public abstract boolean isSpirit();
    public abstract boolean isToon();
    public abstract boolean isUnion();
    public abstract boolean isFusionSummoned();
    public abstract boolean hasAttribute();

    CardModel() { }
}
