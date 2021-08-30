package com.bot.forksendr.botapi.handlers.fillingprofile;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Формирует анкету пользователя.
 */

@Slf4j
@Component
public class  FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE))
        {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SURNAME);
        }
        if (botState.equals(BotState.ASK_SURNAME)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSurname");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setSurname(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);

        }
        if (botState.equals(BotState.ERROR_AGE)){
            replyToUser = messagesService.getReplyMessage(chatId,"reply.badNumber");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);

        }

        if (botState.equals(BotState.ASK_GENDER)) {
            int age = 0;
            try {
                age = Integer.parseInt(usersAnswer);
            } catch (NumberFormatException exp) {
                userDataCache.setUsersCurrentBotState(userId, BotState.ERROR_AGE);
            }
            profileData.setAge(age);
            //profileData.setAge(Integer.parseInt(usersAnswer));
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askGender");
            replyToUser.setReplyMarkup((getGenderButtonsMarkup()));
            //userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

//        if (botState.equals(BotState.ASK_NUMBER)) {
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
//            profileData.setGender(usersAnswer);
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
//        }
//
//        if (botState.equals(BotState.ASK_COLOR)) {
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.askColor");
//            profileData.setNumber(Integer.parseInt(usersAnswer));
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MOVIE);
//        }
//
//        if (botState.equals(BotState.ASK_MOVIE)) {
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.askMovie");
//            profileData.setColor(usersAnswer);
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SONG);
//        }
//
//        if (botState.equals(BotState.ASK_SONG)) {
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSong");
//            profileData.setMovie(usersAnswer);
//            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
//        }
//
        if (botState.equals(BotState.PROFILE_FILLED)) {

            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);


        }

        if (botState.equals(BotState.MYINFO)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.myInfo");

        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }
    private InlineKeyboardMarkup getGenderButtonsMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonGenderMan = new InlineKeyboardButton().setText("М");
        InlineKeyboardButton buttonGenderWoman = new InlineKeyboardButton().setText("Ж");

        //Every button must have callBackData, or else not work !
        buttonGenderMan.setCallbackData("buttonMan");
        buttonGenderWoman.setCallbackData("buttonWoman");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonGenderMan);
        keyboardButtonsRow1.add(buttonGenderWoman);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }


}
