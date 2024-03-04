package top.chengdongqing.weui.ui.screens.demo.orgtree

import top.chengdongqing.weui.ui.components.orgtree.OrgNode

internal val governmentMap = listOf(
    OrgNode(
        "全国人民代表大会", listOf(
            OrgNode(
                "国务院", listOf(
                    OrgNode(
                        "外交部", listOf(
                            OrgNode(
                                "礼宾司", listOf(
                                    OrgNode("接待处"),
                                    OrgNode("国际会议处"),
                                    OrgNode("外交礼宾处"),
                                    OrgNode("国际礼宾处"),
                                    OrgNode("国事访问处")
                                )
                            ),
                            OrgNode("国际司"),
                            OrgNode("新闻司"),
                            OrgNode("驻外使领馆")
                        )
                    ),
                    OrgNode(
                        "财政部", listOf(
                            OrgNode("国家税务总局"),
                            OrgNode("国家统计局"),
                            OrgNode("中国人民银行"),
                            OrgNode("中国外汇管理局"),
                            OrgNode("中国国家发展和改革委员会"),
                            OrgNode("中国证券监督管理委员会"),
                            OrgNode("中国银行保险监督管理委员会")
                        )
                    ),
                    OrgNode(
                        "民政部", listOf(
                            OrgNode("民政局"),
                            OrgNode("社会组织管理局"),
                            OrgNode("慈善事业促进会")
                        )
                    ),
                    OrgNode(
                        "工信部", listOf(
                            OrgNode("工业司"),
                            OrgNode("信息化和软件服务业司"),
                            OrgNode("国际合作司"),
                            OrgNode("电子政务和信息化促进司"),
                            OrgNode("电子商务和信息化促进司")
                        )
                    ),
                    OrgNode(
                        "教育部", listOf(
                            OrgNode("教育司"),
                            OrgNode("科技发展中心"),
                            OrgNode("考试中心"),
                            OrgNode("留学服务中心"),
                            OrgNode("职业教育与成人教育司")
                        )
                    ),
                    OrgNode(
                        "公安部", listOf(
                            OrgNode("治安管理局"),
                            OrgNode("移民管理局"),
                            OrgNode("消防局"),
                            OrgNode("刑事侦查局"),
                            OrgNode("交通管理局")
                        )
                    ),
                    OrgNode(
                        "海关总署", listOf(
                            OrgNode("海关监管局"),
                            OrgNode("海关技术监管局"),
                            OrgNode("进出口税务局"),
                            OrgNode("口岸管理局"),
                            OrgNode("综合管理局")
                        )
                    ),
                    OrgNode(
                        "农业农村部", listOf(
                            OrgNode("农业发展局"),
                            OrgNode("农村经济管理局"),
                            OrgNode("农产品质量安全局"),
                            OrgNode("农村改革局"),
                            OrgNode("国土资源局")
                        )
                    ),
                    OrgNode(
                        "退役军人事务部", listOf(
                            OrgNode("军人离退休服务管理局"),
                            OrgNode("退役军人事务局"),
                            OrgNode("军休服务保障局"),
                            OrgNode("社会保障保险局"),
                            OrgNode("就业安置服务局")
                        )
                    )
                )
            ),
            OrgNode(
                "最高人民法院", listOf(
                    OrgNode("审判委员会"),
                    OrgNode("法官学院"),
                    OrgNode("执行局"),
                    OrgNode("研究室"),
                    OrgNode("信息中心")
                )
            ),
            OrgNode(
                "最高人民检察院", listOf(
                    OrgNode("检察委员会"),
                    OrgNode("反贪局"),
                    OrgNode("刑事执行局"),
                    OrgNode("国家检察官学院"),
                    OrgNode("国家检察院检察技术中心")
                )
            ),
            OrgNode(
                "中央军事委员会", listOf(
                    OrgNode("军委联合参谋部"),
                    OrgNode("军委政治工作部"),
                    OrgNode("军委后勤保障部"),
                    OrgNode("军委装备发展部"),
                    OrgNode("军委训练管理部")
                )
            )
        )
    )
)