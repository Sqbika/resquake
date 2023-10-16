package polina4096.resquake

import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import net.minecraft.text.Text
import net.minecraft.client.gui.screen.Screen
import java.awt.Color

fun ConfigBuilder.createCategory(text: Text, categoryConfig: (ConfigCategory, ConfigBuilder) -> Unit): ConfigBuilder {
    val categoryBuilder = getOrCreateCategory(text)
    categoryConfig(categoryBuilder, this)
    return this
}

fun Color.alphalessRGB() = (rgb and 0x00FFFFFF)
fun generateConfigScreen(parent: Screen?): Screen =
    ConfigBuilder.create()
        .setParentScreen(parent)
        .setSavingRunnable(ReSquakeMod.config::save)
        .setTitle(Text.of(ReSquakeMod.NAME))
        .createCategory(Text.of("Movement")) { category, builder ->
            category.addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("Quake-style movement"), ReSquakeMod.config.quakeMovementEnabled)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_QUAKE_MOVEMENT_ENABLED)
                    .setTooltip(Text.of("Enables/disables all movement changes made by this mod"))
                    .setSaveConsumer { ReSquakeMod.config.quakeMovementEnabled = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("Trimping"), ReSquakeMod.config.trimpingEnabled)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_TRIMPING_ENABLED)
                    .setTooltip(Text.of("Enables/disables trimping (big jump when sneaking)"))
                    .setSaveConsumer { ReSquakeMod.config.trimpingEnabled = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("Sharking"), ReSquakeMod.config.sharkingEnabled)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SHARKING_ENABLED)
                    .setTooltip(Text.of("Enables/disables sharking (water glide)"))
                    .setSaveConsumer { ReSquakeMod.config.sharkingEnabled = it }
                    .build()
            )
        }
        .createCategory(Text.of("Miscellaneous")) { category, builder ->
            category.addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("Uncapped bunnyhop"), ReSquakeMod.config.uncappedBunnyhop)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_UNCAPPED_BUNNYHOP)
                    .setTooltip(Text.of("If enabled, the soft and hard speed caps will not be applied at all"))
                    .setSaveConsumer { ReSquakeMod.config.uncappedBunnyhop = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("No jump cooldown"), ReSquakeMod.config.noJumpCooldown)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_TRIMPING_ENABLED)
                    .setTooltip(Text.of("Enables/disables jump cooldown (better to leave enabled)"))
                    .setSaveConsumer { ReSquakeMod.config.noJumpCooldown = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startIntSlider(Text.of("Jump particles"), ReSquakeMod.config.jumpParticles, 0, 100)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_JUMP_PARTICLES)
                    .setTooltip(Text.of("Amount of particles that spawn when you hit the ground (0 to disable)"))
                    .setSaveConsumer { ReSquakeMod.config.jumpParticles = it }
                    .build()
            )
        }
        .createCategory(Text.of("Speed indicator")) { category, builder ->
            category.addEntry(
                builder.entryBuilder()
                    .startBooleanToggle(Text.of("Delta indicator"), ReSquakeMod.config.speedDeltaIndicatorEnabled)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SPEED_DELTA_INDICATOR_ENABLED)
                    .setTooltip(Text.of("Enables/disables the display of change in speed"))
                    .setSaveConsumer { ReSquakeMod.config.speedDeltaIndicatorEnabled = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Speed delta threshold"), ReSquakeMod.config.speedDeltaThreshold)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SPEED_DELTA_THRESHOLD)
                    .setTooltip(Text.of("Minimum speed needed for indicator to appear"))
                    .setSaveConsumer { ReSquakeMod.config.speedDeltaThreshold = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startColorField(Text.of("Speed gain color"), ReSquakeMod.config.speedGainColor)
                    .setDefaultValue(ReSquakeConfig.SPEED_GAIN_COLOR.alphalessRGB())
                    .setTooltip(Text.of("Color of speed delta indicator when you gain additional speed"))
                    .setSaveConsumer { ReSquakeMod.config.speedGainColor = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startColorField(Text.of("Speed loss color"), ReSquakeMod.config.speedLossColor) // | || || |_
                    .setDefaultValue(ReSquakeConfig.SPEED_LOSS_COLOR.alphalessRGB())
                    .setTooltip(Text.of("Color of speed delta indicator when you lose gained speed"))
                    .setSaveConsumer { ReSquakeMod.config.speedLossColor = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startColorField(Text.of("Speed unchanged color"), ReSquakeMod.config.speedUnchangedColor)
                    .setDefaultValue(ReSquakeConfig.SPEED_UNCHANGED_COLOR.alphalessRGB())
                    .setTooltip(Text.of("Color of speed delta indicator when your speed remains the same"))
                    .setSaveConsumer { ReSquakeMod.config.speedUnchangedColor = it }
                    .build()
            )
        }
        .createCategory(Text.of("Quake-style movement")) { category, builder ->
            category.addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Soft cap threshold"), ReSquakeMod.config.softCapThreshold)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SOFT_CAP_THRESHOLD)
                    .setTooltip(Text.of("soft cap speed = (moveSpeed*softCapThreshold)"))
                    .setSaveConsumer { ReSquakeMod.config.softCapThreshold = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Hard cap threshold"), ReSquakeMod.config.hardCapThreshold)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_HARD_CAP_THRESHOLD)
                    .setTooltip(Text.of("If you jump while above the hard cap speed (moveSpeed*hardCapThreshold), your speed is set to the hard cap speed"))
                    .setSaveConsumer { ReSquakeMod.config.hardCapThreshold = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Acceleration"), ReSquakeMod.config.acceleration)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_ACCELERATION)
                    .setTooltip(Text.of("A higher value means you accelerate faster on the ground"))
                    .setSaveConsumer { ReSquakeMod.config.acceleration = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Air acceleration"), ReSquakeMod.config.airAcceleration)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_AIR_ACCELERATION)
                    .setTooltip(Text.of("A higher value means you can turn more sharply in the air without losing speed"))
                    .setSaveConsumer { ReSquakeMod.config.airAcceleration = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Max air acceleration per tick"), ReSquakeMod.config.maxAAccPerTick)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_MAX_AACEL_PER_TICK)
                    .setTooltip(Text.of("Limit for how much you can accelerate in a tick"))
                    .setSaveConsumer { ReSquakeMod.config.maxAAccPerTick = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Soft cap degen"), ReSquakeMod.config.softCapDegen)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SOFT_CAP_DEGEN)
                    .setTooltip(Text.of("The modifier used to calculate speed lost when jumping above the soft cap"))
                    .setSaveConsumer { ReSquakeMod.config.softCapDegen = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Trimp multiplier"), ReSquakeMod.config.trimpMultiplier)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_TRIMP_MULTIPLIER)
                    .setTooltip(Text.of("A lower value means less horizontal speed converted to vertical speed"))
                    .setSaveConsumer { ReSquakeMod.config.trimpMultiplier = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Sharking surface tension"), ReSquakeMod.config.sharkingSurfaceTension)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SHARKING_SURFACE_TENSION)
                    .setSaveConsumer { ReSquakeMod.config.sharkingSurfaceTension = it }
                    .build()
            ).addEntry(
                builder.entryBuilder()
                    .startDoubleField(Text.of("Sharking friction"), ReSquakeMod.config.sharkingFriction)
                    .setDefaultValue(ReSquakeConfig.DEFAULT_SHARKING_FRICTION)
                    .setSaveConsumer { ReSquakeMod.config.sharkingFriction = it }
                    .build()
            )
        }
        .build()