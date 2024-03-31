package top.chengdongqing.weui.core.ui.components.tree

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.utils.clickableWithoutRipple

@Composable
fun WeTreeNode(
    label: String,
    labelSize: TextUnit = 16.sp,
    icon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    },
    expandedIcon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Filled.FolderOpen,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    },
    children: @Composable (ColumnScope.() -> Unit)? = null
) {
    val expandable = children != null
    var expended by rememberSaveable { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .clickableWithoutRipple {
                    if (expandable) {
                        expended = !expended
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (expandable) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.rotate(
                        animateFloatAsState(
                            targetValue = if (expended) 90f else 0f,
                            label = ""
                        ).value
                    )
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            if (icon != null) {
                if (expended) expandedIcon?.invoke() else icon()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = label, color = MaterialTheme.colorScheme.onPrimary, fontSize = labelSize)
        }

        if (children != null) {
            AnimatedVisibility(visible = expended) {
                Column(modifier = Modifier.padding(start = 28.dp)) {
                    children()
                }
            }
        }
    }
}