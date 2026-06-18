package top.chengdongqing.weui.feature.samples.data.model

data class WasmFileNode(
    val handle: FileSystemDirectoryHandle,
    val parentHandle: FileSystemDirectoryHandle,
    override val id: String,
    override val name: String,
    override val isDirectory: Boolean
) : FileNode(id, name, isDirectory)