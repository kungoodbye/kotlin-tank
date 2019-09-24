package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.*
import org.hk.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter
import kotlin.random.Random

/**
 * 敌方坦克
 * 是可以移动的（可以躲避遮挡物）
 * 自移动
 * 可以阻塞移动
 * 自动射击
 * 被打
 * 可以销毁
 * */
class Enemy(override var x: Int, override var y: Int) : Movable, AutoMovable, Blockable, AutoShot, Sufferable, Destroyable {


    override var boold: Int = 2
    override var currentDirection: Direction = Direction.DOWN
    override val speed: Int = 8
    override val width: Int = Config.block
    override val height: Int = Config.block
    //坦克不可以走的方向
    private var badDirection: Direction? = null
    private var lastShotTIme = 0L
    private var shotFrequency = 800

    private var lastMoveTIme = 0L
    private var moveFrequency = 50
    override fun draw() {
        val imagePath = when (currentDirection) {
            Direction.UP -> "/main/img/enemy_1_u.gif"
            Direction.DOWN -> "/main/img/enemy_1_d.gif"
            Direction.LEFT -> "/main/img/enemy_1_l.gif"
            Direction.RIGHT -> "/main/img/enemy_1_r.gif"
        }

        Painter.drawImage(imagePath, x, y)
    }

    override fun willCollision(block: Blockable): Direction? {
        //将要碰撞时做判断
        //未来的坐标
        var x = this.x
        var y = this.y
        //TODO:检查碰撞 下一步是否碰撞
        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }
        //和边界进行检测

        if (x < 0) return Direction.LEFT
        if (x > Config.gameWidth - width) return Direction.RIGHT
        if (y < 0) return Direction.UP
        if (y > Config.gameHeight - height) return Direction.DOWN

        val checkCollision = checkCollision(block.x, block.y, block.width, block.height, x, y, width, height)
        return if (checkCollision) currentDirection else null
    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        badDirection = direction
    }

    override fun autoMove() {
        //频率检查
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastMoveTIme < moveFrequency) return
        lastMoveTIme = currentTimeMillis

        if (currentDirection == badDirection) {
            //要往错误方向走，不允许的
            //改变自己方向
            currentDirection = randomDirection(badDirection)
            return
        }

        when (currentDirection) {
            Direction.UP -> y -= speed
            Direction.DOWN -> y += speed
            Direction.LEFT -> x -= speed
            Direction.RIGHT -> x += speed
        }

        //越界判断
        if (x < 0) x = 0
        if (x > Config.gameWidth - width) x = Config.gameWidth - width
        if (y < 0) y = 0
        if (y > Config.gameHeight - height) y = Config.gameHeight - height
    }


    private fun randomDirection(bad: Direction?): Direction {
        val direct = when (Random.nextInt(4)) {
            0 -> Direction.UP
            1 -> Direction.DOWN
            2 -> Direction.LEFT
            3 -> Direction.RIGHT
            else -> Direction.UP
        }
        //判断不能要错误的方向
        if (direct == bad) {
            return randomDirection(bad)
        }
        return direct
    }


    override fun autoShot(): IView? {

        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastShotTIme < shotFrequency) return null
        lastShotTIme = currentTimeMillis

        return Bullet(this, currentDirection) { bulletWidth, bulletHeight ->

            //计算子弹真实的坐标
            val tankX = x
            val tankY = y
            val tankWidth = width
            val tankHeight = height
            // 如果坦克是向上的
            //bulletX=tankX+(tankWidth-bullWidth)/2
            //bulletY=tankY-bulletHeight/2
            var bulletX = 0
            var bulletY = 0

//            var bulletWidth = 16//不写死，由子弹自身决定
//            var bulletHeight = 32

            when (currentDirection) {
                Direction.UP -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY - bulletHeight / 2
                }

                Direction.DOWN -> {
                    bulletX = tankX + (tankWidth - bulletWidth) / 2
                    bulletY = tankY + tankHeight - bulletHeight / 2
                }

                Direction.LEFT -> {
                    bulletX = tankX - tankWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }

                Direction.RIGHT -> {
                    bulletX = tankX + tankWidth - bulletWidth / 2
                    bulletY = tankY + (tankHeight - bulletHeight) / 2
                }
            }

            Pair(bulletX, bulletY)
        }
    }

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        if (attackable.owner is Enemy) {
            //挨打，不掉血，不给反应
            return null
        }
        boold -= attackable.attackPower
        //喊疼a
//        Composer.play("/main/snd/test.wav")

        return arrayOf(Blast(x, y))
    }

    override fun isDestroyed(): Boolean = boold <= 0
}