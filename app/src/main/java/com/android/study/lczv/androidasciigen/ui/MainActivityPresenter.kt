package com.android.study.lczv.androidasciigen.ui

import android.graphics.Bitmap
import imageprocessing.ASCIIUtils

class MainActivityPresenter : MainActivityContract.Presenter {

    var originalBitmap: Bitmap? = null
    var grayScaleBitmap: Bitmap? = null
    var asciiBitmap: Bitmap? = null
    var grayLevels = 0

    private var view: MainActivityContract.View? = null

    override fun onAttach(view: MainActivityContract.View) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun convertToAsciiAndSetImage(bitmap: Bitmap, levels: Int, asciiArray: List<String>, charSize: Int) {
        grayScaleBitmap = ASCIIUtils.convertToGrayScale(bitmap, levels)
        asciiBitmap = ASCIIUtils.convertToAscii(grayScaleBitmap!!, asciiArray, charSize)
        view?.setBitmap(asciiBitmap!!)

    }

}