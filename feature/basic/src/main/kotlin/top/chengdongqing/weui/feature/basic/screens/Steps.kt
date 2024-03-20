package top.chengdongqing.weui.feature.basic.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.steps.WeSteps

@Composable
fun StepsScreen() {
    WeScreen(title = "Steps", description = "步骤条") {
        var step by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeSteps(
                value = step,
                options = listOf(
                    {
                        Column {
                            Text(text = "步骤一", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                text = "描述内容详情",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    {
                        Column(modifier = Modifier.height(120.dp)) {
                            Text(text = "步骤二", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                text = "描述内容详情",
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    {
                        Column {
                            Text(text = "步骤三", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                text = "描述内容详情",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    {
                        Column {
                            Text(text = "步骤四", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                )
            )
            WeSteps(
                value = step,
                options = listOf(null, null, null, null)
            )
        }
        Column {
            WeSteps(
                value = step,
                options = listOf(
                    {
                        Text(text = "步骤一", color = MaterialTheme.colorScheme.onPrimary)
                    },
                    {
                        Text(text = "步骤二", color = MaterialTheme.colorScheme.onPrimary)
                    },
                    {
                        Column(
                            modifier = Modifier.width(180.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "步骤三", color = MaterialTheme.colorScheme.onPrimary)
                            Text(
                                text = "描述内容详情",
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                ),
                isVertical = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeSteps(
                value = step,
                options = listOf(null, null, null, null),
                isVertical = false
            )
            Spacer(modifier = Modifier.height(40.dp))
            WeButton(
                text = "更新状态",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            ) {
                if (step < 3) {
                    step++
                } else {
                    step = 0
                }
            }
        }
    }
}