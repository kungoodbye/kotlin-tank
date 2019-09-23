package org.hk.tank.model

import org.hk.tank.Config
import org.hk.tank.business.Blockable
import org.itheima.kotlin.game.core.Painter
/**
 *
 * */
class Wall (override val x: Int, override val y: Int):Blockable{

    //宽高
    override  var width:Int= Config.block
    override var height:Int= Config.block
    //显示
    override fun  draw(){
        Painter.drawImage("main/img/wall.gif",x,y)
    }
}