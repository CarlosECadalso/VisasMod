package TearlamentsMod.cards.kashtira.c_rare;

import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentKitkallos;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class KashtiraAriseHeart extends EvolvingCard {
    public static final String ID = makeID(KashtiraAriseHeart.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 2;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 10;
    private static final int UPG_MAGIC_NUMBER = 12;
    public AbstractCard cardToFuseInto = this;

    public KashtiraAriseHeart() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);
        setExhaust(true);
        setCostUpgrade(1);

        tags.add(CustomTags.KASHTIRA);
        tags.add(CustomTags.FUSION);
        tags.add(CustomTags.CREATURE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));
        for (AbstractCard c : p.exhaustPile.group){
            addToBot(new DamageAction(m, new DamageInfo(p, magicNumber, DamageInfo.DamageType.NORMAL)));
        }
    }


    @Override
    public AbstractCard makeCopy() { //Optional
        return new KashtiraAriseHeart();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = this;
        this.cardsToPreview = null;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        return new KashtiraAriseHeart();
    }
}
