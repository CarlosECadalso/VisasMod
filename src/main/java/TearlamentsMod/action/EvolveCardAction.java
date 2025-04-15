package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.generic.b_uncommon.Mudragon;
import com.evacipated.cardcrawl.mod.stslib.patches.PurgePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.lang.reflect.Field;

public class EvolveCardAction extends AbstractGameAction {
    private static String[] TEXT = {
            "Select a card to evolve shuffle:",
            "Select a card to evolve:"
    }; // TODO Setup the text correctly
    private CardGroup source;
    private CardGroup destination;
    public static int numDiscarded;
    private AbstractCard replacement;
    private EvolvingCard cardToTransform = null;
    private AbstractPlayer p;


    public EvolveCardAction(CardGroup source, CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = destination;
        this.source = source;
        this.amount = 1;
    }

    public EvolveCardAction(AbstractCard cardToTransform, CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.source = null;
        this.cardToTransform = (EvolvingCard) cardToTransform;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
    }

    public EvolveCardAction(EvolvingCard cardToTransform, CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.source = null;
        this.cardToTransform = cardToTransform;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
    }

    public EvolveCardAction(CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.source = null;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
    }

    private void handTransform(){
        if (this.duration == 0.5f) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                int tmp = this.p.hand.size();

                for(int i = 0; i < tmp; ++i) {
                    this.cardToTransform = (EvolvingCard) this.p.hand.getTopCard();
                    this.replacement = this.cardToTransform.getCardToFuseInto();
                    if (this.replacement == null){
                        this.replacement = new Mudragon();
                    }
                    this.destination.addToRandomSpot(this.replacement.makeCopy());
                    this.p.hand.removeCard(this.cardToTransform);

                }

                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            } else {
                numDiscarded = this.amount;
                if (this.p.hand.size() > this.amount) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[1], this.amount, false);
                }

                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.cardToTransform = (EvolvingCard) c;
                this.replacement = this.cardToTransform.getCardToFuseInto();
                if (this.replacement == null){
                    this.replacement = new Mudragon();
                }
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.hand.removeCard(this.cardToTransform);

            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    private void discardPileTransform(){
        if (this.p.hand.size() >= 10 || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
        } else if (this.p.discardPile.size() == 1) {
            this.cardToTransform = (EvolvingCard) this.p.discardPile.group.get(0);
            this.replacement = this.cardToTransform.getCardToFuseInto();
            if (this.replacement == null){
                this.replacement = new Mudragon();
            }

            if (this.p.hand.size() < 10) {
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.discardPile.removeCard(this.cardToTransform);
            }

            this.replacement.lighten(false);
            this.p.hand.refreshHandLayout();
            this.isDone = true;
        } else if (this.duration == 0.5F) {
            AbstractDungeon.gridSelectScreen.open(this.p.discardPile, this.amount, TEXT[0], false);
            this.tickDuration();
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.cardToTransform = (EvolvingCard) c;
                    this.replacement = this.cardToTransform.getCardToFuseInto();
                    if (this.replacement == null){
                        this.replacement = new Mudragon();
                    }

                    this.destination.addToRandomSpot(this.replacement.makeCopy());
                    this.p.discardPile.removeCard(c);

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

    private void specificTransform(){
        if (this.cardToTransform == null || this.replacement == null){
            VisasMod.logger.info("cardToTransform or replacement is null");
            this.isDone = true;
        } else {
            if (this.p.drawPile.contains(this.cardToTransform)){
                VisasMod.logger.info("Transformed card in draw pile");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.drawPile.removeCard(this.cardToTransform);

            } else if (this.p.hand.contains(this.cardToTransform)){
                VisasMod.logger.info("Transformed card in hand");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.hand.removeCard(this.cardToTransform);

            } else if (this.p.discardPile.contains(this.cardToTransform)){
                VisasMod.logger.info("Transformed card in discard pile");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.discardPile.removeCard(this.cardToTransform);

            } else if (this.p.exhaustPile.contains(this.cardToTransform)){
                VisasMod.logger.info("Transformed card in exhaust pile");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.exhaustPile.removeCard(this.cardToTransform);

            } else if (this.p.limbo.contains(this.cardToTransform)){
                VisasMod.logger.info("Transformed card in limbo");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.p.limbo.removeCard(this.cardToTransform);

            } else {
                VisasMod.logger.info("Could not find card");
                this.destination.addToRandomSpot(this.replacement.makeCopy());
                this.isDone = true;
            }
        }

        this.tickDuration();

    }

    public void update() {
        if (this.cardToTransform != null){
            this.replacement = this.cardToTransform.getCardToFuseInto();
            if (this.replacement == null){
                this.replacement = new Mudragon();
            }

            this.specificTransform();
        } else if (this.source == null || this.source == this.p.hand){
            this.handTransform();
        } else if (this.source == this.p.discardPile){
            this.discardPileTransform();
        } else {
            this.isDone = true;
            this.tickDuration();
        }
    }
}
