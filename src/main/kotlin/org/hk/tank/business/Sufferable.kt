package org.hk.tank.business

import org.hk.tank.model.IView
import javax.swing.text.View

/**
 * 遭受攻击的接口
 * */
interface Sufferable :IView{
    /**
     * 生命值
     * */
    val boold:Int


    fun notifySuffer(attackable:Attackable):Array<IView>?
}