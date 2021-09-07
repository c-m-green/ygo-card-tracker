package com.cgreen.ygocardtracker.card.model;

public class MonsterCardModel extends CardModel {
    
    private boolean hasRank, hasLevel, hasScale, isLink, isXyz, isSynchro, hasEffect, isFlip, isTuner, isGemini, isPendulum, isRitualSummoned, isSpirit, isToon, isUnion, isFusionSummoned;
    
    MonsterCardModel() { }
    
    public boolean hasRank() {
        return hasRank;
    }

    void setHasRank(boolean hasRank) {
        this.hasRank = hasRank;
    }

    public boolean hasLevel() {
        return hasLevel;
    }

    void setHasLevel(boolean hasLevel) {
        this.hasLevel = hasLevel;
    }

    public boolean hasScale() {
        return hasScale;
    }

    void setHasScale(boolean hasScale) {
        this.hasScale = hasScale;
    }

    public boolean isLink() {
        return isLink;
    }

    void setIsLink(boolean isLink) {
        this.isLink = isLink;
    }

    public boolean isXyz() {
        return isXyz;
    }

    void setIsXyz(boolean isXyz) {
        this.isXyz = isXyz;
    }

    public boolean isSynchro() {
        return isSynchro;
    }

    void setIsSynchro(boolean isSynchro) {
        this.isSynchro = isSynchro;
    }

    public boolean hasEffect() {
        return hasEffect;
    }

    void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public boolean isFlip() {
        return isFlip;
    }

    void setIsFlip(boolean isFlip) {
        this.isFlip = isFlip;
    }

    public boolean isTuner() {
        return isTuner;
    }

    void setIsTuner(boolean isTuner) {
        this.isTuner = isTuner;
    }

    public boolean isGemini() {
        return isGemini;
    }

    void setIsGemini(boolean isGemini) {
        this.isGemini = isGemini;
    }

    public boolean isPendulum() {
        return isPendulum;
    }

    void setIsPendulum(boolean isPendulum) {
        this.isPendulum = isPendulum;
    }

    public boolean isRitualSummoned() {
        return isRitualSummoned;
    }

    void setIsRitualSummoned(boolean isRitualSummoned) {
        this.isRitualSummoned = isRitualSummoned;
    }

    public boolean isSpirit() {
        return isSpirit;
    }

    void setIsSpirit(boolean isSpirit) {
        this.isSpirit = isSpirit;
    }

    public boolean isToon() {
        return isToon;
    }

    void setIsToon(boolean isToon) {
        this.isToon = isToon;
    }

    public boolean isUnion() {
        return isUnion;
    }

    void setIsUnion(boolean isUnion) {
        this.isUnion = isUnion;
    }

    public boolean isFusionSummoned() {
        return isFusionSummoned;
    }

    void setIsFusionSummoned(boolean isFusionSummoned) {
        this.isFusionSummoned = isFusionSummoned;
    }

    @Override
    public boolean isMonster() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isSpell() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTrap() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasAttribute() {
        // TODO Auto-generated method stub
        return true;
    }
}
