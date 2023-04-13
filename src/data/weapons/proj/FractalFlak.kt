package data.weapons.proj

import FractalProjHandler
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin
import utils.FractalUtils

class FractalFlak: OnFireEffectPlugin
{

    companion object
    {
        val map = mapOf<WeaponSize, String>(
            WeaponSize.SMALL to "fractal_sflak",
            WeaponSize.MEDIUM to "fractal_mflak",
            WeaponSize.LARGE to "fractal_lflak"
        )

    }
    override fun onFire(proj: DamagingProjectileAPI, weapon: WeaponAPI?, engine: CombatEngineAPI)
    {
        if (weapon == null || weapon.ship == null) return

        // ensure plugin and add proj
        var plugin: FractalProjHandler? = engine.customData.get("fractalprojhandler") as? FractalProjHandler
        if (plugin is FractalProjHandler)
        {
            plugin.flakList.add(proj)
        } else
        {
            plugin = FractalProjHandler(engine)
            engine.addPlugin(FractalProjHandler(engine))
            plugin.flakList.add(proj)
        }
        // extra projectile chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
        {
            // +/- 5 to 15 degrees
            val angle = proj.facing + Misc.random.nextFloat() * 20 - 10 + (if (Misc.random.nextBoolean()) -5 else 5)
            val newProj = engine.spawnProjectile(
                weapon.ship,
                weapon,
                map.get(weapon.size),
                proj.location,
                angle,
                weapon.ship.velocity)

        }
    }
}

