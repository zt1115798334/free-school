package com.example.school.common.constant;

import com.example.school.common.mysql.entity.SchoolTimetable;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 10:18
 * description: 系统常量
 */
public class SysConst {

    public static final String ORIGINAL_IMAGE_FILE_URL = "/app/file/findOriginalImg?topicImgId=";
    public static final String COMPRESS_IMAGE_FILE_URL = "/app/file/findCompressImg?topicImgId=";

    public static final String QUESTION_BANK_PDF_FILE_URL = "/app/file/findQuestionBankPdfHtml?topicFileId=";

    public static final int DEFAULT_BATCH_SIZE = 200;
    public static final String DEFAULT_SORT_NAME = "createdTime";

    public static final String DEFAULT_USERNAME = "时光";
    public static final Long DEFAULT_INTEGRAL = 100L;
    public static final String DEFAULT_PASSWORD = "123456";


    ///////////////////////////////////////////////////////////////////////////
    // 通用常量 -- 多实体类通用
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设备信息
     */
    @Getter
    @AllArgsConstructor
    public enum DeviceInfo {
        MOBILE("mobile", "手机"),
        WEB("web", "网页");

        private String type;
        private String name;
    }


    @Getter
    @AllArgsConstructor
    public enum AppSystemType {
        ANDROID("android"),
        IOS("ios");

        private String type;
    }

    /**
     * 搜索范围
     */
    @Getter
    @AllArgsConstructor
    public enum SearchArea {

        ALL("all", "全部"),
        TITLE("title", "标题"),
        CONTENT("content", "内容");

        private String type;
        private String name;
    }

    public static Optional<SearchArea> getSearchAreaByType(String type) {
        return Arrays.stream(SearchArea.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 排序
     */
    @Getter
    @AllArgsConstructor
    public enum SortOrder {

        ASC("asc"),
        DESC("desc");

        private String code;
    }

    public static Optional<SortOrder> getSortOrderByType(String code) {
        return Arrays.stream(SortOrder.values())
                .filter(replyType -> StringUtils.equals(code, replyType.getCode()))
                .findFirst();
    }

    /**
     * 学校类型
     */
    @Getter
    @AllArgsConstructor
    public enum School {
        SCHOOL_YJLG((short) 1, "燕京理工学院"),
        SCHOOL_FZKJXY((short) 2, "防灾科技学院");

        private Short code;
        private String name;
    }

    public static Optional<School> getSchoolByCode(Short code) {
        return Arrays.stream(School.values())
                .filter(replyType -> Objects.equal(code, replyType.getCode()))
                .findFirst();
    }

    public static String getSchoolNameByCode(Short code) {
        return getSchoolByCode(code).map(School::getName).orElse(StringUtils.EMPTY);
    }

    /**
     * 时间类型
     */
    @Getter
    @AllArgsConstructor
    public enum TimeType {
        CUSTOM_TIME("define", "自定义时间"),
        ALL("all", "全部"),
        TODAY("today", "今天"),
        YESTERDAY("yesterday", "昨天"),
        WEEK("week", "近7天"),
        MONTH("month", "近30天");

        private String type;
        private String name;

    }


    public static Optional<TimeType> getTimeTypeByType(String type) {
        return Arrays.stream(TimeType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 删除状态
     */
    @Getter
    @AllArgsConstructor
    public enum DeleteState {

        UN_DELETED((short) 0, "未删除"),
        DELETE((short) 1, "删除");

        private Short code;
        private String name;
    }

    /**
     * 显示状态
     */
    @Getter
    @AllArgsConstructor
    public enum ShowState {

        HIDE((short) 0, "hide", "隐藏"),
        DISPLAY((short) 1, "display", "显示");

        private Short code;
        private String type;
        private String name;
    }

    public static Optional<ShowState> getShowStateByCode(Short code) {
        return Arrays.stream(ShowState.values())
                .filter(replyType -> Objects.equal(code, replyType.getCode()))
                .findFirst();
    }

    /**
     * 开启状态
     */
    @Getter
    @AllArgsConstructor
    public enum EnabledState {

        OFF((short) 0, "停用"),
        ON((short) 1, "开启");

        private Short code;
        private String name;

    }

    private static Optional<EnabledState> getEnabledStateByCode(Short code) {
        return Arrays.stream(EnabledState.values())
                .filter(replyType -> code.equals(replyType.getCode()))
                .findFirst();
    }

    public static String getEnabledStateNameByCode(Short code) {
        return getEnabledStateByCode(code).orElse(EnabledState.ON).getName();
    }

    /**
     * 验证码类型
     */
    @Getter
    @AllArgsConstructor
    public enum VerificationCodeType {

        REGISTER("register", "账户注册"),
        LOGIN("login", "账户登陆"),
        FORGET("forget", "密码重置");

        private String type;
        private String name;
    }

    public static Optional<VerificationCodeType> getVerificationCodeTypeByType(String type) {
        return Arrays.stream(VerificationCodeType.values())
                .filter(replyType -> Objects.equal(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 通知类型
     */
    @Getter
    @AllArgsConstructor
    public enum NoticeType {

        EMAIL("email", "邮件"),
        PHONE("phone", "短信");

        private String type;
        private String name;

    }

    public static Optional<NoticeType> getNoticeTypeByType(String type) {
        return Arrays.stream(NoticeType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 业务常量
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 登录类型
     */
    @Getter
    @AllArgsConstructor
    public enum LoginType {

        AJAX("ajax", "ajax登陆"),
        TOKEN("token", "token登陆"),
        VERIFICATION_CODE("verificationCode", "验证码登陆");

        private String type;
        private String name;

    }


    /**
     * 账户状态
     */
    @Getter
    @AllArgsConstructor
    public enum AccountState {

        FROZEN((short) 0, "冻结"),
        NORMAL((short) 1, "正常");
        private Short code;
        private String name;

    }

    public static Optional<AccountState> getAccountStateByCode(Short code) {
        return Arrays.stream(AccountState.values())
                .filter(replyType -> Objects.equal(code, replyType.getCode()))
                .findFirst();
    }

    /**
     * 账户类型
     */
    @Getter
    @AllArgsConstructor
    public enum AccountType {

        ADMIN("admin", "管理员用户"),
        STUDENT_PRESIDENT("studentPresident", "学生会用户"),
        STUDENT("student", "学生用户");

        private String type;
        private String name;
    }

    public static Optional<AccountType> getAccountTypeByType(String type) {
        return Arrays.stream(AccountType.values())
                .filter(replyType -> StringUtils.equals(type, replyType.getType()))
                .findFirst();
    }

    /**
     * 账户类型
     */
    @Getter
    @AllArgsConstructor
    public enum PermissionType {

        ADMIN("admin", "管理员用户"),
        STUDENT_PRESIDENT("studentPresident", "学生会用户");

        private String type;
        private String name;
    }

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    public enum Sex {

        WOMEN((short) 0, "女"),
        MEN((short) 1, "男"),
        UNKNOWN((short) 99, "未知");

        private Short code;
        private String name;

    }

    public static Optional<Sex> getSexByCode(Short code) {
        return Arrays.stream(Sex.values())
                .filter(replyType -> code.equals(replyType.getCode()))
                .findFirst();
    }


    /**
     * 文件夹类型
     */
    @Getter
    @AllArgsConstructor
    public enum FolderType {
        FOLDER_IMG("img", "图片"),
        FOLDER_HEAD_PORTRAIT("headPortrait", "用户头像"),
        FOLDER_FEEDBACK_IMG("feedbackImg", "反馈图片"),
        FOLDER_QUESTION_BANK_FILE("questionBankFile", "用户头像");

        private String type;
        private String name;
    }


    /**
     * 状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        IN_RELEASE("inRelease", "发布中"),
        NEW_RELEASE("newRelease", "新发布"),
        AFTER_RELEASE("afterRelease", "发布后"),
        SOLVE("solve", "已解决"),
        SELL_OUT("sellOut", "卖出"),
        LOWER_SHELF("lowerShelf", "下架");

        private String type;
        private String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum ReplyType {
        COMMENT((short) 1, "回复评论"),
        REPLY((short) 2, "回复别人的回复");

        private Short code;
        private String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum TopicType {
        TOPIC_TYPE_1((short) 1, "交易"),
        TOPIC_TYPE_2((short) 2, "讯息"),
        TOPIC_TYPE_3((short) 3, "问答"),
        TOPIC_TYPE_4((short) 4, "题库"),
        TOPIC_TYPE_5((short) 5, "时光");

        private Short code;
        private String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum ZanType {
        ZAN_TOPIC((short) 1, "主题"),
        ZAN_COMMENT((short) 2, "评论");

        private Short code;
        private String name;
    }

    /**
     * 主体类型
     */
    @Getter
    @AllArgsConstructor
    public enum CommentState {
        ADOPT("adopt", "采纳"),
        NOT_ADOPTED("notAdopted", "采纳");

        private String type;
        private String name;
    }

    /**
     * 反馈类型
     */
    @Getter
    @AllArgsConstructor
    public enum FeedbackType {
        FEEDBACK_TYPE_CUSTOM((short) 99, "自定义");

        private Short code;
        private String name;
    }

    /**
     * 星期
     */
    @Getter
    @AllArgsConstructor
    public enum Week {

        MON("mon", "星期一"),
        TUE("tue", "星期二"),
        WED("wed", "星期三"),
        THU("thu", "星期四"),
        FRI("fri", "星期五"),
        SAT("sat", "星期六"),
        SUN("sun", "星期天");

        private String type;
        private String name;
    }

    public static List<String> getAllWeekType() {
        return Arrays.stream(Week.values()).map(Week::getType).collect(Collectors.toList());
    }

    /**
     * 课次
     */
    @Getter
    @AllArgsConstructor
    public enum ClassTimes {
        CLASS_ONE((short) 1, "第一节"),
        CLASS_TWO((short) 2, "第二节"),
        CLASS_THREE((short) 3, "第三节"),
        CLASS_FOUR((short) 4, "第四节"),
        CLASS_FIVE((short) 5, "第五节"),
        CLASS_SIX((short) 6, "第六节");

        private short code;
        private String name;
    }

    public static List<Short> getAllClassTimesCode() {
        return Arrays.stream(ClassTimes.values()).map(ClassTimes::getCode).collect(Collectors.toList());
    }

    /**
     * 课次
     */
    @Getter
    @AllArgsConstructor
    public enum UsableState {
        AVAILABLE((short) 0, "可用"),
        NOT_AVAILABLE((short) 1, "不可用");

        private short code;
        private String name;
    }
    /**
     * 新旧状态
     */
    @Getter
    @AllArgsConstructor
    public enum FreshState {
        FRESH((short) 0, "新"),
        PAST((short) 1, "旧");

        private short code;
        private String name;
    }
    /**
     * 异常状态
     */
    @Getter
    @AllArgsConstructor
    public enum AbnormalState {
        NORMAL((short) 0, "正常"),
        ABNORMAL((short) 1, "异常");

        private short code;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public enum Color {

        PANTONE_Yellow_U("#1D1D1F"),
        PANTONE_Yellow_012_U("#F80DA1"),
        PANTONE_Orange_021_U("#FF6C2F"),
        PANTONE_Bright_Red_U("#FE5442"),
        PANTONE_2388_U("#3F5EA9"),
        PANTONE_Warm_Red_U("#FF665E"),
        PANTONE_Red_032_U("#F65058"),
        PANTONE_Rubine_Red_U("#DB487E"),
        PANTONE_Rhodamine_Red_U("#E44C9A"),
        PANTONE_Pink_U("#D1428D"),
        PANTONE_Purple_U("#BF53B6"),
        PANTONE_Medium_Purple_U("#65428A"),
        PANTONE_Violet_U("#7758B3"),
        PANTONE_Blue_072_U("#3F43AD"),
        PANTONE_Dark_Blue_U("#444795"),
        PANTONE_Reflex_Blue_U("#39499C"),
        PANTONE_Process_Blue_U("#0083C3"),
        PANTONE_Green_U("#00AC8C"),
        PANTONE_2070_U("#944B96"),
        PANTONE_Black_U("#615D59"),
        PANTONE_Yellow_0131_U("#D6DE2C"),
        PANTONE_Red_0331_U("#FFB1BE"),
        PANTONE_Magenta_0521_U("#F8AADD"),
        PANTONE_Violet_0631_U("#BA93DF"),
        PANTONE_Blue_0821_U("#6CD1EF"),
        PANTONE_Green_0921_U("#78E6D0"),
        PANTONE_Black_0961_U("#9D9994"),
        PANTONE_801_U("#009CCD"),
        PANTONE_802_U("#3BD23D"),
        PANTONE_803_U("#FFE916"),
        PANTONE_804_U("#FFAA52"),
        PANTONE_805_U("#FF7477"),
        PANTONE_806_U("#FF48B0"),
        PANTONE_807_U("#E838BF"),
        PANTONE_197_U("#F2A1B2"),
        PANTONE_2098_U("#6753AA"),
        PANTONE_198_U("#E76B7C"),
        PANTONE_199_U("#DD5061"),
        PANTONE_200_U("#BD4F5C"),
        PANTONE_232_U("#F16AB7"),
        PANTONE_7665_U("#725E7C"),
        PANTONE_2198_U("#3FC9E6"),
        PANTONE_GCMI_91("#FFFFFF");

        private String colorRGB;
    }

    public static Map<String, String> getColorBySchoolTimetable(List<SchoolTimetable> schoolTimetables) {
        List<String> curriculumList = schoolTimetables.stream()
                .map(SchoolTimetable::getCurriculum)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        List<String> colorList = Arrays.stream(Color.values())
                .map(Color::getColorRGB)
                .collect(Collectors.toList());
        Map<String, String> result = Maps.newHashMap();
        for (int i = 0; i < curriculumList.size(); i++) {
            int i1 = i % curriculumList.size();
            result.put(curriculumList.get(i), colorList.get(i1));
        }
        return result;
    }

}
