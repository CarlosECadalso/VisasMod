package TearlamentsMod.cards.generic.c_rare;

import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraAriseHeart;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VedaKalarcanum extends EvolvingCard {
    public static final String ID = makeID(VedaKalarcanum.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 0;
    private static final int UPG_DAMAGE = 0;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 4;
    private static final int UPG_MAGIC_NUMBER = 3;
    public AbstractCard cardToFuseInto = this;

    public VedaKalarcanum() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE,UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.FUSION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < magicNumber) {
            this.shuffleBackIntoDrawPile = true;
            setExhaust(false);
            addToBot(new GainEnergyAction(this.energyOnUse));
        } else {
            this.shuffleBackIntoDrawPile = false;
            setExhaust(true);
            addToBot(new StunMonsterAction(m, p));
        }
    }

    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = this;
        this.cardsToPreview = null;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        return new VedaKalarcanum();
    }
}
