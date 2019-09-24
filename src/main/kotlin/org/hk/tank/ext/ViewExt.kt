package org.hk.tank.ext

import org.hk.tank.model.IView

fun IView.checkCollision(view: IView): Boolean {
   return checkCollision(x, y, width, height, view.x, view.y, view.width, view.height)
}