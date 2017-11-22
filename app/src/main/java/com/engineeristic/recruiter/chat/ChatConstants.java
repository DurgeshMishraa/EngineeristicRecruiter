package com.engineeristic.recruiter.chat;

public class ChatConstants {
	/*public static final String PUBLISH_KEY = "pub-c-7fad26fe-6c38-4940-b9c3-fbd19a9633af";
    public static final String SUBSCRIBE_KEY = "sub-c-30c17e1a-0007-11e5-a8ef-0619f8945a4f";*/

	/*public static final String PUBLISH_KEY = "pub-c-63069c70-3e81-42b3-b5f6-dc0bd232f845";
	public static final String SUBSCRIBE_KEY ="sub-c-760e7840-9e47-11e5-8db0-0619f8945a4f";*/


	//Development
//	public static final String PUBLISH_KEY = "pub-c-78f4982d-59c1-4f7c-99bd-7840539dce3b";
//	public static final String SUBSCRIBE_KEY ="sub-c-cac4b854-8dc1-11e5-a04a-0619f8945a4f";

	//new development keys for QA
//	public static final String PUBLISH_KEY ="pub-c-78f4982d-59c1-4f7c-99bd-7840539dce3b";
//	public static final String SUBSCRIBE_KEY ="sub-c-cac4b854-8dc1-11e5-a04a-0619f8945a4f";

	//production
	public static final String PUBLISH_KEY ="pub-c-63069c70-3e81-42b3-b5f6-dc0bd232f845";
	public static final String SUBSCRIBE_KEY ="sub-c-760e7840-9e47-11e5-8db0-0619f8945a4f";


	public static final String CHAT_PREFS    = "com.engineeristic.recruiter.SHARED_PREFS";
	public static final String CHAT_USERNAME = "com.engineeristic.recruiter.SHARED_PREFS.USERNAME";
	public static final String CHAT_ROOM     = "com.engineeristic.recruiter.CHAT_ROOM";
	public static final String CHAT_UUID     = "com.engineeristic.recruiter.CHAT_UUID";

	public static final String JSON_GROUP = "groupMessage";
	public static final String JSON_DM    = "directMessage";
	public static final String JSON_USER  = "chatUser";
	public static final String JSON_MSG   = "chatMsg";
	public static final String JSON_TIME  = "chatTime";

	public static final String STATE_LOGIN = "loginTime";

	public static final String GCM_REG_ID    = "gcmRegId";
	public static final String GCM_SENDER_ID = "709361095668"; // Get this from
	public static final String GCM_POKE_FROM = "gcmPokeFrom"; // Get this from
	public static final String GCM_CHAT_ROOM = "gcmChatRoom"; // Get this from
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


	//API Message Constants

	/*public static final int CHAT_NOT_AVAILABLE =0;
	public static final int CHAT_ENDED_BY_USER =2;*/
	public static final int START_CHAT =1;
	public static final int MY_CHAT =2;
	public static final int MAIL_MESSAGE =3;
	public static final int PRE_API_MY_CHAT =4;

	public static final String JOBID_FOR_CHAT ="Job_id";
	public static final String JOBTITLE_FOR_CHAT = "Job_Title";
	public static final String JOBPOSTED_FOR_CHAT = "Job_Posted";
	public static final String MY_CHAT_PROFILE_RESPONSE ="profile";
	public static final String LOGIN_MYNOTIFICATION ="mynotification";
	public static final String LOGIN_UUID ="UUID";
	public static final String CHAT_COUNT ="chatcount";

	public static final String PUSH_NOTIFICATION_ON ="push_notification_on";


}
