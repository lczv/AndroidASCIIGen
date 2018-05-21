package imageprocessing

import android.graphics.*

object ASCIIUtils {

//    var charArray = mutableListOf(
////            "\u2593","\u2592","\u2591"
//            "\u2588", "\u2589", "\u258A", "\u258B", "\u258C", "\u258D", "\u258E", "\u258F"
//    )

    fun convertToGrayScale(bitmap: Bitmap, levels: Int): Bitmap {

        // Creates a new bitmap with the same dimensions as the original
        val grayScaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // Iterate through all image pixels
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {

                // Decompose the pixel to it's RGB components
                val rgb = bitmap.getPixel(x, y)
                val r = Color.red(rgb)
                val g = Color.green(rgb)
                val b = Color.blue(rgb)

                // Converts the image to gray
                var gray = (r + g + b) / 3

                // Groups the pixel in one of the n gray levels
                val finalGray = convertPixelToGrayLevel(originalPixelValue = gray, levels = levels)
//                println(finalGray)

                grayScaleBitmap.setPixel(x, y, Color.rgb(finalGray, finalGray, finalGray))

            }

        }

        return grayScaleBitmap
    }


    fun convertToAscii(grayScaleBitmap: Bitmap, asciiArray: List<String>, charSize: Int): Bitmap {

        val grayScaleBitmapResized = Bitmap.createScaledBitmap(grayScaleBitmap, charSize, charSize, false)

        // Creates a new bitmap with the same dimensions as the original
        val asciiBitmap = Bitmap.createBitmap(grayScaleBitmapResized.width*charSize, grayScaleBitmapResized.height*charSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(asciiBitmap)
//        canvas.drawFilter = PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, 0)
        val paint = Paint()
        val charOffset = charSize.toFloat()
        paint.textSize = charSize.toFloat()
//        paint.strokeWidth = 4f
        paint.typeface = Typeface.MONOSPACE

        // Sets the background color
        canvas.drawColor(Color.WHITE)

        // Sets the text color
        paint.color = Color.BLACK


        for (x in 0 until grayScaleBitmapResized.width) {
            for (y in 0 until grayScaleBitmapResized.height) {

                // Convert the color code to a char
                // As the image is now in grayscale, the values of R, G and B are the same
                canvas.drawText((asciiArray[Color.red(grayScaleBitmapResized.getPixel(x, y)) /*% charArray.size*/].toString()), x * charOffset, y * charOffset, paint)

            }

        }

        return asciiBitmap
    }

    fun convertPixelToGrayLevel(originalPixelValue: Int, maxGrayLevel: Int = 256, levels: Int): Int {

        val intervalSize = Math.round(maxGrayLevel / levels.toFloat())
        val pixelValue: Float = originalPixelValue.toFloat() / intervalSize

        return Math.round(pixelValue)

    }
}