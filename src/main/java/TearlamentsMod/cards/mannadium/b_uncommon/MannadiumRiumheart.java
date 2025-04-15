package TearlamentsMod.cards.mannadium.b_uncommon;

import TearlamentsMod.VisasMod;
import TearlamentsMod.action.EvolveSelfCardAction;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.mannadium.c_rare.MannadiumPrimeHeart;
import TearlamentsMod.cards.mannadium.c_rare.MannadiumTrisukta;
import TearlamentsMod.cards.scareclaw.c_rare.ScareclawTriHeart;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.RemoveAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

public class MannadiumRiumheart extends EvolvingCard {
    public static final String ID = makeID(MannadiumRiumheart.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 9;
    private static final int UPG_DAMAGE = 4;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 1;
    public AbstractCard cardToFuseInto = new MannadiumPrimeHeart();

    public MannadiumRiumheart() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);


        tags.add(CustomTags.MANNADIUM);
        tags.add(CustomTags.CREATURE);
        this.defineCardToFuseInto();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL)));

        int orbCount = p.filledOrbCount();

        addToBot(new RemoveAllOrbsAction());

        if (orbCount > 1){
            addToBot(new GainEnergyAction(magicNumber));
        }
        if (orbCount > 2){
            addToBot(new MakeTempCardInHandAction(new MannadiumTrisukta()));
        }
        if (orbCount > 3){
            addToBot(new EvolveSelfCardAction(this, p.drawPile));
            this.purgeOnUse = true;
        } else {
            this.purgeOnUse = false;
        }
    }



    @Override
    public AbstractCard makeCopy() { //Optional
        return new MannadiumRiumheart();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = new MannadiumPrimeHeart();
        this.cardsToPreview = cardToFuseInto;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        return new MannadiumPrimeHeart();
    }
}
