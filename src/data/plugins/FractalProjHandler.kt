import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin
import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.util.Misc
import data.scripts.util.MagicRender
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

class FractalProjHandler(private val engine: CombatEngineAPI): BaseEveryFrameCombatPlugin()
{
    // yeah this has like five different naming conventions, whatever, it was late, okay?
    val flakList = ArrayList<DamagingProjectileAPI>()

    companion object
    {
        val FIRE_COLOR = Color(255, 71, 0, 200)
        val PUFF_COLOR = Color(50, 50, 50, 128)
    }
    override fun advance(amount: Float, events: MutableList<InputEventAPI>?)
    {
        if (flakList.isEmpty()) return
        val toRemove = ArrayList<DamagingProjectileAPI>()
        for (flakShot in flakList)
        {
            if (flakShot.isExpired || flakShot.didDamage() || !engine.isEntityInPlay(flakShot))
            {
                // TODO: explosion vfx
                //engine.addFloatingText(flakShot.location, "boom!", 24f, Color.CYAN, null, 0f, 0f)
                val flak_puff = Global.getSettings().getSprite("fx", "fractal_flak_puff")
                val flak_fire = Global.getSettings().getSprite("fx", "fractal_flak_fire")
                // dark cloud vfx
                val randAmount = Misc.random.nextFloat() * 5f
                val puffsize = Vector2f(50f + randAmount, 50f + randAmount)
                val puffgrowth = Vector2f(90f, 90f)
                MagicRender.battlespace(flak_puff,
                                        flakShot.location,
                                        Misc.ZERO,
                                        puffsize,
                                        puffgrowth,
                                        Misc.random.nextFloat() * 360f,
                                        0f,
                                        PUFF_COLOR,
                                        true,
                                        0f,
                                        0.33f,
                                        0.66f)
                // bright flame/shard sprite
                val flamesize = Vector2f(35f + randAmount, 35f + randAmount)
                val flamegrowth = Vector2f(5f, 5f)
                MagicRender.battlespace(flak_fire,
                                        flakShot.location,
                                        Misc.ZERO,
                                        flamesize,
                                        flamegrowth,
                                        Misc.random.nextFloat() * 360f,
                                        0f,
                                        FIRE_COLOR,
                                        true,
                                        0f,
                                        0.1f,
                                        0.56f)
                engine.addSmoothParticle(flakShot.location, Misc.ZERO, 90f, 1.0F, 0.05F, Color.WHITE);
                engine.addSmoothParticle(flakShot.location, Misc.ZERO, 90f, 1.0F, 0.1F, Color.WHITE);
                toRemove.add(flakShot)
            }
        }
        if (toRemove.isEmpty()) return
        for (thing in toRemove)
        {
            flakList.remove(thing)
        }
    }

    override fun init(engine: CombatEngineAPI)
    {
        engine.customData.put("fractalprojhandler", this)
    }
}