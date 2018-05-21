package com.android.study.lczv.androidasciigen.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.android.study.lczv.androidasciigen.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    lateinit var presenter: MainActivityContract.Presenter

    var originalBitmap: Bitmap? = null
    var grayLevels = 1
    var charSize = 64

    // From darkest to lightest
    var asciiArray = listOf(
//            "\u2593", "\u2592", "\u2591"
            "\u2588", "\u2589", "\u258A", "\u258B", "\u258C", "\u258D", "\u258E", "\u258F", " "
    )

    companion object {
        const val IMAGE_SELECT = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter()
        presenter.onAttach(this)

        buttonLoadImage.setOnClickListener { loadImage() }

        // The maximum gray levels allowed must be the same ammount of characters
        seekBarGrayLevels.max = asciiArray.size - 1
        seekBarGrayLevels.progress = grayLevels
        updateGrayLevelsValue(grayLevels)

        seekBarGrayLevels.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                updateGrayLevelsValue(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (originalBitmap != null) {
                    presenter.convertToAsciiAndSetImage(originalBitmap!!, grayLevels, asciiArray, charSize)
                }
            }
        })


        seekBarDefinition.max = 64
        seekBarDefinition.progress = charSize
        updateCharDefinitionValue(charSize)

        seekBarDefinition.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                updateCharDefinitionValue(p1+1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (originalBitmap != null) {
                    presenter.convertToAsciiAndSetImage(originalBitmap!!, grayLevels, asciiArray, charSize)
                }

            }
        })

    }

    override fun loadImage() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(Intent.createChooser(intent, "Select an image"), IMAGE_SELECT)
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(this, "Could not load image chooser", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IMAGE_SELECT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data

                    originalBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    presenter.convertToAsciiAndSetImage(originalBitmap!!, grayLevels, asciiArray, charSize)

                }
            }
        }
    }

    override fun updateCharDefinitionValue(value: Int) {
        charSize = value
        textViewCurrentDefinition.text = value.toString()
    }

    override fun updateGrayLevelsValue(value: Int) {
        grayLevels = value
        textViewCurrentGrayLevels.text = value.toString()
    }

    override fun setBitmap(bitmap: Bitmap) {
        imageViewMain.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}