package com.cgreen.ygocardtracker.card.data;

public enum CardVariant {
    UNKNOWN(0, "Unknown"),
    AQUA(1, "Aqua"),
    BEAST(2, "Beast"),
    BEAST_WARRIOR(3, "Beast-Warrior"),
    CREATOR_GOD(4, "Creator-God"),
    CYBERSE(5, "Cyberse"),
    DINOSAUR(6, "Dinosaur"),
    DIVINE_BEAST(7, "Divine-Beast"),
    DRAGON(8, "Dragon"),
    FAIRY(9, "Fairy"),
    FIEND(10, "Fiend"),
    FISH(11, "Fish"),
    INSECT(12, "Insect"),
    MACHINE(13, "Machine"),
    PLANT(14, "Plant"),
    PSYCHIC(15, "Psychic"),
    PYRO(16, "Pyro"),
    REPTILE(17, "Reptile"),
    ROCK(18, "Rock"),
    SEA_SERPENT(19, "Sea Serpent"),
    SPELLCASTER(20, "Spellcaster"),
    THUNDER(21, "Thunder"),
    WARRIOR(22, "Warrior"),
    WINGED_BEAST(23, "Winged Beast"),
    WYRM(24, "Wyrm"),
    ZOMBIE(25, "Zombie"),
    SPELL_NORMAL(26, "Normal", "Normal Spell"),
    SPELL_FIELD(27, "Field", "Field Spell"),
    SPELL_EQUIP(28, "Equip", "Equip Spell"),
    SPELL_CONTINUOUS(29, "Continuous", "Continuous Spell"),
    SPELL_QUICK_PLAY(30, "Quick-Play", "Quick-Play Spell"),
    SPELL_RITUAL(31, "Ritual", "Ritual Spell"),
    TRAP_NORMAL(32, "Normal", "Normal Trap"),
    TRAP_CONTINUOUS(33, "Continuous", "Continuous Trap"),
    TRAP_COUNTER(34, "Counter", "Counter Trap");
    
    private final int index;
    private final String code, label;
    private static final CardVariant[] list = CardVariant.values();
    private CardVariant(int index, String code, String label) {
        this.index = index;
        this.code = code;
        this.label = label;
    }
    
    private CardVariant(int index, String code) {
        this.index = index;
        this.code = code;
        label = this.code;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getCode() {
        return code;
    }
    
    public static int getNumberOfCardVariants() {
        return list.length;
    }
    
    public static int getIndexOf(String code) {
        for (CardVariant cardVariant : list) {
            if (cardVariant.getCode().equalsIgnoreCase(code)) {
                return cardVariant.getIndex();
            }
        }
        return -1;
    }
    
    public static CardVariant getCardVariant(int index) {
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
