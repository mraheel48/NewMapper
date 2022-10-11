package com.example.newmapper

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newmapper.dataModel.ImageView
import com.example.newmapper.dataModel.MapperModel
import com.example.newmapper.dataModel.TextView
import com.example.newmapper.databinding.ActivityMainBinding
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private var textArray: ArrayList<TextView> = ArrayList()
    private var imageArray: ArrayList<ImageView> = ArrayList()

    //Note this is Default Values
    private var viewBoxWidth: Int = 720
    private var viewBoxHeight: Int = 720

    private var screenFactorValues: Double = 1.0
    private var defaultScreenWidth: Int = 720

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.root.post {
            Log.e("myScreenWidth", "${mainBinding.root.width}")
            defaultScreenWidth = mainBinding.root.width
            viewBoxWidth = defaultScreenWidth
            viewBoxHeight = defaultScreenWidth
        }

        mainBinding.btnRead.setOnClickListener {

            val jsonFile = loadJSONFromAsset("2.json")

            if (jsonFile != null) {

                Log.d("myJsonObject", "Json not null")

                val obj = JSONObject(jsonFile)
                val om = ObjectMapper()
                val root: MapperModel = om.readValue(obj.toString(), MapperModel::class.java)

                if (root.absoluteLayout != null) {

                    screenFactorValues =
                        defaultScreenWidth / root.absoluteLayout.androidLayoutWidth!!.replace(
                            "dp",
                            ""
                        ).toDouble()

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

                if (imageArray.size > 0) {
                    Log.d("myJsonObject", "Json is not  null")
                    loadImageFormMap()
                }

                if (textArray.size > 0) {
                    Log.d("myJsonObject", "Json is not  null")
                }

            } else {
                Log.d("myJsonObject", "Json is null")
            }

        }

    }

    private fun loadImageFormMap() {

        imageArray.forEachIndexed { index, imageView ->

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
            val imageViewWidth: Int = if (modelImageWidth == "null") {
                Log.d("myImageY", "value is  if null")
                0
            } else {
                Log.d("myImageY", " value  ${modelImageY.replace("dp", "")}")
                modelImageWidth.replace("dp", "").toInt()
            }

            val modelImageHeight = "${imageView.androidLayoutHeight}"
            val imageViewHeight: Int = if (modelImageHeight == "null") {
                Log.d("myImageY", "value is  if null")
                0
            } else {
                Log.d("myImageY", " value  ${modelImageY.replace("dp", "")}")
                modelImageHeight.replace("dp", "").toInt()
            }

            val lp = RelativeLayout.LayoutParams(
                (imageViewWidth * screenFactorValues).roundToInt(),
                (imageViewHeight * screenFactorValues).roundToInt()
            )

            Log.d("myImagePath", "${imageViewHeight}")

            Glide.with(this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(newImage)

            newImage.x = (imageViewX * screenFactorValues).toFloat()
            newImage.y = (imageViewY * screenFactorValues).toFloat()
            newImage.layoutParams = lp

            mainBinding.mainLayout.addView(newImage, index)

        }
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
}