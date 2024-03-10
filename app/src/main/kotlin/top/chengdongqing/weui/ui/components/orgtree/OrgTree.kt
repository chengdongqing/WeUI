package top.chengdongqing.weui.ui.components.orgtree

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.clickableWithoutRipple

data class OrgNode(val label: String, val children: List<OrgNode> = emptyList())

@Composable
fun WeOrgTree(dataSource: List<OrgNode>, isTopLevel: Boolean = true) {
    val spacing = 20.dp
    val lineColor = MaterialTheme.orgTreeColorScheme.lineColor
    val layoutCoordinates = remember { mutableMapOf<Int, Pair<Float, Float>>() }

    Row(
        modifier = Modifier
            .onGloballyPositioned { layoutCoordinates.clear() }
            .drawHorizontalLine(dataSource, layoutCoordinates, lineColor, spacing)
    ) {
        dataSource.forEachIndexed { index, item ->
            var expended by remember { mutableStateOf(item.children.isEmpty()) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    layoutCoordinates[index] = Pair(
                        coordinates.positionInParent().x,
                        coordinates.size.width.toFloat()
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (item.children.isNotEmpty()) {
                                Modifier
                            } else {
                                Modifier.width(28.dp)
                            }
                        )
                        .drawVerticalLine(
                            isTopLevel,
                            lineColor,
                            spacing,
                            expended,
                            hasChildren = item.children.isNotEmpty()
                        )
                        .clip(RoundedCornerShape(2.dp))
                        .setStyle(expended)
                        .clickableWithoutRipple {
                            if (item.children.isNotEmpty()) {
                                expended = !expended
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.label,
                        color = if (expended) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.White
                        },
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
                if (item.children.isNotEmpty()) {
                    if (expended) {
                        Spacer(modifier = Modifier.height(spacing * 2))
                        WeOrgTree(item.children, false)
                    } else {
                        Spacer(modifier = Modifier.height(5.dp))
                        ExpandableIcon {
                            expended = true
                        }
                    }
                }
            }
            if (index < dataSource.lastIndex) {
                Spacer(modifier = Modifier.width(spacing))
            }
        }
    }
}

@Composable
private fun ExpandableIcon(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSecondary,
                CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "展开",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .size(20.dp)
                .clickableWithoutRipple {
                    onClick()
                }
        )
    }
}

@Composable
private fun Modifier.setStyle(expended: Boolean): Modifier {
    return if (expended) {
        this
            .border(
                width = 0.8.dp,
                color = MaterialTheme.orgTreeColorScheme.borderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .background(MaterialTheme.colorScheme.onBackground)
    } else {
        this.background(PrimaryColor)
    }
}

private fun Modifier.drawVerticalLine(
    isTopLevel: Boolean,
    lineColor: Color,
    space: Dp,
    expended: Boolean,
    hasChildren: Boolean
) = this.drawBehind {
    // 绘制顶部连接线
    if (!isTopLevel) {
        drawLine(
            color = lineColor,
            start = Offset(size.width / 2, -space.toPx()),
            end = Offset(size.width / 2, 0.dp.toPx()),
            strokeWidth = 0.5.dp.toPx()
        )
    }
    // 绘制底部连接线
    if (expended && hasChildren) {
        drawLine(
            color = lineColor,
            start = Offset(size.width / 2, size.height),
            end = Offset(size.width / 2, size.height + space.toPx()),
            strokeWidth = 0.5.dp.toPx()
        )
    }
}

private fun Modifier.drawHorizontalLine(
    dataSource: List<OrgNode>,
    layoutCoordinates: Map<Int, Pair<Float, Float>>,
    lineColor: Color,
    space: Dp
) = this.drawBehind {
    // 确保至少有两个子元素才绘制水平线
    if (dataSource.size > 1) {
        layoutCoordinates.values
            .firstOrNull()
            ?.let { first ->
                layoutCoordinates.values
                    .lastOrNull()
                    ?.let { last ->
                        drawLine(
                            color = lineColor,
                            start = Offset(
                                first.first + (first.second / 2),
                                -space.toPx()
                            ),
                            end = Offset(last.first + (last.second / 2), -space.toPx()),
                            strokeWidth = 0.5.dp.toPx()
                        )
                    }
            }
    }
}

private data class OrgTreeColors(
    val lineColor: Color,
    val borderColor: Color
)

private val MaterialTheme.orgTreeColorScheme: OrgTreeColors
    @Composable
    get() = OrgTreeColors(
        lineColor = if (isSystemInDarkTheme()) {
            colorScheme.outline
        } else {
            Color.Black
        },
        borderColor = if (isSystemInDarkTheme()) {
            colorScheme.outline
        } else {
            colorScheme.onPrimary
        }
    )