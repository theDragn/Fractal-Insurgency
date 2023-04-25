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


class FractalWailerFX: OnHitEffectPlugin, OnFireEffectPlugin
{
    companion object
    {
        const val DAMAGE = 300/800f
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
        if (shieldHit) return

        // literally just copy pasted from breach onhit
        val grid: ArmorGridAPI = target.armorGrid
        val cell = grid.getCellAtLocation(point) ?: return

        val gridWidth = grid.grid.size
        val gridHeight = grid.grid[0].size

        val damageTypeMult = DisintegratorEffect.getDamageTypeMult(proj.source, target)

        var damageDealt = 0f
        for (i in -2..2)
        {
            for (j in -2..2)
            {
                if ((i == 2 || i == -2) && (j == 2 || j == -2)) continue  // skip corners
                val cx = cell[0] + i
                val cy = cell[1] + j
                if (cx < 0 || cx >= gridWidth || cy < 0 || cy >= gridHeight) continue

                val damMult = if (i == 0 && j == 0)
                {
                    1 / 15f
                } else if (i <= 1 && i >= -1 && j <= 1 && j >= -1)
                { // S hits
                    1 / 15f
                } else
                { // T hits
                    1 / 30f
                }
                val armorInCell = grid.getArmorValue(cx, cy)
                var damage = (DAMAGE * proj.damage.damage) * damMult * damageTypeMult
                damage = min(damage, armorInCell)
                if (damage <= 0) continue
                target.armorGrid.setArmorValue(cx, cy, max(0f, armorInCell - damage))
                damageDealt += damage
            }
        }

        if (damageDealt > 0)
        {
            if (Misc.shouldShowDamageFloaty(proj.source, target))
            {
                engine.addFloatingDamageText(
                    point,
                    damageDealt,
                    Misc.FLOATY_ARMOR_DAMAGE_COLOR,
                    target,
                    proj.source
                )
            }
            target.syncWithArmorGridState()
        }
    }

    override fun onFire(proj: DamagingProjectileAPI?, weapon: WeaponAPI?, engine: CombatEngineAPI?)
    {
        proj ?: return; engine ?: return; weapon ?: return; weapon.ship ?: return
        // don't consume ammo chance
        if (Misc.random.nextFloat() <= FractalModPlugin.RECURSIVE_WEP_CHANCE * FractalUtils.getChanceMult(weapon.ship.variant))
            weapon.ammo += 1
    }
}