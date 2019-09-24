package org.hk.tank.business

import org.hk.tank.model.IView
/**
 *
 * 销毁的能力
 * */
interface Destroyable:IView {
    /**
     *
     * */
    fun isDestroyed():Boolean
}