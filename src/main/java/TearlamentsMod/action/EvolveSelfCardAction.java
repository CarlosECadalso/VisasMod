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

public class EvolveSelfCardAction extends AbstractGameAction {
    private static String[] TEXT = {
            "Select a card to evolve shuffle:",
            "Select a card to evolve:"
    }; // TODO Setup the text correctly
    private final CardGroup destination;
    private AbstractCard replacement;
    private EvolvingCard cardToTransform = null;
    private final AbstractPlayer p;


    public EvolveSelfCardAction(EvolvingCard cardToTransform, CardGroup destination) {
        this.p = AbstractDungeon.player;
        this.source = null;
        this.cardToTransform = cardToTransform;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.replacement = null;
        this.destination = destination;
        this.amount = 1;
    }

    private void specificTransform(){
        if (this.replacement == null){
            VisasMod.logger.info("Replacement is null");
            this.isDone = true;
            this.tickDuration();
        } else {
            if (this.p.drawPile.contains(this.cardToTransform)){
                this.destination.addToRandomSpot(this.replacement);
                this.p.drawPile.removeCard(this.cardToTransform);
            } else if (this.p.hand.contains(this.cardToTransform)){
                this.destination.addToRandomSpot(this.replacement);
                this.p.hand.removeCard(this.cardToTransform);
            } else if (this.p.discardPile.contains(this.cardToTransform)){
                this.destination.addToRandomSpot(this.replacement);
                this.p.discardPile.removeCard(this.cardToTransform);
            } else if (this.p.exhaustPile.contains(this.cardToTransform)){
                this.destination.addToRandomSpot(this.replacement);
                this.p.exhaustPile.removeCard(this.cardToTransform);
            } else {
                VisasMod.logger.info("Card to transform was not found and should have been purged");
                this.destination.addToRandomSpot(this.replacement);
            }
        }

        this.isDone = true;
        this.tickDuration();

    }

    public void update() {
        // WARNING: This does NOT get rid of the original card. add "setPurge(true)" to the
        //          card to the cardToTransform before adding this action to the queue
        if (this.cardToTransform != null){
            this.replacement = this.cardToTransform.getCardToFuseInto();
            if (this.cardToTransform.upgraded){this.replacement.upgrade();}
            this.specificTransform();
        } else {
            VisasMod.logger.info("Card to transform is null");
            this.isDone = true;
            this.tickDuration();
        }
    }
}
