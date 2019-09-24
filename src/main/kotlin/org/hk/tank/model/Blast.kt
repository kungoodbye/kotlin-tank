package org.hk.tank.model

import org.hk.tank.business.Destroyable
import sun.security.krb5.Config
import javax.swing.Painter

class Blast(override val x: Int, override val y: Int) : IView,Destroyable {


    override val width: Int = org.hk.tank.Config.block
    override val height: Int = org.hk.tank.Config.block
    private val imagePaths = arrayListOf<String>()
    private var index = 0

    init {
        (1..32).forEach {
            imagePaths.add("/main/img/blast_$it.png")
        }
    }

    override fun draw() {
        val i = index % imagePaths.size
        org.itheima.kotlin.game.core.Painter.drawImage(imagePaths[i], x, y)
        index++
    }

    override fun isDestroyed(): Boolean {
       return index >= imagePaths.size
    }


}