package TearlamentsMod.cards.tearlaments.b_uncommon;

import TearlamentsMod.action.DrawPileToDiscard;
import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.EvolvingCard;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraShangriIra;
import TearlamentsMod.cards.tearlaments.c_rare.TearlamentKitkallos;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TearlamentKashtira extends EvolvingCard {
    public static final String ID = makeID(TearlamentKashtira.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 15;
    private static final int UPG_DAMAGE = 0;
    private static final int BLOCK = 0;
    private static final int UPG_BLOCK = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPG_MAGIC_NUMBER = 1;
    public AbstractCard cardToFuseInto = new KashtiraShangriIra();

    public TearlamentKashtira() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);

        tags.add(CustomTags.TEARLAMENTS);
        tags.add(CustomTags.KASHTIRA);
        tags.add(CustomTags.CREATURE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public void triggerOnManualDiscard() {
        addToBot(new DrawPileToDiscard(AbstractDungeon.player, magicNumber));
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        this.setCostForTurn(this.baseCost);
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group){
            if (c.hasTag(CustomTags.KASHTIRA) || c.hasTag(CustomTags.TEARLAMENTS)){
                addToBot(new ReduceCostAction(this));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TearlamentKashtira();
    }


    @Override
    public void defineCardToFuseInto() {
        cardToFuseInto = new KashtiraShangriIra();
        this.cardsToPreview = cardToFuseInto;
    }

    @Override
    public AbstractCard getCardToFuseInto() {
        return new KashtiraShangriIra();
    }
}
