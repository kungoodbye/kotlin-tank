package org.hk.tank

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.hk.tank.Config.block
import org.hk.tank.Config.gameHeight
import org.hk.tank.Config.gameWidth
import org.hk.tank.business.Blockable
import org.hk.tank.business.Movable
import org.hk.tank.enums.Direction
import org.hk.tank.model.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window
import java.io.File

class GameWindow : Window("坦克大战", "main/img/logo.jpg", gameWidth, gameHeight) {

    //管理元素的集合
    private val views = arrayListOf<IView>()
    private lateinit var tank: Tank
    override fun onCreate() {
        //地图
        //通过读文件的方事创建地图
        val file = File(javaClass.getResource("/main/map/1.map").path)
        //读取文件的行
        val lines = file.readLines()
        //循环遍历
        var lineNum = 0
        lines.forEach { line ->
            //列
            var columnNum = 0
            //一行变数组  [空空空] 可以拿到每一个字
            line.toCharArray().forEach { column ->
                when (column) {
                    '砖' -> views.add(Wall(columnNum * block, lineNum * block))
                    '铁' -> views.add(Steel(columnNum * block, lineNum * block))
                    '草' -> views.add(Grass(columnNum * block, lineNum * block))
                    '水' -> views.add(Water(columnNum * block, lineNum * block))
                }
                columnNum++
            }
            lineNum++
        }

        //添加我方的坦克
        tank = Tank(block * 10, block * 12)
        views.add(tank)
    }

    override fun onDisplay() {
        //绘制地图中的元素
        views.forEach {
            it.draw()
        }

    }


    override fun onKeyPressed(event: KeyEvent) {
        println("${event.code}")
        when (event.code) {
            KeyCode.W -> {
                tank.move(Direction.UP)
            }
            KeyCode.S -> {
                tank.move(Direction.DOWN)
            }
            KeyCode.A -> {
                tank.move(Direction.LEFT)
            }
            KeyCode.D -> {
                tank.move(Direction.RIGHT)
            }
        }

    }

    override fun onRefresh() {
        //业务逻辑
        //判断运动的物体和阻塞物体是否发生碰撞
        //1.找到运动的物体
        val moves = views.filter { it is Movable }.forEach { move ->
            //2.找到阻塞的物体
            move as Movable

            var badDirection: Direction? = null
            var badBlock: Blockable? = null

            val blocks = views.filter { it is Blockable }.forEach blockTag@{ block ->
                //3遍历集合，找到是否发生碰撞
//                move 和 block是否碰撞

                block as Blockable
                val direction = move.willCollision(block)
                direction?.let {
                    //移动的发现碰撞，跳出当前循环
                    badDirection=direction
                    badBlock=block
                    return@blockTag
                }
            }

            //找到和move碰撞的block，找到会碰撞的方向
            //通知可以移动的物体，会在哪个方向和哪个物体碰撞
            move.notifyCollision(badDirection,badBlock)
        }


    }
}
