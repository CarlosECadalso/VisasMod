package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.RecycleAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.Random;

public class ClearNewWorldAction extends RecycleAction {
    private static final AbstractPlayer p = AbstractDungeon.player;
    private final int drawAmount;
    public static String[] TEXT = new String[2];
    public AbstractCard cardDiscarded;
    public AbstractCard.CardType cardTypeToGet;
    private boolean isRandom;

    public ClearNewWorldAction(int drawAmount){
        super();
        this.drawAmount = drawAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.cardDiscarded = null;
        this.isRandom = true;
        this.amount = 1;
        TEXT[0] = "Select a card to discard:";
        TEXT[1] = "Select a card to add to hand";
    }

    public ClearNewWorldAction(int drawAmount, boolean isRandom){
        super();
        this.drawAmount = drawAmount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.isRandom = isRandom;
        TEXT[0] = "Select a card to discard:";
        TEXT[1] = "Select a card to add to hand";
    }

    private void clearNewWorldRandom(){
        this.cardTypeToGet = getCardTypeToGet(this.cardDiscarded);
        addToBot(new DrawPileToHandAction(this.drawAmount, cardTypeToGet));
    }

    private void clearNewWorldNotRandom(){
        // If this is too strong -> rework to exclude cards of the same type as card discarded
//        addToBot(new BetterDrawPileToHandAction(drawAmount));

        // Rework


        if (p.drawPile.size() == 0) {
            VisasMod.logger.info("No cards to discard");
            this.isDone = true;
            return;
        }

        AbstractCard.CardType cardTypeToAvoid = this.cardDiscarded.type;
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard c : p.drawPile.group) {
            if (c.type != cardTypeToAvoid){
                temp.addToTop(c);
            }
        }

        temp.sortAlphabetically(true);
        temp.sortByRarityPlusStatusCardType(false);

        if (temp.isEmpty()){
            VisasMod.logger.info("No cards to discard after exceptions");
            this.isDone = true;
            return;
        } else if (temp.size() == this.amount){
            for (int i = 0; i < this.amount; i++){
                p.drawPile.moveToHand(temp.getTopCard());
                temp.removeTopCard();
            }
            this.isDone = true;
            return;
        } else {
            AbstractDungeon.gridSelectScreen.open(temp, this.amount, TEXT[1], false, false, false, false);
        }
    }

    @Override
    public void update(){
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (p.hand.isEmpty()) {
                this.isDone = true;
            } else if (p.hand.size() == 1) {

                this.cardDiscarded = p.hand.getBottomCard();
                p.hand.moveToDiscardPile(p.hand.getBottomCard());
                this.cardDiscarded.triggerOnManualDiscard();

                if (this.isRandom){
                    this.clearNewWorldRandom();
                } else {
                    this.clearNewWorldNotRandom();
                }
                this.tickDuration();

            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                this.tickDuration();
            }

        } else if (this.duration == this.startDuration){
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {

                    this.cardDiscarded = c;

                    p.hand.moveToDiscardPile(c);
                    this.cardDiscarded.triggerOnManualDiscard();

                    if (this.isRandom){
                        clearNewWorldRandom();
                        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                        this.isDone = true;
                        return;
                    } else {
                        clearNewWorldNotRandom();
                        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                        this.tickDuration();
                    }
                }


            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0){
                p.drawPile.moveToHand(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            }
        }
    }

    private static AbstractCard.CardType getCardTypeToGet(AbstractCard cardDiscarded) {

        
        ArrayList<AbstractCard.CardType> cardTypes = new ArrayList<AbstractCard.CardType>();
                // {AbstractCard.CardType.ATTACK, AbstractCard.CardType.SKILL, AbstractCard.CardType.POWER};

        if (cardDiscarded.type != AbstractCard.CardType.ATTACK && !p.drawPile.getCardsOfType(AbstractCard.CardType.ATTACK).isEmpty()){
            cardTypes.add(AbstractCard.CardType.ATTACK);
        }
        if (cardDiscarded.type != AbstractCard.CardType.POWER && !p.drawPile.getCardsOfType(AbstractCard.CardType.POWER).isEmpty()) {
            cardTypes.add(AbstractCard.CardType.POWER);
        }
        if (cardDiscarded.type != AbstractCard.CardType.SKILL && !p.drawPile.getCardsOfType(AbstractCard.CardType.SKILL).isEmpty()) {
            cardTypes.add(AbstractCard.CardType.SKILL);
        }
        if (p.drawPile.group.isEmpty()) {
            cardTypes.add(AbstractCard.CardType.ATTACK);
        }
        if (p.drawPile.getCardsOfType(AbstractCard.CardType.SKILL).isEmpty() &&
                p.drawPile.getCardsOfType(AbstractCard.CardType.ATTACK).isEmpty() &&
                p.drawPile.getCardsOfType(AbstractCard.CardType.POWER).isEmpty()){
            cardTypes.add(AbstractCard.CardType.ATTACK);
        }

        Random rand = new Random();
        int n = rand.nextInt(cardTypes.size());
        return cardTypes.get(n);
    }
}
