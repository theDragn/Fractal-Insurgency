package data.weapons.proj

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.util.Misc
import data.scripts.util.MagicTargeting
import org.lazywizard.lazylib.MathUtils
import org.lazywizard.lazylib.VectorUtils
import org.lazywizard.lazylib.combat.AIUtils
import org.lazywizard.lazylib.ext.getFacing
import utils.FractalGenericMuzzleFlash
import java.awt.Color
import kotlin.math.abs


class FractalShriekerAI(var missile: MissileAPI, var ship: ShipAPI): GuidedMissileAI, MissileAIPlugin
{
    val VEL_DAMPING_FACTOR = 0.15f
    val MAX_ECCM_TURNING = 4f
    var guidanceTarget: CombatEntityAPI? = null
    var doneVFX = false
    var aimTimer = 1f
    var aimOffset = Misc.random.nextFloat() * 5f - 2.5f
    var eccm = ship.variant.hasHullMod("eccm")
    init
    {
        if (missile.weapon.size == WeaponAPI.WeaponSize.LARGE) aimOffset *= 2.5f
        guidanceTarget = ship.shipTarget ?: MagicTargeting.pickTarget(
                missile,
                MagicTargeting.targetSeeking.NO_RANDOM,
                1600,
                180,
                0,
                1,
                2,
                3,
                3,
                false
            )
    }
    override fun getTarget(): CombatEntityAPI?
    {
        return guidanceTarget
    }

    override fun setTarget(target: CombatEntityAPI?)
    {
        this.guidanceTarget = target
    }

    override fun advance(amount: Float)
    {
        // if target's died or stopped existing since missile launch, retarget
        if (guidanceTarget is ShipAPI && !(guidanceTarget as ShipAPI).isAlive || guidanceTarget == null)
            guidanceTarget = MagicTargeting.pickTarget(
                missile,
                MagicTargeting.targetSeeking.NO_RANDOM,
                1600,
                180,
                0,
                1,
                2,
                3,
                3,
                false
            )
        if (aimTimer > 0f)
        {
            aimTimer -= amount
            guidanceTarget ?: return
            val intercept = AIUtils.getBestInterceptPoint(missile.location, missile.maxSpeed, guidanceTarget?.location, guidanceTarget?.velocity)
            val aimAngle = VectorUtils.getAngle(missile.location, intercept) + aimOffset
            val angleDelta = MathUtils.getShortestRotation(missile.facing, aimAngle)
            if (angleDelta < 0) missile.giveCommand(ShipCommand.TURN_RIGHT)
            else if (angleDelta > 0) missile.giveCommand(ShipCommand.TURN_LEFT)
            if (abs(angleDelta) < abs(missile.angularVelocity) * VEL_DAMPING_FACTOR)
                missile.angularVelocity = angleDelta / VEL_DAMPING_FACTOR
        }
        if (aimTimer < 0.25f && aimTimer > 0f) missile.giveCommand(ShipCommand.DECELERATE)
        if (aimTimer < 0f && !doneVFX)
        {
            doneVFX = true
            aimTimer = 0f
            // TODO: VFX
            // engine ignition vfx
            val engineLoc = MathUtils.getPointOnCircumference(missile.location, 7f, missile.facing + 180f)
        }
        if (aimTimer <= 0f && eccm)
        {
            guidanceTarget ?: return
            val intercept = AIUtils.getBestInterceptPoint(missile.location, missile.maxSpeed, guidanceTarget?.location, guidanceTarget?.velocity)
            val aimAngle = VectorUtils.getAngle(missile.location, intercept)
            val angleDelta = MathUtils.getShortestRotation(missile.facing, aimAngle)
            if (angleDelta < 0) missile.giveCommand(ShipCommand.TURN_RIGHT)
            else if (angleDelta > 0) missile.giveCommand(ShipCommand.TURN_LEFT)
            if (abs(angleDelta) < abs(missile.angularVelocity) * VEL_DAMPING_FACTOR)
                missile.angularVelocity = angleDelta / VEL_DAMPING_FACTOR
            if (missile.angularVelocity > MAX_ECCM_TURNING) missile.angularVelocity = MAX_ECCM_TURNING
            if (missile.angularVelocity < -MAX_ECCM_TURNING) missile.angularVelocity = -MAX_ECCM_TURNING
            missile.giveCommand(ShipCommand.ACCELERATE)
        }
        if (aimTimer <= 0f) missile.giveCommand(ShipCommand.ACCELERATE)
    }
}