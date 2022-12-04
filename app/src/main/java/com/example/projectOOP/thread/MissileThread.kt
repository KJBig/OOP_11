package com.example.projectOOP.thread

import com.example.projectOOP.GameLogic
import com.example.projectOOP.viewobject.Missile


class MissileThread(private val gameLogic: GameLogic) : Thread() {
    private var runCondition = false

    override fun run() {
            while (runCondition) {
                try {
                   gameLogic.missiles.add(Missile(gameLogic.context, gameLogic.playerX, gameLogic.playerY))
                    sleep(gameLogic.missileReload.toLong())
                } catch (e : InterruptedException) {
                    println("쓰레드 꺼짐")
                }
            }
    }
    fun setRunning(state: Boolean) {
        runCondition = state
    }
}