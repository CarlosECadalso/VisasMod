package TearlamentsMod.cards.tearlaments.c_rare;

import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.character.Visas;
import TearlamentsMod.powers.PerlereinoPower;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.ExhumeAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TearlamentCryme extends BaseCard {
    public static final String ID = makeID(TearlamentCryme.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is non-defined cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 0;
    private static final int UPG_DAMAGE = 0;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 1;
    private static final int POWER_DAMAGE = PerlereinoPower.MAGIC_NUMBER;

    public TearlamentCryme() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE,UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);
//        setCostUpgrade(0);

        tags.add(CustomTags.TEARLAMENTS);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, 1, false));
        addToBot(new ApplyPowerAction(p, p, new IntangiblePower(p, magicNumber)));
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
        addToBot(new ExhumeAction(this.upgraded));
        if (AbstractDungeon.player.discardPile.contains(this)){
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
        } else if (AbstractDungeon.player.hand.contains(this)) {
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        } else if (AbstractDungeon.player.drawPile.contains(this)){
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.drawPile));
        }
    }
}
