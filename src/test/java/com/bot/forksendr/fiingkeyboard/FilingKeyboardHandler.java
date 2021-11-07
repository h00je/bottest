package com.bot.forksendr.fiingkeyboard;

import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.model.UserProfileData;
import com.bot.forksendr.cache.UserDataCache;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


/**
*Формирует клавиатуру к сообщению от администратора
 */

@Slf4j
@Component
public class FilingKeyboardHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private String keyboardMessage;
    private String keyboardButton1;
    private String keyboardButton2;
    private String keyboardButton3;
    private String keyboardButton4;

    public FilingKeyboardHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, String keyboardMessage, String keyboardButton1, String keyboardButton2, String keyboardButton3, String keyboardButton4) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.keyboardMessage = keyboardMessage;
        this.keyboardButton1 = keyboardButton1;
        this.keyboardButton2 = keyboardButton2;
        this.keyboardButton3 = keyboardButton3;
        this.keyboardButton4 = keyboardButton4;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.KEYBOARD_CREATE))
        {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.KEYBOARD_CREATE;
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
