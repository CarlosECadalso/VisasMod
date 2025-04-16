package TearlamentsMod.cards.generic.c_rare;

import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.relics.WarpedTongs;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class LokaSamsara extends BaseCard {
    public static final String ID = makeID(LokaSamsara.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 0;
    private static final int UPG_DAMAGE = 0;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 0;

    public LokaSamsara() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE,UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);
        setCostUpgrade(2);

        this.purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(this, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
        AbstractDungeon.player.masterDeck.removeCard(this);
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new FrozenEye());
    }

}
