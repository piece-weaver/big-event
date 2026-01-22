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

SET FOREIGN_KEY_CHECKS = 0;

-- ==================== 新增表结构（阶段一） ====================

-- ----------------------------
-- Table structure for tb_article_version
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_version`;
CREATE TABLE `tb_article_version`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '版本ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容',
  `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '文章状态：0-草稿，1-已发布',
  `category_id` bigint UNSIGNED NOT NULL COMMENT '分类ID',
  `version_number` int NOT NULL COMMENT '版本号',
  `change_summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改摘要',
  `created_by` bigint UNSIGNED NOT NULL COMMENT '创建者用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_article_version`(`article_id` ASC, `version_number` ASC) USING BTREE,
  CONSTRAINT `fk_version_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章版本表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_stats
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_stats`;
CREATE TABLE `tb_article_stats`  (
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `view_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `share_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '分享次数',
  `collect_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏次数',
  `comment_count` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论次数',
  `last_viewed_at` datetime NULL DEFAULT NULL COMMENT '最后浏览时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`article_id`) USING BTREE,
  CONSTRAINT `fk_stats_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_ai_tags
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_ai_tags`;
CREATE TABLE `tb_article_ai_tags`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `confidence` decimal(4, 3) NOT NULL COMMENT '置信度（0-1）',
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ai' COMMENT '来源：ai-AI推荐, manual-手动添加',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_tag`(`tag`(20) ASC) USING BTREE,
  CONSTRAINT `fk_aitags_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章AI标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_summary
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_summary`;
CREATE TABLE `tb_article_summary`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'AI生成的内容摘要',
  `reading_time` int NULL DEFAULT NULL COMMENT '预计阅读时间（分钟）',
  `seo_score` tinyint(3) NULL DEFAULT NULL COMMENT 'SEO评分（0-100）',
  `seo_suggestions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'SEO优化建议（JSON格式）',
  `model_used` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '使用的AI模型',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_id`(`article_id` ASC) USING BTREE,
  CONSTRAINT `fk_summary_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章AI摘要表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_audit_log`;
CREATE TABLE `tb_audit_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID，主键，自增',
  `user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '用户ID，外键到tb_user.id',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `resource_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源类型',
  `resource_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '资源ID',
  `resource_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源名称',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户代理',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数（JSON格式）',
  `response_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '响应状态',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_action`(`action` ASC) USING BTREE,
  INDEX `idx_resource`(`resource_type` ASC, `resource_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '审计日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_scheduled_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_scheduled_task`;
CREATE TABLE `tb_scheduled_task`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '任务ID，主键，自增',
  `task_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务类型：publish-发布, delete-删除',
  `resource_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源类型：article-文章',
  `resource_id` bigint UNSIGNED NOT NULL COMMENT '资源ID',
  `scheduled_time` datetime NOT NULL COMMENT '计划执行时间',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-待执行，1-执行中，2-已完成，3-已取消，4-失败',
  `created_by` bigint UNSIGNED NOT NULL COMMENT '创建者用户ID',
  `executed_at` datetime NULL DEFAULT NULL COMMENT '实际执行时间',
  `result_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '执行结果',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_scheduled_time`(`scheduled_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_resource`(`resource_type` ASC, `resource_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '定时任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_webhook
-- ----------------------------
DROP TABLE IF EXISTS `tb_webhook`;
CREATE TABLE `tb_webhook`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Webhook ID，主键，自增',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Webhook名称',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回调URL',
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '签名密钥',
  `events` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发事件列表（JSON格式）',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否激活：0-禁用，1-激活',
  `created_by` bigint UNSIGNED NOT NULL COMMENT '创建者用户ID',
  `last_triggered_at` datetime NULL DEFAULT NULL COMMENT '最后触发时间',
  `success_count` int NOT NULL DEFAULT 0 COMMENT '成功次数',
  `failed_count` int NOT NULL DEFAULT 0 COMMENT '失败次数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_is_active`(`is_active` ASC) USING BTREE,
  INDEX `idx_created_by`(`created_by` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Webhook配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_webhook_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_webhook_log`;
CREATE TABLE `tb_webhook_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID，主键，自增',
  `webhook_id` bigint UNSIGNED NOT NULL COMMENT 'Webhook ID，外键到tb_webhook.id',
  `event_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '触发事件类型',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求载荷（JSON格式）',
  `response_status` int NULL DEFAULT NULL COMMENT '响应状态码',
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '响应内容',
  `duration` int NULL DEFAULT NULL COMMENT '响应时间（毫秒）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：success-成功，failed-失败，pending-等待中',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_webhook_id`(`webhook_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  CONSTRAINT `fk_webhook_log_webhook` FOREIGN KEY (`webhook_id`) REFERENCES `tb_webhook` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Webhook执行日志表' ROW_FORMAT = Dynamic;

-- ==================== 初始化新表数据 ====================

-- 初始化文章统计表（为现有文章创建统计记录）
INSERT INTO `tb_article_stats` (`article_id`, `view_count`, `like_count`, `share_count`, `collect_count`, `comment_count`)
SELECT `id`, FLOOR(RAND() * 100), FLOOR(RAND() * 20), FLOOR(RAND() * 10), FLOOR(RAND() * 15), FLOOR(RAND() * 5)
FROM `tb_article`;

-- 初始化文章摘要表（AI摘要表，先留空，等待AI生成）
INSERT INTO `tb_article_summary` (`article_id`, `summary`, `reading_time`, `seo_score`)
SELECT `id`, CONCAT('待AI生成 - ', SUBSTRING(content, 1, 20), '...'), CEIL(LENGTH(`content`) / 500), NULL FROM `tb_article`;

-- ==================== 阶段二新增表（风险处理） ====================

-- ----------------------------
-- Table structure for tb_webhook_retry
-- ----------------------------
DROP TABLE IF EXISTS `tb_webhook_retry`;
CREATE TABLE `tb_webhook_retry`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '重试记录ID，主键，自增',
  `webhook_id` bigint UNSIGNED NOT NULL COMMENT 'Webhook ID，外键到tb_webhook.id',
  `event_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事件类型',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求载荷',
  `attempt_count` int NOT NULL DEFAULT 0 COMMENT '已尝试次数',
  `max_attempts` int NOT NULL DEFAULT 3 COMMENT '最大尝试次数',
  `last_error` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '最后错误信息',
  `next_retry_at` datetime NOT NULL COMMENT '下次重试时间',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态：0-等待中，1-重试中，2-成功，3-失败',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_retry`(`status` ASC, `next_retry_at` ASC) USING BTREE,
  INDEX `idx_webhook_id`(`webhook_id` ASC) USING BTREE,
  CONSTRAINT `fk_retry_webhook` FOREIGN KEY (`webhook_id`) REFERENCES `tb_webhook` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Webhook重试任务表' ROW_FORMAT = Dynamic;

-- ==================== 阶段三新增表（社交功能）====================

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID，外键到tb_user.id',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `parent_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '父评论ID（回复），外键到tb_comment.id',
  `like_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `tb_comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_like
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_like`;
CREATE TABLE `tb_article_like`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID，外键到tb_user.id',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_user`(`article_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_like_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_article_collect
-- ----------------------------
DROP TABLE IF EXISTS `tb_article_collect`;
CREATE TABLE `tb_article_collect`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID，主键，自增',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID，外键到tb_article.id',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID，外键到tb_user.id',
  `folder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '默认收藏' COMMENT '收藏夹名称',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_article_user`(`article_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_folder`(`user_id` ASC, `folder_name` ASC) USING BTREE,
  CONSTRAINT `fk_collect_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_collect_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_behavior
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_behavior`;
CREATE TABLE `tb_user_behavior`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID，主键，自增',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章ID',
  `behavior_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '行为类型：view-浏览, like-点赞, collect-收藏, comment-评论',
  `duration` int UNSIGNED NULL DEFAULT NULL COMMENT '停留时长（秒）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_article`(`user_id` ASC, `article_id` ASC) USING BTREE,
  INDEX `idx_user_behavior`(`user_id` ASC, `behavior_type` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  CONSTRAINT `fk_behavior_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_behavior_article` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户行为表（用于推荐系统）' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 0;

-- ==================== 阶段三新增表（RBAC权限管理）====================

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID，主键，自增',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限编码：article:read',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父权限ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  INDEX `idx_parent`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键，自增',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码：ADMIN, EDITOR, VIEWER',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `is_system` tinyint(1) NOT NULL DEFAULT 0 COMMENT '系统内置角色：0-否，1-是',
  `priority` int NOT NULL DEFAULT 0 COMMENT '角色优先级（数值越大权限越高）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id` ASC, `permission_id` ASC) USING BTREE,
  INDEX `idx_permission`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `fk_rp_role` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rp_permission` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色-权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_role`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户-角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` (`code`, `name`, `description`, `parent_id`) VALUES
('article:read', '文章查看', '查看文章列表和详情', 0),
('article:write', '文章编写', '创建和编辑文章', 0),
('article:delete', '文章删除', '删除文章', 0),
('article:export', '文章导出', '导出文章数据', 0),
('category:read', '分类查看', '查看分类', 0),
('category:write', '分类管理', '管理分类', 0),
('category:delete:force', '分类强制删除', '强制删除包含文章的分类', 0),
('user:read', '用户查看', '查看用户信息', 0),
('user:manage', '用户管理', '管理系统用户', 0),
('admin:system', '系统管理', '系统配置管理', 0);

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` (`code`, `name`, `description`, `is_system`, `priority`) VALUES
('ADMIN', '管理员', '系统管理员，拥有所有权限', 1, 100),
('EDITOR', '编辑员', '内容编辑员，拥有文章和分类权限', 1, 50),
('VIEWER', '查看者', '只读用户，只能查看文章和分类', 1, 10);

-- ----------------------------
-- Assign permissions to roles
-- ----------------------------
INSERT INTO `tb_role_permission` (`role_id`, `permission_id`)
SELECT r.id, p.id FROM `tb_role` r, `tb_permission` p WHERE r.code = 'ADMIN';

INSERT INTO `tb_role_permission` (`role_id`, `permission_id`)
SELECT r.id, p.id FROM `tb_role` r, `tb_permission` p
WHERE r.code = 'EDITOR' AND p.code IN ('article:read', 'article:write', 'category:read', 'category:write');

INSERT INTO `tb_role_permission` (`role_id`, `permission_id`)
SELECT r.id, p.id FROM `tb_role` r, `tb_permission` p
WHERE r.code = 'VIEWER' AND p.code IN ('article:read', 'category:read');

-- ----------------------------
-- Assign ADMIN role to user ID 1 (系统管理员)
-- ----------------------------
INSERT INTO `tb_user_role` (`user_id`, `role_id`)
SELECT id, (SELECT id FROM `tb_role` WHERE code = 'ADMIN') FROM `tb_user` WHERE id = 1;

-- ==================== 初始化空表数据 ====================

-- ----------------------------
-- Records of tb_article_version (文章版本历史)
-- ----------------------------
INSERT INTO `tb_article_version` (`article_id`, `title`, `content`, `cover_img`, `state`, `category_id`, `version_number`, `change_summary`, `created_by`, `created_at`) VALUES
(1, '人工智能在医疗领域的应用前景', '随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛。从医学影像诊断到药物研发，AI正在改变传统医疗模式。本文将探讨AI在医疗中的具体应用场景和未来发展趋势...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 1, 1, '初始草稿', 1, '2026-01-10 10:00:00'),
(1, '人工智能在医疗领域的应用前景', '随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛。从医学影像诊断到药物研发，AI正在改变传统医疗模式。本文将探讨AI在医疗中的具体应用场景，包括智能诊断、个性化治疗、药物研发等方面，并分析未来发展趋势...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 1, 1, 2, '补充AI应用场景', 1, '2026-01-12 14:30:00'),
(2, '5G技术对未来社会的影响', '5G不仅是更快的网络速度，更是推动社会数字化转型的关键技术。它将促进物联网、自动驾驶、远程医疗等新兴领域的发展。本文分析5G技术的核心特点及其对各行业的影响...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 1, 1, '初始草稿', 1, '2026-01-11 09:00:00'),
(5, '2024年最值得期待的电影盘点', '从漫威宇宙新作到国内实力导演的最新力作，2024年的电影市场精彩纷呈。本文为您盘点今年最值得关注的电影作品，涵盖各种类型和风格...', 'https://big-event-demo-123.oss-cn-beijing.aliyuncs.com/default_article_cover.png', 0, 3, 1, '初始草稿', 1, '2026-01-09 16:00:00');

-- ----------------------------
-- Records of tb_article_ai_tags (AI标签)
-- ----------------------------
INSERT INTO `tb_article_ai_tags` (`article_id`, `tag`, `confidence`, `source`) VALUES
(1, '人工智能', 0.95, 'ai'),
(1, '医疗', 0.88, 'ai'),
(1, '科技发展', 0.75, 'ai'),
(2, '5G', 0.92, 'ai'),
(2, '数字化转型', 0.85, 'ai'),
(2, '物联网', 0.78, 'ai'),
(3, '收纳整理', 0.91, 'ai'),
(3, '生活技巧', 0.82, 'ai'),
(4, '健康养生', 0.89, 'ai'),
(4, '作息习惯', 0.86, 'ai'),
(5, '电影', 0.94, 'ai'),
(5, '娱乐', 0.88, 'ai'),
(6, '综艺节目', 0.92, 'ai'),
(7, '篮球', 0.90, 'ai'),
(7, '体育训练', 0.85, 'ai'),
(8, '马拉松', 0.93, 'ai'),
(8, '运动健身', 0.87, 'ai'),
(9, '投资理财', 0.91, 'ai'),
(9, '金融', 0.84, 'ai'),
(10, '数字货币', 0.95, 'ai'),
(10, '区块链', 0.89, 'ai'),
(15, '旅游', 0.93, 'ai'),
(15, '小众景点', 0.86, 'ai'),
(17, '烹饪技巧', 0.90, 'ai'),
(17, '美食', 0.85, 'ai'),
(21, '电动汽车', 0.92, 'ai'),
(21, '汽车选购', 0.88, 'ai');

-- ----------------------------
-- Records of tb_audit_log (审计日志)
-- ----------------------------
INSERT INTO `tb_audit_log` (`user_id`, `username`, `action`, `resource_type`, `resource_id`, `resource_name`, `ip_address`, `user_agent`, `response_status`, `created_at`) VALUES
(1, 'admin', 'LOGIN', 'user', 1, '系统管理员', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'success', '2026-01-13 15:41:46'),
(1, 'admin', 'CREATE', 'article', 1, '人工智能在医疗领域的应用前景', '192.168.1.100', 'Mozilla/5.0', 'success', '2026-01-13 15:43:13'),
(1, 'admin', 'CREATE', 'article', 2, '5G技术对未来社会的影响', '192.168.1.100', 'Mozilla/5.0', 'success', '2026-01-13 15:43:15'),
(1, 'admin', 'UPDATE', 'article', 1, '人工智能在医疗领域的应用前景', '192.168.1.100', 'Mozilla/5.0', 'success', '2026-01-14 10:00:00'),
(2, 'wangba', 'LOGIN', 'user', 2, '王八', '192.168.1.101', 'Mozilla/5.0', 'success', '2026-01-14 09:00:00'),
(2, 'wangba', 'CREATE', 'comment', 1, '文章评论', '192.168.1.101', 'Mozilla/5.0', 'success', '2026-01-14 09:30:00'),
(3, 'testuser', 'LOGIN', 'user', 3, '测试用户', '192.168.1.102', 'Mozilla/5.0', 'success', '2026-01-14 11:00:00'),
(1, 'admin', 'CREATE', 'category', 1, '科技', '192.168.1.100', 'Mozilla/5.0', 'success', '2026-01-13 15:42:32'),
(1, 'admin', 'EXPORT', 'article', NULL, '批量导出', '192.168.1.100', 'Mozilla/5.0', 'success', '2026-01-15 14:00:00'),
(4, 'zhangsan', 'LOGIN', 'user', 4, '张三', '192.168.1.103', 'Mozilla/5.0', 'success', '2026-01-15 08:00:00');

-- ----------------------------
-- Records of tb_scheduled_task (定时任务)
-- ----------------------------
INSERT INTO `tb_scheduled_task` (`task_type`, `resource_type`, `resource_id`, `scheduled_time`, `status`, `created_by`, `created_at`) VALUES
('publish', 'article', 5, '2026-01-20 10:00:00', 2, 1, '2026-01-18 09:00:00'),
('publish', 'article', 6, '2026-01-22 14:00:00', 2, 1, '2026-01-19 11:00:00'),
('delete', 'article', 30, '2026-02-01 00:00:00', 0, 1, '2026-01-20 16:00:00'),
('publish', 'article', 7, '2026-01-25 09:00:00', 1, 1, '2026-01-21 10:00:00'),
('publish', 'article', 8, '2026-01-26 15:00:00', 0, 2, '2026-01-21 14:00:00');

-- ----------------------------
-- Records of tb_webhook (Webhook配置)
-- ----------------------------
INSERT INTO `tb_webhook` (`name`, `url`, `secret`, `events`, `is_active`, `created_by`, `created_at`) VALUES
('文章发布通知', 'https://example.com/webhooks/article-published', 'secret123', '[\"article:create\", \"article:update\"]', 1, 1, '2026-01-15 10:00:00'),
('评论通知', 'https://example.com/webhooks/comment-created', 'secret456', '[\"comment:create\"]', 1, 1, '2026-01-15 11:00:00'),
('定时任务回调', 'https://example.com/webhooks/task-completed', 'secret789', '[\"task:completed\"]', 0, 1, '2026-01-16 09:00:00'),
('系统告警', 'https://example.com/webhooks/alert', 'alert_secret', '[\"system:error\"]', 1, 1, '2026-01-17 14:00:00');

-- ----------------------------
-- Records of tb_webhook_log (Webhook执行日志)
-- ----------------------------
INSERT INTO `tb_webhook_log` (`webhook_id`, `event_type`, `payload`, `response_status`, `response_body`, `duration`, `status`, `created_at`) VALUES
(1, 'article:create', '{"article_id":1,"title":"人工智能在医疗领域的应用前景"}', 200, '{"success":true}', 150, 'success', '2026-01-15 10:05:00'),
(1, 'article:update', '{"article_id":1,"title":"人工智能在医疗领域的应用前景"}', 200, '{"success":true}', 120, 'success', '2026-01-15 11:00:00'),
(2, 'comment:create', '{"comment_id":1,"article_id":1}', 200, '{"success":true}', 80, 'success', '2026-01-15 12:00:00'),
(1, 'article:create', '{"article_id":2,"title":"5G技术对未来社会的影响"}', 200, '{"success":true}', 100, 'success', '2026-01-16 09:00:00'),
(2, 'comment:create', '{"comment_id":2,"article_id":2}', 500, '{"error":"server error"}', 200, 'failed', '2026-01-16 10:00:00'),
(4, 'system:error', '{"error":"database connection failed"}', 200, '{"received":true}', 50, 'success', '2026-01-17 15:00:00');

-- ----------------------------
-- Records of tb_webhook_retry (Webhook重试任务)
-- ----------------------------
INSERT INTO `tb_webhook_retry` (`webhook_id`, `event_type`, `payload`, `attempt_count`, `max_attempts`, `last_error`, `next_retry_at`, `status`, `created_at`) VALUES
(2, 'comment:create', '{"comment_id":2,"article_id":2}', 1, 3, 'server error', '2026-01-16 10:10:00', 1, '2026-01-16 10:00:00'),
(2, 'comment:create', '{"comment_id":2,"article_id":2}', 2, 3, 'server error', '2026-01-16 10:30:00', 1, '2026-01-16 10:10:00');

-- ----------------------------
-- Records of tb_comment (评论)
-- ----------------------------
INSERT INTO `tb_comment` (`article_id`, `user_id`, `content`, `parent_id`, `like_count`, `created_at`) VALUES
(1, 2, '这篇文章写得很好，AI在医疗领域的应用确实很广泛！', NULL, 5, '2026-01-14 09:30:00'),
(1, 3, '同意，特别是医学影像诊断这块，AI已经可以帮助医生提高诊断准确率了', 1, 3, '2026-01-14 10:00:00'),
(1, 4, '期待AI能够在更多医疗场景中发挥作用', NULL, 2, '2026-01-14 11:00:00'),
(2, 2, '5G技术确实会改变我们的生活', NULL, 8, '2026-01-14 14:00:00'),
(2, 3, '物联网这块我最期待，智能家居会更普及', NULL, 4, '2026-01-14 15:00:00'),
(3, 5, '收纳技巧很实用，已收藏！', NULL, 6, '2026-01-15 09:00:00'),
(5, 6, '2024年电影列表很全面，期待上映', NULL, 10, '2026-01-15 11:00:00'),
(8, 4, '马拉松训练计划很专业，收藏了', NULL, 3, '2026-01-16 08:00:00'),
(10, 2, '数字货币确实是未来趋势，但也要注意风险', NULL, 7, '2026-01-16 14:00:00'),
(15, 5, '这些小众景点都没去过，今年一定要去几个', NULL, 4, '2026-01-17 10:00:00');

-- ----------------------------
-- Records of tb_article_like (文章点赞)
-- ----------------------------
INSERT INTO `tb_article_like` (`article_id`, `user_id`, `created_at`) VALUES
(1, 2, '2026-01-14 09:35:00'),
(1, 3, '2026-01-14 09:45:00'),
(1, 4, '2026-01-14 10:00:00'),
(1, 5, '2026-01-14 11:00:00'),
(2, 2, '2026-01-14 14:05:00'),
(2, 3, '2026-01-14 14:10:00'),
(2, 6, '2026-01-15 08:00:00'),
(3, 5, '2026-01-15 09:05:00'),
(5, 2, '2026-01-15 11:05:00'),
(5, 4, '2026-01-15 11:10:00'),
(5, 6, '2026-01-15 12:00:00'),
(8, 3, '2026-01-16 08:05:00'),
(8, 4, '2026-01-16 08:10:00'),
(10, 2, '2026-01-16 14:05:00'),
(10, 3, '2026-01-16 14:10:00'),
(15, 5, '2026-01-17 10:05:00'),
(17, 2, '2026-01-17 15:00:00'),
(20, 4, '2026-01-18 09:00:00'),
(25, 3, '2026-01-18 11:00:00'),
(30, 5, '2026-01-19 14:00:00');

-- ----------------------------
-- Records of tb_article_collect (文章收藏)
-- ----------------------------
INSERT INTO `tb_article_collect` (`article_id`, `user_id`, `folder_name`, `created_at`) VALUES
(1, 2, '科技资讯', '2026-01-14 10:00:00'),
(2, 2, '科技资讯', '2026-01-14 14:15:00'),
(3, 5, '生活技巧', '2026-01-15 09:10:00'),
(4, 5, '健康养生', '2026-01-15 10:00:00'),
(5, 6, '娱乐休闲', '2026-01-15 11:15:00'),
(8, 4, '运动健身', '2026-01-16 08:15:00'),
(9, 2, '理财投资', '2026-01-16 09:00:00'),
(10, 3, '理财投资', '2026-01-16 14:15:00'),
(15, 5, '旅游攻略', '2026-01-17 10:10:00'),
(17, 5, '美食食谱', '2026-01-17 15:10:00'),
(21, 4, '汽车知识', '2026-01-18 10:00:00'),
(23, 6, '房产家居', '2026-01-18 14:00:00'),
(25, 3, '人文历史', '2026-01-18 16:00:00'),
(29, 2, '艺术欣赏', '2026-01-19 10:00:00');

-- ----------------------------
-- Records of tb_user_behavior (用户行为)
-- ----------------------------
INSERT INTO `tb_user_behavior` (`user_id`, `article_id`, `behavior_type`, `duration`, `created_at`) VALUES
(1, 1, 'view', 120, '2026-01-13 15:43:13'),
(1, 2, 'view', 90, '2026-01-13 15:44:00'),
(1, 3, 'view', 60, '2026-01-13 15:45:00'),
(2, 1, 'view', 180, '2026-01-14 09:32:00'),
(2, 1, 'like', NULL, '2026-01-14 09:35:00'),
(2, 2, 'view', 150, '2026-01-14 14:02:00'),
(2, 2, 'like', NULL, '2026-01-14 14:05:00'),
(3, 1, 'view', 200, '2026-01-14 10:00:00'),
(3, 1, 'comment', NULL, '2026-01-14 10:00:00'),
(4, 5, 'view', 100, '2026-01-15 11:00:00'),
(4, 8, 'view', 180, '2026-01-16 08:00:00'),
(4, 8, 'like', NULL, '2026-01-16 08:05:00'),
(5, 3, 'view', 90, '2026-01-15 09:00:00'),
(5, 3, 'collect', NULL, '2026-01-15 09:10:00'),
(5, 4, 'view', 80, '2026-01-15 10:00:00'),
(5, 4, 'collect', NULL, '2026-01-15 10:00:00'),
(6, 5, 'view', 120, '2026-01-15 11:00:00'),
(6, 5, 'like', NULL, '2026-01-15 11:05:00'),
(1, 10, 'view', 150, '2026-01-16 14:00:00'),
(2, 10, 'view', 100, '2026-01-16 14:05:00'),
(2, 10, 'like', NULL, '2026-01-16 14:05:00'),
(3, 15, 'view', 90, '2026-01-17 10:00:00'),
(5, 15, 'view', 110, '2026-01-17 10:05:00'),
(5, 15, 'collect', NULL, '2026-01-17 10:10:00'),
(2, 17, 'view', 70, '2026-01-17 15:00:00'),
(2, 17, 'like', NULL, '2026-01-17 15:00:00'),
(4, 20, 'view', 85, '2026-01-18 09:00:00'),
(3, 25, 'view', 130, '2026-01-18 11:00:00'),
(5, 29, 'view', 95, '2026-01-19 10:00:00'),
(1, 30, 'view', 110, '2026-01-19 14:00:00');

SET FOREIGN_KEY_CHECKS = 1;