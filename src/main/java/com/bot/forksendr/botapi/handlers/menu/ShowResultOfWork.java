package com.bot.forksendr.botapi.handlers.menu;

import com.bot.forksendr.KsendrTelegramBot;
import com.bot.forksendr.botapi.BotState;
import com.bot.forksendr.botapi.InputMessageHandler;
import com.bot.forksendr.servise.MainMenuService;
import com.bot.forksendr.servise.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ShowResultOfWork implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private KsendrTelegramBot ksendrTelegramBot;


    public ShowResultOfWork(ReplyMessagesService messagesService, @Lazy KsendrTelegramBot ksendrTelegramBot) {
        this.messagesService = messagesService;
        this.ksendrTelegramBot = ksendrTelegramBot;


    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_RESULT_OF_WORK;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();


        ksendrTelegramBot.sendPhoto(chatId, messagesService.getReplyText("reply.showResultOfWordStart"), "static/image/showresultofworkstart.jpg");
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.showResultOfWork");
        replyToUser.setReplyMarkup(getInlineMessageButtons());


        return replyToUser;
    }
    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonResult1 = new InlineKeyboardButton().setText("1");
        InlineKeyboardButton buttonResult2 = new InlineKeyboardButton().setText("2");
        InlineKeyboardButton buttonResult3 = new InlineKeyboardButton().setText("3");
        InlineKeyboardButton buttonResult4 = new InlineKeyboardButton().setText("4");

        //Every button must have callBackData, or else not work !
        buttonResult1.setCallbackData("buttonResult1");
        buttonResult2.setCallbackData("buttonResult2");
        buttonResult3.setCallbackData("buttonResult3");
        buttonResult4.setCallbackData("buttonResult4");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonResult1);
        keyboardButtonsRow1.add(buttonResult2);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonResult3);
        keyboardButtonsRow2.add(buttonResult4);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}


