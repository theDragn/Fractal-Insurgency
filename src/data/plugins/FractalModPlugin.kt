package data.plugins

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.PluginPick
import com.fs.starfarer.api.campaign.CampaignPlugin
import com.fs.starfarer.api.combat.MissileAIPlugin
import com.fs.starfarer.api.combat.MissileAPI
import com.fs.starfarer.api.combat.ShipAPI
import data.weapons.proj.FractalShriekerAI

class FractalModPlugin: BaseModPlugin() {
    override fun onApplicationLoad() {
        // do things!
    }

    override fun pickMissileAI(missile: MissileAPI, launchingShip: ShipAPI): PluginPick<MissileAIPlugin>?
    {
        return when (missile.projectileSpecId)
        {
            "fractal_shrieker_shot" -> return PluginPick(FractalShriekerAI(missile, launchingShip), CampaignPlugin.PickPriority.MOD_SPECIFIC)
            else -> null
        }
    }
    companion object
    {
        const val RECURSIVE_WEP_CHANCE = 0.15f;
    }
}

