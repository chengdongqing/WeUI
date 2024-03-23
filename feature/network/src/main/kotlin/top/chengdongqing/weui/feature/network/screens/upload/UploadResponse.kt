package top.chengdongqing.weui.feature.network.screens.upload

data class UploadResponse(
    val error: String,
    val strings: List<String>,
    val files: Files
) {
    data class Files(val file: FileItem) {
        data class FileItem(
            val name: String,
            val size: Long,
            val type: String,
            val url: String
        )
    }
}