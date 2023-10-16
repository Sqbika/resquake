package polina4096.resquake

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.TranslatableText
import org.lwjgl.glfw.GLFW


object ReSquakeModClient : ClientModInitializer {
    private var keyToggle: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("key.${ReSquakeMod.ID}.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category.${ReSquakeMod.ID}"))
    private var keyConfig: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("key.${ReSquakeMod.ID}.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.${ReSquakeMod.ID}"))

    override fun onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            if (client.player != null)
                ReSquakePlayer.jumping = client.player!!.input.jumping

            if (keyConfig.wasPressed()) {
                client.setScreen(generateConfigScreen(client.currentScreen))
            }

            while (keyToggle.wasPressed()) {
                ReSquakeMod.config.quakeMovementEnabled = !ReSquakeMod.config.quakeMovementEnabled
                if (ReSquakeMod.config.quakeMovementEnabled)
                     client.player?.sendMessage(TranslatableText("resquake.enabled"), true)
                else client.player?.sendMessage(TranslatableText("resquake.disabled"), true)
            }
        })

        val mc = MinecraftClient.getInstance()
        HudRenderCallback.EVENT.register { matrixStack: MatrixStack, _: Float ->
            val speed = ReSquakePlayer.currentSpeed * 20
            if (!ReSquakeMod.config.speedDeltaIndicatorEnabled || !ReSquakePlayer.jumping || ReSquakePlayer.swimming || speed < ReSquakeMod.config.speedDeltaThreshold)
                return@register

            val posX = mc.window.scaledWidth  / 2.0f
            val posY = mc.window.scaledHeight / 2.0f
            val text = "%.2f".format(speed)
            val centerOffset = mc.textRenderer.getWidth(text) / 2.0f

            val delta = ReSquakePlayer.currentSpeed.compareTo(ReSquakePlayer.previousSpeed)
            val color = when {
                delta > 0 -> ReSquakeMod.config.speedGainColor
                delta < 0 -> ReSquakeMod.config.speedLossColor
                else -> ReSquakeMod.config.speedUnchangedColor
            }

            mc.textRenderer.draw(matrixStack, text, posX - centerOffset, posY + 15, color)
        }
    }
}