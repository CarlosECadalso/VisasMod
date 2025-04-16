package TearlamentsMod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class TrivikarmaAction extends ExhaustAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    public static int numExhausted;
    public int amount = 1;

    public TrivikarmaAction(boolean isRandom, boolean anyNumber, boolean canPickZero) {
        super(1, isRandom, anyNumber, canPickZero);

    }

    public TrivikarmaAction(AbstractCreature target, AbstractCreature source, boolean isRandom, boolean anyNumber) {
        super(1, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }

    public TrivikarmaAction(AbstractCreature target, AbstractCreature source, boolean isRandom) {
        this(isRandom, false, false);
        this.target = target;
        this.source = source;
    }

    public TrivikarmaAction(AbstractCreature target, AbstractCreature source, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this(isRandom, anyNumber, canPickZero);
        this.target = target;
        this.source = source;
    }

    public TrivikarmaAction(boolean canPickZero) {
        this(false, false, canPickZero);
    }

    public TrivikarmaAction(boolean isRandom, boolean anyNumber) {
        this(isRandom, anyNumber, false);
    }

    public TrivikarmaAction(boolean isRandom, boolean anyNumber, boolean canPickZero, float duration) {
        this(isRandom, anyNumber, canPickZero);
        this.duration = this.startDuration = duration;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            if (!this.anyNumber && this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                numExhausted = this.amount;
                int tmp = this.p.hand.size();

                for(int i = 0; i < tmp; ++i) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToExhaustPile(c);

                    switch (c.rarity){
                        case RARE:
                            addToTop(new GainEnergyAction(3));
                        case UNCOMMON:
                            addToTop(new GainEnergyAction(2));
                        default:
                            addToTop(new GainEnergyAction(1));
                    }
                }

                CardCrawlGame.dungeon.checkForPactAchievement();
                return;
            }

            if (!this.isRandom) {
                numExhausted = this.amount;
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, this.anyNumber, this.canPickZero);
                this.tickDuration();
                return;
            }

            for(int i = 0; i < this.amount; ++i) {
                AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                this.p.hand.moveToExhaustPile(c);
                switch (c.rarity){
                    case RARE:
                        addToTop(new GainEnergyAction(3));
                    case UNCOMMON:
                        addToTop(new GainEnergyAction(2));
                    default:
                        addToTop(new GainEnergyAction(1));
                }
            }

            CardCrawlGame.dungeon.checkForPactAchievement();
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToExhaustPile(c);
                switch (c.rarity){
                    case RARE:
                        addToTop(new GainEnergyAction(3));
                    case UNCOMMON:
                        addToTop(new GainEnergyAction(2));
                    default:
                        addToTop(new GainEnergyAction(1));
                }
            }

            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
