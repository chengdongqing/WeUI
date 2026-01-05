package top.chengdongqing.weui.feature.qrcode.scanner

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

internal class BarcodeAnalyzer(
    private val onChange: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                onChange(barcodes)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}

internal fun decodeBarcodeFromUri(context: Context, uri: Uri, onChange: (List<Barcode>) -> Unit) {
    val image = InputImage.fromFilePath(context, uri)
    val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    )

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            onChange(barcodes)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}