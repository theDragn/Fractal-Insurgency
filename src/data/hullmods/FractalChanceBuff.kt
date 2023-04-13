package data.hullmods

import com.fs.starfarer.api.combat.BaseHullMod

class FractalChanceBuff: BaseHullMod()
{
    // doesn't actually do anything on its own
    // effects are handled in weapon and hullmod scripts
    companion object
    {
        const val CHANCE_BUFF = 2f
    }
}