package darkmatter

import com.badlogic.gdx.math.Rectangle

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


//Player bounding box
val PLAYER_BOUNDING_BOX = Rectangle(
        PLAYER_SIZE / 7f,
        PLAYER_SIZE / 8f,
        PLAYER_SIZE - PLAYER_SIZE / 3.5f,
        PLAYER_SIZE - PLAYER_SIZE / 3.8f)

val POWERUP_BOUNDING_BOX = Rectangle(
        POWERUP_SIZE / 4f,
        POWERUP_SIZE / 5f,
        POWERUP_SIZE - POWERUP_SIZE / 2f,
        POWERUP_SIZE - POWERUP_SIZE / 2.5f)