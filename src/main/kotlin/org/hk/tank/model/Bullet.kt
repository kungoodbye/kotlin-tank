package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Attackable
import org.hk.tank.business.AutoMovable
import org.hk.tank.business.Destroyable
import org.hk.tank.business.Sufferable
import org.hk.tank.enums.Direction
import org.hk.tank.ext.checkCollision
import org.itheima.kotlin.game.core.Painter
import javax.swing.text.View

/**
 * 子弹
 * create()函数返回两个值，是x，y
 * */
class Bullet(override val owner: IView, override val currentDirection: Direction, create: (width: Int, height: Int) -> Pair<Int, Int>)
    : AutoMovable
        , Destroyable, Attackable, Sufferable {
    override val boold: Int = 1



    override val attackPower: Int = 1

    //给子弹一个方向,方向由坦克来决定的
    override val width: Int
    override val height: Int
    override val speed: Int = 8

    override var x: Int = 0
    override var y: Int = 0
    private val imagePath: String = when (currentDirection) {
        Direction.UP -> "/main/img/bullet_u.gif"
        Direction.DOWN -> "/main/img/bullet_d.gif"
        Direction.LEFT -> "/main/img/bullet_l.gif"
        Direction.RIGHT -> "/main/img/bullet_r.gif"
    }

    init {
        //先计算宽度和高度

        val size = Painter.size(imagePath)
        width = size[0]
        height = size[1]

        val pair = create.invoke(width, height)
        x = pair.first
        y = pair.second
    }

    override fun draw() {


        Painter.drawImage(imagePath, x, y)
    }


    override fun autoMove() {
        //根据自己的方向来改变自己的x，y
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }

    }

    private var isDestroyed = false

    override fun isDestroyed(): Boolean {
        if (isDestroyed) return true

        //子弹再脱离了屏幕后
        if (x < -width) return true
        if (x > Config.gameWidth) return true
        if (y < -height) return true
        if (y > Config.gameHeight) return true

        return false
    }

    override fun isCollision(sufferable: Sufferable): Boolean {


        return checkCollision(sufferable)
    }

    override fun notifyAttack(sufferable: Sufferable) {
        //自己人打自己人子弹不消失
        isDestroyed =
                if ((sufferable is Enemy) and (this.owner is Enemy)) {
                    false
                } else !((sufferable is Tank) and (this.owner is Tank))
    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        return null
    }
}