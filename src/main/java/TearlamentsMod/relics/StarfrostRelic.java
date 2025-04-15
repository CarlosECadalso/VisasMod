package TearlamentsMod.relics;

import TearlamentsMod.cards.generic.e_generated.Anger;
import TearlamentsMod.cards.generic.e_generated.Fear;
import TearlamentsMod.cards.generic.e_generated.Peace;
import TearlamentsMod.cards.generic.e_generated.Sorrow;
import TearlamentsMod.character.Visas;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

import static TearlamentsMod.VisasMod.makeID;

public class StarfrostRelic extends BaseRelic{

    private static final String NAME = "StarfrostRelic"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.STARTER; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.MAGICAL; //The sound played when the relic is clicked.

    public StarfrostRelic() {
        super(ID, NAME, Visas.Meta.CARD_COLOR, RARITY, SOUND);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        ArrayList<AbstractCard> stanceChoices = new ArrayList();
        stanceChoices.add(new Anger());
        stanceChoices.add(new Peace());
        stanceChoices.add(new Sorrow());
        stanceChoices.add(new Fear());

        this.addToBot(new ChooseOneAction(stanceChoices));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
