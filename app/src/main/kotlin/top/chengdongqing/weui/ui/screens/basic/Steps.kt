package top.chengdongqing.weui.ui.screens.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.steps.WeSteps
import top.chengdongqing.weui.ui.theme.FontSecondaryColorLight

@Composable
fun StepsScreen() {
    WeScreen(title = "Steps", description = "步骤条") {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var step by remember {
                mutableIntStateOf(0)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeSteps(
                    value = step,
                    items = listOf(
                        {
                            Column {
                                Text(text = "步骤一")
                                Text(
                                    text = "描述内容详情",
                                    color = FontSecondaryColorLight,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        {
                            Column(modifier = Modifier.height(120.dp)) {
                                Text(text = "步骤二")
                                Text(
                                    text = "描述内容详情",
                                    color = FontSecondaryColorLight,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        {
                            Column {
                                Text(text = "步骤三")
                                Text(
                                    text = "描述内容详情",
                                    color = FontSecondaryColorLight,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        {
                            Column {
                                Text(text = "步骤四")
                            }
                        }
                    )
                )
                WeSteps(
                    value = step,
                    items = listOf(null, null, null, null)
                )
            }
            Column {
                WeSteps(
                    value = step,
                    items = listOf(
                        {
                            Text(text = "步骤一")
                        },
                        {
                            Text(text = "步骤二")
                        },
                        {
                            Column(
                                modifier = Modifier.width(180.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "步骤三")
                                Text(
                                    text = "描述内容详情",
                                    color = FontSecondaryColorLight,
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
                    items = listOf(null, null, null, null),
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
}