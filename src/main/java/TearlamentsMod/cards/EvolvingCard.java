package TearlamentsMod.cards;

import TearlamentsMod.cards.generic.b_uncommon.Mudragon;
import TearlamentsMod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;


public abstract class EvolvingCard extends BaseCard {

    public AbstractCard cardToFuseInto;

    public EvolvingCard(String ID, CardStats info) {
        super(ID, info);
        this.defineCardToFuseInto();
//        this.defineCardToFuseIntoFallback();
    }

    public EvolvingCard(String ID, CardStats info, String cardImage) {
        super(ID, info, cardImage);
        this.defineCardToFuseInto();
//        this.defineCardToFuseIntoFallback();
    }

    public EvolvingCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color) {
        super(ID, cost, cardType, target, rarity, color);
        this.defineCardToFuseInto();
//        this.defineCardToFuseIntoFallback();
    }

    public EvolvingCard(String ID, int cost, CardType cardType, CardTarget target, CardRarity rarity, CardColor color, String cardImage) {
        super(ID, cost, cardType, target, rarity, color, cardImage);
        this.defineCardToFuseInto();
//        this.defineCardToFuseIntoFallback();
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (cardToFuseInto != null && cardToFuseInto != this && !cardToFuseInto.upgraded){
            cardToFuseInto.upgrade();
        }
    }

    public abstract void defineCardToFuseInto();

    public abstract AbstractCard getCardToFuseInto();

    public void defineCardToFuseIntoFallback(){
        if (cardToFuseInto == null){
            cardToFuseInto = new Mudragon();
        }
    }

    @Override
    public void onChoseThisOption() {
        super.onChoseThisOption();
        addToBot(new MakeTempCardInHandAction(this));
    }
}