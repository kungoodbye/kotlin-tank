package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.enums.Direction
import org.itheima.kotlin.game.core.Painter
import javax.swing.text.View

/**
 * 我方坦克
 * */
class Tank(override var x: Int, override var y: Int) : IView {


    override var width: Int = Config.block
    override var height: Int = Config.block
    var currentDirection: Direction = Direction.UP
    var speed: Int = 8
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
}