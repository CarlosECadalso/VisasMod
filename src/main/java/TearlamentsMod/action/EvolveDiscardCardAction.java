package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.generic.b_uncommon.Mudragon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.HashSet;

public class EvolveDiscardCardAction extends AbstractGameAction {
    private static String[] TEXT = {
            "Select a card to evolve shuffle:",
            "Select a card to evolve:"
    }; // TODO Setup the text correctly
    private final CardGroup destination;
    private AbstractCard replacement;
    private final AbstractPlayer p;
    private HashSet<AbstractCard> exceptions = null;
    private boolean optional = true;

    public EvolveDiscardCardAction(CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
    }

    public EvolveDiscardCardAction(CardGroup destination, HashSet<AbstractCard> exceptions) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_MED;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
        this.exceptions = exceptions;

    }

    private void evolveEvolvingCard(EvolvingCard card){
        VisasMod.logger.info("Transforming evolving card");
        this.p.discardPile.removeCard(card);
        this.replacement = card.getCardToFuseInto();
        if (card.upgraded){this.replacement.upgrade();}
        if (this.p.hand == this.destination && this.p.hand.size() < 10){
            this.destination.addToBottom(this.replacement);
            this.replacement.lighten(false);
            this.p.hand.refreshHandLayout();
        } else if (this.p.hand == this.destination || this.p.drawPile == this.destination) {
            this.p.discardPile.addToRandomSpot(this.replacement);
            this.p.discardPile.moveToDeck(this.replacement, true);
        } else {
            this.destination.addToRandomSpot(this.replacement);
        }
        this.isDone = true;
    }

    private void evolveAbstractCard(AbstractCard card){
        VisasMod.logger.info("Transforming abstract card");
        this.p.discardPile.removeCard(card);
        this.replacement = new Mudragon();
        if (card.upgraded){this.replacement.upgrade();}
        if (this.p.hand == this.destination && this.p.hand.size() < 10){
            this.destination.addToBottom(this.replacement);
            this.replacement.lighten(false);
            this.p.hand.refreshHandLayout();
        } else if (this.p.hand == this.destination || this.p.drawPile == this.destination) {
            this.p.discardPile.addToRandomSpot(this.replacement);
            this.p.discardPile.moveToDeck(this.replacement, true);
        } else {
            this.destination.addToRandomSpot(this.replacement);
        }
        this.isDone = true;
    }

    private void discardPileTransform(){
        AbstractCard cardToTransform = null;
        EvolvingCard cardToEvolve = null;

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            this.tickDuration();

        } else if (this.p.discardPile.size() == 1) {
            if (this.p.discardPile.getBottomCard() instanceof EvolvingCard){
                cardToEvolve = (EvolvingCard) this.p.discardPile.getBottomCard();
                this.evolveEvolvingCard(cardToEvolve);

            } else {
                cardToTransform = this.p.discardPile.getBottomCard();
                this.evolveAbstractCard(cardToTransform);
            }
            this.isDone = true;
            this.tickDuration();


        } else if (this.duration == 0.5F) {
            AbstractDungeon.gridSelectScreen.open(this.p.discardPile, this.amount, TEXT[0], false);
            this.tickDuration();
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {

                    if (c instanceof EvolvingCard){
                        cardToEvolve = (EvolvingCard) c;
                        this.evolveEvolvingCard(cardToEvolve);
                    } else {
                        cardToTransform = c;
                        this.evolveAbstractCard(cardToTransform);
                    }

                    c.lighten(false);
                    c.unhover();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();

                for(AbstractCard c : this.p.discardPile.group) {
                    c.unhover();
                    c.target_x = (float)CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0F;
                }

                this.isDone = true;
            }

            this.tickDuration();
        }

    }

    public void fromHologramExample() {
        AbstractCard cardToTransform = null;
        EvolvingCard cardToEvolve = null;

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead() || (this.destination == this.p.hand && this.p.hand.size() >= 10)) {
            VisasMod.logger.info("Hand too full");
            this.isDone = true;
            return;
        } else if (this.p.discardPile.size() == 0) {
            VisasMod.logger.info("No cards to evolve");
            this.isDone = true;
            return;
        } else if (this.duration == 0.5F) {
            VisasMod.logger.info("Select cards from discard");
            // TODO:

            //  3. Add an animation to the deck to indicate evolution was successful
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(AbstractCard c : this.p.drawPile.group) {
                if (this.exceptions == null || !this.exceptions.contains(c)){
                    if (c instanceof EvolvingCard){
                        temp.addToTop(c);
                    }
                }
            }

            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);

            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            } else if (temp.size() == 1){
                cardToEvolve = (EvolvingCard) temp.getTopCard();
                this.evolveEvolvingCard(cardToEvolve);
                this.isDone = true;
                return;
            } else if (this.optional) {
                AbstractDungeon.gridSelectScreen.open(temp, this.amount, TEXT[0], false, false, true, false);
            } else {
                AbstractDungeon.gridSelectScreen.open(temp, this.amount, TEXT[0], false, false, false, false);
            }

            this.tickDuration();
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                VisasMod.logger.info("Selected cards from discard recognized");
                for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    cardToEvolve = (EvolvingCard) c;
                    this.evolveEvolvingCard(cardToEvolve);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();

                for(AbstractCard c : this.p.discardPile.group) {
                    c.unhover();
                    c.target_x = (float)CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0F;
                }

                this.isDone = true;
            } else {
                VisasMod.logger.info("No selection occurred");
            }

            this.tickDuration();
        }
    }

    public void update() {
        this.fromHologramExample();
    }
}
