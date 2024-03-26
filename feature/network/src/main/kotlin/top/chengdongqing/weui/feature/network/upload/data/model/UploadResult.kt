package top.chengdongqing.weui.feature.network.upload.data.model

data class UploadResult(
    val error: String,
    val strings: List<String>,
    val files: Files
) {
    data class Files(
        val file: FileItem
    ) {
        data class FileItem(
            val name: String,
            val size: Long,
            val type: String,
            val url: String
        )
    }
}