package org.hk.tank

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.hk.tank.Config.block
import org.hk.tank.Config.gameHeight
import org.hk.tank.Config.gameWidth
import org.hk.tank.business.*
import org.hk.tank.enums.Direction
import org.hk.tank.model.*
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter
import org.itheima.kotlin.game.core.Window
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList
import javax.security.auth.Destroyable

class GameWindow : Window("坦克大战", "main/img/logo.jpg", gameWidth, gameHeight) {

    //管理元素的集合
//    private val views = arrayListOf<IView>()
    //线程安全的集合
    private val views = CopyOnWriteArrayList<IView>()
    private lateinit var tank: Tank
    //游戏是否结束
    private var gameOver: Boolean = false
    //敌方坦克数量
    private var enemyTotalSize = 8
    //敌方坦克显示的多少
    private var enmeyActiveSize = 4
    //敌方的初始点
    private val enemyBornLocation = arrayListOf<Pair<Int, Int>>()
    //出生地点下标
    private var bornIndex = 0

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
                    '敌' -> {
                        enemyBornLocation.add(Pair(columnNum * Config.block, lineNum * Config.block))
                        views.add(Enemy(columnNum * block, lineNum * block))
                    }
                }
                columnNum++
            }
            lineNum++
        }

        //添加我方的坦克
        tank = Tank(block * 10, block * 12)
        views.add(tank)


        //添加大本营
        views.add(Camp(Config.gameWidth / 2 - Config.block, Config.gameHeight - 96))
    }

    override fun onDisplay() {
        //绘制地图中的元素
        views.forEach {
            it.draw()
        }
    }


    override fun onKeyPressed(event: KeyEvent) {
        if (!gameOver) {
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
                KeyCode.ENTER -> {
                    //发射子弹
                    val bullet = tank.shot()

                    views.add(bullet)
                }
                else -> {}
            }
        }
    }

    override fun onRefresh() {
        //业务逻辑

        //业务逻辑

        //先判断是否被销毁再判断游戏是否结束
        /**
         * 检测依据被销毁的移除
         */
        views.filter { it is org.hk.tank.business.Destroyable }.forEach {
            if ((it as org.hk.tank.business.Destroyable).isDestroyed()) {
                views.remove(it)

                //  检测敌方坦克被销毁
                if (it is Enemy) {
                    enemyTotalSize--
                }

                //只有大本营
                val destroy = it.showDestroy()
                destroy?.let {
                    views.addAll(destroy)
                }


            }


        }

        if (gameOver) return


        //判断运动的物体和阻塞物体是否发生碰撞
        //1.找到运动的物体
        val moves = views.filter { it is Movable }.forEach { move ->
            //2.找到阻塞的物体
            move as Movable

            var badDirection: Direction? = null
            var badBlock: Blockable? = null
            //不要和自己比较
            val blocks = views.filter { (it is Blockable) and (move != it) }.forEach blockTag@{ block ->
                //3遍历集合，找到是否发生碰撞
//                move 和 block是否碰撞

                block as Blockable
                val direction = move.willCollision(block)
                direction?.let {
                    //移动的发现碰撞，跳出当前循环
                    badDirection = direction
                    badBlock = block
                    return@blockTag
                }
            }

            //找到和move碰撞的block，找到会碰撞的方向
            //通知可以移动的物体，会在哪个方向和哪个物体碰撞
            move.notifyCollision(badDirection, badBlock)
        }

        //检测自动移动能力的物体，让他们自己动起来
        views.filter { it is AutoMovable }.forEach {
            (it as AutoMovable).autoMove()
        }

        //检测销毁
        views.filter { it is org.hk.tank.business.Destroyable }.forEach {
            //判断具备销毁能力

            if ((it as org.hk.tank.business.Destroyable).isDestroyed()) {
                views.remove(it)
            }


        }

        //检测 具备攻击能力和被攻击能力的物体间是否产生碰撞
        //1)过滤具备攻击能力的
        views.filter { it is Attackable }.forEach { attack ->
            attack as Attackable

            //2)过滤 受攻击能力的 攻击方的源不可以是发射方
            views.filter { (it is Sufferable) and (attack.owner != it) and (attack != it) }.forEach sufferTag@{ suffer ->
                suffer as Sufferable
                //3)判断是否产生碰撞
                if (attack.isCollision(suffer)) {
                    //产生碰撞，找到碰撞者
                    //通知攻击者 产生碰撞
                    attack.notifyAttack(suffer)

                    //通知被攻击者，产生碰撞
                    val sufferView: Array<IView>? = suffer.notifySuffer(attack)
                    sufferView?.let {
                        //显示挨打的效果
                        views.addAll(sufferView)
                    }


                    return@sufferTag
                }
            }
        }

        //检测自动射击
        views.filter { it is AutoShot }.forEach {
            it as AutoShot
            val shot = it.autoShot()
            shot?.let {
                views.add(shot)
            }

        }
        //判断大本营是否存在 或者敌方坦克是否都消失了
        if (views.none { it is Camp } or
                (enemyTotalSize <= 0) or
                views.none { it is Tank }) {
            gameOver = true
        }

        //去生成新的坦克
        if ((enemyTotalSize - enmeyActiveSize >= 0) and
                (views.filter { it is Enemy }.size < enmeyActiveSize)) {

            //找到坦克的出生地
            val index = bornIndex % enemyBornLocation.size
            val pair = enemyBornLocation[index]
            val born = Born(pair.first, pair.second)

            //生产敌机
            //是否生产
            var show = true
            views.filter { it is Movable }.forEach bornTag@{ move ->
                move as Movable
                //println("生产基地:(${born.x},${born.y})")
                //println("坦克的地址:(${move.x},${move.y},${move.currentDirection})")
                //println("是否可以生产${born.isBorn(move)}")
                if (!born.isBorn(move)) {
                    show = false
                    return@bornTag
                }
            }

            if (show) {
                val enemy = Enemy(born.x, born.y)
                views.add(enemy)
                bornIndex++
            }
        }
    }
}
