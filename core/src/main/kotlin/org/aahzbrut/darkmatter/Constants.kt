package org.aahzbrut.darkmatter

import com.badlogic.gdx.math.Rectangle

const val MAX_SOUND_INSTANCES = 32
const val WORLD_WIDTH = 64f
const val WORLD_HEIGHT = 36f
const val PLAYER_SIZE = 4f
const val MAX_PLAYER_LIVES = 3
const val TOUCH_TOLERANCE_DISTANCE = PLAYER_SIZE / 8
const val UPDATE_RATE = 1 / 30f
const val MAX_DELTA_TIME = 1 / 25f
const val HOR_ACCELERATION = WORLD_WIDTH / 2
const val VER_ACCELERATION = WORLD_HEIGHT / 2
const val MAX_HOR_SPEED = 16f
const val MAX_VER_SPEED = 8f
const val DEFAULT_ANIMATION_FRAME_DURATION = 1 / 20f
const val POWERUP_SIZE = 4f
const val POWERUP_SPEED = 4f
const val POWER_UP_SPAWN_SCORE = 1000
const val ENEMY_SPAWN_DELAY = 1f
const val ENEMY_SPAWN_DELAY_DELTA = .99f
const val ENEMY_SIZE = 3f
const val ENEMY_SPEED = 3f
const val MAX_WEAPON_DELAY = .5f
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
