package dallaz.winline.app

import java.util.logging.Logger

object AppUtils {

    fun Any.logger() = Logger.getLogger(this::class.simpleName.toString())
}