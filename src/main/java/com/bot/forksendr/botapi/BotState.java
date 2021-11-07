package com.bot.forksendr.botapi;

/**Возможные состояния бота
 */

public enum BotState {
    START_MENU_BOT,
    ASK_DESTINY,
    ASK_NAME,
    ASK_AGE,
    ASK_SURNAME,
    ASK_GENDER,
    ASK_CONTACT,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU,
    SHOW_USER_PROFILE,
    MY_INFO,
    MY_CONTACT,
    SHOW_RESULT_OF_WORK,
    ERROR_AGE,
    SHOW_METODICAL_MATERIAL,
    KEYBOARD_CREATE,
    ADMIN_MENU;
}