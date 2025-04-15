package TearlamentsMod.powers;

import TearlamentsMod.action.DrawPileToDiscard;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static TearlamentsMod.VisasMod.makeID;

public class TearlamentSulliekPower extends BasePower {
    public static final String POWER_ID = makeID("TearlamentSulliekPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public static int MAGIC_NUMBER;

    public TearlamentSulliekPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        MAGIC_NUMBER = 3;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if (usedCard.hasTag(CustomTags.TEARLAMENTS)){
            addToBot(new DrawPileToDiscard(AbstractDungeon.player, 1));
            addToBot(new GainBlockAction(AbstractDungeon.player, MAGIC_NUMBER));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + MAGIC_NUMBER + DESCRIPTIONS[1];
    }
}