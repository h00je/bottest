package com.bot.forksendr.botapi.handlers.fiingkeyboard;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.botapi.handlers.fillingprofile.FillingProfileHandler;
import com.bot.forksendr.botapi.handlers.fillingprofile.UserProfileData;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.ReplyMessagesService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class FilingKeyboardHandler extends FillingProfileHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private String keyboardMessage;
    private String keyboardButton1;
    private String keyboardButton2;
    private String keyboardButton3;
    private String keyboardButton4;

    public FilingKeyboardHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        super(userDataCache, messagesService);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.KEYBOARD_CREATE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.keyboardCreate");
            keyboardMessage = usersAnswer;
            replyToUser = messagesService.getReplyMessage(chatId, "reply.keyboardButtons1");
            keyboardButton1 = usersAnswer;
            replyToUser = messagesService.getReplyMessage(chatId, "reply.keyboardButtons2");
            keyboardButton2 = usersAnswer;
            replyToUser = messagesService.getReplyMessage(chatId, "reply.keyboardButtons3");
            keyboardButton3 = usersAnswer;
            replyToUser = messagesService.getReplyMessage(chatId, "reply.keyboardButtons4");
            keyboardButton4 = usersAnswer;
            replyToUser = messagesService.getReplyMessage(chatId, keyboardMessage);
            replyToUser.setReplyMarkup((getRandomButtonsMarkup()));
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_HELP_MENU);
        }

        return replyToUser;
    }
    private InlineKeyboardMarkup getRandomButtonsMarkup() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonCreateKeyboard1 = new InlineKeyboardButton().setText(keyboardButton1);
        InlineKeyboardButton buttonCreateKeyboard2 = new InlineKeyboardButton().setText(keyboardButton2);
        InlineKeyboardButton buttonCreateKeyboard3 = new InlineKeyboardButton().setText(keyboardButton3);
        InlineKeyboardButton buttonCreateKeyboard4 = new InlineKeyboardButton().setText(keyboardButton4);

        //Every button must have callBackData, or else not work !
        buttonCreateKeyboard1.setCallbackData("button1");
        buttonCreateKeyboard2.setCallbackData("button2");
        buttonCreateKeyboard3.setCallbackData("button3");
        buttonCreateKeyboard4.setCallbackData("button4");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonCreateKeyboard1);
        keyboardButtonsRow1.add(buttonCreateKeyboard2);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonCreateKeyboard3);
        keyboardButtonsRow2.add(buttonCreateKeyboard4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
