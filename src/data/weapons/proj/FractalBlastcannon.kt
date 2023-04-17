package data.weapons.proj

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.combat.OnFireEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin
import utils.FractalUtils

class FractalBlastcannon: OnFireEffectPlugin
{
    //var lastProj: DamagingProjectileAPI? = null
    //var adjust = false
    override fun onFire(proj: DamagingProjectileAPI?, weapon: WeaponAPI?, engine: CombatEngineAPI?)
    {
        proj ?: return; weapon ?: return; engine ?: return
        /*if (adjust && lastProj is DamagingProjectileAPI)
        {
            proj.facing = lastProj!!.facing
            proj.velocity.set(lastProj!!.velocity)
            proj.angularVelocity = lastProj!!.angularVelocity
        } else
            lastProj = proj
        adjust = !adjust*/

        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
        {
            engine.spawnProjectile(proj.source, weapon, "fractal_blast_charged", proj.location, proj.facing, proj.source.velocity)
            engine.removeEntity(proj)
        }

        // TODO: smoke vfx
    }
}