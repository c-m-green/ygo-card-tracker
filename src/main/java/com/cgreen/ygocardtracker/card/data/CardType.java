package com.cgreen.ygocardtracker.card.data;

public enum CardType {
    UNKNOWN(0, "Unknown"),
    MONSTER_EFFECT(1, "Effect Monster"),
    MONSTER_FLIP_EFFECT(2, "Flip Effect Monster"),
    MONSTER_FLIP_TUNER_EFFECT(3, "Flip Tuner Effect Monster"),
    MONSTER_GEMINI(4, "Gemini Monster"),
    MONSTER_NORMAL(5, "Normal Monster"),
    MONSTER_NORMAL_TUNER(6, "Normal Tuner Monster"),
    MONSTER_PENDULUM_EFFECT(7, "Pendulum Effect Monster"),
    MONSTER_PENDULUM_FLIP_EFFECT(8, "Pendulum Flip Effect Monster"),
    MONSTER_PENDULUM_NORMAL(9, "Pendulum Normal Monster"),
    MONSTER_PENDULUM_TUNER_EFFECT(10, "Pendulum Tuner Effect Monster"),
    MONSTER_RITUAL_EFFECT(11, "Ritual Effect Monster"),
    MONSTER_RITUAL(12, "Ritual Monster"),
    SKILL(13, "Skill Card"),
    SPELL(14, "Spell Card"),
    MONSTER_SPIRIT(15, "Spirit Monster"),
    MONSTER_TOON(16, "Toon Monster"),
    TRAP(17, "Trap Card"),
    MONSTER_TUNER(18, "Tuner Monster"),
    MONSTER_UNION_EFFECT(19, "Union Effect Monster"),
    MONSTER_FUSION(20, "Fusion Monster"),
    MONSTER_LINK(21, "Link Monster"),
    MONSTER_PENDULUM_EFFECT_FUSION(22, "Pendulum Effect Fusion Monster"),
    MONSTER_SYNCHRO(23, "Synchro Monster"),
    MONSTER_SYNCHRO_PENDULUM_EFFECT(24, "Synchro Pendulum Effect Monster"),
    MONSTER_SYNCHRO_TUNER(25, "Synchro Tuner Monster"),
    MONSTER_XYZ(26, "XYZ Monster"),
    MONSTER_XYZ_PENDULUM_EFFECT(27, "XYZ Pendulum Effect Monster");
    
    private final int index;
    private final String label;
    private static final CardType[] list = CardType.values();
    private CardType(int index, String label) {
        this.index = index;
        this.label = label;
    }
    public int getIndex() {
        return this.index;
    }
    
    public static int getNumberOfCardTypes() {
        return list.length;
    }
    
    public static int getIndexOf(String label) {
        for (CardType cardType : list) {
            if (cardType.toString().equalsIgnoreCase(label)) {
                return cardType.getIndex();
            }
        }
        return -1;
    }
    
    public static CardType getCardType(int index) {
        try {
            return list[index];
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            return UNKNOWN;
        }
    }
    
    @Override
    public String toString() {
        return label;
    }
}
