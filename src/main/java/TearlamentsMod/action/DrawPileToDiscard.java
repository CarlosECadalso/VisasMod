package TearlamentsMod.action;

import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawPileToDiscard extends DiscardPileToTopOfDeckAction {
    private final AbstractPlayer p = AbstractDungeon.player;
    private int amount;

    public DrawPileToDiscard(AbstractCreature source, int amount) {
        super(source);
        this.amount = amount;
    }

    public DrawPileToDiscard(AbstractCreature source) {
        super(source);
        this.amount = 1;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.isDone = true;
        } else {
            if (this.p.drawPile.group.size() == 0) {
                this.tickDuration();
                this.isDone = true;
                return;
            } else if (this.p.drawPile.size() <= this.amount) {
                int tmp = this.p.drawPile.size();
                for (int i = 0; i < tmp; i++){
                    AbstractCard c = this.p.drawPile.getTopCard();
                    this.p.drawPile.removeCard(c);
                    this.p.discardPile.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                }
                this.isDone = true;
            } else {
                for (int i = 0; i < this.amount; i++){
                    AbstractCard c = this.p.drawPile.getTopCard();
                    this.p.drawPile.removeCard(c);
                    this.p.discardPile.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                }
                this.isDone = true;
            }

            this.tickDuration();
        }

    }
}
