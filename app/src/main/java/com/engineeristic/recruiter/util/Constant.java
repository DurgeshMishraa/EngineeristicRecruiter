package com.engineeristic.recruiter.util;

/**
 * Created by dell on 9/20/2016.
 */
public class Constant {

    // public static final String TEST_URL = "http://recqa.engineeristic.com/";
    //public static final String BASE_URL = "http://rec.engineeristic.com/";
    //public static final String BASE_URL = "https://suitor.engineeristic.com/";

    public static final String POST_BASE_URL = "https://suitor.engineeristic.com/";
    public static final String GET_BASE_URL = "https://patron.engineeristic.com/";
    //public static final String SEARCH_BASE_URL = "http://vasco.engineeristic.com/";
    // TEST_URL;//"http://rec.engineeristic.com/";

    public static boolean isLiveServer = false;
    public static String USER_COOKIE = "";
    public static final String SHARE_APP_TEXT = "Hey check out the engineeristic recruiter app at: https://play.google.com/store/apps/details?id=com.engineeristic.recruiter";
    private static final String MYJOBS_URL = "jobslist/";
    public static final String JOB_REFRESH_URL = GET_BASE_URL + "refreshjob/"; // get
    public static final String UNPUBLISHED_JOB_URL = GET_BASE_URL + "unpublishjob/";//get
    public static final String REC_PROFILE_URL = GET_BASE_URL + "profile/"; //get
    public static final String UNPUBLISH_MESSAGE = GET_BASE_URL + "unpublishmessages"; //get
    public static final String COVER_LATTER_URL = GET_BASE_URL + "viewcoverletter/";//get
    public static final String REC_ACTION_URL = GET_BASE_URL + "recruiteraction/"; // get
    public static final String RESUME_VIEW_URL = GET_BASE_URL + "resumeview"; //get
    public static final String VIEW_CLICK_COUNT_URL = GET_BASE_URL + "resumedetailview/"; //get
    public static final String URL_STARTCHAT=GET_BASE_URL +"startchat/"; //get
    public static final String URL_MYCHAT=GET_BASE_URL +"mychat/"; //get
    public static final String MY_JOB_URL = GET_BASE_URL + MYJOBS_URL; //get
    public static final String UPDATE_APP = GET_BASE_URL + "updateapp/"; // get

    public static final String JOB_APPLIED_LIST_URL = POST_BASE_URL + "jobappliedlist/"; //post
    public static final String ADD_COMMENT_URL = POST_BASE_URL+"addcomment/"; // post
    public static final String EDIT_PROFILE_URL = POST_BASE_URL + "editprofile/"; //post
    public static final String CHANGE_PASSWORD_URL = POST_BASE_URL + "changepassword/";//post
    public static final String LOGIN_URL     = POST_BASE_URL + "login"; //post
    public static final String FORGOT_PASS_URL = POST_BASE_URL + "forgetpassword/"; // post
    public static final String REGISTER_USER_URL =  POST_BASE_URL + "register/"; //post
    public static final String LOCATION_URL = POST_BASE_URL + "geocode/"; //post
    public static final String FEEDBACK_URL = POST_BASE_URL + "feedback/";//post
    public static final String APN_UPDATE_URL = POST_BASE_URL +"apn/"; //post
    public static final String LOGOUT_URL = POST_BASE_URL + "logout/"; // post
    public static final String PIC_UPLOAD_URL = POST_BASE_URL + "updateprofilepic/"; //post
    public static final String SEND_CHAT_MAIL=POST_BASE_URL +"sendchatmail/";//post

    public static final String KEY_VIEW_ID = "VIEW_ID";
    public static final String KEY_VIEW_MESSAGE = "VIEW_MESSAGE";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_OBJECT = "OBJECT";
    public static final String KEY_SCREEN_TYPE = "SCREEN_TYPE";
    public static final String KEY_JOB_ID = "JOB_ID";
    public static final String KEY_JOB_TITLE = "TITLE_NAME";
    public static final String KEY_DATA_TYPE = "data_type";
    public static final String KEY_FOLLOW_UP = "FOLLOW UP";
    public static final String KEY_ACTION_ID = "ACTION_ID";
    public static final String KEY_PROFILE_MODEL = "PROFILE_MODEL_OBJ";
    public static final String KEY_USER_COOKIE = "USER_COOKIE";
    public static final String KEY_USER_REC_ID = "RECRUITER_ID";
    public static final String KEY_USER_NAME = "USER_NAME";
    public static final String KEY_USER_EMAIL = "USER_EMAIL";
    public static final String KEY_USER_PHONE = "USER_CONTACT";
    public static final String KEY_USER_ORGANIZATION = "USER_ORG";
    public static final String KEY_USER_DESIGNATION = "USER_DESIGNATION";
    public static final String KEY_USER_UUID = "USER_UUID";
    public static final String KEY_USER_IMAGE = "USER_IMAGE";
    public static final String KEY_USER_NOTIFICATION = "USER_NOTICATION";
    public static final String KEY_USER_AUTH_KEY = "USER_AUTH_KEY";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_REG_ID = "registration_id";
    public static final String KEY_APP_VERSION = "appVersion";
    public static final String KEY_NOTIFICATION_LANDS = "NOTIFICATION_LAND";

    public static final String UPGRADE_APP = "https://play.google.com/store/apps/details?id=com.engineeristic.recruiter";

    public static final String KEY_ID = "ID";
    public static byte PROFILE_RESUME_TYPE = -1;
    public static final String HTTP_OK = "200";
    public static final int SOCKET_TIMEOUT_DURATION = 30000;
    public static final byte FILTER_INDUSTRY_PARAMS = 1;
    public static final byte FILTER_FUNCTIONAL_PARAMS = 2;
    public static final byte FILTER_MIN_EXPERIENCE_PARAMS = 3;
    public static final byte FILTER_MAX_EXPERIENCE_PARAMS = 4;
    public static final byte FILTER_INSTITUTE_PARAMS = 5;
    public static final byte FILTER_LOCATION_PARAMS = 6;
    public static final byte FILTER_MIN_BATCH_PARAMS = 7;
    public static final byte FILTER_MAX_BATCH_PARAMS = 8;
    public static final byte FILTER_MIN_SALARY_PARAMS = 9;
    public static final byte FILTER_MAX_SALARY_PARAMS = 10;
    public static final byte FILTER_GENDER_PARAMS = 11;
    public static final byte FILTER_NOTICE_PERIOD_PARAMS = 12;

    public static final byte RECRUITER_ALL_TYPE = 0;
    public static final byte RECRUITER_UNREAD_TYPE = 1;
    public static final byte RECRUITER_MAGIC_TYPE = 2;
    public static final byte RECRUITER_SHORTLIST_TYPE = 3;
    public static final byte RECRUITER_REJECT_TYPE = 4;
    public static final byte RECRUITER_SAVE_TYPE = 5;

    public static final byte LOGIN_WEBSERVICE = 0;
    public static final byte FORGOT_PASSWORD_WEBSERVICE = 1;
    public static final byte ALL_CANDIDATE_WEBSERVICE = 2;
    public static final byte NEW_CANDIDATE_WEBSERVICE = 3;
    public static final byte SHORTLISTED_WEBSERVICE = 4;
    public static final byte SAVED_WEBSERVICE = 5;
    public static final byte REJECTED_WEBSERVICE = 6;
    public static final byte PROFILE_INFO_WEBSERVICE = 7;
    public static final byte APN_WEBSERVICE = 8;

    public static final byte ALL_JOB_WEBSERVICE = 9;
    public static final byte PUBLISH_JOB_WEBSERVICE = 10;
    public static final byte UPDATED_JOB_WEBSERVICE = 11;
    public static final byte REJECTED_JOB_WEBSERVICE = 12;
    public static final byte PENDING_JOB_WEBSERVICE = 13;
    public static final byte UNPUBLISH_JOB_WEBSERVICE = 14;
    public static final byte RESUME_DETAILS_WEBSERVICE = 15;
    public static final byte RECRUITER_ACTION_WEBSERVICE = 16;
    public static final byte POST_COMMENT_WEBSERVICE = 17;
    public static final byte COVER_LETTER_WEBSERVICE = 18;
    public static final byte FEEDBACK_WEBSERVICE = 19;
    public static final byte REGISTER_WEBSERVICE = 20;
    public static final byte JOB_REFRESH_WEBSERVICE = 21;
    public static final byte JOB_UNPUBLISHED_WEBSERVICE = 22;
    public static final byte PROFILE_EDIT_WEBSERVICE = 23;
    public static final byte CHANGE_PASSWORD_WEBSERVICE = 24;
    public static final byte UPLOAD_PROFILE_PICTURE_WEBSERVICE = 25;
    public static final byte SENDCHATMAIL = 26;
    public static final byte CANDIDATE_DETAILS_WEBSERVICE = 27;
    public static final byte MAGIC_SHORT_WEBSERVICE = 28;
    public static final byte LOCATION_SERVICE = 29;

    public static String JUMP_RESUME_ACTIVITY = "profile_resume";
    public static boolean isRefreshUserProfile = false;
    public static boolean isRecruiterAction = false;
    public static int candidatePos = 0;

    public static final String MSG_NO_NETWORK = "Sorry, you're not connected to the Internet. Please check your connection settings";
    public static final String MSG_TIME_OUT = "Connection Timeout Click here to reload.";
    //public static final String MSG_RECORD_NOT_FOUND = "Data not found";
    public static final String MSG_SERVER_ERROR = "Internal server error occured.Please try after sometime";
    public static final String CHAT_UPGRADE_MSG = "To improve your chat experience, we have made changes at the technical front and will no longer be able to support chat on this version.Please upgrade the app to continue using chat.";
}
