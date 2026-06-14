package top.chengdongqing.weui.basic.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import top.chengdongqing.weui.components.WeScreen
import top.chengdongqing.weui.components.WeTreeNode
import weui_kmp.shared.generated.resources.Res
import weui_kmp.shared.generated.resources.ic_apk
import weui_kmp.shared.generated.resources.ic_document

@Composable
fun TreeScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Tree",
        description = "树型菜单",
        horizontalAlignment = Alignment.Start,
        onBack = onBack
    ) {
        WeTreeNode("WeUI") {
            WeTreeNode("安装包") {
                repeat(10) { index ->
                    ApkNode("v${index + 1}.0")
                }
                WeTreeNode("未发布的版本") {
                    ApkNode("alpha01")
                    ApkNode("beta05")
                }
            }
        }
        WeTreeNode("文档") {
            DocumentNode("个人简历.pdf")
            DocumentNode("技术分享.pptx")
            DocumentNode("销售账单.xlsx")
            DocumentNode("开发手册.docx")
        }
    }
}

@Composable
private fun DocumentNode(name: String) {
    WeTreeNode(
        label = name,
        labelSize = 14.sp,
        icon = {
            Image(
                painter = painterResource(Res.drawable.ic_document),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}

@Composable
private fun ApkNode(version: String) {
    WeTreeNode(
        label = "WeUI_$version.apk",
        labelSize = 14.sp,
        icon = {
            Image(
                painter = painterResource(Res.drawable.ic_apk),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}