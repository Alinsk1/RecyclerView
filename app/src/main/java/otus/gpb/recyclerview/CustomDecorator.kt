package otus.gpb.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CustomDecorator(context: Context): ItemDecoration() {


    private val bounds = Rect()
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.gray_light)
        strokeWidth = 1f
    }
    val leftMarginInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        78f,
        context.resources.displayMetrics
    ).toInt()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val positionCurrent = parent.getChildAdapterPosition(child)
            if (positionCurrent != RecyclerView.NO_POSITION) {
                val lastElementPosition = parent.adapter?.itemCount?.minus(1)
                if (positionCurrent != lastElementPosition) {
                    c.drawLine(
                        leftMarginInPx.toFloat(),
                        bounds.bottom.toFloat(),
                        bounds.right.toFloat(),
                        bounds.bottom.toFloat(),
                        paint,
                    )
                }
            }
        }
    }
}



