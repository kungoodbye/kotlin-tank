package org.hk.tank.business

import org.hk.tank.enums.Direction
import org.hk.tank.model.IView
/**
 * 自动移动的能力
 * */
interface AutoMovable :IView{
    //速度
    val speed:Int
    //方向
    val currentDirection: Direction
    fun autoMove()
}