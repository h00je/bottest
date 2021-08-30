package com.bot.forksendr.botapi;

/**Возможные состояния бота
 */

public enum BotState {
    ASK_DESTINY,
    ASK_NAME,
    ASK_AGE,
    ASK_SURNAME,
    ASK_GENDER,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU,
    SHOW_USER_PROFILE,
    MYINFO,
    MY_CONTACT,
    SHOW_RESULT_OF_WORK,
    ERROR_AGE,
    KEYBOARD_CREATE;
}