package com.example.newmapper

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils.replace
import android.util.Log
import android.util.TypedValue
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newmapper.dataModel.ImageView
import com.example.newmapper.dataModel.MapperModel
import com.example.newmapper.dataModel.TextView
import com.example.newmapper.databinding.ActivityMainBinding
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private var textArray: ArrayList<TextView> = ArrayList()
    private var imageArray: ArrayList<ImageView> = ArrayList()

    //Note this is Default Values
    private var viewBoxWidth: Int = 720
    private var viewBoxHeight: Int = 720

    private var screenFactorValues: Double = 1.0
    private var jsonFactorValues: Double = 1.0
    private var defaultScreenWidth: Int = 720


    private var workerHandler = Handler(Looper.getMainLooper())
    private var workerThread: ExecutorService = Executors.newCachedThreadPool()

    private var job: Job = Job()
    var scope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.root.post {
            Log.e("myScreenWidth", "${mainBinding.mainLayout.width}")
            defaultScreenWidth = mainBinding.mainLayout.width
            viewBoxWidth = defaultScreenWidth
            viewBoxHeight = defaultScreenWidth
        }

        mainBinding.btnRead.setOnClickListener {

            val jsonFile = loadJSONFromAsset("9.json")

            if (jsonFile != null) {

                Log.d("myJsonObject", "Json not null")

                val obj = JSONObject(jsonFile)
                val om = ObjectMapper()
                val root: MapperModel = om.readValue(obj.toString(), MapperModel::class.java)

                if (root.absoluteLayout != null) {

                    val modelBaseWidth: String? = root.absoluteLayout.androidLayoutWidth

                    modelBaseWidth?.let {

                        val modelBaseNewWidth = it.replace("dp", "").toDouble()

                        val modelBaseHeight: String =
                            root.absoluteLayout.androidLayoutHeight?.replace("dp", "")?.toDouble()
                                .toString()

                        screenFactorValues = defaultScreenWidth / modelBaseNewWidth

                        if (modelBaseHeight.toDouble() != modelBaseNewWidth) {

                            jsonFactorValues =
                                modelBaseHeight.toDouble() / modelBaseNewWidth.toDouble()

                            val finalRatio = "1:${jsonFactorValues}"

                            val set = ConstraintSet()
                            set.clone(mainBinding.mainRoot)
                            set.setDimensionRatio(mainBinding.mainLayout.id, finalRatio)
                            set.applyTo(mainBinding.mainRoot)
                        }


                        Log.d(
                            "myBaseFactor",
                            "${screenFactorValues}"
                        )

                        if (root.absoluteLayout.imageView != null) {
                            root.absoluteLayout.imageView.forEachIndexed { index, imageView ->
                                imageArray.add(index, imageView)
                            }
                        }

                        if (root.absoluteLayout.textView != null) {
                            root.absoluteLayout.textView.forEachIndexed { index, textview ->
                                textArray.add(index, textview)
                            }
                        }

                    }

                }

                workerThread.execute {
                    if (imageArray.size > 0) {
                        Log.d("myJsonObject", "Json is not  null")
                        loadImageFormMap()
                    }

                    if (textArray.size > 0) {
                        Log.d("myJsonObject", "Json is not  null")
                        loadTextFormMap()
                    }
                }


            } else {
                Log.d("myJsonObject", "Json is null")
            }

        }

    }

    private fun loadImageFormMap() {

        imageArray.forEachIndexed { _, imageView ->

            val newImage = android.widget.ImageView(this@MainActivity)

            val modelImageName = "${imageView.appSrcCompat?.replace("@drawable/", "")}.png"
            val imagePath = "file:///android_asset/${modelImageName}"

            val modelImageX = "${imageView.androidLayoutX}"
            val imageViewX: Float = if (modelImageX == "null") {
                Log.d("myImageX", "value is  if null")
                0f
            } else {
                Log.d("myImageX", " value  ${modelImageX.replace("dp", "")}")
                modelImageX.replace("dp", "").toFloat()
            }

            val modelImageY = "${imageView.androidLayoutY}"
            val imageViewY: Float = if (modelImageY == "null") {
                Log.d("myImageY", "value is  if null")
                0f
            } else {
                Log.d("myImageY", " value  ${modelImageY.replace("dp", "")}")
                modelImageY.replace("dp", "").toFloat()
            }

            val modelImageWidth = "${imageView.androidLayoutWidth}"

            val imageViewWidth: Int = when (modelImageWidth) {
                "null" -> {
                    Log.d("myImageY", "value is  if null")
                    defaultScreenWidth
                }
                "wrap_content" -> {
                    Log.d("myImageY", "value is  if null")
                    defaultScreenWidth
                }
                else -> {
                    modelImageWidth.replace("dp", "").toInt()
                }
            }

            val modelImageHeight = "${imageView.androidLayoutHeight}"
            val imageViewHeight: Int = when (modelImageHeight) {
                "null" -> {
                    Log.d("myImageY", "value is  if null")
                    defaultScreenWidth
                }
                "wrap_content" -> {
                    Log.d("myImageY", "value is  if null")
                    defaultScreenWidth
                }
                else -> {
                    modelImageHeight.replace("dp", "").toInt()
                }
            }

            val lp =
                if (imageViewWidth == defaultScreenWidth && imageViewHeight == defaultScreenWidth) {
                    RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                } else {
                    RelativeLayout.LayoutParams(
                        (imageViewWidth * screenFactorValues).roundToInt(),
                        (imageViewHeight * screenFactorValues).roundToInt()
                    )
                }

            newImage.x = (imageViewX * screenFactorValues).toFloat()
            newImage.y = (imageViewY * screenFactorValues).toFloat()

            newImage.layoutParams = lp

            workerHandler.post {

                mainBinding.mainLayout.addView(newImage)

                Glide.with(this)
                    .load(imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(newImage)
            }

        }
    }

    private fun loadTextFormMap() {

        textArray.forEachIndexed { _, textView ->

            val newText = android.widget.TextView(this@MainActivity)

            val modelTextName = "${textView.androidText}"
            val textViewName: String = if (modelTextName == "null") {
                "${getText(R.string.app_name)}"
            } else {
                modelTextName
            }

            val modelTextFont = "${textView.androidFontFamily}"
            val textViewFontName: String? = if (modelTextFont == "null") {
                null
            } else {
                "${modelTextFont.replace("@font/", "")}.ttf"
            }

            val modelTextColor = "${textView.androidTextColor}"
            val textViewColor: String? = if (modelTextColor == "null") {
                null
            } else {
                modelTextColor
            }

            val modelTextWidth = "${textView.androidLayoutWidth}"
            val textViewWidth: String? = if (modelTextWidth == "wrap_content") {
                null
            } else {
                modelTextWidth.replace("dp", "")
            }

            val modelTextHeight = "${textView.androidLayoutHeight}"
            val textViewHeight: String? = if (modelTextHeight == "wrap_content") {
                null
            } else {
                modelTextHeight.replace("dp", "")
            }

            if (textViewWidth != null && textViewHeight != null) {

                val lp = RelativeLayout.LayoutParams(
                    (textViewWidth.toInt() * screenFactorValues).roundToInt(),
                    (textViewHeight.toInt() * screenFactorValues).roundToInt()
                )

                newText.layoutParams = lp

            }

            val modelTextSize = "${textView.androidTextSize}"
            val textViewSize: Float = if (modelTextSize == "null") {
                20f
            } else {
                modelTextSize.replace("dp", "").toFloat()
            }

            val modelTextX = "${textView.androidLayoutX}"
            val textViewX: Float = if (modelTextX == "null") {
                0f
            } else {
                modelTextX.replace("dp", "").toFloat()
            }

            val modelTextY = "${textView.androidLayoutY}"
            val textViewY: Float = if (modelTextY == "null") {
                0f
            } else {
                modelTextY.replace("dp", "").toFloat()
            }

            val modelTextRotation = "${textView.androidRotation}"
            val textViewRotation: Float = if (modelTextRotation == "null") {
                0f
            } else {
                modelTextRotation.toFloat()
            }

            val modelTextShadowColor = "${textView.androidShadowColor}"
            val textViewShadowColor: String? = if (modelTextShadowColor == "null") {
                null
            } else {
                "${textView.androidShadowColor}"
            }

            textViewShadowColor?.let {

                val modelTextShadowDx = "${textView.androidShadowDx}"
                val textViewShadowDx: Float = if (modelTextShadowDx == "null") {
                    0f
                } else {
                    modelTextShadowDx.toFloat()
                }

                val modelTextShadowDy = "${textView.androidShadowDy}"
                val textViewShadowDy: Float = if (modelTextShadowDy == "null") {
                    3f
                } else {
                    modelTextShadowDy.toFloat()
                }

                val modelTextShadowRadius = "${textView.androidShadowRadius}"
                val textViewShadowRadius: Float = if (modelTextShadowRadius == "null") {
                    3f
                } else {
                    modelTextShadowRadius.toFloat()
                }

                newText.setShadowLayer(
                    textViewShadowRadius,
                    textViewShadowDx,
                    textViewShadowDy,
                    Color.parseColor(it)
                )

            }

            val modelTextAlpha = "${textView.androidAlpha}"
            val textViewAlpha: Float = if (modelTextAlpha == "null") {
                1f
            } else {
                modelTextAlpha.toFloat()
            }

            val modelTextTag = "${textView.androidTag}"
            val textViewTag: String? = if (modelTextTag == "null") {
                null
            } else {
                modelTextTag
            }

            val modelTextLetterSpacing = "${textView.androidLetterSpacing}"
            val textViewLetterSpacing: Float? = if (modelTextLetterSpacing == "null") {
                null
            } else {
                modelTextLetterSpacing.toFloat()
            }

            newText.text = textViewName

            textViewFontName?.let {

                if (textViewTag != null) {
                    try {
                        val typeface =
                            Typeface.createFromAsset(
                                assets,
                                "font/${it.replace("ttf", "${textViewTag}")}"
                            )
                        newText.typeface = typeface
                        Log.d("myFontSet", "if calling")
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                } else {
                    try {
                        val typeface = Typeface.createFromAsset(assets, "font/${it}")
                        newText.typeface = typeface
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                }

            }

            textViewLetterSpacing?.let {
                newText.letterSpacing = it
            }

            textViewColor?.let {
                newText.setTextColor(Color.parseColor(it))
            }

            Log.d("myTextX", "${screenFactorValues}")

            newText.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                (textViewSize * screenFactorValues).toFloat()
            )

            newText.x = (textViewX * screenFactorValues).toFloat()
            newText.y = (textViewY * screenFactorValues).toFloat()

            newText.rotation = textViewRotation

            newText.alpha = textViewAlpha

            workerHandler.post {
                mainBinding.mainLayout.addView(newText)
            }

            /*newText.post {

                val modelWidthSize = "${textView.androidLayoutWidth}"
                val textViewWidthSize: Float = if (modelWidthSize == "null") {
                    20f
                } else {
                    modelWidthSize.replace("dp", "").toFloat()
                }

                val fontSizeTarget =
                    correctWidth(newText, (textViewWidthSize * screenFactorValues).roundToInt())

                Log.d("textSize", "second Size ${(textViewWidthSize * screenFactorValues)}")

                newText.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizeTarget)

                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                val newX = newText.x + 20
                newText.x = newX

                val newY = newText.y + 20
                newText.y = newY

                Log.d("myTag", "${newY}")


                newText.layoutParams = params

            }*/

        }
    }

    private fun correctWidth(textView: android.widget.TextView, desiredWidth: Int): Float {
        val paint = Paint()
        val bounds = Rect()
        paint.typeface = textView.typeface
        var textSize = textView.textSize
        paint.textSize = textSize
        val text = textView.text.toString()
        paint.getTextBounds(text, 0, text.length, bounds)
        if (!textView.text.toString().contains("\n")) {
            while (bounds.width() > desiredWidth) {
                textSize--
                paint.textSize = textSize
                paint.getTextBounds(text, 0, text.length, bounds)
            }
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        return textSize
    }

    //*******************This method return the Json String *********************//
    private fun loadJSONFromAsset(fileName: String): String? {
        val json: String = try {
            val `is`: InputStream = this.assets.open(fileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    private fun convertDdpoPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    private fun convertPxToDp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }
}