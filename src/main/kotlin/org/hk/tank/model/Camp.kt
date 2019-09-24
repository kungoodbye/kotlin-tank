package org.hk.tank.model

import org.hk.tank.Config
import org.itheima.kotlin.game.core.Painter

class Camp(override val x: Int, override val y: Int) : IView {
    override val width: Int = Config.block * 2
    override val height: Int = Config.block + 32

    override fun draw() {

        //绘制外围的钻块
        Painter.drawImage("/main/img/steel_small.gif",x,y)
    }
}