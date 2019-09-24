package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Attackable
import org.hk.tank.business.Blockable
import org.hk.tank.business.Destroyable
import org.hk.tank.business.Sufferable
import org.itheima.kotlin.game.core.Composer
import org.itheima.kotlin.game.core.Painter

/**
 *钻墙
 * 具备阻塞能力
 * 具备挨打能力
 * 具备销毁能力
 * */
class Wall(override val x: Int, override val y: Int) : Blockable, Sufferable, Destroyable {


    override var boold: Int = 3


    //宽高
    override var width: Int = Config.block
    override var height: Int = Config.block
    //显示
    override fun draw() {
        Painter.drawImage("main/img/wall.gif", x, y)
    }

    override fun isDestroyed(): Boolean = boold <= 0


    override fun notifySuffer(attackable: Attackable): Array<IView> {
        //砖墙要被销毁，钻墙掉血
        boold -= attackable.attackPower
        //喊疼a
//        Composer.play("/main/snd/test.wav")

        return arrayOf(Blast(x, y))
    }
}