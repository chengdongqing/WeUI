package top.chengdongqing.weui.core.ui.components.cardlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.divider.WeDivider

@Composable
fun Modifier.cartList(padding: PaddingValues = PaddingValues(horizontal = 16.dp)) = this
    .fillMaxWidth()
    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
    .padding(padding)

@Composable
fun WeCardListItem(label: String, value: String? = null) {
    Row(
        modifier = Modifier
            .heightIn(60.dp)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        value?.let {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = value,
                modifier = Modifier.weight(2f),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    WeDivider()
}