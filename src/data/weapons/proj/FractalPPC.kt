package data.weapons.proj

import com.fs.starfarer.api.combat.CombatEngineAPI
import com.fs.starfarer.api.combat.DamagingProjectileAPI
import com.fs.starfarer.api.combat.OnFireEffectPlugin
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin.Companion.RECURSIVE_WEP_CHANCE
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.util.vector.Vector2f
import utils.FractalUtils.Companion.getChanceMult
import java.awt.Color
import kotlin.math.min

class FractalPPC: OnFireEffectPlugin
{
    override fun onFire(proj: DamagingProjectileAPI, weapon: WeaponAPI?, engine: CombatEngineAPI)
    {
        if (weapon == null || proj.source == null) return
        for (i in 1..Companion.MUZZLE_FLASH_NUM)
        {
            val vel = Vector2f()
            val loc = Vector2f()
            vel.set(proj.velocity).scale(Misc.random.nextFloat() * 0.1f+0.1f)
            loc.set(proj.location)
            Vector2f.add(loc, MathUtils.getRandomPointOnCircumference(Misc.ZERO, 2f), loc)
            engine.addHitParticle(loc, vel, Companion.MUZZLE_FLASH_SIZE,
                                  Companion.MUZZLE_FLASH_BRIGHTNESS, Companion.MUZZLE_FLASH_DURATION,
                                  Companion.MUZZLE_FLASH_COLOR
            )
        }
        if (Misc.random.nextFloat() <= RECURSIVE_WEP_CHANCE * getChanceMult(weapon.ship.variant))
        {
            val fluxCost = weapon.fluxCostToFire / weapon.spec.burstSize
            val softFlux = proj.source.fluxTracker.currFlux - proj.source.fluxTracker.hardFlux
            proj.source.fluxTracker.decreaseFlux(min(fluxCost, softFlux))
        }
    }

    companion object
    {
        private val MUZZLE_FLASH_COLOR: Color = Color(190, 86, 255, 255)
        private const val MUZZLE_FLASH_DURATION = 0.75f
        private const val MUZZLE_FLASH_BRIGHTNESS = 1f
        private const val MUZZLE_FLASH_SIZE = 5f
        private const val MUZZLE_FLASH_NUM = 5
    }
}