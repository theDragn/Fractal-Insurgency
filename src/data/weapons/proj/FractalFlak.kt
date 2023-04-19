package data.weapons.proj

import FractalProjHandler
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize
import com.fs.starfarer.api.util.Misc
import data.hullmods.FractalChanceBuff
import data.plugins.FractalModPlugin
import utils.FractalUtils

class FractalFlak: OnFireEffectPlugin
{

    companion object
    {
        val map = mapOf(
            WeaponSize.SMALL to "fractal_sflak",
            WeaponSize.MEDIUM to "fractal_mflak",
            WeaponSize.LARGE to "fractal_lflak"
        )

    }
    override fun onFire(proj: DamagingProjectileAPI, weapon: WeaponAPI?, engine: CombatEngineAPI)
    {
        weapon ?: return; weapon.ship ?: return
        var newProj: DamagingProjectileAPI? = null
        // extra projectile chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
        {
            // +/- 5 to 15 degrees
            val angle = proj.facing + (Misc.random.nextFloat() - 0.5f) * weapon.currSpread
            newProj = engine.spawnProjectile(
                weapon.ship,
                weapon,
                map[weapon.size],
                proj.location,
                angle,
                weapon.ship.velocity) as DamagingProjectileAPI

        }
        // flux cost from recursive amplifier
        if (weapon.ship.variant.hasHullMod("fractal_chancebuff"))
            weapon.ship.fluxTracker.increaseFlux((weapon.fluxCostToFire / weapon.spec.burstSize) * (FractalChanceBuff.WEAPON_FLUX_MULT - 1f), false)
        // ensure plugin and add proj
        var plugin: FractalProjHandler? = engine.customData.get("fractalprojhandler") as? FractalProjHandler
        if (plugin is FractalProjHandler)
        {
            plugin.flakList.add(proj)
            if (newProj != null) plugin.flakList.add(newProj)
        } else
        {
            plugin = FractalProjHandler(engine)
            engine.addPlugin(FractalProjHandler(engine))
            plugin.flakList.add(proj)
            if (newProj != null) plugin.flakList.add(newProj)
        }

        // TODO: smoke vfx?
    }
}

