package data.weapons.proj

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.combat.OnFireEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin
import utils.FractalUtils

class FractalVolley: OnFireEffectPlugin
{
    override fun onFire(proj: DamagingProjectileAPI, weapon: WeaponAPI?, engine: CombatEngineAPI)
    {
        if (weapon == null || weapon.ship == null) return
        // extra projectile chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
        {
            // +/- 5 to 15 degrees
            val angle = proj.facing + Misc.random.nextFloat() * 20 - 10 + (if (Misc.random.nextBoolean()) -5 else 5)
            val newProj = engine.spawnProjectile(
                weapon.ship,
                weapon,
                "fractal_svolley",
                proj.location,
                angle,
                weapon.ship.velocity)

        }
    }
}