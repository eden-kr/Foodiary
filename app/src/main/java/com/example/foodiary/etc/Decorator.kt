package com.example.foodiary.etc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.style.ForegroundColorSpan
import android.text.style.LineBackgroundSpan
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.foodiary.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

//Calendar Decorator
class Decorator(val context: Context, val list : MutableList<CalendarDay?>?) : DayViewDecorator{

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return list!!.contains(day)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(6F,Color.RED))
    }
}
class MemoDecorator(val context: Context) : LineBackgroundSpan{
    override fun drawBackground(p0: Canvas, p1: Paint, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: CharSequence, p8: Int, p9: Int, p10: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
//sunday
class SundayDecorator : DayViewDecorator{
    private val cal = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(cal)
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.RED))
    }
}

class SaturdayDecorator : DayViewDecorator{
    private val cal = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(cal)
        val weekDay = cal.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.BLUE))
    }
}