package com.example.newmapper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.newmapper.dataModel.ImageView
import com.example.newmapper.dataModel.MapperModel
import com.example.newmapper.dataModel.TextView
import com.example.newmapper.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets


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
        }

        mainBinding.btnRead.setOnClickListener {

            val jsonFile = loadJSONFromAsset("2.json")

            val userObject: MapperModel = Gson().fromJson(jsonFile, MapperModel::class.java)

            viewBoxWidth =
                userObject.absoluteLayout?.androidLayoutWidth.toString().replace("dp", "").toInt()
            viewBoxHeight =
                userObject.absoluteLayout?.androidLayoutHeight.toString().replace("dp", "").toInt()

            userObject.absoluteLayout?.textView?.forEach {
                textArray.add(it)
            }

            userObject.absoluteLayout?.imageView?.forEach {
                imageArray.add(it)
            }

            screenFactorValues = defaultScreenWidth.toDouble() / viewBoxWidth.toDouble()

            Log.d(
                "myTag",
                " $screenFactorValues"
            )

            if (imageArray.isNotEmpty()) {
                Log.d("myMapper", "list is not null")
                loadImageFormMap()
            } else {
                Log.d("myMapper", "list is null")
            }

        }

    }


    private fun loadImageFormMap() {
        imageArray.forEachIndexed { index, imageView ->


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