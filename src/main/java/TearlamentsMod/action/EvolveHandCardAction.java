package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.generic.b_uncommon.Mudragon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.HashSet;

public class EvolveHandCardAction extends AbstractGameAction {
    private static String[] TEXT = {
            "Select a card to evolve shuffle:",
            "Select a card to evolve:"
    }; // TODO Setup the text correctly
    private final CardGroup destination;
    private AbstractCard replacement;
    private AbstractPlayer p;
    private HashSet<AbstractCard> exceptions = null;
    private ArrayList<AbstractCard> nonEvolvingCards;


    public EvolveHandCardAction(CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = this.p.hand;
        this.nonEvolvingCards = new ArrayList<AbstractCard>();
        this.amount = 1;
    }

    public EvolveHandCardAction(CardGroup destination, HashSet<AbstractCard> exceptions) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = this.p.hand;
        this.nonEvolvingCards = new ArrayList<AbstractCard>();
        this.exceptions = exceptions;
        this.amount = 1;
    }

    private void evolveEvolvingCard(EvolvingCard card){
        VisasMod.logger.info("Evolving a card of type EvolvingCard");
        this.p.hand.removeCard(card);
        this.replacement = card.getCardToFuseInto();
        if (card.upgraded){this.replacement.upgrade();}
        if (this.p.hand == this.destination){
            this.destination.addToTop(this.replacement);
            this.replacement.lighten(false);
            this.p.hand.refreshHandLayout();
        } else {
            this.destination.addToRandomSpot(this.replacement);
        }
        this.isDone = true;
    }

    private void evolveAbstractCard(AbstractCard card){
        VisasMod.logger.info("Evolving a card of type AbstractCard");
        this.p.hand.removeCard(card);
        this.replacement = new Mudragon();
        if (card.upgraded){this.replacement.upgrade();}
        if (this.p.hand == this.destination){
            this.destination.addToTop(this.replacement);
            this.replacement.lighten(false);
            this.p.hand.refreshHandLayout();
        } else {
            this.destination.addToRandomSpot(this.replacement);
        }
        this.isDone = true;
    }

    private void handTransform(){
        AbstractCard cardToTransform = null;
        EvolvingCard cardToEvolve = null;


        if (this.p.hand.group.isEmpty() || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            VisasMod.logger.info("Hand is empty");
            this.isDone = true;
        } else if (this.p.hand.size() == 1) {
            VisasMod.logger.info("Only 1 card in hand");
            if (this.p.hand.getTopCard() instanceof EvolvingCard){
                cardToEvolve = (EvolvingCard) this.p.hand.getTopCard();
                this.evolveEvolvingCard(cardToEvolve);
            } else {
                cardToTransform = this.p.hand.getTopCard();
                this.evolveAbstractCard(cardToTransform);
            }

            AbstractDungeon.player.hand.applyPowers();
            this.tickDuration();

        } else if (this.duration == 0.5f) {
            VisasMod.logger.info("Selecting card to evolve?");
            AbstractDungeon.handCardSelectScreen.open(TEXT[1], this.amount, false, false);
            AbstractDungeon.player.hand.applyPowers();

            this.tickDuration();
        } else {
            if (AbstractDungeon.handCardSelectScreen.selectedCards.size() != 0) {
                VisasMod.logger.info("Cards selected to evolve recognized");
                for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    VisasMod.logger.info(c.name);
                    if (c instanceof EvolvingCard){
                        cardToEvolve = (EvolvingCard) c;
                        this.evolveEvolvingCard(cardToEvolve);
                    } else {
                        cardToTransform = c;
                        this.evolveAbstractCard(cardToTransform);
                    }
                }

                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            }

            this.tickDuration();
        }
    }

    public void fromExhaustExample() {
        AbstractCard cardToTransform = null;
        EvolvingCard cardToEvolve = null;

        if (this.duration == this.startDuration) {
            for (AbstractCard c : this.p.hand.group){
                if (this.exceptions == null || !this.exceptions.contains(c)){
                    if (c instanceof EvolvingCard){
                        this.nonEvolvingCards.add(c);
                    }
                }
            }

            if (this.p.hand.size() == this.nonEvolvingCards.size()) {
                this.isDone = true;
                return;
            }

            if (this.p.hand.size() - this.nonEvolvingCards.size() == 1) {
                VisasMod.logger.info("Only 1 card in hand");
                for (AbstractCard c : this.p.hand.group){
                    if (c instanceof EvolvingCard){
                        this.evolveEvolvingCard((EvolvingCard) c);
                        AbstractDungeon.player.hand.applyPowers();
                        this.tickDuration();
                        this.isDone = true;
                        return;
                    }
                }
            }

            this.p.hand.group.removeAll(this.nonEvolvingCards);

            if (this.p.hand.group.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, false);
                this.tickDuration();
                return;
            }

            if (this.p.hand.group.size() == 1) {
                EvolvingCard c = (EvolvingCard) this.p.hand.getTopCard();
                this.evolveEvolvingCard(c);
                AbstractDungeon.player.hand.applyPowers();
                this.p.hand.getTopCard().superFlash();
                this.returnCards();
                this.tickDuration();
                this.isDone = true;
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            VisasMod.logger.info("Cards selected to evolve recognized");
            for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                VisasMod.logger.info(c.name);
                cardToEvolve = (EvolvingCard) c;
                this.evolveEvolvingCard(cardToEvolve);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
        this.isDone = true;
    }

    private void returnCards() {
        for (AbstractCard c : this.nonEvolvingCards){
            this.p.hand.addToTop(c);
        }

        this.p.hand.refreshHandLayout();
    }

    public void update() {
        this.fromExhaustExample();
        // TODO Following armaments example:
        //  1. remove cards from hand
        //  2. preform upgrade
        //  3. return cards to hand
    }
}
