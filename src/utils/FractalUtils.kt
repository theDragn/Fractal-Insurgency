package utils

import com.fs.starfarer.api.combat.ShipVariantAPI
import data.hullmods.FractalChanceBuff

class FractalUtils
{
    companion object
    {
        fun getChanceMult(ship: ShipVariantAPI?): Float
        {
            if (ship == null) return 1f
            var mult = 1f
            if (ship.hasHullMod("fractal_chancebuff")) mult *= FractalChanceBuff.CHANCE_BUFF
            if (ship.hasHullMod("fractal_chanceremove")) mult *= 0
            return mult
        }
    }
}