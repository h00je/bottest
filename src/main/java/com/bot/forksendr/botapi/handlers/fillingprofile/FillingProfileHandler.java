package com.bot.forksendr.botapi.handlers.fillingprofile;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.model.UserProfileData;
import com.bot.forksendr.servise.ReplyMessagesService;
import com.bot.forksendr.servise.UsersProfileDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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
    private UsersProfileDataService profileDataService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService,
                                 UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.profileDataService = profileDataService;
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
        String userUsername = inputMsg.getFrom().getUserName();
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
            profileData.setUserName(userUsername);
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
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);

        }

        if (botState.equals(BotState.ASK_GENDER)) {
          try {
                profileData.setAge(Integer.parseInt(usersAnswer));
                } catch (NumberFormatException exp) {
                userDataCache.setUsersCurrentBotState(userId, BotState.ERROR_AGE);
            }
            profileData.setAge(Integer.parseInt(usersAnswer));

            replyToUser = messagesService.getReplyMessage(chatId, "reply.askGender");
            replyToUser.setReplyMarkup((getGenderButtonsMarkup()));

        }

        if (botState.equals(BotState.ASK_CONTACT)) {
            profileData.setChatId(chatId);
            profileData.setContact(usersAnswer);

            profileDataService.saveUserProfileData(profileData);

            replyToUser = messagesService.getReplyMessage(chatId, "reply.forMetodical");
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_METODICAL_MATERIAL);

        }

        if (botState.equals(BotState.PROFILE_FILLED)) {

            replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_METODICAL_MATERIAL);

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
