/*
 Navicat Premium Dump SQL

 Source Server         : onepiece
 Source Server Type    : MySQL
 Source Server Version : 90500 (9.5.0)
 Source Host           : localhost:3306
 Source Schema         : big_event

 Target Server Type    : MySQL
 Target Server Version : 90500 (9.5.0)
 File Encoding         : 65001

 Date: 13/01/2026 15:46:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章ID，主键，自增',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容，支持富文本',
  `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png' COMMENT '封面图片URL',
  `state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '文章状态：0-草稿，1-已发布',
  `category_id` bigint UNSIGNED NOT NULL COMMENT '分类ID，外键到tb_category.id',
  `create_user_id` bigint UNSIGNED NOT NULL COMMENT '创建者用户ID，外键到tb_user.id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_state`(`category_id` ASC, `state` ASC) USING BTREE,
  INDEX `idx_user_state`(`create_user_id` ASC, `state` ASC) USING BTREE,
  INDEX `idx_state_time`(`state` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_user_time`(`create_user_id` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_title`(`title`(20) ASC) USING BTREE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `tb_category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1, '人工智能在医疗领域的应用前景', '随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛。从医学影像诊断到药物研发，AI正在改变传统医疗模式。本文将探讨AI在医疗中的具体应用场景和未来发展趋势...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 1, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (2, '5G技术对未来社会的影响', '5G不仅是更快的网络速度，更是推动社会数字化转型的关键技术。它将促进物联网、自动驾驶、远程医疗等新兴领域的发展。本文分析5G技术的核心特点及其对各行业的影响...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 1, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (3, '家庭收纳整理的十大技巧', '合理的收纳整理能让家居空间更加整洁舒适。本文分享十个实用的收纳技巧，包括空间规划、物品分类、收纳工具选择等，帮助您打造一个井然有序的家...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 2, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (4, '如何建立健康的作息习惯', '良好的作息习惯对身心健康至关重要。本文将介绍科学的时间管理方法，帮助您调整生物钟，提高睡眠质量，从而拥有更充沛的精力和更高的工作效率...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 2, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (5, '2024年最值得期待的电影盘点', '从漫威宇宙新作到国内实力导演的最新力作，2024年的电影市场精彩纷呈。本文为您盘点今年最值得关注的电影作品，涵盖各种类型和风格...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 3, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (6, '综艺节目的创新与突破', '近年来，综艺节目在形式和内容上不断创新。本文分析当前热门综艺的特点，探讨综艺节目如何平衡娱乐性和社会价值，满足观众多样化需求...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 3, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (7, '篮球训练中的投篮技巧提升', '投篮是篮球运动中最基础也是最重要的技术之一。本文详细介绍正确的投篮姿势、发力技巧和训练方法，帮助篮球爱好者提高投篮命中率...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 4, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (8, '马拉松比赛的备战策略', '参加马拉松需要科学的训练和充分的准备。本文提供从训练计划、饮食营养到比赛装备的全方位指导，帮助跑者安全完赛并取得好成绩...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 4, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (9, '个人投资理财的入门指南', '对于理财新手来说，建立正确的投资观念至关重要。本文介绍基本的理财知识、风险评估方法和适合初学者的投资渠道，帮助您迈出理财第一步...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 5, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (10, '数字货币的发展现状与未来', '区块链技术和数字货币正在重塑金融体系。本文分析全球数字货币的发展现状、监管政策和未来趋势，探讨其对传统金融的影响...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 5, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (11, 'STEAM教育理念在基础教育中的应用', 'STEAM教育强调科学、技术、工程、艺术和数学的跨学科融合。本文探讨如何将STEAM理念融入中小学课堂，培养学生的创新思维和实践能力...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 6, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (12, '在线教育平台的优势与挑战', '疫情加速了在线教育的发展。本文分析在线教育的优势、存在的问题以及未来发展趋势，为教育工作者和家长提供参考...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 6, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (13, '办公室人群的颈椎保健方法', '长时间使用电脑容易导致颈椎问题。本文介绍适合办公室人群的颈椎保健操、正确坐姿和日常注意事项，帮助预防颈椎病...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 7, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (14, '科学饮食与体重管理', '健康的饮食习惯是保持理想体重的关键。本文将介绍营养均衡的饮食原则、科学的热量控制和实用的减肥建议...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 7, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (15, '国内小众旅游目的地推荐', '避开人山人海的热门景点，探索那些鲜为人知的美丽地方。本文推荐几个具有特色的国内小众旅游目的地，适合喜欢安静旅行的朋友...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 8, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (16, '自驾游的安全注意事项', '自驾游虽然自由方便，但也需要注意安全问题。本文提供车辆检查、路线规划、应急处理等全方位自驾游安全指南...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 8, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (17, '家常菜烹饪技巧大全', '掌握一些基本的烹饪技巧，能让家常菜更加美味。本文分享刀工、火候、调味等方面的实用技巧，提升您的厨艺水平...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 9, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (18, '健康早餐的制作与搭配', '早餐是一天中最重要的一餐。本文将介绍营养均衡的早餐搭配原则，以及几款简单易做的健康早餐食谱...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 9, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (19, '2024年春季流行趋势预测', '从色彩到款式，从面料到搭配，本文将为您解析2024年春季的时尚流行趋势，帮助您打造时尚的春季造型...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 10, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (20, '职场穿搭的基本原则', '得体的职场穿搭能提升专业形象。本文介绍不同行业、不同场合的职场穿搭技巧，平衡专业性与个人风格...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 10, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (21, '电动汽车选购指南', '随着电动汽车技术的成熟，越来越多消费者考虑购买电动汽车。本文从续航、充电、品牌等方面为您提供选购建议...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 11, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (22, '汽车日常保养的注意事项', '定期保养能延长汽车使用寿命，确保行车安全。本文将介绍汽车日常保养的项目、周期和注意事项...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 11, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (23, '首次购房者的注意事项', '购买第一套房是重要的人生决策。本文从地段选择、户型评估、贷款办理等方面为首次购房者提供全面指导...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 12, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (24, '二手房交易流程详解', '二手房交易相对复杂，涉及多个环节。本文将详细解析二手房交易的完整流程和注意事项，帮助买卖双方顺利完成交易...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 12, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (25, '中国传统节日文化内涵探析', '中国传统节日承载着丰富的文化内涵。本文以春节、端午、中秋为例，探讨节日的起源、习俗及其文化意义...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 13, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (26, '现代社会中的家庭关系变迁', '随着社会发展，家庭结构和家庭关系也在发生变化。本文分析当代家庭的特点、面临的挑战和应对策略...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 13, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (27, '丝绸之路的历史意义与现代启示', '丝绸之路不仅是古代贸易通道，更是文明交流的桥梁。本文回顾丝绸之路的历史，探讨其对现代一带一路倡议的启示...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 14, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (28, '工业革命对人类社会的深远影响', '工业革命改变了人类的生产和生活方式。本文分析工业革命的技术创新、社会变革及其对现代世界的影响...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 14, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (29, '中国书画艺术的欣赏方法', '欣赏中国书画需要了解一定的背景知识。本文将介绍中国书画的基本特点、流派演变和欣赏要点，提升艺术鉴赏能力...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 15, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');
INSERT INTO `tb_article` VALUES (30, '数字艺术的发展与未来', '数字技术正在改变艺术创作和传播方式。本文探讨数字艺术的特点、表现形式以及与传统艺术的关系...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 15, 1, '2026-01-13 15:43:13', '2026-01-13 15:43:13');

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键，自增',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `category_alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类别名',
  `create_user_id` bigint UNSIGNED NOT NULL COMMENT '创建用户ID（关联tb_user.id）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_alias`(`create_user_id` ASC, `category_alias` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_category_user` FOREIGN KEY (`create_user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_category
-- ----------------------------
INSERT INTO `tb_category` VALUES (1, '科技', 'tech', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (2, '生活', 'life', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (3, '娱乐', 'entertainment', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (4, '体育', 'sports', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (5, '财经', 'finance', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (6, '教育', 'education', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (7, '健康', 'health', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (8, '旅游', 'travel', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (9, '美食', 'food', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (10, '时尚', 'fashion', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (11, '汽车', 'car', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (12, '房产', 'realestate', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (13, '人文', 'humanities', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (14, '历史', 'history', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');
INSERT INTO `tb_category` VALUES (15, '艺术', 'art', 1, '2026-01-13 15:42:32', '2026-01-13 15:42:32');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自增',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名，5-16个字符，唯一',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码，加密存储',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称，1-20个字符',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱地址，唯一',
  `user_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png' COMMENT '头像URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'admin@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');
INSERT INTO `tb_user` VALUES (2, 'wangba', 'e10adc3949ba59abbe56e057f20f883e', '王八', 'wangba@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');
INSERT INTO `tb_user` VALUES (3, 'testuser', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 'test@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');
INSERT INTO `tb_user` VALUES (4, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'zhangsan@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');
INSERT INTO `tb_user` VALUES (5, 'editor', 'e10adc3949ba59abbe56e057f20f883e', '编辑员', 'editor@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');
INSERT INTO `tb_user` VALUES (6, 'viewer', 'e10adc3949ba59abbe56e057f20f883e', '查看者', 'viewer@example.com', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_avatar.png', '2026-01-13 15:41:46', '2026-01-13 15:41:46');

SET FOREIGN_KEY_CHECKS = 1;
