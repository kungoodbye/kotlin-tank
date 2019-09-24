package org.hk.tank.business

import org.hk.tank.model.IView
/**
 * 攻击接口
 * */
interface Attackable :IView {

    /**
     * 所有者
     * */
    val owner:IView


    /**
    *攻击力
    * **/
    val attackPower:Int

    //判断是否碰撞
    fun isCollision(sufferable:Sufferable):Boolean

    fun notifyAttack(sufferable: Sufferable)
}