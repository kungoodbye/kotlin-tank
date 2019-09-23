package org.hk.tank.business

import org.hk.tank.enums.Direction
import org.hk.tank.model.IView

//移动运动的能力
interface Movable :IView{
    /**
     * 可移动物体方向
     *
     * */
    val currentDirection: Direction
    /**
     * 可移动物体需要有移动的速度
     ***/
    val speed: Int

    /**判断移动的物体是否和阻塞物体发生碰撞
     **@return 要碰撞的方向,如果为null，说明没有碰撞的
     **/

    fun willCollision(block: Blockable): Direction?

    /***
     * 通知碰撞
     * */
    fun notifyCollision(direction:Direction?,block: Blockable?)
}