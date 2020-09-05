package org.aahzbrut.darkmatter.asset

enum class ShaderProgramAsset(private val asset: BaseShaderProgramAsset) {
    SHOCKWAVE_SHADER(BaseShaderProgramAsset(fileName = "vertex.vert", fragmentFileName = "fragment.frag"));

    val descriptor
        get() = asset.descriptor
}