package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Attackable
import org.hk.tank.business.Blockable
import org.hk.tank.business.Destroyable
import org.hk.tank.business.Sufferable
import org.itheima.kotlin.game.core.Painter
/**
 * 大本营
 *
 * 具备阻挡得功能
 * 具备接受攻击得功能
 * */
class Camp(override var x: Int, override var y: Int) : Blockable,Sufferable ,Destroyable{



    override var boold: Int=12
    override var width: Int = Config.block * 2
    override var height: Int = Config.block + 32

    override fun draw() {
        //血量低于 6个时画的时 砖墙
        //血量低于 3个时画的时 没有墙
        when {
            boold <= 3 -> {
                width = Config.block
                height = Config.block
                x = (Config.gameWidth - Config.block) / 2
                y = Config.gameHeight - Config.block
                Painter.drawImage("/main/img/camp.gif", x, y)

            }
            boold <= 6 -> {

                //绘制外围的砖块
                Painter.drawImage("/main/img/wall_small.gif", x, y)
                Painter.drawImage("/main/img/wall_small.gif", x + 32, y)
                Painter.drawImage("/main/img/wall_small.gif", x + 64, y)
                Painter.drawImage("/main/img/wall_small.gif", x + 96, y)

                Painter.drawImage("/main/img/wall_small.gif", x, y + 32)
                Painter.drawImage("/main/img/wall_small.gif", x, y + 64)

                Painter.drawImage("/main/img/wall_small.gif", x + 96, y + 32)
                Painter.drawImage("/main/img/wall_small.gif", x + 96, y + 64)

                Painter.drawImage("/main/img/camp.gif", x + 32, y + 32)

            }
            else -> {

                //绘制外围的砖块
                Painter.drawImage("/main/img/steel_small.gif", x, y)
                Painter.drawImage("/main/img/steel_small.gif", x + 32, y)
                Painter.drawImage("/main/img/steel_small.gif", x + 64, y)
                Painter.drawImage("/main/img/steel_small.gif", x + 96, y)

                Painter.drawImage("/main/img/steel_small.gif", x, y + 32)
                Painter.drawImage("/main/img/steel_small.gif", x, y + 64)

                Painter.drawImage("/main/img/steel_small.gif", x + 96, y + 32)
                Painter.drawImage("/main/img/steel_small.gif", x + 96, y + 64)

                Painter.drawImage("/main/img/camp.gif", x + 32, y + 32)

            }
        }
    }


    override fun notifySuffer(attackable: Attackable): Array<IView>? {

        boold -= attackable.attackPower
        if (boold == 3 || boold == 6) {
            val x = x - 32
            val y = y - 32
            return arrayOf(Blast(x, y)
                    , Blast(x + 32, y)
                    , Blast(x + Config.block, y)
                    , Blast(x + Config.block + 32, y)
                    , Blast(x + Config.block * 2, y)
                    , Blast(x, y + 32)
                    , Blast(x, y + Config.block)
                    , Blast(x, y + Config.block + 32)
                    , Blast(x + Config.block * 2, y + 32)
                    , Blast(x + Config.block * 2, y + Config.block)
                    , Blast(x + Config.block * 2, y + Config.block + 32))
        }
        //显示爆炸物
        return  null
    }

    override fun isDestroyed(): Boolean=boold<=0

}