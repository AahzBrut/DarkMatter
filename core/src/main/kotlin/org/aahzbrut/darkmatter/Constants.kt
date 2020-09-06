package org.aahzbrut.darkmatter

import com.badlogic.gdx.math.Rectangle

const val ASSET_STORAGE_LOADER_THREAD_NUMBER = 4

const val MAX_SOUND_INSTANCES = 32
const val WORLD_WIDTH = 64f
const val WORLD_HEIGHT = 36f
const val PPU = 10f
const val WORLD_WIDTH_UI = WORLD_WIDTH * PPU
const val WORLD_HEIGHT_UI = WORLD_HEIGHT * PPU
const val LIVES_INDICATOR_HEIGHT = 32f
const val LIVES_INDICATOR_WIDTH = 64f
const val PLAYER_SIZE = 4f
const val PLAYER_ROLL_MAX_VALUE = 9
const val PLAYER_ROLL_SPEED = PLAYER_ROLL_MAX_VALUE * 4f
const val PLAYER_DISTANCE_TO_TARGET_ALLOWANCE = .1f
const val PLAYER_ROLL_TOLERANCE = .1f
const val MAX_PLAYER_LIVES = 3
const val UPDATE_RATE = 1 / 30f
const val MAX_DELTA_TIME = 1 / 25f
const val MAX_ACCELERATION = WORLD_WIDTH / 2
const val MAX_SPEED = 16f
const val DEFAULT_ANIMATION_FRAME_DURATION = 1 / 20f
const val POWERUP_SIZE = 4f
const val POWERUP_SPEED = 4f
const val POWER_UP_SPAWN_SCORE = 1000
const val POWER_UP_SHIELD_MAX_ACTIVE_TIME = 10f
const val POWER_UP_SPEED_MAX_ACTIVE_TIME = 10f
const val POWER_UP_TRIPLE_SHOT_MAX_ACTIVE_TIME = 10f
const val ENEMY_SPAWN_DELAY = 1f
const val ENEMY_SPAWN_DELAY_DELTA = .99f
const val ENEMY_SIZE = 3f
const val ENEMY_SPEED = 3f
const val MAX_WEAPON_DELAY = .5f
const val EXPLOSION_TIME = 6f
const val MAX_RAY_NUMBER = 16
const val PROJECTILE_SIZE = 2f
const val PROJECTILE_SPEED = 10f
const val ENEMY_KILL_SCORE = 100
const val ENEMY_ESCAPE_SCORE = -10

// Player bounding box
val PLAYER_BOUNDING_BOX = Rectangle(
        PLAYER_SIZE / 7f,
        PLAYER_SIZE / 8f,
        PLAYER_SIZE - PLAYER_SIZE / 3.5f,
        PLAYER_SIZE - PLAYER_SIZE / 3.8f)

// Power Up bounding box
val POWERUP_BOUNDING_BOX = Rectangle(
        POWERUP_SIZE / 4f,
        POWERUP_SIZE / 5f,
        POWERUP_SIZE - POWERUP_SIZE / 2f,
        POWERUP_SIZE - POWERUP_SIZE / 2.5f)

// Enemy bounding box
val ENEMY_BOUNDING_BOX = Rectangle(
        ENEMY_SIZE / 4f,
        ENEMY_SIZE / 5f,
        ENEMY_SIZE - ENEMY_SIZE / 2f,
        ENEMY_SIZE - ENEMY_SIZE / 2.5f)

// Projectile bounding box
val PROJECTILE_BOUNDING_BOX = Rectangle(
        PROJECTILE_SIZE / 16f,
        PROJECTILE_SIZE / 18f,
        PROJECTILE_SIZE / 8f,
        PROJECTILE_SIZE - PROJECTILE_SIZE / 10f)
