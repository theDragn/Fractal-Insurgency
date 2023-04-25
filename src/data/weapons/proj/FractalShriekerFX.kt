package data.weapons.proj

import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI
import com.fs.starfarer.api.impl.combat.DisintegratorEffect
import com.fs.starfarer.api.util.Misc
import data.plugins.FractalModPlugin
import org.lwjgl.util.vector.Vector2f
import utils.FractalUtils
import kotlin.math.max
import kotlin.math.min

class FractalShriekerFX: OnFireEffectPlugin, OnHitEffectPlugin
{
    companion object
    {
        const val DAMAGE = 500/1000f
    }
    override fun onHit(
        proj: DamagingProjectileAPI?,
        target: CombatEntityAPI?,
        point: Vector2f?,
        shieldHit: Boolean,
        damageResult: ApplyDamageResultAPI?,
        engine: CombatEngineAPI?
    )
    {
        proj ?: return; target ?: return; point ?: return; engine ?: return; if (target !is ShipAPI) return
        engine.applyDamage(target, point, DAMAGE, DamageType.FRAGMENTATION, 0f, false, false, proj.source)
    }

    override fun onFire(proj: DamagingProjectileAPI?, weapon: WeaponAPI?, engine: CombatEngineAPI?)
    {
        proj ?: return; engine ?: return; weapon ?: return; weapon.ship ?: return
        // don't consume ammo chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
            weapon.ammo += 1
    }
}