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
    SPELL_NORMAL(24, "Normal (S)"),
    SPELL_FIELD(25, "Field"),
    SPELL_EQUIP(26, "Equip"),
    SPELL_CONTINUOUS(27, "Continuous"),
    SPELL_QUICK_PLAY(28, "Quick-Play"),
    SPELL_RITUAL(29, "Ritual"),
    TRAP_NORMAL(30, "Normal (T)"),
    TRAP_CONTINUOUS(31, "Continuous"),
    TRAP_COUNTER(32, "Counter");
    
    private int index;
    private String label;
    private static CardVariant[] list = CardVariant.values();
    private CardVariant(int index, String label) {
        this.index = index;
        this.label = label;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public static int getNumberOfCardVariants() {
        return list.length;
    }
    
    public static int getIndexOf(String label) {
        for (CardVariant cardVariant : list) {
            if (cardVariant.toString().equalsIgnoreCase(label)) {
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
