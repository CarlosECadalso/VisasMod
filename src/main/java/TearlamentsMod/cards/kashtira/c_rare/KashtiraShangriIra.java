package TearlamentsMod.cards.kashtira.c_rare;

import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class KashtiraShangriIra extends EvolvingCard {
    public static final String ID = makeID(KashtiraShangriIra.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 0;
    private static final int UPG_DAMAGE = 0;
//    private static final int BLOCK = 8;
    private static final int BLOCK = 15;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 2;
    private static final int UPG_MAGIC_NUMBER = 1;
    public AbstractCard cardToFuseInto = this;

    public KashtiraShangriIra() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.KASHTIRA);
        tags.add(CustomTags.FUSION);
        tags.add(CustomTags.CREATURE);
        this.defineCardToFuseInto();
        this.purgeOnUse = true;

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

//        for (int i = 0; i < p.exhaustPile.group.size(); i++){
//            addToBot(new GainBlockAction(p, block));
//        }

        addToBot(new GainBlockAction(p, block));

        if (p.exhaustPile.group.size() > 0){
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), 1));
        }
        if (p.exhaustPile.group.size() > 1){
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), 1));
        }
        if (p.exhaustPile.group.size() > 2){
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), 1));
        }
        if (p.exhaustPile.group.size() > 3){
            addToBot(new ApplyPowerAction(m, p, new SlowPower(m, magicNumber), 1));
        }
        if (p.exhaustPile.group.size() > 4){
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber)));
        }
        if (p.exhaustPile.group.size() > 5){
            addToBot(new StunMonsterAction(m, p));;
        }

    }


    @Override
    public AbstractCard makeCopy() { //Optional
        return new KashtiraShangriIra();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = this;
        this.cardsToPreview = null;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        return new KashtiraShangriIra();
    }
}
