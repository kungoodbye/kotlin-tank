package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Attackable
import org.hk.tank.business.Blockable
import org.hk.tank.business.Sufferable
import org.itheima.kotlin.game.core.Painter

class Steel(override val x: Int, override val y: Int) : Blockable, Sufferable {
    override val boold: Int = 1

    override fun notifySuffer(attackable: Attackable): Array<IView>? {
        return null
    }

    //宽高
    override var width: Int = Config.block
    override var height: Int = Config.block
    //显示
    override fun draw() {
        Painter.drawImage("main/img/steel.gif", x, y)
    }
}