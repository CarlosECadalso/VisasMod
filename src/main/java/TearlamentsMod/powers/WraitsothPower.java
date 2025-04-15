package TearlamentsMod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static TearlamentsMod.VisasMod.makeID;

public class WraitsothPower extends BasePower {
    public static final String POWER_ID = makeID("WraitsothPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public static final int MAGIC_NUMBER = 2;

    public WraitsothPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    @Override
    public void onExhaust(AbstractCard card) {
        super.onExhaust(card);
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, MAGIC_NUMBER, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + MAGIC_NUMBER + DESCRIPTIONS[1];
    }
}