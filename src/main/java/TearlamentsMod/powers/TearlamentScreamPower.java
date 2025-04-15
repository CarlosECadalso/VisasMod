package TearlamentsMod.powers;

import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static TearlamentsMod.VisasMod.makeID;

public class TearlamentScreamPower extends BasePower {
    public static final String POWER_ID = makeID("TearlamentScreamPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public static int MAGIC_NUMBER;

    public TearlamentScreamPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        MAGIC_NUMBER = amount;
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        super.onAfterCardPlayed(usedCard);
        if (usedCard.hasTag(CustomTags.TEARLAMENTS)){
            for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new StrengthPower(mo, -amount), -amount, true, AbstractGameAction.AttackEffect.NONE));
            }

            for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.hasPower("Artifact")) {
                    addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new GainStrengthPower(mo, amount), amount, true, AbstractGameAction.AttackEffect.NONE));
                }
            }
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}