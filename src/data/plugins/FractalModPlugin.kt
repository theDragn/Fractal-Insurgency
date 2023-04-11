package data.plugins

import com.fs.starfarer.api.BaseModPlugin

class FractalModPlugin: BaseModPlugin() {
    override fun onApplicationLoad() {
        // do things!
    }

    companion object
    {
        const val RECURSIVE_WEP_CHANCE = 0.15f;
    }
}

