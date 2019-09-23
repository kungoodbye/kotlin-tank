package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Blockable
import org.hk.tank.business.Movable
import org.hk.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter
import javax.swing.text.View

/**
 * 我方坦克
 * 具备移动能力
 * */
class Tank(override var x: Int, override var y: Int) : Movable {


    override var width: Int = Config.block
    override var height: Int = Config.block
    override var currentDirection: Direction = Direction.UP
    override var speed: Int = 8
    //坦克不可以走的方向
    private var badDirection: Direction? = null

    override fun draw() {

        //方式一
//        when(currentDirection){
//            Direction.UP-> Painter.drawImage("/main/img/tank_u.gif",x,y)
//            Direction.DOWN-> Painter.drawImage("/main/img/tank_d.gif",x,y)
//            Direction.LEFT-> Painter.drawImage("/main/img/tank_l.gif",x,y)
//            Direction.RIGHT-> Painter.drawImage("/main/img/tank_r.gif",x,y)
//        }

        //方式二
        val imagePath = when (currentDirection) {
            Direction.UP -> "/main/img/tank_u.gif"
            Direction.DOWN -> "/main/img/tank_d.gif"
            Direction.LEFT -> "/main/img/tank_l.gif"
            Direction.RIGHT -> "/main/img/tank_r.gif"
        }

        Painter.drawImage(imagePath, x, y)

    }

    /**
     * 坦克移动
     * */
    fun move(direction: Direction) {

        //判断是否是往要碰撞的方向走
        if (direction == badDirection) {
            //不往下执行
            return
        }

        //当前的方向和希望的方向不一致时，只做方向改变
        if (this.currentDirection != direction) {
            this.currentDirection = direction
            return
        }


        //坦克的坐标需要变化
        //根据不通融的放现，改变对应的坐标
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

        val collision = when {
            block.y + block.height <= y -> //如果 阻挡物在运动物体上方时,不碰撞
                false
            y + height <= block.y -> //如果 阻挡物在运动物体下方时,不碰撞
                false
            block.x + block.width <= x -> //如果 阻挡物在运动物体左边时,不碰撞
                false
            x + width <= block.x -> //如果 阻挡物在运动物体右方时,不碰撞
                false
            else -> //碰撞
                true
        }
        return if (collision) currentDirection else null

    }

    override fun notifyCollision(direction: Direction?, block: Blockable?) {
        //TODO：接收到碰撞信息
        this.badDirection = direction
    }
}