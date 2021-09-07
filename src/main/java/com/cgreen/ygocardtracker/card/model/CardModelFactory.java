package com.cgreen.ygocardtracker.card.model;

import com.cgreen.ygocardtracker.card.data.CardType;

public class CardModelFactory {
    public CardModelFactory() { }
    
    public CardModel getCardModel(CardType cardType) {
        CardModel cardModel = null;
        switch(cardType) {
        case MONSTER_FLIP_TUNER_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsFlip(true);
            ((MonsterCardModel) cardModel).setIsTuner(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_FLIP_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsFlip(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_GEMINI:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsGemini(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_NORMAL:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_NORMAL_TUNER:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsTuner(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_PENDULUM_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_PENDULUM_FLIP_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setIsFlip(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_PENDULUM_NORMAL:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_PENDULUM_TUNER_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setIsTuner(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_RITUAL_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsRitualSummoned(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_RITUAL:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsRitualSummoned(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_SPIRIT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsSpirit(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_TOON:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsToon(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_TUNER:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsTuner(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_UNION_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsUnion(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_FUSION:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsFusionSummoned(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_LINK:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsLink(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            break;
        case MONSTER_PENDULUM_EFFECT_FUSION:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setIsFusionSummoned(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_SYNCHRO:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsSynchro(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_SYNCHRO_PENDULUM_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsSynchro(true);
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case MONSTER_SYNCHRO_TUNER:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsSynchro(true);
            ((MonsterCardModel) cardModel).setIsTuner(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasLevel(true);
            break;
        case MONSTER_XYZ:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsXyz(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasRank(true);
            break;
        case MONSTER_XYZ_PENDULUM_EFFECT:
            cardModel = new MonsterCardModel();
            ((MonsterCardModel) cardModel).setIsXyz(true);
            ((MonsterCardModel) cardModel).setIsPendulum(true);
            ((MonsterCardModel) cardModel).setHasEffect(true);
            ((MonsterCardModel) cardModel).setHasRank(true);
            ((MonsterCardModel) cardModel).setHasScale(true);
            break;
        case SPELL:
            cardModel = new SpellCardModel();
            break;
        case TRAP:
            cardModel = new TrapCardModel();
            break;
        case UNKNOWN:
            // TODO: Need to figure out what a default card looks like
        case SKILL:
            // TODO: Need to figure out what to do here
        default:
            throw new IllegalArgumentException("Unrecognized card type");
        }
        return cardModel;
    }
}
