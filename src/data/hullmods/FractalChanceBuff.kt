package data.hullmods

import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.ShipAPI

class FractalChanceBuff: BaseHullMod()
{
    // chance buff is handled in the various weapon scripts
    companion object
    {
        const val CHANCE_BUFF = 2f
        const val WEAPON_FLUX_MULT = 1.1f
    }

    override fun getDescriptionParam(index: Int, hullSize: ShipAPI.HullSize?): String
    {
        return when (index)
        {
            0 -> "Doubles"
            1 -> (WEAPON_FLUX_MULT * 100f - 100f).toInt().toString() + "%"
            else -> "oops"
        }
    }
}