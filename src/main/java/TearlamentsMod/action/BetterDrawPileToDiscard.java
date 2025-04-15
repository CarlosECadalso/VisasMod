package TearlamentsMod.action;

import TearlamentsMod.VisasMod;
import com.megacrit.cardcrawl.actions.common.BetterDrawPileToHandAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BetterDrawPileToDiscard extends BetterDrawPileToHandAction {
    private final AbstractPlayer player;
    private final int numberOfCards;
    private final boolean optional;
    private HashSet<AbstractCard> exceptions = null;

    public BetterDrawPileToDiscard(int numberOfCards, boolean optional) {
        super(numberOfCards, optional);
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }

    public BetterDrawPileToDiscard(int numberOfCards, boolean optional, HashSet<AbstractCard> exceptions) {
        super(numberOfCards, optional);
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.exceptions = exceptions;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                VisasMod.logger.info("Battle Ended");
                this.isDone = true;
                return;
            } else if (this.player.drawPile.size() == 0) {
                VisasMod.logger.info("No cards to discard");
                this.isDone = true;
                return;
            }

            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(AbstractCard c : this.player.drawPile.group) {
                if (this.exceptions == null || !this.exceptions.contains(c)){
                    temp.addToTop(c);
                }
            }

            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);

            if (temp.isEmpty()){
                VisasMod.logger.info("No cards to discard after exceptions");
                this.isDone = true;
                return;
            } else if (temp.size() <= this.numberOfCards){
                for (int i = 0; i < this.numberOfCards; i++){
                    this.player.drawPile.moveToDiscardPile(temp.getTopCard());
                    temp.getTopCard().triggerOnManualDiscard();
                    temp.removeTopCard();
                }
                this.isDone = true;
                return;
            } else if (this.numberOfCards == 1) {
                if (this.optional) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false, false, false, false);
                }
            } else if (this.optional) {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
            } else {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false, false, false, false);
            }

            this.tickDuration();

        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for(AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.player.drawPile.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }

    }
}
