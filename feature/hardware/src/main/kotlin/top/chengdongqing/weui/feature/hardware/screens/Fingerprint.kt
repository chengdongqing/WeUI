package top.chengdongqing.weui.feature.hardware.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.showToast

@Composable
fun FingerprintScreen() {
    WeScreen(title = "Fingerprint", description = "指纹认证") {
        val context = LocalContext.current

        WeButton(text = "指纹认证") {
            context.startActivity(BiometricActivity.newIntent(context))
        }
    }
}

class BiometricActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("指纹认证")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setNegativeButtonText("取消认证")
            .build()
        createBiometricPrompt(this).authenticate(promptInfo)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, BiometricActivity::class.java)
    }
}

private fun createBiometricPrompt(activity: FragmentActivity): BiometricPrompt {
    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            activity.finish()
            activity.showToast(errString.toString())
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            activity.showToast("认证失败")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            activity.setResult(Activity.RESULT_OK)
            activity.finish()
            activity.showToast("认证成功")
        }
    }
    return BiometricPrompt(activity, callback)
}

// 使用旧API可以自定义界面样式，但安全性和兼容性需要自己控制
//class FingerprintViewModel(private val application: Application) : AndroidViewModel(application) {
//
//    private lateinit var fingerprintManager: FingerprintManager
//    private lateinit var cancellationSignal: CancellationSignal
//
//    fun initFingerprintManager(context: Context) {
//        fingerprintManager =
//            context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
//        startListening(null)
//    }
//
//    private fun startListening(cryptoObject: FingerprintManager.CryptoObject?) {
//        cancellationSignal = CancellationSignal()
//        if (ActivityCompat.checkSelfPermission(
//                application,
//                Manifest.permission.USE_FINGERPRINT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        fingerprintManager.authenticate(
//            cryptoObject,
//            cancellationSignal,
//            0,
//            authenticationCallback,
//            null
//        )
//    }
//
//    private val authenticationCallback = object : FingerprintManager.AuthenticationCallback() {
//        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//            super.onAuthenticationError(errorCode, errString)
//            // 处理错误情况
//            application.showToast("出现错误")
//        }
//
//        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
//            super.onAuthenticationSucceeded(result)
//            // 认证成功
//            application.showToast("认证成功")
//        }
//
//        override fun onAuthenticationFailed() {
//            super.onAuthenticationFailed()
//            // 认证失败
//            application.showToast("认证失败")
//        }
//    }
//
//    fun cancelAuthentication() {
//        cancellationSignal.cancel()
//    }
//}