package TearlamentsMod.cards.kashtira.b_uncommon;

import TearlamentsMod.cards.BaseCard;
import TearlamentsMod.cards.kashtira.a_common.ScareclawKashtira;
import TearlamentsMod.cards.kashtira.c_rare.KashtiraFenrir;
import TearlamentsMod.cards.tearlaments.b_uncommon.TearlamentKashtira;
import TearlamentsMod.character.Visas;
import TearlamentsMod.util.CardStats;
import TearlamentsMod.util.CustomTags;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;

public class KashtiraBirth extends BaseCard {
    public static final String ID = makeID(KashtiraBirth.class.getSimpleName());

    private static final CardStats info = new CardStats(
            Visas.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
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

    public KashtiraBirth() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK, UPG_BLOCK); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC_NUMBER, UPG_MAGIC_NUMBER);
        setExhaust(true, true);

        tags.add(CustomTags.KASHTIRA);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<AbstractCard>();
        choices.add(new KashtiraFenrir());
        choices.add(new KashtiraUnicorn());
        choices.add(new KashtiraOgre());
        choices.add(new KashtiraRiseheart());
        choices.add(new TearlamentKashtira());
        choices.add(new ScareclawKashtira());
        Collections.shuffle(choices);

        for (AbstractCard c : choices){
            c.setCostForTurn(0);
        }

        if (this.upgraded) {
            int i = 3;
            while (i < choices.size()) {
                choices.remove(0);
            }
            addToBot(new ChooseOneAction(choices));
        } else {
            AbstractCard c = choices.get(0);
            addToBot(new MakeTempCardInHandAction(c));
        }
    }


    @Override
    public AbstractCard makeCopy() { //Optional
        return new KashtiraBirth();
    }


}
