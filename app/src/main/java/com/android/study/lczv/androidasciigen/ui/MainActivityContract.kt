package com.android.study.lczv.androidasciigen.ui

import android.graphics.Bitmap

interface MainActivityContract {
    interface View {

        fun loadImage()
        fun updateCharDefinitionValue(value: Int)
        fun updateGrayLevelsValue(value: Int)
        fun setBitmap(bitmap: Bitmap)

    }

    interface Presenter {

        fun onAttach(view: View)
        fun onDetach()

        fun convertToAsciiAndSetImage(bitmap: Bitmap, levels: Int, asciiArray: List<String>, charSize: Int)
    }
}