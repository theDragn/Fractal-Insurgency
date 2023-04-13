import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin
import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.input.InputEventAPI

class FractalProjHandler(private val engine: CombatEngineAPI): BaseEveryFrameCombatPlugin()
{
    val flakList = ArrayList<DamagingProjectileAPI>()

    override fun advance(amount: Float, events: MutableList<InputEventAPI>?)
    {
        if (flakList.isEmpty()) return
        val toRemove = ArrayList<DamagingProjectileAPI>()
        for (flakShot in flakList)
        {
            if (flakShot.isExpired || flakShot.didDamage())
            {
                // TODO: explosion vfx
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