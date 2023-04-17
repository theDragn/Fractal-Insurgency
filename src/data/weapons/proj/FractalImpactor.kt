package data.weapons.proj

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.combat.OnFireEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin
import utils.FractalUtils

class FractalImpactor: OnFireEffectPlugin
{
    companion object
    {
        val map = mapOf(
            WeaponAPI.WeaponSize.SMALL to "fractal_simpactor",
            WeaponAPI.WeaponSize.MEDIUM to "fractal_mimpactor",
            WeaponAPI.WeaponSize.LARGE to "oops"
        )
    }
    override fun onFire(proj: DamagingProjectileAPI?, weapon: WeaponAPI?, engine: CombatEngineAPI?)
    {
        proj ?: return; engine ?: return; weapon ?: return
        // extra projectile chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
        {
            val angle = proj.facing + (Misc.random.nextFloat() - 0.5f) * weapon.currSpread
            engine.spawnProjectile(
                weapon.ship,
                weapon,
                map[weapon.size],
                proj.location,
                angle,
                weapon.ship.velocity)

        }
    }
}