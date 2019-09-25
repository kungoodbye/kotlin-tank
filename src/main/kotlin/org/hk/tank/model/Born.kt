package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Destroyable
import org.hk.tank.business.Movable
import org.hk.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter

class Born(override val x: Int, override val y: Int)
    : Destroyable {


    override val width: Int = Config.block
    override val height: Int = Config.block

    //是否销毁
    var isdestroyed: Boolean = false

    override fun isDestroyed(): Boolean {
        return isdestroyed
    }


    override fun draw() {
        val imagePath = "img/born_1.gif"
        Painter.drawImage(imagePath, x, y)
    }

    private val block = Config.block
    fun isBorn(move: Movable): Boolean {
        //根据他的朝向判断
        when (move.currentDirection) {
            Direction.UP -> {
                if ((move.x >= x - block) and (move.x <= x + block) and
                        (move.y >= y - block) and (move.y <= y + block)) {
                    return false
                }
                return true
            }
            Direction.DOWN -> {
                if ((move.x >= x) and (move.x <= (x + block * 2)) and
                        (move.y >= y) and (move.y <= (y + block * 2))) {
                    return false
                }
                return true
            }
            Direction.LEFT -> {
                if ((move.x >= x - block) and (move.x <= x + block) and
                        (move.y >= y) and (move.y <= y + block * 2)) {
                    return false
                }
                return true
            }
            Direction.RIGHT -> {
                if ((move.x >= x) and (move.x <= x + block * 2) and
                        (move.y >= y - block) and (move.y <= y + block)) {
                    return false
                }
                return true
            }

        }

    }


}